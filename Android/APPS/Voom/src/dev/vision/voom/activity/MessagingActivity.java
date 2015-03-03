package dev.vision.voom.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.messaging.Message;
import com.sinch.android.rtc.messaging.MessageClient;
import com.sinch.android.rtc.messaging.MessageClientListener;
import com.sinch.android.rtc.messaging.MessageDeliveryInfo;
import com.sinch.android.rtc.messaging.MessageFailureInfo;
import com.sinch.android.rtc.messaging.WritableMessage;

import dev.vision.voom.R;
import dev.vision.voom.SinchService;
import dev.vision.voom.adapters.MessageAdapter;
import dev.vision.voom.parse.ParseMessage;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MessagingActivity extends BaseActivity {

    private String recipientId;
    private EditText messageBodyField;
    private String messageBody;
    private SinchService.SinchServiceInterface messageService;
    private MessageAdapter messageAdapter;
    private ListView messagesList;
    private String currentUserId;
    private MessageClientListener messageClientListener = new MyMessageClientListener();
    //private ArrayList<String> regIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messaging);

        //bindService(new Intent(this, SinchService.class), this, BIND_AUTO_CREATE);

        Intent intent = getIntent();
        recipientId = intent.getStringExtra("RECIPIENT_ID");
        currentUserId = ParseUser.getCurrentUser().getObjectId();
        
        Toast.makeText(getApplicationContext(), recipientId + " / " +currentUserId , Toast.LENGTH_SHORT).show();
        
        messagesList = (ListView) findViewById(R.id.listMessages);
        messageAdapter = new MessageAdapter(this);
        messagesList.setAdapter(messageAdapter);
        populateMessageHistory();

        messageBodyField = (EditText) findViewById(R.id.messageBodyField);

        findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
    }

    //get previous messages from parse & display
    private void populateMessageHistory() {
        String[] userIds = {currentUserId, recipientId};
        ParseQuery<ParseMessage> query = ParseMessage.getQuery();
        query.whereContainedIn(ParseMessage.SENDER_ID, Arrays.asList(userIds));
        query.whereContainedIn(ParseMessage.RECIPIENT_ID, Arrays.asList(userIds));
        query.orderByAscending(ParseMessage.CREATED_AT);
        query.findInBackground(new FindCallback<ParseMessage>() {
            @Override
            public void done(List<ParseMessage> messageList, com.parse.ParseException e) {
                if (e == null) {
                    for (int i = 0; i < messageList.size(); i++) {
                        WritableMessage message = new WritableMessage(messageList.get(i).getRecipientId(), messageList.get(i).getMessage());
                        if (messageList.get(i).getSenderId().equals(currentUserId)) {
                            messageAdapter.addMessage(message, MessageAdapter.DIRECTION_OUTGOING);
                        } else {
                            messageAdapter.addMessage(message, MessageAdapter.DIRECTION_INCOMING);
                        }
                    }
                }
            }
        });
    }

    private void sendMessage() {
        messageBody = messageBodyField.getText().toString();
        if (messageBody == null || messageBody.length() == 0) {
            Toast.makeText(this, "Please enter a message", Toast.LENGTH_LONG).show();
            return;
        }

        final WritableMessage writableMessage = new WritableMessage(recipientId, messageBody);
        messageAdapter.addMessage(writableMessage, MessageAdapter.DIRECTION_OUTGOING);

        messageService.sendMessage(recipientId, messageBody);
        messageBodyField.setText("");
    }

    @Override
    public void onDestroy() {
        messageService.removeMessageClientListener(messageClientListener);
        //unbindService(this);
        super.onDestroy();
    }

    @Override
    public void onServiceConnected() {
        messageService = getSinchServiceInterface();
        messageService.addMessageClientListener(messageClientListener);
    }

    @Override
    public void onServiceDisconnected() {
        messageService = null;
    }

    private class MyMessageClientListener implements MessageClientListener {
        @Override
        public void onMessageFailed(MessageClient client, Message message,
                                    MessageFailureInfo failureInfo) {
            Toast.makeText(MessagingActivity.this, "Message failed to send. " + failureInfo.getSinchError().getMessage().toString() , Toast.LENGTH_LONG).show();
        }

        @Override
        public void onIncomingMessage(MessageClient client, Message message) {
            if (message.getSenderId().equals(recipientId)) {
                WritableMessage writableMessage = new WritableMessage(message.getRecipientIds().get(0), message.getTextBody());
                messageAdapter.addMessage(writableMessage, MessageAdapter.DIRECTION_INCOMING);
            }
        }

        @Override
        public void onMessageSent(MessageClient client, Message message, String recipientId) {

            final WritableMessage writableMessage = new WritableMessage(message.getRecipientIds().get(0), message.getTextBody());
            messageAdapter.changeStatus(message);
            
            //only add message to parse database if it doesn't already exist there
            ParseQuery<ParseMessage> query = ParseMessage.getQuery();
            query.whereEqualTo(ParseMessage.SINCH_ID, message.getMessageId());
            query.findInBackground(new FindCallback<ParseMessage>() {
                @Override
                public void done(List<ParseMessage> messageList, com.parse.ParseException e) {
                    if (e == null) {
                        if (messageList.size() == 0) {
                        	ParseMessage parseMessage = new ParseMessage();
                            parseMessage.setSenderId(currentUserId);
                            parseMessage.setRecipientId(writableMessage.getRecipientIds().get(0));
                            parseMessage.setMessage(writableMessage.getTextBody());
                            parseMessage.setSinchId(writableMessage.getMessageId());
                            parseMessage.saveInBackground();

                           // messageAdapter.addMessage(writableMessage, MessageAdapter.DIRECTION_OUTGOING);
                        }
                    }
                }
            });
        }

        @Override
        public void onMessageDelivered(MessageClient client, MessageDeliveryInfo deliveryInfo) {}

        @Override
        public void onShouldSendPushData(MessageClient client, Message message, List<PushPair> pushPairs) {
            final String regId = new String(pushPairs.get(0).getPushData());

            class SendPushTask extends AsyncTask<Void, Void, Void> {

                @Override
                protected Void doInBackground(Void... voids) {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost("http://your-domain.com?reg_id=" + regId);

                    try {
                        HttpResponse response = httpclient.execute(httppost);
                        ResponseHandler<String> handler = new BasicResponseHandler();
                        Log.d("HttpResponse", handler.handleResponse(response));
                    } catch (ClientProtocolException e) {
                        Log.d("ClientProtocolException", e.toString());
                    } catch (IOException e) {
                        Log.d("IOException", e.toString());
                    }

                    return null;
                }

            }

            (new SendPushTask()).execute();

        }
    }
}




