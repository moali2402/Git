package dev.vision.relationshipninjas.Classes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Relationships extends ArrayList<Relationship> {
	
	
	
	public interface DataChangeListener {
		public void refresh();
	}

	HashMap<String, Relationship> rMap= new HashMap<String, Relationship>();
	DataChangeListener onDataChangeListener;
	
	public Relationships() {
		super();
	}

	public Relationships(Collection<? extends Relationship> collection) {
		super(collection);
	}

	public Relationships(int capacity) {
		super(capacity);
	}
	
	public void setOnDataChangeListener(DataChangeListener onDataChangeListener) {
		this.onDataChangeListener = onDataChangeListener;
	}
	
	@Override
	public boolean add(Relationship object){
		if(super.add(object)){
			rMap.put(object.getId(), object);
			return true;
		}
		return false;
	}
	
	@Override
	public int size(){
		return super.size();
	}
	
	@Override
	public void clear(){
		rMap.clear();
		super.clear();
	}
	
	public Relationship get(String object) {
		return rMap.get(object);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void refresh() {
		onDataChangeListener.refresh();		
	}

	

}
