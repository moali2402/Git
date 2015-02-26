package dev.vision.rave.views;

import java.io.FileNotFoundException;
import java.util.ArrayList;

//Remove this once MainActivity	 import dev.vision.rave.MainActivity;
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
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
@SuppressLint("NewApi")
public class QuickShareTouch extends View {

	static QuickShareTouch qs;
	ArrayList<Share> shares = new ArrayList<Share>();
	private boolean drawCompleted;
	private OnShareClickedListener listener;
	int indexSelected = -1;
	
	private static String subject;
	private static String textbody;
	private static CharSequence htmlbody;
	private static String twitterBody;
	private static String facebookBody;
	static Context c;
	static UiLifecycleHelper uiHelper;
	
	public static QuickShareTouch NewInstance(final Context c, Bundle savedInstanceState){
		qs= new QuickShareTouch(c);
		
		qs.setOnShareClickedListener(new OnShareClickedListener() {
			
			@Override
			public void onClick(int index) {
				Intent intent = new Intent(android.content.Intent.ACTION_SEND);
			    
			    ResolveInfo i = null;

				if(index == 0){  
					//shareToFacebook(" vv ", " vv ", " vv ", "No App");
					//Remove this once MainActivity			postImageonWall(((MainActivity) c).uri, "");
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
		QuickShareTouch.uiHelper.onCreate(savedInstanceState);
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
	public static QuickShareTouch getQuickShare(){
		return qs;
	}
	 public QuickShareTouch(Context context) {
		super(context);
		c=context;	
		if (Build.VERSION.SDK_INT >= 11) {
		    setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		}
		uiHelper = new UiLifecycleHelper((Activity) c, null);
	}

	public QuickShareTouch(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (Build.VERSION.SDK_INT >= 11) {
		    setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		}
		c=context;
		uiHelper = new UiLifecycleHelper((Activity) c, null);
	}

	
	public QuickShareTouch(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		if (Build.VERSION.SDK_INT >= 11) {
		    setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		}
		c=context;
		uiHelper = new UiLifecycleHelper((Activity) c, null);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.TRANSPARENT);
		Context cx = getContext();
		int wid = (int) DataHolder.pixelsFromDp(cx,200);
		int hie = (int) DataHolder.pixelsFromDp(cx,200);
		int radius = (int) DataHolder.pixelsFromDp(cx, 33.33f);

        Paint p = new Paint();
     
		p.setAntiAlias(true);
        p.setShadowLayer(DataHolder.pixelsFromDp(cx, 6.66f), 0.0f, 0.0f, Color.parseColor("#50000000"));
		setLayerType(LAYER_TYPE_SOFTWARE, p);

        for(Share share: shares){
        	
	       
        	if(share.name.equalsIgnoreCase("twitter") && !share.drawn){
        		setAlpha(0.3f);
        		p.setColor(Color.parseColor("#39cbfc"));

		        //Drawing the traiangle with Path class which moves our pencil to a point
		        Path path = new Path();
		        path.addCircle(wid/2, DataHolder.pixelsFromDp(cx, 40), radius, Direction.CW);
		        path.moveTo(wid/2, DataHolder.pixelsFromDp(cx, (float) 86.66));
		        path.lineTo((wid/2)-DataHolder.pixelsFromDp(cx, (float) 23.33), DataHolder.pixelsFromDp(cx, (float) 53.33));
		        path.lineTo((wid/2)+DataHolder.pixelsFromDp(cx, (float) 23.33), DataHolder.pixelsFromDp(cx, (float) 53.33));
		        path.close();
		        
		        share.setPath(path);
		        share.setRegion(new Region((wid/2)-radius, (int) DataHolder.pixelsFromDp(cx, 40)-radius, (wid/2)+radius, (int) DataHolder.pixelsFromDp(cx, 40)+radius));

		        canvas.drawPath(path, p);
		      
		        Bitmap icon = BitmapFactory.decodeResource(cx.getResources(),
                        R.drawable.twii);
		        canvas.drawBitmap(icon, (wid/2)-(icon.getWidth()/2), (int)DataHolder.pixelsFromDp(cx, 40)-(icon.getHeight()/2), p);
		        setAlpha(0.4f);

        	}
        	
        	if(share.name.equalsIgnoreCase("email")  && !share.drawn){
        		setAlpha(0.8f);
        		p.setShadowLayer(DataHolder.pixelsFromDp(cx, 6.66f), 0.0f, 0.0f, Color.parseColor("#50000000"));
		        p.setColor(Color.parseColor("#505050"));
	        	
		        Path path1 = new Path();
		        path1.addCircle(wid/2, hie-DataHolder.pixelsFromDp(cx, 40), radius, Direction.CCW);
		        path1.moveTo(wid/2, hie-DataHolder.pixelsFromDp(cx, (float) 86.66));
		        path1.lineTo((wid/2)-DataHolder.pixelsFromDp(cx, (float) 23.33), hie-DataHolder.pixelsFromDp(cx, (float) 53.33));
		        path1.lineTo((wid/2)+DataHolder.pixelsFromDp(cx, (float) 23.33), hie-DataHolder.pixelsFromDp(cx, (float) 53.33));
		        path1.close();
		        
		        share.setPath(path1);
		        share.setRegion(new Region((wid/2)-radius, (int) (hie-DataHolder.pixelsFromDp(cx, 40)-radius), (wid/2)+radius,(int) (hie-DataHolder.pixelsFromDp(cx, 40)+radius)));
		        canvas.drawPath(path1, p);
		        
		        Bitmap icon = BitmapFactory.decodeResource(cx.getResources(),
                        R.drawable.mail);
		        canvas.drawBitmap(icon, (wid/2)-(icon.getWidth()/2), (hie-DataHolder.pixelsFromDp(cx, 40))-(icon.getHeight()/2), p);
		     
		        setAlpha(1f);

        	}
        	
        	if(share.name.equalsIgnoreCase("other")  && !share.drawn){
        		p.setShadowLayer(DataHolder.pixelsFromDp(cx, 6.66f), 0.0f, 0.0f, Color.parseColor("#50000000"));
		        p.setColor(Color.parseColor("#f840c6"));
		        setAlpha(0.5f);
		        
		        Path path2 = new Path();
		        path2.addCircle(DataHolder.pixelsFromDp(cx, 40), hie/2, radius, Direction.CCW);
		        path2.moveTo(DataHolder.pixelsFromDp(cx, (float) 86.66), hie/2);
		        path2.lineTo(DataHolder.pixelsFromDp(cx, 53.33f), (hie/2)-DataHolder.pixelsFromDp(cx, (float) 23.33));
		        path2.lineTo(DataHolder.pixelsFromDp(cx, 53.33f), (hie/2)+DataHolder.pixelsFromDp(cx, (float) 23.33));
		        path2.close();
		        
		        share.setPath(path2);
		        share.setRegion(new Region((int) DataHolder.pixelsFromDp(cx, 40)-radius, (hie/2)-radius, (int) DataHolder.pixelsFromDp(cx, 40)+radius, (hie/2)+radius));
				
		        canvas.drawPath(path2, p);
		        
		        Bitmap icon = BitmapFactory.decodeResource(cx.getResources(),
                        R.drawable.instagram);
		        canvas.drawBitmap(icon, DataHolder.pixelsFromDp(cx, 40)-(icon.getWidth()/2), (hie/2)-(icon.getHeight()/2), p);
		     
		       setAlpha(0.6f);
        	}
        	
        	if(share.name.equalsIgnoreCase("facebook")  && !share.drawn){
        		setAlpha(0.1f);
        		p.setShadowLayer(DataHolder.pixelsFromDp(cx, 6.66f), 0.0f, 0.0f, Color.parseColor("#50000000"));
        		p.setColor(Color.parseColor("#405d99"));
		
	
		        Path path3 = new Path();
		        path3.addCircle(wid-DataHolder.pixelsFromDp(cx, 40), hie/2, radius, Direction.CW);
		        path3.moveTo(wid-DataHolder.pixelsFromDp(cx, (float) 86.66), hie/2);
		        path3.lineTo(wid-DataHolder.pixelsFromDp(cx, (float) 53.33), (hie/2)-DataHolder.pixelsFromDp(cx, (float) 23.33));
		        path3.lineTo(wid-DataHolder.pixelsFromDp(cx, (float) 53.33), (hie/2)+DataHolder.pixelsFromDp(cx, (float) 23.33));
		        path3.close();
		        
		        share.setPath(path3);
		        share.setRegion(new Region((int) (wid-DataHolder.pixelsFromDp(cx, 40)-radius), (hie/2)-radius, (int) (wid-DataHolder.pixelsFromDp(cx, 40)+radius), (hie/2)+radius));
				
		        canvas.drawPath(path3, p);
		        
		        Bitmap icon = BitmapFactory.decodeResource(cx.getResources(), R.drawable.fb);
		        canvas.drawBitmap(icon, (wid-DataHolder.pixelsFromDp(cx, 40))-(icon.getWidth()/2), (hie/2)-(icon.getHeight()/2), p);
		     
		        setAlpha(0.2f);

        	}
        }
        
        drawCompleted=true;
        super.onDraw(canvas);
    }
	
	
	@SuppressLint("NewApi")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (drawCompleted) {
			
			Point point = new Point();
			point.x = (int)(event.getX()-getX());//
			point.y = (int)(event.getY()+DataHolder.pixelsFromDp(getContext(), (float) 33.33)-getY());//
			for (Share slice : shares){
				Region r = new Region();
				r.set(slice.getRegion());
				if (event.getAction() == MotionEvent.ACTION_UP){

					if (r.contains(point.x,point.y) && listener != null){
						indexSelected = shares.indexOf(slice);
						if (indexSelected > -1){
							//Toast.makeText(c, "" + indexSelected, Toast.LENGTH_LONG).show();

							listener.onClick(indexSelected);
						}
						indexSelected = -1;
						break;
					}
					
				}
				else if(event.getAction() == MotionEvent.ACTION_CANCEL)
					indexSelected = -1;
			}
			clear();
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

	static void Instagram(){
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
