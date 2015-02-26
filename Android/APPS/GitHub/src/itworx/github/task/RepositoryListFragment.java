package itworx.github.task;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.TypedValue;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import itworx.github.task.adapters.Repositories_Adapter;
import itworx.github.task.repositories.Repositories;
import itworx.github.task.repositories.Repository;

/**
 * A list fragment representing a list of Repositories. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link RepositoryDetailFragment}.
 * <p>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
/**
 * @author Mo
 *
 */
public class RepositoryListFragment extends ListFragment {
	
	//Starting Page for Pagination
	private static int current_page = 1;
		
	//MAX REPOSITORES per pager
	private static int PER_PAGE = 10;

	//API To Retrieve BLACKBERRY Repositories
	private static String API = "https://api.github.com/orgs/blackberry/repos?per_page="+PER_PAGE+"&page=";
	
	//Activity Context
	private RepositoryListActivity ctx;
	
	// lv, the ListFragment list will be assigned to lv
	private ListView lv;

    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private Callbacks mCallbacks = listCallBack;

    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;

    /**
     * The LoadMore Button to be attached as ListView Footer.
     */
	private Button loadMore_Button;
	
	/**
     * The ActionBar Height.
     */
	private int actionBarHeight;

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        public void onItemSelected(String id);
    }

    /**
     * A  implementation of the {@link Callbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static Callbacks listCallBack = new Callbacks() {
        @Override
        public void onItemSelected(String id) {
        }
    };
    

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RepositoryListFragment() {
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        ctx = (RepositoryListActivity) getActivity();

        lv = getListView();

        setEmptyText("No Data Available");

        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
        
        TypedValue tv = new TypedValue();
		if (ctx.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
        {
            actionBarHeight  = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
        }
        
        
    }
    
   
    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        current_page = 1;
        
        lv.setOnScrollListener(new OnScrollListener() {
            int mLastFirstVisibleItem = 0;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {   }           

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {    
            	
                if (view.getId() == lv.getId()) {
                    final int currentFirstVisibleItem = lv.getFirstVisiblePosition();

                    if (currentFirstVisibleItem > mLastFirstVisibleItem) {
                        ctx.getSupportActionBar().hide();
                    } else if (currentFirstVisibleItem < mLastFirstVisibleItem) {
                    	ctx.getSupportActionBar().show();
                    }

                    mLastFirstVisibleItem = currentFirstVisibleItem;
                }               

            }
        });
        
        //Only If Overlay Action Bar is Set
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1){
	        //Dummy Header
	        View header =  new View(ctx);
	        header.setLayoutParams(new ListView.LayoutParams(0, actionBarHeight));
	        
			//Add Header and Footer to List 
	        lv.addHeaderView(header);
        }
        
        //Create New Button to be added as ListView Footer to loadMore Repos.
        loadMore_Button = new Button(ctx);
        loadMore_Button.setText("Load More");

		lv.addFooterView(loadMore_Button);
		
		
		loadMore_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(isConnected())
                new loadMoreRepositories().execute();
            }
        });
		 
		//Load Offline Data. If Any Available
		Writer_Reader.Init(ctx);

        setListAdapter(new Repositories_Adapter(ctx));
       
        lv.removeFooterView(loadMore_Button);
        
        if(isConnected())
        new LoadRepositories().execute();
    }
    
    /*
     * Checks if there is Any Internet Connection Available or Not
     */
    private boolean isConnected(){
    	boolean isConnected = Utils.isNetworkConnected(ctx);
    	if(!isConnected)
            buildDialog().show();
    	return isConnected;
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mCallbacks = listCallBack;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        int posWithoutHeader = position- listView.getHeaderViewsCount();
        mCallbacks.onItemSelected(Repositories.REPOSITORIES.get(posWithoutHeader).getId());

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != AdapterView.INVALID_POSITION) {
            // Serialize and persist the activated position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        getListView().setChoiceMode(activateOnItemClick
                ? AbsListView.CHOICE_MODE_SINGLE
                : AbsListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == AdapterView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }
    
    //Run a new Thread to initate creating XML File
    private void save(){
    	Runnable r = new Runnable()
    	{
    	    @Override
    	    public void run()
    	    {
				Writer_Reader.createFile();
    	    }
    	};

    	Thread t = new Thread(r);
    	t.start();
    }

    /*
     * Refresh the ListView Data.
     */
	public void refresh(){
        if(isConnected()){
			current_page = 1;
			lv.removeFooterView(loadMore_Button);
		    new LoadRepositories().execute();
        }
	}
    
    /*
     * Show Not Connected Dialog
     */
	public AlertDialog.Builder buildDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle("No Internet connection.");
        builder.setMessage("You have no internet connection");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        return builder;
    }

	/**
     * @author Mo
     * LoadRepositories extends Async to do backgroundTask 
     * to Retrieve Repositories.
     */
    class LoadRepositories extends AsyncTask<Void, Boolean, Boolean>{

    	private List<Repository> REPOSITORIES = new ArrayList<Repository>();
    	private Map<String, Repository> REPOSITORY_MAP = new HashMap<String, Repository>();

    	@Override
		protected void onPreExecute() {
        	ctx.getSupportActionBar().show();
        	ctx.setSupportProgressBarIndeterminateVisibility(true);
		}
    	
		@Override
		protected Boolean doInBackground(Void... params) {
			DefaultHttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet(API+current_page);
			boolean isMore = false;

	       
			try{
				
				HttpResponse httpResponse = client.execute(request);
		
				
				if(httpResponse.getStatusLine().getStatusCode() == 200){
					
					BufferedReader rd = new BufferedReader(
						new InputStreamReader(httpResponse.getEntity().getContent()));
				 
					StringBuffer result = new StringBuffer();
					String line = "";
					while ((line = rd.readLine()) != null) {
						result.append(line);
					}

					JSONArray js = new JSONArray(result.toString());
					int size = js.length();
					//Log.d("Result", ""+size);
					for(int i=0 ; i<size; i++ )
					{
						JSONObject jo = (JSONObject) js.get(i);
						Repository rs = new Repository(jo);
						addRepository(rs);						
					}
					String s = httpResponse.getFirstHeader("Link").getValue();
					isMore = s.contains("next");

				}
					
			}catch (Exception e){
				e.printStackTrace();
			}
			return isMore;
		}
		
		@Override
		public void onPostExecute(final Boolean isMore){
			ctx.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						if(!isMore.booleanValue()){
							lv.removeFooterView(loadMore_Button);
						}else{
					        lv.addFooterView(loadMore_Button);
						}
						if(REPOSITORIES.size() > 0){
							Repositories.clearRepository();
							Repositories.addRepository(REPOSITORIES, REPOSITORY_MAP);
							save();
						}
						((Repositories_Adapter)getListAdapter()).notifyDataSetChanged();
		            	ctx.setSupportProgressBarIndeterminateVisibility(false);

					}
			});
		}
		
		public void addRepository(Repository REPOSITORY) {
			REPOSITORIES.add(REPOSITORY);
			REPOSITORY_MAP.put(REPOSITORY.getId(), REPOSITORY);
		}
    	
    }
    
    
    /**
     * @author Mo
     * loadMoreRepositories extends Async to do backgroundTask 
     * to Retrieve CurrentPage + 1 (Extra 10) Repositories.
     */
    private class loadMoreRepositories extends AsyncTask<Void, Boolean, Boolean> {

		@Override
		protected void onPreExecute() {
        	ctx.getSupportActionBar().show();
        	ctx.setSupportProgressBarIndeterminateVisibility(true);
		}

		@Override
		protected Boolean doInBackground(Void... unused) {
			current_page ++;
			DefaultHttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet(API+current_page);
			boolean isMore = false;

			try{
				final HttpResponse httpResponse = client.execute(request);
		
				if(httpResponse.getStatusLine().getStatusCode() == 200){
					
					BufferedReader rd = new BufferedReader(
						new InputStreamReader(httpResponse.getEntity().getContent()));
				 
					StringBuffer result = new StringBuffer();
					String line = "";
					while ((line = rd.readLine()) != null) {
						result.append(line);
					}
					
					String res_s =result.toString();

					
					JSONArray js = new JSONArray(res_s);

					int size = js.length();
					//Log.d("Result", ""+size);
					for(int i=0 ; i<size; i++ )
					{
						JSONObject jo = (JSONObject) js.get(i);
						Repository rs = new Repository(jo);
						Repositories.addRepository(rs);						
					}
					
					String s = httpResponse.getFirstHeader("Link").getValue();
					isMore = s.contains("next");

				}else{
					current_page--;
				}
					
			}catch (Exception e){
				current_page--;
				e.printStackTrace();
			}
					
				
			return isMore;
		}		

		@Override
		protected void onPostExecute(final Boolean isMore) {
			ctx.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					int currentPosition = lv.getFirstVisiblePosition();
					lv.setSelectionFromTop(currentPosition + 1, 0);

					if(!isMore.booleanValue()){
						lv.removeFooterView(loadMore_Button);
					}			
					save();
					
					((Repositories_Adapter) getListAdapter()).notifyDataSetChanged();

	            	ctx.setSupportProgressBarIndeterminateVisibility(false);

				}
			});
			
		}
	}
    
        
    
}
