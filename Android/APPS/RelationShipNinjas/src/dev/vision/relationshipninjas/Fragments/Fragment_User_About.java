package dev.vision.relationshipninjas.Fragments;

import dev.vision.relationshipninjas.R;
import dev.vision.relationshipninjas.API.API;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
 class Fragment_User_About extends Fragment {
	 TextView name, email, gender, birthday;
	 @Override
		public View onCreateView(LayoutInflater inf, ViewGroup container,Bundle savedInstanceState){
			super.onCreateView(inf, container, savedInstanceState);
			View v= inf.inflate(R.layout.user_about, null);
			name = (TextView) v.findViewById(R.id.name);
			email = (TextView) v.findViewById(R.id.email);
			gender = (TextView) v.findViewById(R.id.gender);
			birthday = (TextView) v.findViewById(R.id.birthday);

			return v;
		}
		@Override
        public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			name.setText(API.user.getName());
			email.setText(API.user.getEmail());
			gender.setText(API.user.getGender());
			birthday.setText(API.user.getBirthday());

		}
    }