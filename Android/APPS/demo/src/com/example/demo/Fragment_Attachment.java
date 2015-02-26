package com.example.demo;

import java.io.File;
import java.util.ArrayList;











import com.example.demo.Quote_Activity.AttachmentAdapter;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;

public class Fragment_Attachment extends Fragment {
	View v;
	Activity c;
	private GridView gv;
	private File root;
	Uri outputFileUri;

	 
    public Fragment_Attachment(){};
    
    public static final Fragment_Attachment newInstance()
    {
    	Fragment_Attachment f = new Fragment_Attachment();
    	   	 	
        return f;
    }
    
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
		
		v= inflater.inflate(R.layout.attachments, null);
  		gv = (GridView) v.findViewById(R.id.gridView1);

		return v;
	}
	
	void setList(){
		if(gv!=null){
			gv.setAdapter(new AttachmentAdapter(((Fragment_Activity) getActivity()).quote.attachments));
		     gv.setOnItemClickListener(new OnItemClickListener() {
	
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Intent intent = new Intent();
					intent.setAction(Intent.ACTION_VIEW);
					
	
					intent.setDataAndType(Uri.fromFile(new File(((Fragment_Activity) getActivity()).quote.attachments.get(position))), "image/*");
					startActivity(intent);				
				}
			});
		}
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		 c = getActivity();
		 setList();

	     
	     
	     v.findViewById(R.id.camera).setOnClickListener(new OnClickListener() {
				
			@Override
			public void onClick(View v) {
				openImageIntent();				
			}
		});
	     

	     v.findViewById(R.id.gallery).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			     openGallery();				
			}
		 });
	}

	void openGallery(){
		Intent i = new Intent(
		Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		
		getActivity().startActivityForResult(i, 1);
	 }
	
	 
	 private void openImageIntent() {
		    // Determine Uri of camera image to save.
	        root = new File(((Fragment_Activity) getActivity()).filesPath + ((Fragment_Activity) getActivity()).quote.user.store.Storeid + File.separator + ((Fragment_Activity) getActivity()).quote.getQuoteId() + File.separator);
	        root.mkdirs();


		    final String fname = "img_"+ System.currentTimeMillis() + ".jpg";
		    final File sdImageMainDirectory = new File(root, fname);
		    outputFileUri = Uri.fromFile(sdImageMainDirectory);

		    // Camera.
		    final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		    captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);


		    getActivity().startActivityForResult(captureIntent, 212);
		}
	
	class AttachmentAdapter extends BaseAdapter{
		ArrayList<String> attachments;

		public AttachmentAdapter(ArrayList<String> attachments){
			
			this.attachments = attachments;

		}
		
		@Override
		public int getCount() {
			return attachments.size();
		}
	
		@Override
		public String getItem(int position) {
			
			return attachments.get(position);
		}
	
		@Override
		public long getItemId(int position) {
			return position;
		}
		
		
	
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder v;
			if(convertView == null){
				v= new ViewHolder();
				convertView = c.getLayoutInflater().inflate(R.layout.grid_item, null);
				v.im = (ImageView) convertView.findViewById(R.id.grid_image);
				v.rm = (ImageView) convertView.findViewById(R.id.im_rm);

				convertView.setTag(v);
			}else{
				v = (ViewHolder) convertView.getTag();
			}
            Bitmap bm = decodeSampledBitmapFromUri(getItem(position), 125, 125);

            v.im.setImageBitmap(bm);
            
            v.rm.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(new File(getItem(position)).delete()){
						try{
							c.getContentResolver() .delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
						          MediaStore.Images.Media.DATA
						              + "='"
						              + getItem(position)
						              + "'", null);
						  } catch (Exception e) {
						      e.printStackTrace();
						  }
						((Fragment_Activity) getActivity()).quote.removeAttachment(position);
						notifyDataSetChanged();
					}
				}
			});
			//((ImageView)convertView).setLayoutParams(new AbsListView.LayoutParams(300, 300));;
			return convertView;
		}
		
		class ViewHolder{
			ImageView im;
			ImageView rm;
		}
		
		
	}
	

	 public Bitmap decodeSampledBitmapFromUri(String path, int reqWidth, int reqHeight) {

	        Bitmap bm = null;
	        // First decode with inJustDecodeBounds=true to check dimensions
	        final BitmapFactory.Options options = new BitmapFactory.Options();
	        options.inJustDecodeBounds = true;
	        BitmapFactory.decodeFile(path, options);

	        // Calculate inSampleSize
	        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

	        //  Decode bitmap with inSampleSize set
	        options.inJustDecodeBounds = false;
	        bm = BitmapFactory.decodeFile(path, options); 

	        return bm;   
	    }

	    public int calculateInSampleSize(

	        BitmapFactory.Options options, int reqWidth, int reqHeight) {
	        // Raw height and width of image
	        final int height = options.outHeight;
	        final int width = options.outWidth;
	        int inSampleSize = 1;

	        if (height > reqHeight || width > reqWidth) {
	            if (width > height) {
	                inSampleSize = Math.round((float)height / (float)reqHeight);    
	            } else {
	                inSampleSize = Math.round((float)width / (float)reqWidth);    
	            }   
	        }

	        return inSampleSize;    
	    }
	    
	    @Override
		public void onActivityResult(int requestCode, int resultCode, Intent data) {
	         super.onActivityResult(requestCode, resultCode, data);
	         if(resultCode == Activity.RESULT_OK){
			        if (requestCode == 1 && null != data) {
			            Uri selectedImage = data.getData();
			            if(selectedImage != null){
				            String[] filePathColumn = { MediaStore.Images.Media.DATA };
				 
				            Cursor cursor = c.getContentResolver().query(selectedImage,
				                    filePathColumn, null, null, null);
				            cursor.moveToFirst();
				 
				            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				            String picturePath = cursor.getString(columnIndex);
				            cursor.close();
				             
				            //Bitmap b = BitmapFactory.decodeFile(picturePath);
				            ((Fragment_Activity) getActivity()).quote.attach(picturePath);
				            ((AttachmentAdapter)gv.getAdapter()).notifyDataSetChanged();
			        	}
			        }else if(requestCode == 212)
			        {
			            final boolean isCamera;
			            if(data == null){
			                isCamera = true;
			            }
			            else
			            {
			                final String action = data.getAction();
			                if(action == null)
			                {
			                    isCamera = false;
			                }
			                else
			                {
			                    isCamera = action.equals(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			                }
			            }
		
			            Uri selectedImageUri;
			            if(isCamera)
			            {
			                selectedImageUri = outputFileUri;
			               // Bitmap bm = decodeSampledBitmapFromUri(itemList.get(position), 220, 220);
			               // Bitmap bm = BitmapFactory.decodeFile(selectedImageUri.getPath());
			                ((Fragment_Activity) getActivity()).quote.attach(selectedImageUri.getPath());
				            ((AttachmentAdapter) gv.getAdapter()).notifyDataSetChanged();
			            }
			            
			       }
	     
	         }
	     }
}
