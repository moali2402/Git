package dev.vision.voom.parse;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import dev.vision.voom.messaging.Message.STATUS;

@ParseClassName("ParseMessage")
public class ParseMessage extends ParseObject{
	
	public String getSenderId() {
        return getString("senderId");
    }
     
    public void setSenderId(String senderId) {
        put("senderId",  senderId);
    }
    
    
    public void setRecipientId(String recipientId) {
        put("recipientId", recipientId);
    }
     
    public String getRecipientId() {
        return getString("recipientId");
    }
    
    public void setStatus(STATUS status) {
        put("status", status.name());
    }
     
    public STATUS getStatus() {
        return STATUS.valueOf(getString("status"));
    }
    
    public void setMessage(String messageBody) {
        put("messageText", messageBody);
    }
     
    public String getMessage() {
        return getString("messageText");
    }
    
    public void setSinchId(String sinchId) {
        put("sinchId", sinchId);
    }
     
    public String getSinchId() {
        return getString("sinchId");
    }
    
    public static ParseQuery<ParseMessage> getQuery() {
        return ParseQuery.getQuery(ParseMessage.class);
    }
}