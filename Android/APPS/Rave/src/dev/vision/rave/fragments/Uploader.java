package dev.vision.rave.fragments;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.f2prateek.progressbutton.ProgressButton;

import dev.vision.rave.API;
import dev.vision.rave.DataHolder;
import dev.vision.rave.user.Post;
import dev.vision.rave.user.User;

class Uploader extends AsyncTask<Object, Integer, Boolean> {
			int startProgress = 0;
			int endProgress = 100;
			int totalProgress = endProgress - startProgress;
			int readSoFar;
			long streamLength;
			final ProgressButton b;
			String sourceFileUri;
	    	String filename ;
	    	Post post ;
	    	User user;
			public Uploader(String sourceFileUri, String filename , Post post, User user,ProgressButton f){
				this.sourceFileUri = sourceFileUri;
		    	this.filename = filename;
		    	this.post = post;
		    	this.user = user;
		    	b= f;
			}

			@Override
			protected Boolean doInBackground(Object... data) {
			    	 int serverResponseCode = 0;
					 	//String sourceFileUri, String filename , Post post, User user, final ProgressButton progress
			    	 
			 		String fileName = sourceFileUri+filename;
			 	        HttpURLConnection conn = null;
			 	        DataOutputStream dos = null;  
			 	        String lineEnd = "\r\n";
			 	        String twoHyphens = "--";
			 	        String boundary = "*****";
			 	        int bytesRead, bytesAvailable, bufferSize;
			 	        
			              	   
	                 	publishProgress(startProgress);
			               
			 	        
			 	        byte[] buffer;
			 	        int maxBufferSize = 4096; 
			 	        File sourceFile = new File(fileName); 
			 	        int prog = 0;
			 	        if (!sourceFile.isFile()) {

			 	        }
			 	        else
			 	        {
			 	             
			 				
			 	            try { 
			 	
			 	                   // open a URL connection to the Servlet
			 	                 FileInputStream fileInputStream = new FileInputStream(sourceFile);
			 	                 URL url = new URL(API.UPLOAD);
			 	                  
			 	                 // Open a HTTP  connection to  the URL
			 	                 conn = (HttpURLConnection) url.openConnection(); 
			 	                 conn.setDoInput(true); // Allow Inputs
			 	                 conn.setDoOutput(true); // Allow Outputs
			 	                 conn.setUseCaches(false); // Don't use a Cached Copy
			 	                 conn.setRequestMethod("POST");
			 	                 conn.setRequestProperty("Connection", "Keep-Alive");
			 	                 conn.setRequestProperty("ENCTYPE", "multipart/form-data");
			 	                 conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
			 	                 conn.setRequestProperty("uploaded_file", fileName); 
			 	                 conn.setRequestProperty("name", filename); 
			 	                 conn.setRequestProperty("user", user.getId()); 
			 	                 
			 	                 dos = new DataOutputStream(conn.getOutputStream());
			 	        
			 	                 
			 	                 dos.writeBytes(twoHyphens + boundary + lineEnd);
			 	                 // The keyword "type" is the key value and 
			 	
			 	                 dos.writeBytes("Content-Disposition: form-data; name=\"user\""
			 			                         + lineEnd);
			 	                 dos.writeBytes(lineEnd);
			 			
			 			                 // You can assign values as like follows : 
			 			
			 	                 dos.writeBytes(user.getId());
			 	                 dos.writeBytes(lineEnd);
			 	            
			 	                 dos.writeBytes(twoHyphens + boundary + lineEnd);
			 			         
			 	                 dos.writeBytes(twoHyphens + boundary + lineEnd); 
			 	                
			 	                 dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""+filename+"\"" + lineEnd);
			 	                 // The keyword "type" is the key value and 
			 	               
			 	                 dos.writeBytes(lineEnd);
			 	
			 	
			 	                 // create a buffer of  maximum size
			 	                 bytesAvailable = fileInputStream.available(); 
			 	                 streamLength = bytesAvailable;
			 	                 bufferSize = Math.min(bytesAvailable, maxBufferSize);
			 	                 buffer = new byte[bufferSize];
			 	        
			 	                 // read file and write it into form...
			 	                 bytesRead = fileInputStream.read(buffer, 0, bufferSize);  
			 	                 readSoFar = bytesRead;
			 	                 
			 	                 prog =startProgress + Math.round(totalProgress * readSoFar / streamLength);
		                       	 publishProgress(prog);

			 	                 while (bytesRead > 0) {
			 	                      
			 	                   dos.write(buffer, 0, bufferSize);
			 	                   bytesAvailable = fileInputStream.available();
			 	                   bufferSize = Math.min(bytesAvailable, maxBufferSize);
			 	                   bytesRead = fileInputStream.read(buffer, 0, bufferSize);   
			 	                   readSoFar += bytesRead;
			 	                   
			 	                   
			 	                   prog =startProgress + Math.round(totalProgress * readSoFar / streamLength);
			                       publishProgress(prog);
			 	                   
			 	                 }
			 	        
			 	                 // send multipart form data necesssary after file data...
			 	                 dos.writeBytes(lineEnd);
			 	                 dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
			 	        
			 	                 // Responses from the server (code and message)
			 	                 serverResponseCode = conn.getResponseCode();
			 	                 String serverResponseMessage = conn.getResponseMessage();
			 	                   
			 	                 Log.i("uploadFile", "HTTP Response is : "
			 	                         + serverResponseMessage + ": " + serverResponseCode);
			 	                  if(serverResponseCode == 200){
								           
			 	                  }

	     	       
			 	                  
			 	                 //close the streams //
			 	                 fileInputStream.close();
			 	                 dos.flush();
			 	                 dos.close();
			 	                   
			 	            } catch (MalformedURLException ex) {
			 	               
			 	                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);  
			 	            } catch (Exception e) {
			 	                 
			 	               Log.e("Upload file to server Exception", "Exception : "
			 	                                                 + e.getMessage(), e);  
			 	            }
			 	            if(prog == 100){
	                       	   publishProgress(endProgress);
	                       	   String ss = String.format(API.CONTENTS, user.getId(),fileName);
					           post.setImageUrl(ss);
					           DataHolder.ToUpload.remove(fileName);
			 	            }
			 	           // dialog.dismiss();    
				     }
					return serverResponseCode == 200 ? Boolean.TRUE : Boolean.FALSE;
			 	 }

				@Override
			     protected void onProgressUpdate(final Integer... progress) {
	             //	 b.setVisibility(View.GONE);
					post.progress = progress[0];
	             	//demoListAdapter.notifyDataSetChanged(); wasn't commented

			     }
			     
			     void runTask(Runnable r, boolean sync, Handler handler) {
			 		if (sync) {
			 			r.run();
			 		} else {
			 			handler.post(r);
			 		}
			 	}
			     @Override
			     protected void onPostExecute(Boolean b) {
			    	 if(DataHolder.ToUpload.size() >0){
			    		 String s = DataHolder.ToUpload.get(0);
			    		 String so = s.substring(0, s.lastIndexOf("/")+1);
			    		 String name = s.substring(s.lastIndexOf("/"), s.length());
						 //up = new Uploader(so,name ,post,user,null).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR); wasn't commented
			    	 }
			     }
		}