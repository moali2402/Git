package itworx.github.task;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import itworx.github.task.imageloader.ImageLoader;
import itworx.github.task.repositories.Repositories;
import itworx.github.task.repositories.Repository;

public class RepositoryDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String REPO_ID = "REPO_ID";

    /**
     * The content this fragment is presenting.
     */
    private Repository repository;

	private TextView title;
	private ImageLoader imgLoader;
	private ImageView owner_image;
	private TextView html;

	private TextView cdate;
	
    
    public RepositoryDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imgLoader = new ImageLoader(getActivity());

        if (getArguments().containsKey(REPO_ID)) {
        	String id = getArguments().getString(REPO_ID);
        	repository = Repositories.REPOSITORY_MAP.get(id);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_repository_detail, container, false);

        title  = ((TextView) rootView.findViewById(R.id.repository_title));
        owner_image = (ImageView) rootView.findViewById(R.id.owner_image);
        html  = ((TextView) rootView.findViewById(R.id.html_url));
        cdate  = ((TextView) rootView.findViewById(R.id.cdate));

        
        return rootView;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (repository != null) {
            final String url = repository.getHtml_url();

        	String[] st = repository.getFull_name().split("/");
            Spanned fullName =  Html.fromHtml(st[0]+"<b>/"+ st[1] + "</b>");

        	title.setText(fullName); 
        	String s;
        	try {

				DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
				DateFormat output = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss a", Locale.US);
				Date d = sdf.parse(repository.getCreated_at());
				String formattedTime = output.format(d);
				
				s = formattedTime;
	            cdate.setText("Created On : "+ s);

			} catch (ParseException e) {
				e.printStackTrace();
			}
            html.setText(url);
            
            imgLoader.DisplayImage(repository.getOwner().getAvatar_url(), 0, owner_image);


            html.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(url != null){
						Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
				        startActivity(browserIntent);
					}
				}
			});
        }
    }
}
