package dev.vision.rave.nfc;

import java.util.ArrayList;
import java.util.List;
import dev.vision.rave.Abc;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.GINGERBREAD_MR1) 
public class Nfc extends Abc {
	
	private NfcAdapter mNfcAdapter;
	private PendingIntent mNfcPendingIntent;
	private IntentFilter[] mNdefExchangeFilters;
	private boolean mWriteMode;

	@Override
	public void onCreate(Bundle b){
		super.onCreate(b);
		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
		mNfcPendingIntent = PendingIntent.getActivity(this, 0,
		    new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		// Intent filters for exchanging over p2p.
		IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
		try {
		    ndefDetected.addDataType("text/plain");
		} catch (MalformedMimeTypeException e) {
		}
		mNdefExchangeFilters = new IntentFilter[] { ndefDetected };
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
	    // NDEF exchange mode
	    if (!mWriteMode && NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
	        List<NdefMessage> msgs = NfcUtils.getMessagesFromIntent(intent);
	        fireFriendRequest(getFromRecords(msgs.get(0)));
	        Toast.makeText(this, "sent friend request via nfc!", Toast.LENGTH_LONG).show();
	    }
	}


	private void fireFriendRequest(List<String> fromRecords) {
		// TODO Auto-generated method stub
		
	}

	List<String> getFromRecords(NdefMessage message){
		List<String> payloadStrings = new ArrayList<String>();

		for (NdefRecord record : message.getRecords()) {
			byte[] payload = record.getPayload();
			String payloadString = new String(payload);

			if (!TextUtils.isEmpty(payloadString))
				payloadStrings.add(payloadString);
		}
		return payloadStrings;
	}
	
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi") 
	private void enableNdefExchangeMode() {
		int build = Build.VERSION.SDK_INT;
		if(build > Build.VERSION_CODES.HONEYCOMB_MR2){
			 mNfcAdapter.setNdefPushMessage(
				        NfcUtils.createMessage("text/plain", user.getUsername().getBytes()),this);
				  
		}else if(build>Build.VERSION_CODES.GINGERBREAD){
		    mNfcAdapter.enableForegroundNdefPush(this,
		        NfcUtils.createMessage("text/plain", user.getUsername().getBytes()));
		}
	    mNfcAdapter.enableForegroundDispatch(this, mNfcPendingIntent, 
	        mNdefExchangeFilters, null);
	}

	NdefMessage[] getNdefMessages(Intent intent) {
	    // Parse the intent
	    NdefMessage[] msgs = null;
	    String action = intent.getAction();
	    if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
	        || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
	        Parcelable[] rawMsgs = 
	            intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
	        if (rawMsgs != null) {
	            msgs = new NdefMessage[rawMsgs.length];
	            for (int i = 0; i < rawMsgs.length; i++) {
	                msgs[i] = (NdefMessage) rawMsgs[i];
	            }
	        } else {
	            // Unknown tag type
	            byte[] empty = new byte[] {};
	            NdefRecord record = 
	                new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, empty, empty);
	            NdefMessage msg = new NdefMessage(new NdefRecord[] {
	                record
	            });
	            msgs = new NdefMessage[] {
	                msg
	            };
	        }
	    } else {
	        Log.d(NfcUtils.getLogTag(), "Unknown intent.");
	        finish();
	    }
	    return msgs;
	}
}
