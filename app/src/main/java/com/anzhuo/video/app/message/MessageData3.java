package com.anzhuo.video.app.message;

/**
 * Created with IntelliJ IDEA.
 * User: xiejm
 * Date: 3/5/13
 * Time: 10:57 AM
 */
public class MessageData3<T1, T2, T3> {
    public T1 o1;
    public T2 o2;
    public T3 o3;
    public MessageData3(T1 o1, T2 o2, T3 o3) {
        this.o1 = o1;
        this.o2 = o2;
        this.o3 = o3;
    }

    public MessageData3() { }
}
