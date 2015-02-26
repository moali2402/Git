package dev.vision.rave.camera;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.Build;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * This class assumes the parent layout is RelativeLayout.LayoutParams.
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private static boolean DEBUGGING = true;
    private static final String LOG_TAG = "CameraPreviewSample";
    private static final String CAMERA_PARAM_ORIENTATION = "orientation";
    private static final String CAMERA_PARAM_LANDSCAPE = "landscape";
    private static final String CAMERA_PARAM_PORTRAIT = "portrait";
    protected CameraPreviewSampleActivity mActivity;
    private SurfaceHolder mHolder;
    protected Camera mCamera;
    protected List<Camera.Size> mPreviewSizeList;
    protected List<Camera.Size> mPictureSizeList;
    protected Camera.Size mPreviewSize;
    protected Camera.Size mPictureSize;
    private int mSurfaceChangedCallDepth = 0;
    private int mCameraId;
    private LayoutMode mLayoutMode;
    private int mCenterPosX = -1;
    private int mCenterPosY;
    ImageView view;
    
    PreviewReadyCallback mPreviewReadyCallback = null;
    
    public static enum LayoutMode {
        FitToParent, // Scale to the size that no side is larger than the parent
        NoBlank // Scale to the size that no side is smaller than the parent
    };
    
    public interface PreviewReadyCallback {
        public void onPreviewReady();
        
    }
 
    /**
     * State flag: true when surface's layout size is set and surfaceChanged()
     * process has not been completed.
     */
    protected boolean mSurfaceConfiguring = false;
    private static final double ASPECT_TOLERANCE=0.1;

    public CameraPreview(Activity activity, int cameraId, LayoutMode mode, ImageView v) {
        super(activity); // Always necessary
	    Init( activity, cameraId, mode, v);
    }
    
    void Init(Activity activity, int cameraId, LayoutMode mode, ImageView v){
    	view = v;
        mActivity = (CameraPreviewSampleActivity) activity;
        
        mLayoutMode = mode;
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            if (Camera.getNumberOfCameras() > cameraId) {
                mCameraId = cameraId;
            } else {
                mCameraId = 0;
            }
        } else {
            mCameraId = 0;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            mCamera = Camera.open(mCameraId);
        } else {
            mCamera = Camera.open();
        }
        Camera.Parameters cameraParams = mCamera.getParameters();
        mPreviewSizeList = cameraParams.getSupportedPreviewSizes();
        mPictureSizeList = cameraParams.getSupportedPictureSizes();
    }
   
    
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    	if(mCamera == null){
    	    Init( mActivity, mCameraId, mLayoutMode, view);
    	}
        try {
            mCamera.setPreviewDisplay(mHolder);
        } catch (IOException e) {
            mCamera.release();
            mCamera = null;
        }
    }
    
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mSurfaceChangedCallDepth++;
        doSurfaceChanged(width, height);
        mSurfaceChangedCallDepth--;
    }
    
    void switchCam (int currentCameraId){
    	if (mCamera != null) {
		
	   	    mCamera.stopPreview();
	       	//NB: if you don't release the current camera before switching, you app will crash
	    	mCamera.release();
	    	mCameraId = currentCameraId;
	    	//swap the id of the camera to be used
	    	mCamera = Camera.open(currentCameraId);
            try {
				mCamera.setPreviewDisplay(mHolder);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

            doSurfaceChanged(getWidth(), getHeight());
    	}
    }

    
    public void onClick() {
    	if(mActivity.rect.equals(new Rect()))
    		getGlobalVisibleRect(mActivity.rect);
   // add user to fix  //  mCamera.takePicture(null, null,
      //      new PhotoHandler(getContext(),mActivity.rect,view, mCameraId));
    }
    
    private void doSurfaceChanged(int width, int height) {
    	
        mCamera.stopPreview();
        
        Camera.Parameters cameraParams = mCamera.getParameters();
        boolean portrait = isPortrait();

        // The code in this if-statement is prevented from executed again when surfaceChanged is
        // called again due to the change of the layout size in this if-statement.
        if (!mSurfaceConfiguring) {
            Camera.Size previewSize = determinePreviewSize(portrait, width, height);
            Camera.Size pictureSize = determinePictureSize(previewSize);
            if (DEBUGGING) { Log.v(LOG_TAG, "Desired Preview Size - w: " + width + ", h: " + height); }
            mPreviewSize = previewSize;
            mPictureSize = pictureSize;
            mSurfaceConfiguring = adjustSurfaceLayoutSize(previewSize, portrait, width, height);
            // Continue executing this method if this method is called recursively.
            // Recursive call of surfaceChanged is very special case, which is a path from
            // the catch clause at the end of this method.
            // The later part of this method should be executed as well in the recursive
            // invocation of this method, because the layout change made in this recursive
            // call will not trigger another invocation of this method.
            if (mSurfaceConfiguring && (mSurfaceChangedCallDepth <= 1)) {
                return;
            }
        }

        configureCameraParameters(cameraParams, portrait);
        mSurfaceConfiguring = false;

        try {
            mCamera.startPreview();
            mCamera.autoFocus(null);
        } catch (Exception e) {
            Log.w(LOG_TAG, "Failed to start preview: " + e.getMessage());

            // Remove failed size
            mPreviewSizeList.remove(mPreviewSize);
            mPreviewSize = null;

            // Reconfigure
            if (mPreviewSizeList.size() > 0) { // prevent infinite loop
                surfaceChanged(null, 0, width, height);
            } else {
                Toast.makeText(mActivity, "Can't start preview", Toast.LENGTH_LONG).show();
                Log.w(LOG_TAG, "Gave up starting preview");
            }
        }
        
        if (null != mPreviewReadyCallback) {
            mPreviewReadyCallback.onPreviewReady();
        }
    }
    
    /**
     * @param cameraParams
     * @param portrait
     * @param reqWidth must be the value of the parameter passed in surfaceChanged
     * @param reqHeight must be the value of the parameter passed in surfaceChanged
     * @return Camera.Size object that is an element of the list returned from Camera.Parameters.getSupportedPreviewSizes.
     */
    protected Camera.Size determinePreviewSize(boolean portrait, int reqWidth, int reqHeight) {
        // Meaning of width and height is switched for preview when portrait,
        // while it is the same as user's view for surface and metrics.
        // That is, width must always be larger than height for setPreviewSize.
        int reqPreviewWidth; // requested width in terms of camera hardware
        int reqPreviewHeight; // requested height in terms of camera hardware
        if (portrait) {
            reqPreviewWidth = reqHeight;
            reqPreviewHeight = reqWidth;
        } else {
            reqPreviewWidth = reqWidth;
            reqPreviewHeight = reqHeight;
        }

        if (DEBUGGING) {
            Log.v(LOG_TAG, "Listing all supported preview sizes");
            for (Camera.Size size : mPreviewSizeList) {
                Log.v(LOG_TAG, "  w: " + size.width + ", h: " + size.height);
            }
            Log.v(LOG_TAG, "Listing all supported picture sizes");
            for (Camera.Size size : mPictureSizeList) {
                Log.v(LOG_TAG, "  w: " + size.width + ", h: " + size.height);
            }
        }

        // Adjust surface size with the closest aspect-ratio
        float reqRatio = ((float) reqPreviewWidth) / reqPreviewHeight;
        float curRatio, deltaRatio;
        float deltaRatioMin = Float.MAX_VALUE;
        Camera.Size retSize = null;
        for (Camera.Size size : mPreviewSizeList) {
            curRatio = ((float) size.width) / size.height;
            deltaRatio = Math.abs(reqRatio - curRatio);
            if (deltaRatio < deltaRatioMin) {
                deltaRatioMin = deltaRatio;
                retSize = size;
            }
        }

        return retSize;
    }

    protected Camera.Size determinePictureSize(Camera.Size previewSize) {
        Camera.Size retSize = null;
        for (Camera.Size size : mPictureSizeList) {
            if (size.equals(previewSize)) {
                return size;
            }
        }
        
        if (DEBUGGING) { Log.v(LOG_TAG, "Same picture size not found."); }
        
        // if the preview size is not supported as a picture size
        float reqRatio = ((float) previewSize.width) / previewSize.height;
        float curRatio, deltaRatio;
        float deltaRatioMin = Float.MAX_VALUE;
        for (Camera.Size size : mPictureSizeList) {
            curRatio = ((float) size.width) / size.height;
            deltaRatio = Math.abs(reqRatio - curRatio);
            if (deltaRatio < deltaRatioMin) {
                deltaRatioMin = deltaRatio;
                retSize = size;
            }
        }
        
        return retSize;
    }
    
    protected boolean adjustSurfaceLayoutSize(Camera.Size previewSize, boolean portrait,
            int availableWidth, int availableHeight) {
        float tmpLayoutHeight, tmpLayoutWidth;
        if (portrait) {
            tmpLayoutHeight = previewSize.width;
            tmpLayoutWidth = previewSize.height;
        } else {
            tmpLayoutHeight = previewSize.height;
            tmpLayoutWidth = previewSize.width;
        }

        float factH, factW, fact;
        factH = availableHeight / tmpLayoutHeight;
        factW = availableWidth / tmpLayoutWidth;
        if (mLayoutMode == LayoutMode.FitToParent) {
            // Select smaller factor, because the surface cannot be set to the size larger than display metrics.
            if (factH < factW) {
                fact = factH;
            } else {
                fact = factW;
            }
        } else {
            if (factH < factW) {
                fact = factW;
            } else {
                fact = factH;
            }
        }

        FixedAspectRatioFrameLayout.LayoutParams layoutParams = (FixedAspectRatioFrameLayout.LayoutParams)this.getLayoutParams();

        int layoutHeight = (int) (tmpLayoutHeight * fact);
        int layoutWidth = (int) (tmpLayoutWidth * fact);
        if (DEBUGGING) {
            Log.v(LOG_TAG, "Preview Layout Size - w: " + layoutWidth + ", h: " + layoutHeight);
            Log.v(LOG_TAG, "Scale factor: " + fact);
        }

        boolean layoutChanged = false;
        int cH= getHeight();
        int cW= getWidth();
        
        if ((layoutWidth != cW) || (layoutHeight != cH)) {
			layoutParams.height = layoutHeight;
			layoutParams.width = layoutWidth;
		
			layoutParams.topMargin = (cH - layoutHeight)/2;
			layoutParams.leftMargin =(cW - layoutWidth) / 2;
		
			
			this.setLayoutParams(layoutParams); // this will trigger another surfaceChanged invocation.
			layoutChanged = true;
		} else {
			layoutChanged = false;
		}
     
        return layoutChanged;
    }

    /**
     * @param x X coordinate of center position on the screen. Set to negative value to unset.
     * @param y Y coordinate of center position on the screen.
     */
    public void setCenterPosition(int x, int y) {
        mCenterPosX = x;
        mCenterPosY = y;
    }
    
    protected void configureCameraParameters(Camera.Parameters cameraParams, boolean portrait) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) { // for 2.1 and before
            if (portrait) {
                cameraParams.set(CAMERA_PARAM_ORIENTATION, CAMERA_PARAM_PORTRAIT);
            } else {
                cameraParams.set(CAMERA_PARAM_ORIENTATION, CAMERA_PARAM_LANDSCAPE);
            }
        } else { // for 2.2 and later
            int angle;
            Display display = ((WindowManager) mActivity.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            switch (display.getRotation()) {
                case Surface.ROTATION_0: // This is display orientation
                    angle = 90; // This is camera orientation
                    break;
                case Surface.ROTATION_90:
                    angle = 0;
                    break;
                case Surface.ROTATION_180:
                    angle = 270;
                    break;
                case Surface.ROTATION_270:
                    angle = 180;
                    break;
                default:
                    angle = 90;
                    break;
            }
            Log.v(LOG_TAG, "angle: " + angle);

            cameraParams.setRotation(mCameraId == 0 ? angle : angle+180);
            mCamera.setDisplayOrientation(angle);
        }

        cameraParams.setPreviewSize(mPreviewSize.width,mPreviewSize.height);
        cameraParams.setPictureSize(mPictureSize.width, mPictureSize.height);
        if (DEBUGGING) {
            Log.v(LOG_TAG, "Preview Actual Size - w: " + mPreviewSize.width + ", h: " + mPreviewSize.height);
            Log.v(LOG_TAG, "Picture Actual Size - w: " + mPictureSize.width + ", h: " + mPictureSize.height);
        }

        mCamera.setParameters(cameraParams);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stop();
    }
    
    public void stop() {
        if (null == mCamera) {
            return;
        }
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
    }

    public boolean isPortrait() {
        return (mActivity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);
    }
    
    public void setOneShotPreviewCallback(PreviewCallback callback) {
        if (null == mCamera) {
            return;
        }
        mCamera.setOneShotPreviewCallback(callback);
    }
    
    public void setPreviewCallback(PreviewCallback callback) {
        if (null == mCamera) {
            return;
        }
        mCamera.setPreviewCallback(callback);
    }
    
    public Camera.Size getPreviewSize() {
        return mPreviewSize;
    }
    
    public void setOnPreviewReady(PreviewReadyCallback cb) {
        mPreviewReadyCallback = cb;
    }
    
    public static Camera.Size getOptimalPreviewSize(int displayOrientation,
            int width,
            int height,
            Camera.Parameters parameters) {
			double targetRatio=(double)width / height;
			List<Camera.Size> sizes=parameters.getSupportedPreviewSizes();
			Camera.Size optimalSize=null;
			double minDiff=Double.MAX_VALUE;
			int targetHeight=height;
			
			if (displayOrientation == 90 || displayOrientation == 270) {
			targetRatio=(double)height / width;
			}
			
			// Try to find an size match aspect ratio and size
			
			for (Size size : sizes) {
			double ratio=(double)size.width / size.height;
			
			if (Math.abs(ratio - targetRatio) <= ASPECT_TOLERANCE) {
			if (Math.abs(size.height - targetHeight) < minDiff) {
			optimalSize=size;
			minDiff=Math.abs(size.height - targetHeight);
			}
			}
			}
			
			// Cannot find the one match the aspect ratio, ignore
			// the requirement
			
			if (optimalSize == null) {
			minDiff=Double.MAX_VALUE;
			
			for (Size size : sizes) {
			if (Math.abs(size.height - targetHeight) < minDiff) {
			optimalSize=size;
			minDiff=Math.abs(size.height - targetHeight);
			}
			}
			}
			
			return(optimalSize);
}

