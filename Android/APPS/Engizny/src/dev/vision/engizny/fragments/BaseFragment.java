package dev.vision.engizny.fragments;

import dev.vision.engizny.Engizny;
import dev.vision.engizny.R;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;

public abstract class BaseFragment extends Fragment{
	
	public abstract int getIcon();

	public abstract String getTitle();
	
	public View v;
	private ImageView icon;
	public ListView list;

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		v= inflater.inflate(R.layout.fragment_base, null);
		icon = (ImageView) v.findViewById(R.id.menu);
		list = (ListView) v.findViewById(R.id.listView1);

		setIcon();

		icon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((Engizny) getActivity()).toggle();				
			}
		});
		return v;
	}
	
	public void setIcon(){
		icon.setImageResource(getIcon());
	}

}
