package dev.vision.voom.classes;


import com.parse.ParseQuery;
import com.parse.ParseUser;

public class Contact extends ParseUser{
	
	public static final String ID = "objectId";

	/*
	ArrayList<Profiles> profile = new ArrayList<Profiles>();
	
	
    private ParseRelation<Profiles> getProfileRelation() {
    	return getRelation("profiles");
    }
        	    
    public ParseQuery<Profiles> getProfileQuery() {
    	ParseRelation<Profiles> relation = getProfileRelation();
    	return relation.getQuery();
    }
    
    
    
    public void addProfile(Profiles p) {
    	put("profiles", p);
    	//saveInBackground();
    }
   
    public ParseQuery<Profiles> getProfile() {
    	ParseQuery<Profiles> usersQ = getProfileQuery();
    	usersQ.whereEqualTo("users", getCurrentUser());
    	
    	ParseQuery<Profiles> defaultQ = getProfileQuery();
    	defaultQ.whereEqualTo("isDefault", true);
    	
    	ArrayList<ParseQuery<Profiles>> qu = new ArrayList<ParseQuery<Profiles>>();
    	qu.add(usersQ);
    	qu.add(defaultQ);
        ParseQuery<Profiles> finalQuery = ParseQuery.or(qu);

    	return finalQuery;
    }
    */
    
	/*
    public void addProfile(Profiles p) {
    	ParseRelation<Profiles> relation = getProfileRelation();
    	relation.add(p);
    	saveInBackground();
    }
    */
       
    public static ParseQuery<Contact> getUpdatedQuery() {
        return ParseQuery.getQuery(Contact.class);
    }
    
}