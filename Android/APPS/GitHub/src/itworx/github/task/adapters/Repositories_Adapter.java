package itworx.github.task.adapters;

import itworx.github.task.R;
import itworx.github.task.repositories.Repositories;
import itworx.github.task.repositories.Repository;
import java.util.List;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Repositories_Adapter extends BaseAdapter {

	 	private List<Repository> repos;
		private LayoutInflater inflater;
	 	
		public Repositories_Adapter(Context ctx){
			this.inflater = LayoutInflater.from(ctx);
	 		this.repos = Repositories.REPOSITORIES;
	 	}
		@Override
		public int getCount() {
			return this.repos.size();
		}

		@Override
		public Repository getItem(int position) {
			return this.repos.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder vh;
			if(convertView == null){
				vh= new ViewHolder();
				convertView = inflater.inflate(R.layout.activity_repository_list_item, null);
				
				vh.title = (TextView) convertView.findViewById(R.id.title);
				vh.desc = (TextView) convertView.findViewById(R.id.description);
				vh.login = (TextView) convertView.findViewById(R.id.login);

				convertView.setTag(vh);
			}else{
				vh = (ViewHolder) convertView.getTag();
			}
			
			Repository rs =  getItem(position);
			
			vh.title.setText(rs.getName());
			vh.desc.setText(rs.getDescription());
			vh.login.setText(rs.getOwner().getLogin());
			
			if(rs.isFork()){
				convertView.setBackgroundColor(Color.LTGRAY);
			}else{
				convertView.setBackgroundResource(0);
			}

			return convertView;
		}
		
		class ViewHolder{
			private TextView title;
			private TextView desc;
			private TextView login;
		}
    	
}