public static Camera.Size getBestAspectPreviewSize(int displayOrientation,
               int width,
               int height,
               Camera.Parameters parameters) {
	return(getBestAspectPreviewSize(displayOrientation, width, height,
	parameters, 0.0d));
}

public static Camera.Size getBestAspectPreviewSize(int displayOrientation,
               int width,
               int height,
               Camera.Parameters parameters,
               double closeEnough) {
	double targetRatio=(double)width / height;
	Camera.Size optimalSize=null;
	double minDiff=Double.MAX_VALUE;
	
	if (displayOrientation == 90 || displayOrientation == 270) {
	targetRatio=(double)height / width;
	}
	
	List<Size> sizes=parameters.getSupportedPreviewSizes();
	
	Collections.sort(sizes,
	Collections.reverseOrder(new SizeComparator()));
	
	for (Size size : sizes) {
	double ratio=(double)size.width / size.height;
	
	if (Math.abs(ratio - targetRatio) < minDiff) {
	optimalSize=size;
	minDiff=Math.abs(ratio - targetRatio);
	}
	
	if (minDiff < closeEnough) {
	break;
	}
	}
	
	return(optimalSize);
}
private static class SizeComparator implements
	Comparator<Camera.Size> {
	@Override
	public int compare(Size lhs, Size rhs) {
	int left=lhs.width * lhs.height;
	int right=rhs.width * rhs.height;
	
	if (left < right) {
	  return(-1);
	}
	else if (left > right) {
	  return(1);
	}
	
	return(0);
}
}
}