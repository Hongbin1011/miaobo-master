package com.anzhuo.video.app.dongtu.utils;

import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

/**
 * 公共的Event类，统一，以免混乱
 * <p/>
 * -------------------------------------
 * EventBUS使用步骤：//已经在appcation初始化了
 * 1.首先需要定义一个消息类（定义事件），该类可以不继承任何基类也不需要实现任何接口。所有能被实例化为Object的实例都可以作为事件：
 * ///public class MessageEvent {//这个类的作用就是定义消息类
 * ///......
 * ///}
 * 注意：在最新版的eventbus 3中如果用到了索引加速，事件类的修饰符必须为public，不然编译时会报错：Subscriber method must be public。
 * <p/>
 * ///
 * //
 * <p/>
 * 2.监听事件
 * 2.1在需要监听事件的地方注册
 * EventBus.getDefault().register(this);
 * 2.2  顺手在onDestroy或者onStop中反注册   EventBus.getDefault().unregister(this);  以免忘了反注册
 * <p/>
 * 2.3监听事件
 * //@Subscribe(threadMode = ThreadMode.MAIN)
 * //public void handleEvent(DriverEvent event) {
 * //Log.d(TAG, event.info);
 * //}
 * <p/>
 * ///.
 * <p/>
 * //
 * .
 * 3.发布事件（可以在任何地方发布）
 * EventBus.getDefault().post(messageEvent);
 * messageEvent也就是上面定义的事件
 * <p/>
 * 注意：如果一个Activity或者Fragment只有发布事件的话，就不需要注册，如果注册了，就一定要写监听事件，当然监听事件和发布事件可以在同一个类中。
 */

/**
 * 作者：熊晓清 on 2016/5/4 18:09
 * 实现Eventbus传值
 */
public class EventUtil {

    /**
     * 通知列表更新事件
     /*   *//*
    public static class NotifyJokeListEvent {
        @Override
        public String toString() {
            return "NotifyJokeListEvent{" +
                    "JokeDetails=" + JokeDetails +
                    ", parentString='" + parentString + '\'' +
                    ", childString='" + childString + '\'' +
                    ", clickPosition='" + clickPosition + '\'' +
                    '}';
        }

        public JokeEntity JokeDetails;
        public String parentString = "";
        public String childString = "";
        public String clickPosition = "";
        public String postParent = "";//从哪里发送事件
    }*/

    /**
     * 接受推送通知
     *//*
    public static class JpushReceiverEvent {
        public boolean isGetJpushMessage = false;
    }
*/

    /**
     * 消息种类（互动和通知）
     */
    public static class MessageTypeEvent {
        public String MessageType = "";
    }
/*
    *//**
     * 发送接受通知事件
     *//*
    public static void PostMessageTypeEvent(String MessageType) {
        MessageTypeEvent event = new MessageTypeEvent();
        event.MessageType = MessageType;
        EventBus.getDefault().post(event);
//        Logger.i("发送更新列表事件" + event.toString());
    }*/
/*
    *//**
     * 主要针对个人资料提示图不是居中显示
     *//*
    public static class PersonStateEvent {
        public boolean isMatch;
    }*/
/*
    *//**
     * AppBarLayout滑动的时候滑动的值，主要用于我的界面和个人界面解决滑动冲突
     * https://github.com/liaohuqiu/android-Ultra-Pull-To-Refresh/issues/207
     * 将Activity中的AppBarLayout滑动事件传递给fragment
     *//*
    public static class AppBarLayoutOnOffsetChangedEvent {
        public String fragmentGetActivityName;//
        public int verticalOffset;
    }*/


    /**
     * 更新个人中心的列表
     */
    public static class PersonListRefreshEvent {
        public String parentString; //
        public String titleString;//标题  ：投稿、收藏、评论

    }

//    /**
//     * 发送通知个人界面刷新,在个人界面接受
//     */
//    public static void PostPersonListRefreshEvent(String parentString, String titleString) {
//        PersonListRefreshEvent event = new PersonListRefreshEvent();
//        event.parentString = parentString;
//        event.titleString = titleString;
//        EventBus.getDefault().post(event);
//    }

    /**
     * 关注事件，主要用于详情界面和个人中心的通讯
     */
    public static class AttentionEvent {
        public String AttentionUserID; //关注人的ID
        public boolean isAttention;//是否关注
    }

//    public static void PostAttentionEvent(String AttentionUserID, boolean isAttention) {
//        AttentionEvent event = new AttentionEvent();
//        event.AttentionUserID = AttentionUserID;
//        event.isAttention = isAttention;
//        EventBus.getDefault().post(event);
//    }

    /**
     * 点击 首页 刷新
     */
    public static class ClickHomeRefreshEvent {
    }

//    /**
//     * 点击 首页 刷新
//     */
//    public static void PostClickRefresh() {
//        Logger.i("发送  home刷新事件");
//        EventBus.getDefault().post(new ClickHomeRefreshEvent());
//    }

    /**
     * 监听网络状态
     */
    public static class ListenerNetEvent {
        public boolean isHasNet;

    }

    public static void PostListenerNetEvent(boolean isHasNet) {
        Logger.i("发送  home刷新事件");
        ListenerNetEvent event = new ListenerNetEvent();
        event.isHasNet = isHasNet;
        EventBus.getDefault().post(event);
    }

    /**
     * 主页列表“上次看到这里，点击刷新”，事件
     */
    public static class RecordPositionRefreshEvent {
        public String parentString = "";
        public String childString = "";

    }
//
//    public static void PostRecordPositionRefreshEvent(String parentString, String childString) {
//        RecordPositionRefreshEvent recordPositionRefreshEvent = new RecordPositionRefreshEvent();
//        recordPositionRefreshEvent.parentString = parentString;
//        recordPositionRefreshEvent.childString = childString;
//        EventBus.getDefault().post(recordPositionRefreshEvent);
//        Logger.i("发送刷新");
//    }

    public static class HomeRefreshEvent {
        public String parentString = "";
        public String childString = "";

    }

//    public static void PostHomeRefreshEvent(String parentString, String childString) {
//        HomeRefreshEvent event = new HomeRefreshEvent();
//        event.parentString = parentString;
//        event.childString = childString;
//        EventBus.getDefault().post(event);
//        Logger.i("发送刷新");
//    }


    /*============================================================*/

    /**
     * 通知HomeFragment改变数据
     */
    public static class EnventChangesData {
        public static int positions;
        public static boolean isAdd = false;
    }

    /**
     * 点赞状态改变传递
     */
    public static class EnventChangesStateData {
        public static int position;
        public static String titleTypes;
        public static String isType;
    }

    /**
     * mainactivity 发送事件通知改变
     */
    public static class EnventSendStateChangeLocaData {
    }

    /**
     * mainactivity 发送事件通知改变
     */
    public static class EnventSendStateChangePosition {
        public static int ToPosition;
    }

}
