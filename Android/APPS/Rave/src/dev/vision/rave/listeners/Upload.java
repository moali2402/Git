package dev.vision.rave.listeners;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import dev.vision.rave.DataHolder;
import dev.vision.rave.user.Post;
import dev.vision.rave.user.User;

public class Upload {
	String ip_address ="41.233.33.220";
	Handler h = new Handler();
	AsyncTask<Object, Integer, Boolean> up;
	
	
	public class UploadAsync implements Runnable {
		 FileProgressListener proLis;
		int startProgress = 0;
		 int endProgress = 100;
		 int totalProgress = endProgress - startProgress;
		 int readSoFar;
		 long streamLength;
		 String sourceFileUri;
    	String filename ;
    	Post post ;
    	User user;
		public UploadAsync(String sourceFileUri, String filename , Post post, User user,FileProgressListener f){
			this.sourceFileUri = sourceFileUri;
	    	this.filename = filename;
	    	this.post = post;
	    	this.user = user;
		}
		@Override
		public void run() {
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
	 	         
	 	        if (!sourceFile.isFile()) {

	 	        }
	 	        else
	 	        {
	 	             
	 				
	 	            try { 
	 			         DataHolder.ToUpload.add(fileName);
	 	
	 	                   // open a URL connection to the Servlet
	 	                 FileInputStream fileInputStream = new FileInputStream(sourceFile);
	 	                 URL url = new URL("http://"+ip_address+":80/selfiie/upload.php");
	 	                  
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
	 	                 
	 	                 int prog =startProgress + Math.round(totalProgress * readSoFar / streamLength);
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
	 	                  
	 	                 
                       	 publishProgress(endProgress);

 	       
	 	                  
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
	 	           // dialog.dismiss();    
		     }
		}
		private void publishProgress(final int startProgress2) {
			if (proLis != null) {
        		Runnable r = new Runnable() {
        			@Override
        			public void run() {
        				proLis.onProgressUpdate(startProgress2);
        			}
        		};
        		runTask(r, false, h);
        	}
		}
		
		 void runTask(Runnable r, boolean sync, Handler handler) {
		 		if (sync) {
		 			r.run();
		 		} else {
		 			handler.post(r);
		 		}
		 	 }
	}
		 


	
		     

	
}
