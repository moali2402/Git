package dev.vision.voom.parse;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

@ParseClassName("Profiles")
public class Profiles extends ParseObject {
     
    public ParseUser getUser() {
        return getParseUser("user");
    }
     
    public void setUser(ParseUser currentUser) {
        put("user",  currentUser);
    }
    
    
    public void setDisplayName(String display_name) {
        put("display_name", display_name);
    }
     
    public String getDisplayName() {
        return getString("display_name");
    }
    
    public void setStatus(String status) {
        put("status", status);
    }
     
    public String getStatus() {
        return getString("status");
    }
    
    public void setProfilePic(String imageUrl) {
        put("imageUrl", imageUrl);
    }
     
    public String getProfilePic() {
        return getString("imageUrl");
    }

    public boolean isDefault() {
        return getBoolean("isDefault");
    }
     
    public void setDefault(boolean isDefault) {
        put("isDefault", isDefault);
    }
    
    public void assignUser(String id) {
    	JSONObject js = new JSONObject();
    	try {

	    	js.put("__type", "Pointer");
	    	js.put("className", "_User");
			js.put("objectId", id);
		} catch (JSONException e) {
			e.printStackTrace();
		}

        add("users", js);
    }
    
    public void assignUsers(ArrayList<String> ids) {
    	ArrayList<JSONObject> ajs = new ArrayList<JSONObject>();
    	try {
	    	for(String id : ids){
		    	JSONObject js = new JSONObject();
		    	js.put("__type", "Pointer");
		    	js.put("className", "_User");
		    	js.put("objectId", id);
		    	
		    	ajs.add(js);
	    	}
	    } catch (JSONException e) {
			e.printStackTrace();
		}
        add("users", ajs);
    }
    
    public void setAssignedUsers(ArrayList<String> ids) {
    	ArrayList<JSONObject> ajs = new ArrayList<JSONObject>();
    	try {
	    	for(String id : ids){
		    	JSONObject js = new JSONObject();
		    	js.put("__type", "Pointer");
		    	js.put("className", "_User");
		    	js.put("objectId", id);
		    	
		    	ajs.add(js);
	    	}
	    } catch (JSONException e) {
			e.printStackTrace();
		}
        put("users", ajs);
    }
     
	public List<ParseUser> getAssignedUsers() {
        return getList("users");
    }
            
    public static ParseQuery<Profiles> getQuery() {
        return ParseQuery.getQuery(Profiles.class);
    }
}