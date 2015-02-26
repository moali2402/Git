package com.example.demo;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.XMLFormatter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import com.example.demo.Activity_Manage.StoreList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Toast;

public class Quote implements Serializable {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -5673377523289853588L;

	final static String filesPath = Environment.getExternalStorageDirectory() + File.separator + "TheQuote" + File.separator+ "quotes" + File.separator ;

	ArrayList<Row> rows;
	Client_Details cd;
	ArrayList<String> attachments;
	
	private String QuoteId = "";
	String date = "";
	User user;
	private boolean calculated;
	
		
	double total_price = 0;
	double payable_1 = 0;
	double payable_2 = 0;
	double payable_3 = 0;
	double payable_4 = 0;
	double payable_5 = 0;
	
	
	double total_payable_1 = 0;
	double total_payable_2 = 0;
	double total_payable_3 = 0;
	double total_payable_4 = 0;
	double total_payable_5 = 0;

	private Context context;

	private String term = "";
	
	public Quote(User user){
		this.user =user;
		setQuoteId(generateRandomQuoteId());
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");      

		//Date dateWithoutTime = sdf.parse(sdf.format(new Date()));
		date = sdf.format(new Date());
		
		rows = new ArrayList<Row>();
		attachments = new ArrayList<String>();
		cd = new Client_Details();
	}
	
	public Quote(Context c,User user, String Quote_id){
		this.user =user;
		setQuoteId(Quote_id);
		
		rows = new ArrayList<Row>();
		attachments = new ArrayList<String>();
		cd = new Client_Details();
		
		readXml(c, Quote_id);
	}
		
	String generateRandomQuoteId(){
		 Random rnd = new Random();
		 int numLetters = 5;
		 Calendar c = Calendar.getInstance();
		 String quoteId = user.store.Storeid +  c.get(Calendar.SECOND) + c.get(Calendar.MINUTE) + c.get(Calendar.HOUR_OF_DAY);
		 String randomLetters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

		 for (int n=0; n<numLetters; n++)
		   quoteId += randomLetters.charAt(rnd.nextInt(randomLetters.length()));
		 quoteId = quoteId.toUpperCase();
		 return quoteId;
	 }
	
	
	void add(Row r){
		rows.add(r);
	}
	 

	void calculateData(){
		 total_price = 0;
		 payable_1 = 0;
		 payable_2 = 0;
		 payable_3 = 0;
		 payable_4 = 0;
		 payable_5 = 0;
		 total_payable_1 = 0;
		 total_payable_2 = 0;
		 total_payable_3 = 0;
		 total_payable_4 = 0;
		 total_payable_5 = 0;
		
		if(this.rows.size() >0 ){
			 for(Row r : rows){
				 total_price += r.i.price;
				
				 payable_1 += r.i.first.getPayment();
				 payable_2 += r.i.second.getPayment();
				 payable_3 += r.i.third.getPayment();
				 payable_4 += r.i.fourth.getPayment();
				 payable_5 += r.i.fifth.getPayment();
	
				 
				 total_payable_1 += r.i.first.getTotalPayable();
				 total_payable_2 += r.i.second.getTotalPayable();
				 total_payable_3 += r.i.third.getTotalPayable();
				 total_payable_4 += r.i.fourth.getTotalPayable();
				 total_payable_5 += r.i.fifth.getTotalPayable();
			 }
		     calculated = total_price > 0;
		}else{
			calculated = false;
		}
	}


	void set(int pos, Row r) {
		rows.set(pos, r);
	}
	
	void remove(int pos) {
		rows.remove(pos);
	}

	public void attach(String string) {
		attachments.add(string);
	}
	
