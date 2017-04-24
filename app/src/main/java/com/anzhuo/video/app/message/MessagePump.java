package com.anzhuo.video.app.message;


import com.anzhuo.video.app.manager.LogManager;
import com.anzhuo.video.app.manager.fuli.TaskExecutor;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;


/**
 * Created with IntelliJ IDEA.
 * User: neevek
 * Date: 7/20/13
 * Time: 14:50 AM
 */
public class MessagePump extends Thread {

    private PriorityBlockingQueue<Message> mMsgPump;
    private List<List<WeakReference<MessageCallback>>> mMsgAndObserverList;

    public MessagePump() {
        setName(this.getClass().getSimpleName());
        mMsgPump = new PriorityBlockingQueue<Message>(10, new Comparator<Message>() {
            public int compare(Message o1, Message o2) {
                return o1.priority < o2.priority ? -1 : o1.priority > o2.priority ? 1 : 1;
            }
        });
        mMsgAndObserverList = new ArrayList<List<WeakReference<MessageCallback>>>(Collections.<List<WeakReference<MessageCallback>>> nCopies(Message.Type.values().length, null));
        start();

    }

    public void destroyMessagePump() {
        broadcastMessage(Message.Type.DESTROY_MESSAGE_PUMP, null, Message.PRIORITY_EXTREMELY_HIGH);
    }

    @Override
    public void run() {
        Thread.currentThread().setPriority(MIN_PRIORITY);
        dispatchMessages();
    }

    public synchronized void register(Message.Type msgType, MessageCallback callback) {//注册
        List<WeakReference<MessageCallback>> observerList = mMsgAndObserverList.get(msgType.ordinal());

        if (observerList == null) {
            observerList = new ArrayList<WeakReference<MessageCallback>>(1);//容量为1
            mMsgAndObserverList.set(msgType.ordinal(), observerList);
        }

        if (indexOf(callback, observerList) == -1) {
            observerList.add(new WeakReference<MessageCallback>(callback));
        }
    }

    private int indexOf(MessageCallback callback, List<WeakReference<MessageCallback>> observerList) {
        try {
            for (int i = observerList.size() - 1; i >= 0; i--) {
                if (observerList.get(i).get() == callback) {
                    return i;
                }
            }
        } catch (Exception e) {

        }
        return -1;
    }

    public synchronized void unregister(Message.Type msgType, MessageCallback callback) {//反注册
        List<WeakReference<MessageCallback>> observerList = mMsgAndObserverList.get(msgType.ordinal());

        if (observerList != null) {
            int index = indexOf(callback, observerList);

            if (index != -1) {
                observerList.remove(index);
            }
        }
    }

    @Deprecated
    public synchronized void unregister(MessageCallback callback) {
        Message.Type[] types = Message.Type.values();

        for (int i = 0; i < types.length; ++i) {
            unregister(types[i], callback);
        }
    }

    public void broadcastMessage(Message.Type msgType, Object data) {//将消息存储到队列中
        mMsgPump.put(Message.obtainMessage(msgType, data, Message.PRIORITY_NORMAL, null));
    }

    public void broadcastMessage(Message.Type msgType) {
        mMsgPump.put(Message.obtainMessage(msgType, null, Message.PRIORITY_NORMAL, null));
    }

    public void broadcastMessage(Message.Type msgType, Object data, int priority) {
        mMsgPump.put(Message.obtainMessage(msgType, data, priority, null));
    }

    public void broadcastMessage(Message.Type msgType, Object data, Object sender, int priority) {
        mMsgPump.put(Message.obtainMessage(msgType, data, priority, sender));
    }

    private void dispatchMessages() {//消息分发
        while (true) {
            try {
                final Message message = mMsgPump.take();

                if (message.type == Message.Type.DESTROY_MESSAGE_PUMP)//销毁消息队列
                    break;

                final List<WeakReference<MessageCallback>> observerList = mMsgAndObserverList.get(message.type.ordinal());

                if (observerList != null && observerList.size() > 0) {
                    message.referenceCount = observerList.size();

                    for (int i = 0; i < observerList.size(); ++i) {
                        final MessageCallback callback = observerList.get(i).get();

                        if (callback == null) {
                            observerList.remove(i);
                            --i;

                            if (--message.referenceCount == 0) {
                                message.recycle();
                            }

                        } else {

                            TaskExecutor.runTaskOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        callback.onReceiveMessage(message);

                                    } catch (Exception e) {
                                        LogManager.e(e);
                                    }

                                    // recycle the Message object
                                    if (--message.referenceCount == 0) {
                                        message.recycle();
                                    }
                                }
                            });
                        }
                    }

                } else {
                    message.recycle();
                }

            } catch (Exception e) {
                LogManager.e(e);
            }
        }
    }
}