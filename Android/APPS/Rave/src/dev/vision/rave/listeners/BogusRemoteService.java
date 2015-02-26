package dev.vision.rave.listeners;
import java.util.ArrayList;
import java.util.List;

import dev.vision.rave.user.Post;

/**
 * Mimic the server side logic to return results with some delay
 */
public class BogusRemoteService {

	private static final String TAG = BogusRemoteService.class.getSimpleName();
	private ArrayList<Post> Entries; 
	private int sushiIdx = 0;
	
	public void setEntries(List<Post> result){
		sushiIdx = 0;
		Entries = new ArrayList<Post>();
		Entries.addAll(result);
	}
	
	public void updateEntries(Post result){
		Entries.add(0,result);
	}
	
	
	/*
	public List<Post> getNextBatch(int batchSize) {
		List<Post> results = new ArrayList<Post>();
		results.addAll(Entries.subList(sushiIdx, (sushiIdx + batchSize < Entries.size() ? sushiIdx + batchSize : Entries.size())));
    	sushiIdx +=batchSize;
		return results;
	}
	*/
	
	public List<Post> getNextBatch(int batchSize) {
		List<Post> results = new ArrayList<Post>();
		int index = sushiIdx;
		int batch = sushiIdx + batchSize < Entries.size() ? batchSize : Entries.size()-sushiIdx;
		
		//results.addAll(Entries.subList(sushiIdx, (sushiIdx + batchSize < Entries.size() ? sushiIdx + batchSize : Entries.size())));
		for(Post p : Entries.subList(sushiIdx, (sushiIdx + batchSize < Entries.size() ? sushiIdx + batchSize : Entries.size()))){
			results.add(p);
			try {
				Thread.sleep(400);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		sushiIdx +=batch;
		return results;
	}

	public void reset() {
		// Reset both pointers
		sushiIdx = 0;
	}

}