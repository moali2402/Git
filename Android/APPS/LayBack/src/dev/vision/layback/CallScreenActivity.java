package dev.vision.layback;

import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallEndCause;
import com.sinch.android.rtc.calling.CallListener;
import com.sinch.android.rtc.calling.CallState;

import dev.vision.layback.classes.Contact;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class CallScreenActivity extends BaseActivity {

    static final String TAG = CallScreenActivity.class.getSimpleName();

    private AudioPlayer mAudioPlayer;
    private Timer mTimer;
    private UpdateCallDurationTask mDurationTask;

    private String mCallId;
    private long mCallStart = 0;

    private TextView mCallDuration;
    private TextView mCallState;
    private TextView mCallerName;

    private class UpdateCallDurationTask extends TimerTask {

        @Override
        public void run() {
            CallScreenActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateCallDuration();
                }
            });
        }
    }
    ImageView loud,mute,hide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.callscreen);
        int c = getResources().getColor(R.color.sinch_purple);
        
        mAudioPlayer = new AudioPlayer(this);
        mCallDuration = (TextView) findViewById(R.id.callDuration);
        mCallerName = (TextView) findViewById(R.id.remoteUser);
        mCallState = (TextView) findViewById(R.id.callState);
        loud=(ImageView)findViewById(R.id.loud);
        mute=(ImageView)findViewById(R.id.mute);
        hide=(ImageView)findViewById(R.id.hide);
        Button endCallButton = (Button) findViewById(R.id.hangupButton);

        
        loud.setColorFilter(c, Mode.SRC_ATOP);
        mute.setColorFilter(c, Mode.SRC_ATOP);
        hide.setColorFilter(c, Mode.SRC_ATOP);
        toggleEnabled(false);
        
        mute.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				v.setPressed(mAudioPlayer.toggleMute());
					
			}
		});

        loud.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				v.setPressed(mAudioPlayer.toggleLoudSpeaker());
					
			}
		});

        endCallButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                endCall();
            }
        });
        mCallStart = System.currentTimeMillis();
        mCallId = getIntent().getStringExtra(SinchService.CALL_ID);
    }

    @Override
    public void onServiceConnected() {
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            call.addCallListener(new SinchCallListener());
            String number = call.getRemoteUserId();
            Contact c = Static.Contacts.getContact(number);
            String callerId = c != null ? c.getName() : number;
            mCallerName.setText(callerId);
            if(call.getState() == CallState.INITIATING)
            mCallState.setText("Calling..");
        } else {
            Log.e(TAG, "Started with invalid callId, aborting.");
            finish();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mDurationTask.cancel();
        mTimer.cancel();
    }

    @Override
    public void onResume() {
        super.onResume();
        mTimer = new Timer();
        mDurationTask = new UpdateCallDurationTask();
        mTimer.schedule(mDurationTask, 0, 500);
    }
    
    @Override
    public void onBackPressed() {
        // User should exit activity by ending call, not by going back.
    }

    private void endCall() {
        mAudioPlayer.stopProgressTone();
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            call.hangup();
        }
        finish();
    }

    private String formatTimespan(long timespan) {
        long totalSeconds = timespan / 1000;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        return String.format(Locale.US, "%02d:%02d", minutes, seconds);
    }

    private void updateCallDuration() {
        if (mCallStart > 0) {
            mCallDuration.setText(formatTimespan(System.currentTimeMillis() - mCallStart));
        }
    }

    private class SinchCallListener implements CallListener {

        @Override
        public void onCallEnded(Call call) {
            CallEndCause cause = call.getDetails().getEndCause();
            Log.d(TAG, "Call ended. Reason: " + cause.toString());
            mAudioPlayer.stopProgressTone();
            setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
            String state = "Call Ended";
            switch (call.getDetails().getEndCause()){
			case CANCELED:
				state = "Cancelled";
				break;
			case DENIED:
				state = "Busy..";
				break;
			case FAILURE:
				state = "Failed!";
				break;
			case NO_ANSWER:
				state = "No Answer!";
				break;
			case OTHER_DEVICE_ANSWERED:
				state = "Answered from other device";
				break;
			case TIMEOUT:
				state = "Failed!";
				break;
			case TRANSFERRED:
				state = "Transferred";
				break;
			default:
				state = "Call Ended";
				break;
            	
            }
            mCallState.setText(state);

            //String endMsg = "Call ended: " + .toString();
            //Toast.makeText(CallScreenActivity.this, endMsg, Toast.LENGTH_LONG).show();
            endCall();
        }

        @Override
        public void onCallEstablished(Call call) {
            Log.d(TAG, "Call established");
            mAudioPlayer.stopProgressTone();
            mCallState.setText("Connected");//call.getState().toString());
            setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
            mCallStart = System.currentTimeMillis();
            toggleEnabled(true);
        }

        @Override
        public void onCallProgressing(Call call) {
            Log.d(TAG, "Call progressing");
            mCallState.setText("Ringing...");
            mAudioPlayer.playProgressTone();
        }

        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> pushPairs) {
            // Send a push through your push provider here, e.g. GCM
        }
    }

	public void toggleEnabled(boolean b) {
		mute.setEnabled(b);
        loud.setEnabled(b);		
	}
}
