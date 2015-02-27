package dev.vision.voom.classes;

import android.graphics.Color;

public enum STATUS{
	ONLINE(Color.GREEN),OFFLINE(Color.GRAY),TYPING(Color.BLUE);
	
	public int COLOR;
	
	STATUS(int c){
		this.COLOR = c;
	}
}
