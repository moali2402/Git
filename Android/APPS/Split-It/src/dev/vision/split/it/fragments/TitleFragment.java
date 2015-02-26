package dev.vision.split.it.fragments;

import android.support.v4.app.Fragment;

public class TitleFragment extends Fragment {

	String title;
	
	public TitleFragment() {
	}
	
	public TitleFragment(String title) {
		super();
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

}
