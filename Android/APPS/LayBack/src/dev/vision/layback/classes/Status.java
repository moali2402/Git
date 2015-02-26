package dev.vision.layback.classes;

import android.graphics.Color;

public enum Status{
	ONLINE,OFFLINE,BUSY,AWAY;
	
	public int getColor(){
		int color=-1;
		switch (this) {
		case ONLINE:
			color= Color.GREEN;
			break;
		case OFFLINE:
			color= Color.GRAY;
			break;
		case BUSY:
			color= Color.RED;
			break;
		case AWAY:
			color= Color.YELLOW;
			break;
		default:
			color= Color.GRAY;
			break;
		}
		return color;
	}
	
	public static Status Match(String s){
		return valueOf(s.toUpperCase());
	}
}
