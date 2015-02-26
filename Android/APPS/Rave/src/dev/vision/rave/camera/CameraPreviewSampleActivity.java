package dev.vision.rave.camera;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import dev.vision.rave.R;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;

public class CameraPreviewSampleActivity extends Activity implements OnClickListener {
    private CameraPreview mPreview;
    private FrameLayout current;
    
    private FixedAspectRatioFrameLayout Top;
    private FixedAspectRatioFrameLayout Bottom;

    
    Rect rect = new Rect();
	private Bitmap TOP;
	private Bitmap BOTTOM;
	private boolean second;
    LayoutParams previewLayoutParams;
    LayoutParams FrameLayoutParams;
    FrameLayout view1;
    FrameLayout view2;
    int CURRENT_CAM;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Hide status-bar
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Hide title-bar, must be before setContentView
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        // Requires RelativeLayout.
        setContentView(R.layout.camera_preview);
        previewLayoutParams = new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        FrameLayoutParams = new LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.FILL_PARENT);
        
        Top = (FixedAspectRatioFrameLayout) findViewById(R.id.fl);
        Bottom = (FixedAspectRatioFrameLayout) findViewById(R.id.fl2);
        view1 = (FrameLayout) Top.findViewById(R.id.containner);
      	view2 = (FrameLayout) Bottom.findViewById(R.id.containner);
        
        final GestureDetector gd = new GestureDetector(this, new OnGestureListener() {
			
			@Override
			public boolean onSingleTapUp(MotionEvent e) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public void onShowPress(MotionEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
					float distanceY) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public void onLongPress(MotionEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
					float velocityY) {
				 try {
			            float SWIPE_MAX_OFF_PATH = 60;
			            float SWIPE_MIN_DISTANCE = 0;
						float SWIPE_THRESHOLD_VELOCITY = 1;
						float XOFF_PATH = Math.abs(e1.getX() - e2.getX());
						float YOFF_PATH = Math.abs(e1.getY() - e2.getY());

						float UpToDown = e1.getY() - e2.getY();
						float DownToUp = e2.getY() - e1.getY();
						float LeftToRight = e1.getX() - e2.getX();
						float RightToLeft = e2.getX() - e1.getX();

						//if (OFF_PATH > SWIPE_MAX_OFF_PATH)
			             //   return false;
						// right to left swipe
						if(YOFF_PATH < SWIPE_MAX_OFF_PATH){

							if (LeftToRight > SWIPE_MIN_DISTANCE
				                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
								CURRENT_CAM = CURRENT_CAM == 0 ? 1 : 0;
				            	mPreview.switchCam(CURRENT_CAM);
								return true;

	
				            } else if (RightToLeft > SWIPE_MIN_DISTANCE
				                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
				            	CURRENT_CAM = CURRENT_CAM == 0 ? 1 : 0;
				            	mPreview.switchCam(CURRENT_CAM);
								return true;

				            }
						}else if(XOFF_PATH < SWIPE_MAX_OFF_PATH){
							if (UpToDown > SWIPE_MIN_DISTANCE
				                    && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
				            	
								ExhchangeViews();
								return true;

	
				            } else if (DownToUp > SWIPE_MIN_DISTANCE
				                    && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
	
				            	ExhchangeViews();
								return true;

				            }
						}
			        } catch (Exception e) {
			            // nothing
			        }
			        return false;
			}
			
			@Override
			public boolean onDown(MotionEvent e) {
				// TODO Auto-generated method stub
				return true;
			}
		});
        
        Top.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent ev) {
				return gd.onTouchEvent(ev);
			}
		});
        
        Bottom.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent ev) {
				return gd.onTouchEvent(ev);
			}
		});

        current = view1; 
       
        //Capture Button
        Button capture = (Button) findViewById(R.id.next);  
        capture.setOnClickListener(this);
        
        
    }

    @Override
    protected void onResume() {
        super.onResume();
        CURRENT_CAM=0;
        mPreview = new CameraPreview(this, CURRENT_CAM, CameraPreview.LayoutMode.NoBlank, (ImageView) findViewById(R.id.back));

        current.addView(mPreview, 0, previewLayoutParams);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPreview.stop();
        current.removeView(mPreview); // This is necessary.
        mPreview = null;
    }

	@Override
	public void onClick(View v) {
		if(v.getTag() == null){
		  registerReceiver(new CameraSwitch(), new IntentFilter("dev.vision.rave.camera.switch"));
		  mPreview.onClick();
		  if(second)
			  v.setTag("save");
		}else{
			SaveImages();
		}
	}

	private void SaveImages() {
		FrameLayout vTop = (FrameLayout) Top.findViewById(R.id.containner);
		FrameLayout vBottom = (FrameLayout) Bottom.findViewById(R.id.containner);

		for(int i=0; i<vTop.getChildCount(); ++i){
			View view = vTop.getChildAt(i);
			if (view instanceof ImageView) {
	            ImageView imageView = (ImageView) view;
	            TOP = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
	            if(TOP == null)
	            	return;
	        }
		}
		for(int i=0; i<vBottom.getChildCount(); ++i){
			View view = vBottom.getChildAt(i);
			if (view instanceof ImageView) {
	            ImageView imageView = (ImageView) view;
	            BOTTOM = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
	            if(BOTTOM == null)
	            	return;
	        }
		}		
		Bitmap last = combineImages(TOP, BOTTOM);
        if(last != null)
		Save(last);
	}

	private void Save(Bitmap last) {
		File pictureFileDir = getDir();
		
		if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {
		
		    Log.d("PIC_Capture", "Can't create directory to save image.");
		    Toast.makeText(this, "Can't create directory to save image.",
		          Toast.LENGTH_LONG).show();
		    return;
		
		}
		
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
	    String date = dateFormat.format(new Date());
	    String photoFile = "Picture_" + date + ".jpg";
	
	    String filename = pictureFileDir.getPath() + File.separator + photoFile;
	
	    File pictureFile = new File(filename);
	
	    try {
	      FileOutputStream fos = new FileOutputStream(pictureFile);
	      last.compress(Bitmap.CompressFormat.JPEG, 100, fos);
	      fos.close();
	      Toast.makeText(this, "New Image saved:" + photoFile,
	          Toast.LENGTH_LONG).show();
	    } catch (Exception error) {
	      Log.d("PIC_Capture", "File" + filename + "not saved: "
	          + error.getMessage());
	      Toast.makeText(this, "Image could not be saved.",
	          Toast.LENGTH_LONG).show();
	    }		
	}

	private File getDir() {
	    File sdDir = Environment
	      .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
	    return new File(sdDir, "Selfie");
	}
	
	public Bitmap combineImages(Bitmap c, Bitmap s) { // can add a 3rd parameter 'String loc' if you want to save the new image - left some code to do that at the bottom 
	    Bitmap cs = null; 
	 
	    int width, height = 0; 
	     
	    if(c.getWidth() > s.getWidth()) { 
	      width = c.getWidth(); 
	    } else { 
	      width = s.getWidth(); 
	    } 
	    
	    height = c.getHeight() + s.getHeight(); 

	    cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888); 
	 
	    Canvas comboImage = new Canvas(cs); 
	 
	    comboImage.drawBitmap(c, 0f, 0f, null); 
	    comboImage.drawBitmap(s, 0f, c.getHeight(), null); 
	    
	    return cs; 
    } 

	void ExhchangeViews(){     
	    //mPreview.getHolder().removeCallback(mPreview);
        mPreview.stop();
    	if(rect.equals(new Rect()))
    		mPreview.getGlobalVisibleRect(rect);

        ViewGroup parent1 = (ViewGroup) view1.getParent();
		ViewGroup parent2 = (ViewGroup) view2.getParent();

		if (parent1 != null) {
			parent1.removeView(view1);
		}
		
		if (parent2 != null) {
			parent2.removeView(view2);
		}
		
		//mPreview = new CameraPreview(CameraPreviewSampleActivity.this, CURRENT_CAM, CameraPreview.LayoutMode.NoBlank,  CURRENT_CAM == 0? (ImageView) findViewById(R.id.back) :(ImageView) findViewById(R.id.front));

		parent1.addView(view2, FrameLayoutParams);
		parent2.addView(view1, FrameLayoutParams);
		mPreview.setLayoutParams(previewLayoutParams);
		//current.addView(mPreview, 0, previewLayoutParams);
	}
	
	public class CameraSwitch extends BroadcastReceiver{

		@Override
		public void onReceive(Context arg0, Intent intent) {
			if(intent.getAction() == "dev.vision.rave.camera.switch"){
				//mCamera.stopPreview(); 
			    if(!second)
			    {	
				    mPreview.getHolder().removeCallback(mPreview);
				    mPreview.mCamera.release();
				    
			        current.removeView(mPreview); // This is necessary.
	      		    current = view2;
	      		    CURRENT_CAM = CURRENT_CAM == 0 ? 1 : 0;
	
					mPreview = new CameraPreview(CameraPreviewSampleActivity.this, CURRENT_CAM, CameraPreview.LayoutMode.NoBlank,  (ImageView) findViewById(R.id.front));
				   
	
				    current.addView(mPreview, 0, previewLayoutParams);
				    second = true;
			    }
			    unregisterReceiver(this);
			}
		}
		
	}
}