package dev.vision.voom.parse;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import dev.vision.voom.messaging.Message;


@ParseClassName("ParseMessage")
public class ParseMessage extends ParseObject{
	

	public final static String SENDER_ID = "senderId";
	public final static String RECIPIENT_ID = "recipientId";
	public final static String MESSAGE_TEXT = "messageText";
	public final static String SINCH_ID = "sinchId";
	public final static String STATUS = "status";
	public final static String CREATED_AT = "createdAt";

	
	public String getSenderId() {
        return getString(SENDER_ID);
    }
     
    public void setSenderId(String senderId) {
        put(SENDER_ID,  senderId);
    }
    
    public void setRecipientId(String recipientId) {
        put(RECIPIENT_ID, recipientId);
    }
     
    public String getRecipientId() {
        return getString(RECIPIENT_ID);
    }
    
    public void setMessage(String messageBody) {
        put(MESSAGE_TEXT, messageBody);
    }
     
    public String getMessage() {
        return getString(MESSAGE_TEXT);
    }
    
    public void setSinchId(String sinchId) {
        put(SINCH_ID, sinchId);
    }
     
    public String getSinchId() {
        return getString(SINCH_ID);
    }
    
    public void setStatus(Message.STATUS status) {
        put(STATUS, status.name());
    }
     
    public Message.STATUS getStatus() {
        return Message.STATUS.valueOf(getString(STATUS));
    }
    
    public static ParseQuery<ParseMessage> getQuery() {
        return ParseQuery.getQuery(ParseMessage.class);
    }
}