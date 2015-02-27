package dev.vision.voom.classes;

import dev.vision.voom.R;

public enum Buttons{
	BACK(R.drawable.abc_ab_share_pack_holo_dark);
	
	public int ICON;
	
	Buttons(int d){
		this.setICON(d);
	}

	public int ICON() {
		return ICON;
	}

	public void setICON(int iCON) {
		ICON = iCON;
	}
	
}