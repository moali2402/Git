package dev.vision.rave.camera;
import java.io.IOException;
import dev.vision.rave.DataHolder;
import dev.vision.rave.user.User;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.widget.ImageView;

@SuppressLint("SimpleDateFormat") 

public class PhotoHandler implements PictureCallback {

  private final Context context;
  Rect rect;
  ImageView v;
  int cam;
  User user;
  public PhotoHandler(Context context, Rect rec, ImageView view, int camera, User user) {
	  this.context = context;
	  rect= rec;
	  v = view;
	  cam = camera;
	  this.user = user;
  }
  
  public PhotoHandler(Context context, Rect rec, int camera) {
	  this.context = context;
	  rect= rec;
	  cam = camera;
  }

  @SuppressLint("NewApi") @Override
  public void onPictureTaken(byte[] data, Camera camera) {
	  
      Bitmap croppedBitmap = null;

      try {
			if (VERSION.SDK_INT >= VERSION_CODES.GINGERBREAD_MR1)
			{
			   BitmapRegionDecoder decoder= BitmapRegionDecoder.newInstance(data,0,data.length, true);
			   croppedBitmap = decoder.decodeRegion(rect, null);
			   decoder.recycle();
			}
			else 
			{
			   Bitmap bitmapOriginal=BitmapFactory.decodeByteArray(data, 0, data.length);//decodeFile(imageFilePath, null);
			   croppedBitmap = Bitmap.createBitmap(bitmapOriginal,rect.left,rect.top,rect.width(),rect.height());
			}
	   } catch (IOException e) {
			e.printStackTrace();
	  }
      
      if(cam == 1){
	      Matrix flipHorizontalMatrix = new Matrix();
	      flipHorizontalMatrix.setScale(-1,1);
	      flipHorizontalMatrix.postTranslate(croppedBitmap.getWidth(),0);
	      croppedBitmap =Bitmap.createBitmap(croppedBitmap, 0, 0, croppedBitmap.getWidth(), croppedBitmap.getHeight(), flipHorizontalMatrix, true);
      }
      
      DataHolder.picTaken = croppedBitmap;
      
      if(croppedBitmap!=null && v!=null)
    	//  v.setImageBitmap(croppedBitmap);
      

      context.sendBroadcast(new Intent("dev.vision.rave.camera.switch"));
      
  }
  

} 