package com.anzhuo.video.app.message;



import com.anzhuo.video.app.config.VideoApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: neevek
 * Date: 7/20/13
 * Time: 15:50 AM
 */
public class MessageHelper {
	private MessageCallback mMessageCallback;//
	private List<Message.Type> mMessageTypes = new ArrayList<>();

	public void setMessageCallback(MessageCallback messageCallback) {
		this.mMessageCallback = messageCallback;
	}

	public void registerMessages() {
		if (mMessageCallback == null) {
			return;
		}

		for (Message.Type messageType : mMessageTypes) {
			VideoApplication.getInstance().getMessagePump().register(messageType, mMessageCallback);
		}
	}

	public void unRegisterMessages() {
		if (mMessageCallback == null)
			return;

		for (Message.Type messageType : mMessageTypes) {
			VideoApplication.getInstance().getMessagePump().unregister(messageType, mMessageCallback);
		}
	}

	/**
	 * 监听消息
	 * @param mssageType
	 */
	public void attachMessage(Message.Type mssageType) {
		if (mMessageCallback == null) {
			throw new IllegalStateException("You need call setMessageCallback() at first.");
		}

		mMessageTypes.add(mssageType);
	}

	public void clearMessages(){
		mMessageTypes.clear();
		mMessageTypes = null;
		this.mMessageCallback = null;
	}

}
