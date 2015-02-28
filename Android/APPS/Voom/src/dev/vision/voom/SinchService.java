package dev.vision.voom;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.parse.ParseUser;
import com.sinch.android.rtc.ClientRegistration;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.SinchClientListener;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;
import com.sinch.android.rtc.messaging.MessageClient;
import com.sinch.android.rtc.messaging.MessageClientListener;
import com.sinch.android.rtc.messaging.WritableMessage;

public class SinchService extends Service implements SinchClientListener, CallClientListener {

    private final SinchServiceInterface serviceInterface = new SinchServiceInterface();
    private SinchClient sinchClient = null;
    private MessageClient messageClient = null;
	private CallClient callClient;

    private String currentUserId;
    private LocalBroadcastManager broadcaster;
    private Intent broadcastIntent = new Intent("dev.vision.voom.ListUsersActivity");
    private String regId;
    
    public static final String CALL_ID = "CALL_ID";
    static final String TAG = SinchService.class.getSimpleName();
    private StartFailedListener mListener;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        currentUserId = ParseUser.getCurrentUser().getObjectId();
        regId = intent.getStringExtra("regId");

        if (currentUserId != null && !isSinchClientStarted()) {
            startSinchClient(currentUserId);
        }

        broadcaster = LocalBroadcastManager.getInstance(this);

        return super.onStartCommand(intent, flags, startId);
    }

    public void startSinchClient(String username) {
        sinchClient = Sinch.getSinchClientBuilder().context(this).userId(username).applicationKey(CREDENTIALS.SINCH_APP_KEY)
                .applicationSecret(CREDENTIALS.SINCH_APP_SECRET).environmentHost(CREDENTIALS.SINCH_ENVIRONMENT).build();

        sinchClient.addSinchClientListener(this);

        sinchClient.setSupportMessaging(true);
        sinchClient.setSupportCalling(true);

        sinchClient.setSupportActiveConnectionInBackground(true);
        sinchClient.setSupportPushNotifications(true);
        
        
        sinchClient.getCallClient().addCallClientListener(this);

        sinchClient.checkManifest();
        sinchClient.start();
        sinchClient.registerPushNotificationData(regId.getBytes());
    }

    private boolean isSinchClientStarted() {
        return sinchClient != null && sinchClient.isStarted();
    }

    @Override
    public void onClientFailed(SinchClient client, SinchError error) {
    	if (mListener != null) {
            mListener.onStartFailed(error);
        }
        broadcastIntent.putExtra("success", false);
        broadcaster.sendBroadcast(broadcastIntent);

        sinchClient = null;
    }

    @Override
    public void onClientStarted(SinchClient client) {
    	if (mListener != null) {
            mListener.onStarted();
        }
        broadcastIntent.putExtra("success", true);
        broadcaster.sendBroadcast(broadcastIntent);

        client.startListeningOnActiveConnection();
        messageClient = client.getMessageClient();
        callClient = client.getCallClient();
    }

    @Override
    public void onClientStopped(SinchClient client) {
        sinchClient = null;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return serviceInterface;
    }

    @Override
    public void onLogMessage(int level, String area, String message) {
    	switch (level) {
        case Log.DEBUG:
            Log.d(area, message);
            break;
        case Log.ERROR:
            Log.e(area, message);
            break;
        case Log.INFO:
            Log.i(area, message);
            break;
        case Log.VERBOSE:
            Log.v(area, message);
            break;
        case Log.WARN:
            Log.w(area, message);
            break;
    }
    }

    @Override
    public void onRegistrationCredentialsRequired(SinchClient client, ClientRegistration clientRegistration) {
    }

    public void sendMessage(String recipientUserId, String textBody) {
        if (messageClient != null) {
            WritableMessage message = new WritableMessage(recipientUserId, textBody);
            messageClient.send(message);
        }
    }

    public void addMessageClientListener(MessageClientListener listener) {
        if (messageClient != null) {
            messageClient.addMessageClientListener(listener);
        }
    }

    public void removeMessageClientListener(MessageClientListener listener) {
        if (messageClient != null) {
            messageClient.removeMessageClientListener(listener);
        }
    }
    
    public Call getCall(String callId) {
        if (callClient != null)
        	return callClient.getCall(callId);
		return null;
	}

	public Call callPhoneNumber(String phoneNumber) {
		if (callClient != null)
        	return callClient.callPhoneNumber(phoneNumber);
		return null;
	}

	public Call callUser(String userId) {
		if (callClient != null)
        	return callClient.callUser(userId);
		return null;
	}

    @Override
    public void onDestroy() {
        if (ParseUser.getCurrentUser() == null) {
            sinchClient.unregisterPushNotificationData();
        }
        sinchClient.stopListeningOnActiveConnection();
        sinchClient.terminate();
    }

    @Override
    public void onIncomingCall(CallClient callClient, Call call) {
        Log.d(TAG, "Incoming call");
        Intent intent = new Intent(SinchService.this, IncomingCallScreenActivity.class);
        intent.putExtra(CALL_ID, call.getCallId());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        SinchService.this.startActivity(intent);
    }
    
    public class SinchServiceInterface extends Binder {

		public void sendMessage(String recipientUserId, String textBody) {
            SinchService.this.sendMessage(recipientUserId, textBody);
        }

        public void addMessageClientListener(MessageClientListener listener) {
            SinchService.this.addMessageClientListener(listener);
        }

        public void removeMessageClientListener(MessageClientListener listener) {
            SinchService.this.removeMessageClientListener(listener);
        }
        
        public String getUserName() {
            return currentUserId;
        }

        public void startClient(String userName) {
        	if(!isSinchClientStarted())
        		startSinchClient(userName);
        }

        public boolean isSinchClientStarted() {
            return SinchService.this.isSinchClientStarted();
        }
        
        public Call callPhoneNumber(String phoneNumber) {
            return SinchService.this.callPhoneNumber(phoneNumber);
        }

        public Call callUser(String userId) {
            return SinchService.this.callUser(userId);
        }

        public Call getCall(String callId) {
            return SinchService.this.getCall(callId);
        }
        
        public void setStartListener(StartFailedListener listener) {
            mListener = listener;
        }
    }

    public interface StartFailedListener {
        void onStartFailed(SinchError error);

        void onStarted();
    }
	
}

