package dev.vision.voom.parse;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Toast;

import com.dexafree.materialList.cards.HexImageCard;
import com.dexafree.materialList.cards.OnButtonPressListener;
import com.dexafree.materialList.cards.SimpleCard;
import com.dexafree.materialList.model.Card;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import dev.vision.voom.R;

@ParseClassName("Profiles")
public class Profiles extends ParseObject {
     
	public static final String ID = "objectId";
	public static final String USER = "user";
	public static final String DISPLAY_NAME = "display_name";
	public static final String IMAGE_URL = "imageUrl";
	public static final String STATUS = "status";
	public static final String USERS = "users";
	public static final String IS_DEFAULT = "isDefault";


	public ParseUser getUser() {
        return getParseUser(USER);
    }
     
    public void setUser(ParseUser currentUser) {
        put(USER,  currentUser);
    }
    
    public void setDisplayName(String display_name) {
        put(DISPLAY_NAME, display_name);
    }
     
    public String getDisplayName() {
        return getString(DISPLAY_NAME);
    }
    
    public void setStatus(String status) {
        put(STATUS, status);
    }
     
    public String getStatus() {
        return getString(STATUS);
    }
    
    public void setProfilePic(String imageUrl) {
        put(IMAGE_URL, imageUrl);
    }
     
    public String getProfilePic() {
        return getString(IMAGE_URL);
    }

    public boolean isDefault() {
        return getBoolean(IS_DEFAULT);
    }
     
    public void setDefault(boolean isDefault) {
        put(IS_DEFAULT, isDefault);
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

        add(USERS, js);
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
        add(USERS, ajs);
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
        put(USERS, ajs);
    }
     
	public List<ParseUser> getAssignedUsers() {
        return getList(USERS);
    }
            
    public static ParseQuery<Profiles> getQuery() {
        return ParseQuery.getQuery(Profiles.class);
    }

    public Card buildCard(Context c) {
        SimpleCard card = new HexImageCard(c);
        card.setTitle(getDisplayName());
        card.setDescription(getStatus());
        card.setDrawable(getProfilePic());
        card.setDefault(isDefault());
        card.setTitleColor(Color.DKGRAY);
        ((HexImageCard) card).setRightButtonIcon(R.drawable.contact);

        ((HexImageCard) card).setOnRightButtonPressedListener(new OnButtonPressListener() {
            @Override
            public void onButtonPressedListener(View view, Card card) {
            }
        });
        
        ((HexImageCard) card).setOnLeftButtonPressedListener(new OnButtonPressListener() {
            @Override
            public void onButtonPressedListener(View view, Card card) {
            }
        });
		return card;
    }
}