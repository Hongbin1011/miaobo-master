package com.anzhuo.video.app.message;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created with IntelliJ IDEA.
 * User: neevek
 * Date: 7/21/13
 * Time: 10:23 AM
 */
public class Message {
    public final static int PRIORITY_NORMAL = 1;
    public final static int PRIORITY_HIGH = 2;
    public final static int PRIORITY_EXTREMELY_HIGH = 3;
    private final static int MAX_CACHED_MESSAGE_OBJ = 15;
    private static ConcurrentLinkedQueue<Message> mCachedMessagePool = new ConcurrentLinkedQueue<Message>();

    public static enum Type {
        NONE,
        DESTROY_MESSAGE_PUMP,//destroy_message_pump  销毁消息队列

        USER_PROGRAM_DATA_LOAD_SUCCESS,//用户的程序列表加载完成
        USER_GAME_MANAGE_SWITCH_OPEN,//管理用户程序状态开启
        USER_GAME_MANAGE_SWITCH_CLOSE,//管理用户程序状态关闭
        USER_PROGRAM_MANAGE_SWITCH_OPEN,//管理用户程序状态开启
        USER_PROGRAM_MANAGE_SWITCH_CLOSE,//管理用户程序状态关闭
        USER_PROGRAM_MANAGE_SWITCH_PUT_LANCHER,//管理用户程序放到桌面
        USER_PROGRAM_MANAGE_PAGE_NUM,//用户程序的页面(处于哪一页)
        ALL_PROGRAM_MANAGE_SWITCH_OPEN,//管理所有程序状态开启
        ALL_PROGRAM_MANAGE_SWITCH_CLOSE,//管理所有程序状态关闭
        ALL_GAME_MANAGE_SWITCH_OPEN,//管理所有游戏状态开启
        ALL_GAME_MANAGE_SWITCH_CLOSE,//管理所有游戏状态关闭
        GAME_PROGRAM_DATA_LOAD_SUCCESS,//游戏列表加载完成
        ADD_PROGRAM,//添加程序
        DELETE_PROGRAM,//删除程序
        ADD_GAME,//添加游戏
        DELETE_GAME,//删除游戏

        USER_DATA_PHOTO_CHAANGE,//头像更新
        USER_DATA_STATE,//用户状态更新
        USER_EXIT_SATEA,//用户退出状态

        USER_START_LOGIN,//开始登陆
        USER_LOGIN_END,//登陆成功或者失败


        NETWORK_CHANGE,   //网络状态改变
        USER_LOGIN_SUCCESS, //用户登录成功
        USER_LOGOUT_SUCCESS,//用户注销成功
        USER_DATA_CHANGE,//用户数据发生变动

        REQUEST_H5_SPEECHRECOGNIZER, //语音识别后h5的回调
        REQUEST_H5_SETWALLPAPER, //设置壁纸后h5的回调
        REQUEST_H5_UPLOAD, //上传完成后h5的回调
        REQUEST_H5_REDUCTIONCONTACT, //还原联系人
        REQUEST_H5_SET_TITLE_COLOR, //设置导航栏背景色

        CLOSE_SEARCH_FRAGMENT,
        LOAD_MOVIE_TYPE,
        UPDATE_LABEL_LIST,//更新标签显示列表
        Refresh_current_page//刷新当前页
    }


    public Type type;
    public Object data;
    public int priority;//优先级
    public Object sender;

    public int referenceCount;
    public Message(Type type, Object data, int priority, Object sender) {
        this.type = type;
        this.data = data;
        this.priority = priority;
        this.sender = sender;
    }

    public Message(Type type, Object data, int priority) {
        this(type, data, priority, null);
    }

    public Message(Type type, Object data) {
        this(type, data, PRIORITY_NORMAL, null);
    }

    public Message(Type type, int priority) {
        this(type, null, priority);
    }


    public void reset() {
        type = Type.NONE;
        data = null;
        priority = PRIORITY_NORMAL;
        sender = null;
    }

    public void recycle() {
        if (mCachedMessagePool.size() < MAX_CACHED_MESSAGE_OBJ) {
            reset();
            mCachedMessagePool.add(this);
        }
    }


    public static Message obtainMessage(Type msgType, Object data, int priority, Object sender) {
        Message message = mCachedMessagePool.poll();
        if (message != null) {
            message.type = msgType;
            message.data = data;
            message.priority = priority;
            message.sender = sender;
        } else {
            message = new Message(msgType, data, priority, sender);
        }
        return message;
    }


}
