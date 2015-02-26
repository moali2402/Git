package dev.vision.rave.views;

import java.io.FileNotFoundException;
import java.util.ArrayList;

//Remove this once MainActivity	import dev.vision.rave.MainActivity;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;

import dev.vision.rave.DataHolder;
import dev.vision.rave.R;
import dev.vision.rave.Share;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Region;
import android.graphics.Path.Direction;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class QuickShare extends View {

	static QuickShare qs;
	ArrayList<Share> shares = new ArrayList<Share>();
	private boolean drawCompleted;
	private OnShareClickedListener listener;
	int indexSelected = -1;
	RelativeLayout parent;
	
	private static String subject;
	private static String textbody;
	private static CharSequence htmlbody;
	private static String twitterBody;
	private static String facebookBody;
	static Context c;
	static UiLifecycleHelper uiHelper;
	
	public static QuickShare NewInstance(final Context c, Bundle savedInstanceState){
		qs= new QuickShare(c);
		qs.setOnShareClickedListener(new OnShareClickedListener() {
			
			@Override
			public void onClick(int index) {
				Intent intent = new Intent(android.content.Intent.ACTION_SEND);
			    
			    ResolveInfo i = null;

				if(index == 0){  
					//shareToFacebook(" vv ", " vv ", " vv ", "No App");
			//Remove this once MainActivity		postImageonWall(((MainActivity) c).uri, "");
					return;
				}else if (index == 1){
					intent.setType("text/plain");
				    intent.putExtra(Intent.EXTRA_TITLE, subject);
				    intent.putExtra(Intent.EXTRA_SUBJECT, subject);
					i = DataHolder.htmlActivitiesPackages.get("twitter");
			        intent.putExtra(Intent.EXTRA_TEXT, twitterBody);
					Toast.makeText(c, "Twitter", Toast.LENGTH_LONG).show();
				}else if (index == 2){
					//Remove this once MainActivity			i = shareToInstagram(intent, ((MainActivity) c).uri);
					
				}else if (index == 3){
				
					i = DataHolder.htmlActivitiesPackages.get("email");
			        intent.putExtra(Intent.EXTRA_TEXT, textbody);
					Toast.makeText(c, "Mail", Toast.LENGTH_LONG).show();
				
				}
				if(i!=null){
					intent.setClassName(i.activityInfo.packageName, i.activityInfo.name);
			    	((Activity) c).startActivity(intent);
				}else{
					Toast.makeText(c, "You Must Install the Supporting application", Toast.LENGTH_LONG).show();
				}

			}
		});
		QuickShare.uiHelper.onCreate(savedInstanceState);
		return qs;
		
	}
	
	static ResolveInfo shareToInstagram(Intent intent,Uri uri){
		
	    ResolveInfo i = DataHolder.htmlActivitiesPackages.get("instagram");
	    if (i!= null){
			intent.setType("image/*");
			intent.putExtra(Intent.EXTRA_STREAM, uri);
	    }else
        {
            // bring user to the market to download the app.
            // or let them choose an app?
            intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("market://details?id="+"com.instagram.android"));
            c.startActivity(intent);
        }
		return i;
		
	}
	public static QuickShare getQuickShare(){
		return qs;
	}
	
	public QuickShare(Context context) {
		super(context);
		c=context;		
		uiHelper = new UiLifecycleHelper((Activity) c, null);
	}

	public QuickShare(Context context, AttributeSet attrs) {
		super(context, attrs);
		c=context;
		uiHelper = new UiLifecycleHelper((Activity) c, null);
	}

	
	public QuickShare(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		c=context;
		uiHelper = new UiLifecycleHelper((Activity) c, null);
	}

	@SuppressLint({ "NewApi", "DrawAllocation" })
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.TRANSPARENT);

		
        Paint p = new Paint();
     
		p.setAntiAlias(true);
        p.setShadowLayer(10.0f, 0.0f, 0.0f, Color.parseColor("#90000000"));
		setLayerType(LAYER_TYPE_SOFTWARE, p);

        for(Share share: shares){
        	
	        int radius = 50;

        	if(share.name.equalsIgnoreCase("twitter")){
        		setAlpha(0.3f);
        		p.setColor(Color.parseColor("#39cbfc"));

		        //Drawing the traiangle with Path class which moves our pencil to a point
		        Path path = new Path();
		        path.addCircle(getWidth()/2, 60, radius, Direction.CW);
		        path.moveTo(getWidth()/2, 130);
		        path.lineTo((getWidth()/2)-35f, 80);
		        path.lineTo((getWidth()/2)+35f, 80);
		        path.close();
		        
		        share.setPath(path);
		        share.setRegion(new Region((getWidth()/2)-radius, 60-radius, (getWidth()/2)+radius, 60+radius));

		        canvas.drawPath(path, p);
		      
		        Bitmap icon = BitmapFactory.decodeResource(getContext().getResources(),
                        R.drawable.twii);
		        canvas.drawBitmap(icon, (getWidth()/2)-(icon.getWidth()/2), 60-(icon.getHeight()/2), p);
		        setAlpha(0.4f);
        	}
        	
        	if(share.name.equalsIgnoreCase("email")){
        		setAlpha(0.8f);
		        p.setColor(Color.parseColor("#505050"));
	        	
		        Path path1 = new Path();
		        path1.addCircle(getWidth()/2, getHeight()-60, radius, Direction.CCW);
		        path1.moveTo(getWidth()/2, getHeight()-60-70);
		        path1.lineTo((getWidth()/2)-35f, getHeight()-80);
		        path1.lineTo((getWidth()/2)+35f, getHeight()-80);
		        path1.close();
		        
		        share.setPath(path1);
		        share.setRegion(new Region((getWidth()/2)-radius, getHeight()-60-radius, (getWidth()/2)+radius, getHeight()-60+radius));
		        canvas.drawPath(path1, p);
		        
		        Bitmap icon = BitmapFactory.decodeResource(getContext().getResources(),
                        R.drawable.mail);
		        canvas.drawBitmap(icon, (getWidth()/2)-(icon.getWidth()/2), (getHeight()-60)-(icon.getHeight()/2), p);
		     
		        setAlpha(1f);

        	}
        	
        	if(share.name.equalsIgnoreCase("other")){
		        p.setColor(Color.parseColor("#f840c6"));
		        setAlpha(0.5f);
		        
		        Path path2 = new Path();
		        path2.addCircle(60, getHeight()/2, radius, Direction.CCW);
		        path2.moveTo(130, getHeight()/2);
		        path2.lineTo(80, (getHeight()/2)-35f);
		        path2.lineTo(80, (getHeight()/2)+35f);
		        path2.close();
		        
		        share.setPath(path2);
		        share.setRegion(new Region(60-radius, (getHeight()/2)-radius, 60+radius, (getHeight()/2)+radius));
				
		        canvas.drawPath(path2, p);
		        
		        Bitmap icon = BitmapFactory.decodeResource(getContext().getResources(),
                        R.drawable.instagram);
		        canvas.drawBitmap(icon, 60-(icon.getWidth()/2), (getHeight()/2)-(icon.getHeight()/2), p);
		     
		       setAlpha(0.6f);

        	}
        	
        	if(share.name.equalsIgnoreCase("facebook")){
        		setAlpha(0.1f);
        		p.setColor(Color.parseColor("#405d99"));
		
	
		        Path path3 = new Path();
		        path3.addCircle(getWidth()-60, getHeight()/2, radius, Direction.CW);
		        path3.moveTo(getWidth()-60-70, getHeight()/2);
		        path3.lineTo(getWidth()-80, (getHeight()/2)-35f);
		        path3.lineTo(getWidth()-80, (getHeight()/2)+35f);
		        path3.close();
		        
		        share.setPath(path3);
		        share.setRegion(new Region(getWidth()-60-radius, (getHeight()/2)-radius, getWidth()-60+radius, (getHeight()/2)+radius));
				
		        canvas.drawPath(path3, p);
		        
		        Bitmap icon = BitmapFactory.decodeResource(getContext().getResources(),
                        R.drawable.fb);
		        canvas.drawBitmap(icon, (getWidth()-60)-(icon.getWidth()/2), (getHeight()/2)-(icon.getHeight()/2), p);
		     
		        setAlpha(0.2f);
        	}
        }
        
        drawCompleted=true;
        super.onDraw(canvas);
    }
	
	public void setParent(RelativeLayout par){
		parent=par;
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (drawCompleted) {
			
			Point point = new Point();
			point.x = (int)(event.getX());//-getX()
			point.y = (int)(event.getY());//-getY()
			if (event.getAction() == MotionEvent.ACTION_UP){

				for (Share slice : shares){
					Region r = new Region();
					r.set(slice.getRegion());
	
						if (r.contains(point.x,point.y) && listener != null){
							indexSelected = shares.indexOf(slice);
							if (indexSelected > -1){
								//listener.onClick(indexSelected);
								Toast.makeText(c, "" + indexSelected, Toast.LENGTH_LONG).show();

							}
							indexSelected = -1;
							break;
						}
				}
				clear();
			}
			else if(event.getAction() == MotionEvent.ACTION_CANCEL){
				indexSelected = -1;
			clear();
			}
			DataHolder.stopped=true;
	    }
		return true;
	}
	
	
	public void setOnShareClickedListener(OnShareClickedListener listener) {
		this.listener = listener;
	}
	
	public void AddShare(String name){
		Share s= new Share(name);
		shares.add(s);
		postInvalidate();
	}
	
	public void clear(){
		if(parent!=null){
			parent.removeView(qs);
			parent=null;
		}
		shares.clear();
		postInvalidate();
	}
	
	//Facebook share__________________________________________________________________________________________________________________________________
	public static void shareToFacebook(String Title, String Link, String Description, String NoFBApp) {

		if (FacebookDialog.canPresentShareDialog(c.getApplicationContext(), FacebookDialog.ShareDialogFeature.SHARE_DIALOG)) {

			FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder((Activity) c)
					.setLink("http://4.bp.blogspot.com/--i_OyTvprpk/US2YfX6YmHI/AAAAAAAAPy4/cW-AkmVRcUA/s1600/Happy-Anniversary-Wallpaper-4.jpg")
					.setDescription(Description)
					.setName(Title)
					.build();
			uiHelper.trackPendingDialogCall(shareDialog.present());

		} else {
			Toast.makeText(c, NoFBApp, Toast.LENGTH_LONG).show();
		}
	}

	public static void postImageonWall(Uri imageUri,String msg ) {

	    try {
	        Bitmap image = MediaStore.Images.Media.getBitmap(c.getContentResolver(), imageUri);

	    	Bundle parameters = new Bundle();
	    	parameters.putParcelable("source", image);
	    	parameters.putString("message", "my message for the page");
	    	Request request = new Request(Session.getActiveSession(), "me/photos", parameters, HttpMethod.POST, new Request.Callback() {
	    	    @Override
	    	    public void onCompleted(Response response) {
	    	        //showPublishResult(c.getString(R.string.app_name), response.getGraphObject(), response.getError());
	    	    }
	    	});
	    	request.executeAsync();
	    } catch (Exception e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }

	}

	public static void Instagram(){
		Intent intent = c.getPackageManager().getLaunchIntentForPackage("com.instagram.android");
        if (intent != null)
        {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.setPackage("com.instagram.android");
            try {
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(MediaStore.Images.Media.insertImage(c.getContentResolver(), "", "I am Happy", "Share happy !")));
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            shareIntent.setType("image/jpeg");

            c.startActivity(shareIntent);
        }
        else
        {
            // bring user to the market to download the app.
            // or let them choose an app?
            intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("market://details?id="+"com.instagram.android"));
            c.startActivity(intent);
        }

    }
	

	 // this method share test.jpg file from sd card
	
	 private void shareFromSDCard(){
	
	  //shareInstagram(Uri.parse("file://"+Environment.getExternalStorageDirectory()+"/test.jpg"));
	
	 }
		

	public static interface OnShareClickedListener {
		public abstract void onClick(int index);
	}
	
}
