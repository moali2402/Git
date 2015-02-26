package dev.vision.rave.messaging;

import java.net.URL;

import android.graphics.Bitmap;
import android.text.Spannable;

public class Message{
	Object message;
	TYPE type;
	public Spannable sp;
	Bitmap Image;
	URL url;
	public Message(TYPE t){
		type=t;
	}
	
	public enum TYPE{
		USER,GUEST;
	}
	
	public TYPE getType() {
		return type;
	}

	public Object getMessage() {
		return message;
	}

	public void setMessage(Object message) {
		this.message = message;
	}



	public static TYPE Match(String s){
		return TYPE.valueOf(s.toUpperCase());
	}
}