	public void save(Context c, View view) {
		this.context = c;
		cd.getFields(view);
        try {
		 	File f = new File(filesPath+user.store.Storeid+File.separator+getQuoteId());
		 	f.mkdirs();
		 	f = new File(filesPath+user.store.Storeid+File.separator+getQuoteId(), "quote.xml");

		 	
	        FileOutputStream myFile= new FileOutputStream(f);
	        Log.v("WriteFile","file created");  
				myFile.write(writeXml().getBytes());
			
            myFile.close();
            
            for(String l : attachments){
            	if(!l.contains(user.store.Storeid+File.separator+getQuoteId()))
            	copyfile(l, filesPath+user.store.Storeid+File.separator+getQuoteId());
            }
            Toast.makeText(c, "Application Saved", Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            Toast.makeText(c, "Failed to save application!", Toast.LENGTH_LONG).show();
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	public void save(Context c) {
		this.context = c;
		cd.getFields(((Quote_Activity)c));
        try {
		 	File f = new File(filesPath+user.store.Storeid+File.separator+getQuoteId());
		 	f.mkdirs();
		 	f = new File(filesPath+user.store.Storeid+File.separator+getQuoteId(), "quote.xml");

		 	
	        FileOutputStream myFile= new FileOutputStream(f);
	        Log.v("WriteFile","file created");  
				myFile.write(writeXml().getBytes());
			
            myFile.close();
            
            for(String l : attachments){
            	if(!l.contains(user.store.Storeid+File.separator+getQuoteId()))
            	copyfile(l, filesPath+user.store.Storeid+File.separator+getQuoteId());
            }
            Toast.makeText(c, "Application Saved", Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            Toast.makeText(c, "Failed to save application!", Toast.LENGTH_LONG).show();
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	public void upload(Context c){
		save(c);
        new upload().execute();
	}
	
	public void upload(Context c,View v){
		save(c, v);
        new upload().execute();
	}
	
	 class upload extends AsyncTask<Store, Store, Boolean>{
		 
		 	ProgressDialog pr;
	    	@Override
			protected void onPreExecute() {
	    		super.onPreExecute();
				pr = ProgressDialog.show(context, null, "Please wait while we submit your application");
			}

			@Override
			protected Boolean doInBackground(Store... stores) {
				 
				DefaultHttpClient client = new DefaultHttpClient();
				HttpPost request = new HttpPost(API.UPLOAD);
				boolean success = false;
				try{
					MultipartEntity mpEntity = new MultipartEntity();

					//Path of the file to be uploaded
					for(String filepath : attachments ){
				        File file = new File(filepath);
				        System.out.println(filepath);
				        ContentBody cbFile = new FileBody(file, "image/jpeg");         
				        //Add the data to the multipart entity
				        mpEntity.addPart("uploaded_file[]", cbFile);
					}
					
					File xml = new File(filesPath+user.store.Storeid+File.separator+getQuoteId()+File.separator+"quote.xml");
			        ContentBody cbFile = new FileBody(xml, "text/xml");         
			        //Add the data to the multipart entity
			        mpEntity.addPart("uploaded_file[]", cbFile);
				
			        mpEntity.addPart("storeid", new StringBody(user.store.Storeid, Charset.forName("UTF-8")));
			        mpEntity.addPart("quoteid", new StringBody(getQuoteId(), Charset.forName("UTF-8")));
			        mpEntity.addPart("email", new StringBody(user.store.email, Charset.forName("UTF-8")));


					request.setEntity(mpEntity);
				
					HttpResponse httpResponse = client.execute(request);
					
					
					if(httpResponse.getStatusLine().getStatusCode() == 200){
						BufferedReader rd = new BufferedReader(
							new InputStreamReader(httpResponse.getEntity().getContent()));
					 
						StringBuffer result = new StringBuffer();
						String line = "";
						while ((line = rd.readLine()) != null) {
							result.append(line);
						}
						Log.d("Result", result.toString());
						JSONObject js = new JSONObject(result.toString());
						success = js.getBoolean("success");
						if(success)	{
						}
					}
						
				}catch (Exception e){
					
				}
				return success;
			}
			
			@Override
			public void onPostExecute(Boolean b){
				pr.dismiss();
				if(b.booleanValue()){
					Toast.makeText(context, "Application Successfully Submitted", Toast.LENGTH_LONG).show();
				 	File f = new File(filesPath+user.store.Storeid+File.separator+getQuoteId());
				 	DeleteRecursive(f);
					((Fragment_Activity) context).recreateA();
				}else{
					Toast.makeText(context, "There was a problem submitting application", Toast.LENGTH_LONG).show();
				}
			}
			
			void DeleteRecursive(File fileOrDirectory) {
			    if (fileOrDirectory.isDirectory())
			        for (File child : fileOrDirectory.listFiles())
			            DeleteRecursive(child);

			    fileOrDirectory.delete();
			}
				
	    	
	    }
	 
	 private String writeXml() throws TransformerConfigurationException, TransformerFactoryConfigurationError{
		 
		final String NAMESPACE = "ns1"; 
	    int i = 1;
	    XmlSerializer serializer = Xml.newSerializer();
	    StringWriter writer = new StringWriter();
	    try {
	        serializer.setOutput(writer);
	        serializer.startDocument("UTF-8", true);
	        serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
	        serializer.setPrefix("ns1", "http://tempuri.org/XMLSchema.xsd");
	        serializer.setPrefix("xsi","http://www.w3.org/2001/XMLSchema-instance");
	        serializer.setPrefix(NAMESPACE,NAMESPACE);
	        serializer.startTag(NAMESPACE , "HBA");
	        

            serializer.startTag(NAMESPACE, "DATE");
            serializer.text(date);
            serializer.endTag(NAMESPACE, "DATE");
            
            serializer.startTag(NAMESPACE, "QUOTENUMBER");
            serializer.text(QuoteId);
            serializer.endTag(NAMESPACE, "QUOTENUMBER");
            
	        serializer.startTag(NAMESPACE, "TITLE");
            serializer.text(cd.title);
            serializer.endTag(NAMESPACE, "TITLE");
            
            serializer.startTag(NAMESPACE, "FIRSTNAME");
            serializer.text(cd.f_name);
            serializer.endTag(NAMESPACE, "FIRSTNAME");
            
            serializer.startTag(NAMESPACE, "MIDDLENAME");
            serializer.text(cd.m_name);
            serializer.endTag(NAMESPACE, "MIDDLENAME");
            
            serializer.startTag(NAMESPACE, "LASTNAME");
            serializer.text(cd.s_name);
            serializer.endTag(NAMESPACE, "LASTNAME");

            serializer.startTag(NAMESPACE, "MOBILE");
            serializer.text(cd.mob);
            serializer.endTag(NAMESPACE, "MOBILE");
            
            serializer.startTag(NAMESPACE, "ADDRESS");
            serializer.text(cd.address);
            serializer.endTag(NAMESPACE, "ADDRESS");
            
            serializer.startTag(NAMESPACE, "SUBURB");
            serializer.text(cd.suburb);
            serializer.endTag(NAMESPACE, "SUBURB");

            serializer.startTag(NAMESPACE, "STATE");
            serializer.text(cd.state);
            serializer.endTag(NAMESPACE, "STATE");
            
            serializer.startTag(NAMESPACE, "POSTCODE");
            serializer.text(cd.pcode);
            serializer.endTag(NAMESPACE, "POSTCODE");
      
            serializer.startTag(NAMESPACE, "HOMEPHONE");
            serializer.text(cd.tel);
            serializer.endTag(NAMESPACE, "HOMEPHONE");

            serializer.startTag(NAMESPACE, "WORK");
            serializer.text(cd.work);
            serializer.endTag(NAMESPACE, "WORK");
            
            serializer.startTag(NAMESPACE, "DOB");
            serializer.text(cd.DOB);
            serializer.endTag(NAMESPACE, "DOB");

            serializer.startTag(NAMESPACE, "DRIVERLICENSE");
            serializer.text(cd.id);
            serializer.endTag(NAMESPACE, "DRIVERLICENSE");

            serializer.startTag(NAMESPACE, "NOTES");
            serializer.text(cd.notes);
            serializer.endTag(NAMESPACE, "NOTES");
            
            serializer.startTag(NAMESPACE, "TERM");
            serializer.text(term.contains("weeks")? term.replace(" Weeks", "") : "0");
            serializer.endTag(NAMESPACE, "TERM");

	        i = 1;
	        for (Row row: rows){
	            serializer.startTag(NAMESPACE, "CHATTEL"+i);
	            serializer.text(row.item);
	            serializer.endTag(NAMESPACE, "CHATTEL"+i);

	            serializer.startTag(NAMESPACE, "VALUE"+i);
	            serializer.text(""+row.i.price);
	            serializer.endTag(NAMESPACE, "VALUE"+i);
	            
	            serializer.startTag(NAMESPACE, "DISCOUNT"+i);
	            serializer.text(""+row.i.discount);
	            serializer.endTag(NAMESPACE, "DISCOUNT"+i);

	            
	            i++;
	        }
	        
	        /*
	        serializer.startTag(NAMESPACE, "PRICETOTAL");
            serializer.text(String.format("%.2f",total_price));
            serializer.endTag(NAMESPACE, "PRICETOTAL");
            */
            
            //TotalWK
            i=1;

            serializer.startTag(NAMESPACE, "TOTALAMOUNTwk"+i);
            serializer.text(String.format("%.2f",payable_1));
            serializer.endTag(NAMESPACE, "TOTALAMOUNTwk"+i);
            
            i++;

            serializer.startTag(NAMESPACE, "TOTALAMOUNTwk"+i);
            serializer.text(String.format("%.2f",payable_2));
            serializer.endTag(NAMESPACE, "TOTALAMOUNTwk"+i);
            
            i++;

            serializer.startTag(NAMESPACE, "TOTALAMOUNTwk"+i);
            serializer.text(String.format("%.2f",payable_3));
            serializer.endTag(NAMESPACE, "TOTALAMOUNTwk"+i);
            
            i++;	            

            serializer.startTag(NAMESPACE, "TOTALAMOUNTwk"+i);
            serializer.text(String.format("%.2f",payable_4));
            serializer.endTag(NAMESPACE, "TOTALAMOUNTwk"+i);
            
            i++;

            serializer.startTag(NAMESPACE, "TOTALAMOUNTwk"+i);
            serializer.text(String.format("%.2f",payable_5));
            serializer.endTag(NAMESPACE, "TOTALAMOUNTwk"+i);

            i = 1;
            
            
            //TotalAll

            serializer.startTag(NAMESPACE, "TOTALAMOUNTall"+i);
            serializer.text(String.format("%.2f",total_payable_1));
            serializer.endTag(NAMESPACE, "TOTALAMOUNTall"+i);
            
            i++;

            serializer.startTag(NAMESPACE, "TOTALAMOUNTall"+i);
            serializer.text(String.format("%.2f",total_payable_2));
            serializer.endTag(NAMESPACE, "TOTALAMOUNTall"+i);
            
            i++;

            serializer.startTag(NAMESPACE, "TOTALAMOUNTall"+i);
            serializer.text(String.format("%.2f",total_payable_3));
            serializer.endTag(NAMESPACE, "TOTALAMOUNTall"+i);
            
            i++;

            serializer.startTag(NAMESPACE, "TOTALAMOUNTall"+i);
            serializer.text(String.format("%.2f",total_payable_4));
            serializer.endTag(NAMESPACE, "TOTALAMOUNTall"+i);
        
            i++;

            serializer.startTag(NAMESPACE, "TOTALAMOUNTall"+i);
            serializer.text(String.format("%.2f",total_payable_5));
            serializer.endTag(NAMESPACE, "TOTALAMOUNTall"+i);
            
            
	        serializer.startTag(NAMESPACE, "STOREREP");
	        serializer.text(user.storerep);
	        serializer.endTag(NAMESPACE, "STOREREP");
	         
	        serializer.startTag(NAMESPACE, "STORENAME");
	        serializer.text(user.store.Storename);
	        serializer.endTag(NAMESPACE, "STORENAME");
         

	        serializer.endTag(NAMESPACE, "HBA");
	        serializer.endDocument();
	        return writer.toString();
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    } 
	}
	 
	 
	 public void readXml(Context c, String quote_id){
		File file = new File(filesPath+user.store.Storeid+File.separator+quote_id, "quote.xml"); 
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
			 cd = new Client_Details();
			 String tag = null;
			 while (eventType != XmlPullParser.END_DOCUMENT){
			     if (eventType == XmlPullParser.START_DOCUMENT) {
			         //System.out.println("Start document");
			     }
			     else if (eventType == XmlPullParser.START_TAG) {
			         tag = xpp.getName();
			         //System.out.println("Start tag "+ tag);
	
			     }
			     else if (eventType == XmlPullParser.END_TAG) {
			         //System.out.println("End tag "+xpp.getName());
			         tag = null;
			     }
			     else if(eventType == XmlPullParser.TEXT) {
			         if(tag != null){
			        	 String text = xpp.getText();
				         System.out.println(tag +": " +text);
	
			        	 if(tag.equals("TITLE")){
			        		 cd.title = text;
			        	 }else if(tag.equals("FIRSTNAME")){
			        		 cd.f_name = text;
			        	 }else if(tag.equals("MIDDLENAME")){
			        		 cd.m_name = text;		        		 
			        	 }else if(tag.equals("LASTNAME")){
			        		 cd.s_name = text;
			        	 }else if(tag.equals("ADDRESS")){
			        		 cd.address = text;	        		 
			        	 }else if(tag.equals("SUBURB")){
			        		 cd.suburb = text;	        		 
			        	 }else if(tag.equals("POSTCODE")){
			        		 cd.pcode = text;	        		 
			        	 }else if(tag.equals("STATE")){
			        		 cd.state = text;	        		 
			        	 }else if(tag.equals("MOBILE")){
			        		 cd.mob = text;	        		 
			        	 }else if(tag.equals("STOREREP")){
			        		 user.storerep = text;
			        		 user.Username = text;
			        	 }else if(tag.equals("STORENAME")){
			        		 user.store.Storename = text;  
			        	 }else if(tag.equals("DATE")){
			        		 date = text;
			        	 }else if(tag.equals("HOMEPHONE")){
			        		 cd.tel = text;
			        	 }else if(tag.equals("WORK")){
			        		 cd.work = text;
			        	 }else if(tag.equals("DOB")){
			        		 cd.DOB = text;
			        	 }else if(tag.equals("DRIVERLICENSE")){
			        		 cd.id = text;
			        	 }else if(tag.equals("NOTES")){
			        		 cd.notes = text;
			        	 }else if(tag.equals("TERM")){
			        		 term = text;
			        	 }else if(tag.startsWith("CHATTEL")){
			        		 char n = tag.charAt(tag.length()-1);
			        		 int i = Character.getNumericValue(n) - 1;
			        		 if(i >= rows.size()){
			        			 Row r = new Row();
				        		 r.item = text;
			        			 add(r);
			        		 }else{
			        			 Row r = rows.get(i);
				        		 r.item = text;
			        			 set(i, r);
			        		 }
			        	 }else if(tag.startsWith("VALUE")){
			        		 char n = tag.charAt(tag.length()-1);
			        		 int i = Character.getNumericValue(n) - 1;
			        		 if(i >= rows.size()){
			        			 Row r = new Row();
				        		 r.setPrice(Float.parseFloat(text));
			        			 add(r);
			        		 }else{
			        			 Row r = rows.get(i);
				        		 r.setPrice(Float.parseFloat(text));
			        			 set(i, r);
			        		 }
			        	 }else if(tag.equals("DISCOUNT")){
			        		 char n = tag.charAt(tag.length()-1);
			        		 int i = Character.getNumericValue(n) - 1;
			        		 if(i >= rows.size()){
			        			 Row r = new Row();
				        		 r.setDisc(Integer.parseInt(text));
			        			 add(r);
			        		 }else{
			        			 Row r = rows.get(i);
				        		 r.setDisc(Integer.parseInt(text));
			        			 set(i, r);
			        		 }
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
			         // TODO Auto-generated catch block
			         e.printStackTrace();
			     }
			 }
		 }

		 for(Row r : rows){
			 r.calculate();
		 }
		 File yourDir = new File(filesPath+user.store.Storeid+File.separator+quote_id);
		 for (File f : yourDir.listFiles()) {
		     if (f.isFile()){
		         String name = f.getName();
		         if(!name.substring(name.lastIndexOf('.') + 1).contains("xml"))
		          attachments.add(Uri.fromFile(new File(filesPath+user.store.Storeid+File.separator+quote_id,name)).getPath());
		     }
		 }
	 }
	 
	 private static void copyfile(String srFile, String dtFile){
		 try{
		   File f1 = new File(srFile);
		   File f2 = new File(dtFile + File.separator+ f1.getName());
		   InputStream in = new FileInputStream(f1);

		   //For Append the file.
		   //OutputStream out = new FileOutputStream(f2,true);

		   //For Overwrite the file.
		   OutputStream out = new FileOutputStream(f2);

		   byte[] buf = new byte[1024];
		   int len;
		   while ((len = in.read(buf)) > 0){
		     out.write(buf, 0, len);
		   }
		   in.close();
		   out.close();
		 }
		 catch(FileNotFoundException ex){
		   System.out.println(ex.getMessage() + " in the specified directory.");
		   System.exit(0);
		 }
		 catch(IOException e){
		   System.out.println(e.getMessage());      
		 }
    }
	 

	/**
	 * @return the quoteId
	 */
	public String getQuoteId() {
		return QuoteId;
	}

	/**
	 * @param quoteId the quoteId to set
	 */
	public void setQuoteId(String quoteId) {
		QuoteId = quoteId;
	}

	public boolean isCalulated() {
		return calculated;
	}

	public void removeAttachment(int position) {
		attachments.remove(position);
	}

	public void setTerm(String term) {
		this.term =term;
	}
	 
	 
}
