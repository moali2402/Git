package itworx.github.task;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;


public class RepositoryListActivity extends ActionBarActivity
        implements RepositoryListFragment.Callbacks {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private RepositoryListFragment rlf;
    
    @SuppressLint("InlinedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1)
        	getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        setContentView(R.layout.activity_repository_list);
        
        ActionBar ab = getSupportActionBar();
        ab.setSubtitle("ItWorx-Task");
        ab.setTitle("GitHub-BlackBerry");
       
        rlf = ((RepositoryListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.repository_list));
           

        if (findViewById(R.id.repository_detail_container) != null) {
            //If this view is present, then the
            // activity should be in two-pane mode. 
        	//(res/values-large and res/values-sw600dp)
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            rlf.setActivateOnItemClick(true);
        }

        
    }

    /**
     * Callback method from {@link RepositoryListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(String id) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(RepositoryDetailFragment.REPO_ID, id);
            RepositoryDetailFragment fragment = new RepositoryDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.repository_detail_container, fragment)
                    .commit();

        } else {
            // In single-pane mode, start the Repository detail activity
            Intent detailIntent = new Intent(this, RepositoryDetailActivity.class);
            detailIntent.putExtra(RepositoryDetailFragment.REPO_ID, id);
            startActivity(detailIntent);
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.menu, menu);
    	return true;
    } 
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	      switch (item.getItemId()) {
		      case R.id.action_refresh:
		    	rlf.refresh();
		        break;
		      default:
		        break;
	      }
	      return true;
    }
}
