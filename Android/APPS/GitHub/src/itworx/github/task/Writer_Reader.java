package itworx.github.task;

import itworx.github.task.repositories.Repositories;
import itworx.github.task.repositories.Repository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;

public class Writer_Reader {
	
	private static String FILE_NAME = "repos";
	private static String FILE_PATH = Environment.getExternalStorageDirectory() + File.separator + FILE_NAME;
	
	
	//Init Reader
	public static void Init(Context c) {
		Repositories.clearRepository();
		readXml();
		//readJSON(); //NOT USED
	}
	
	//Creates File Based On Repositories.REPOSITORIES List
	public static void createFile() {
		createXMLFile();
	}
		
	//NOT USED
	public static void createFile(String mJsonResponse) {		
		//createJsonFile(mJsonResponse); //NOT USED
	}
	
	/*
	 * Creates XML file based on required Data.
	 */
	private static void createXMLFile() {
		try {
		 	File f = new File(FILE_PATH+ ".xml");

		 	
	        FileOutputStream myFile= new FileOutputStream(f);
	        //Log.v("WriteFile","file created");  
			myFile.write(writeXml().getBytes());
			myFile.flush();
	        myFile.close();
	        
	    } catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Writes XML Based On Required Data
	 */
	private static  String writeXml(){
	    XmlSerializer serializer = Xml.newSerializer();
	    StringWriter writer = new StringWriter();
	    try {
	        serializer.setOutput(writer);
	        serializer.startDocument("UTF-8", true);
	        serializer.setPrefix("ns1", "http://tempuri.org/XMLSchema.xsd");
	        serializer.setPrefix("xsi","http://www.w3.org/2001/XMLSchema-instance");
	        serializer.startTag("ns1" , "REPOS");
	        
	        
	        for (Repository repo: Repositories.REPOSITORIES){
	            serializer.startTag("ns1", "REPOSITORY");

	        		 
	        		serializer.startTag("ns1", "REPO_ID");
		            serializer.text(repo.getId());
		            serializer.endTag("ns1", "REPO_ID");
		
		            serializer.startTag("ns1", "DESCRIPTION");
		            serializer.text(repo.getDescription());
		            serializer.endTag("ns1", "DESCRIPTION");
		
		            serializer.startTag("ns1", "NAME");
		            serializer.text(repo.getName());
		            serializer.endTag("ns1", "NAME");
	
		            serializer.startTag("ns1", "FULL_NAME");
		            serializer.text(repo.getFull_name());
		            serializer.endTag("ns1", "FULL_NAME");
		            
		            serializer.startTag("ns1", "HTML_URL");
		            serializer.text(repo.getHtml_url());
		            serializer.endTag("ns1", "HTML_URL");
		            
		            serializer.startTag("ns1", "LOGIN");
		            serializer.text(repo.getOwnerLogin());
		            serializer.endTag("ns1", "LOGIN");
		            
		            serializer.startTag("ns1", "OWNER_AVATAR");
		            serializer.text(repo.getOwnerAvatar_url());
		            serializer.endTag("ns1", "OWNER_AVATAR");
		            
		            serializer.startTag("ns1", "CREATION_DATE");
		            serializer.text(repo.getCreated_at());
		            serializer.endTag("ns1", "CREATION_DATE");

		        serializer.endTag("ns1", "REPOSITORY");

	        }
	        
           
            
	        serializer.endTag("ns1", "REPOS");
	        serializer.endDocument();
	        return writer.toString();
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    } 
	}
	 
	 /*
	  * Reads XML File
	  */
	 private static void readXml(){
		File file = new File(FILE_PATH+".xml"); 
		String data = null;
		if(file.exists()){
			try {
			     FileInputStream fis = new FileInputStream(file);
			     InputStreamReader isr = new InputStreamReader(fis);
			     char[] inputBuffer = new char[fis.available()];
			     isr.read(inputBuffer);
			     data = new String(inputBuffer);
			     isr.close();
			     fis.close();
			 }
			 catch (FileNotFoundException e3) {
			     e3.printStackTrace();
			 }
			 catch (IOException e) {
			     e.printStackTrace();
			 }

			 XmlPullParserFactory factory = null;
			 try {
			     factory = XmlPullParserFactory.newInstance();
			 }
			 catch (XmlPullParserException e2) {
			     e2.printStackTrace();
			 }
	
			 factory.setNamespaceAware(true);
			 XmlPullParser xpp = null;
			 try {
			     xpp = factory.newPullParser();
			 }
			 catch (XmlPullParserException e2) {
			     e2.printStackTrace();
			 }
			 try {
			     xpp.setInput(new StringReader(data));
			 }
			 catch (XmlPullParserException e1) {
			     e1.printStackTrace();
			 }
			 int eventType = 0;
			 try {
			     eventType = xpp.getEventType();
			 }
			 catch (XmlPullParserException e1) {
			     e1.printStackTrace();
			 }
			 String tag = null;
			 Repository r = null;
			 while (eventType != XmlPullParser.END_DOCUMENT){
				 
			     if (eventType == XmlPullParser.START_DOCUMENT) {
			         //System.out.println("Start document");
			     }else if (eventType == XmlPullParser.START_TAG) {
			         tag = xpp.getName();
			         if(tag.equals("REPOSITORY")){
			        	 r = new Repository();
			         }
	
			     }else if (eventType == XmlPullParser.END_TAG) {
			         tag = xpp.getName();

			         if(tag.equals("REPOSITORY")){
			        	 if(r!=null)
			        	 Repositories.addRepository(r);
			         }
			         tag = null;

			     }else if(eventType == XmlPullParser.TEXT) {
			         if(tag != null){
			        	 String text = xpp.getText();
				         //System.out.println(tag +": " +text);
				         
	
			        	 if(tag.equals("REPO_ID")){
			        		 r.setId(text);
			        	 }else if(tag.equals("NAME")){
			        		 r.setName(text);
			        	 }else if(tag.equals("FULL_NAME")){
			        		 r.setFull_name(text);
			        	 }else if(tag.equals("LOGIN")){
			        		 r.setOwnerLogin(text);
			        	 }else if(tag.equals("DESCRIPTION")){
			        		 r.setDescription(text);
			        	 }else if(tag.equals("HTML_URL")){
			        		 r.setHtml_url(text);
			        	 }else if(tag.equals("OWNER_AVATAR")){
			        		 r.setOwnerAvatar(text);
			        	 }else if(tag.equals("CREATION_DATE")){
			        		 r.setCreated_at(text);        		 
			        	 }
			         }
			     }
			     try {
			         eventType = xpp.next();
			     }
			     catch (XmlPullParserException e) {
			         e.printStackTrace();
			     }
			     catch (IOException e) {
			         e.printStackTrace();
			     }
			 }
		 }

	 }
	 
	 

	//-NOT USED ANYMORE 
	private static void createJsonFile(String mJsonResponse) {
		try {
		    FileWriter file = new FileWriter(FILE_PATH+".json");
		    file.write(mJsonResponse);
		    file.flush();
		    file.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}
	
	
	//-NOT USED ANYMORE 
	private static void readJSON() throws JSONException {
	    try {
	        File f = new File(FILE_PATH);
	        if(f.exists()){
		        FileInputStream is = new FileInputStream(f);
		        int size = is.available();
		        byte[] buffer = new byte[size];
		        is.read(buffer);
		        is.close();
		        String result = new String(buffer);
		        
		        JSONArray js = new JSONArray(result.toString());
				int count = js.length();

				for(int i=0 ; i<count; i++ )
				{
					JSONObject jo = (JSONObject) js.get(i);
					Repository rs = new Repository(jo);
					Log.d("Result", rs.toString());
					Repositories.addRepository(rs);
				}
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
}
