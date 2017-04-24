package com.anzhuo.video.app.message;

/**
 * Created with IntelliJ IDEA.
 * User: xiejm
 * Date: 3/5/13
 * Time: 10:57 AM
 */
public class MessageData2<T1, T2> {
    public T1 o1;
    public T2 o2;
    public MessageData2(T1 o1, T2 o2) {
        this.o1 = o1;
        this.o2 = o2;
    }
    public MessageData2() { }
    
    
    @Override
    public String toString() {
        return "MessageData2 [o1=" + o1 + ", o2=" + o2 + "]";
    }
    
    
    
}
