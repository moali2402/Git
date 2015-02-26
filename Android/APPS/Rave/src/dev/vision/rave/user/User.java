package dev.vision.rave.user;

import dev.vision.rave.Constants;
import dev.vision.rave.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

public class User implements Cloneable{
	
	public enum Type{
		FEMALE,MALE;
    }
	
	/**
	 * @param username
	 * @param display_Name
	 * @param profile_Pic
	 * @param cover_Pic
	 * @param birth_Date
	 * @param sex
	 * @param status
	 * @param age
	 * @param following
	 * @param followers
	 */
	public User(String username, String display_Name, Bitmap profile_Pic,
			Bitmap cover_Pic, String birth_Date, String sex, String status,
			String age, String following, String followers, String number) {
		super();
		Username = username;
		Display_Name = display_Name;
		Profile_Pic = profile_Pic;
		Cover_Pic = cover_Pic;
		Birth_Date = birth_Date;
		Sex = Type.valueOf(sex.toUpperCase());
		Status = status;
		Age = age;
		Following = following;
		Followers = followers;
		id = number;
	}

	String Username = null;
	String Display_Name = null;
	String Profile_PicURL = null;
	String Cover_PicURL = null;
	Bitmap Profile_Pic = null;
	Bitmap Cover_Pic = null;
	String Birth_Date = null;
	Type Sex = null;
	String Status = "";
	String Age = null;
	String Following = null;
	String Followers = null;
	private String id;
	
	/**
	 * @param username
	 * @param display_Name
	 * @param birth_Date
	 * @param sex
	 */
	public User(Context c, String username, String display_Name, String birth_Date,
			String sex) {
		Constants.saveData(c, Constants.USER_ID, username);
		Constants.saveData(c, Constants.DISPLAY_NAME, display_Name);
		Constants.saveData(c, Constants.BIRTH_DATE, birth_Date);
		Constants.saveData(c, Constants.SEX, sex);
		Username = username;
		Display_Name = display_Name;
		Birth_Date = birth_Date;
		Sex =Type.valueOf(sex.toUpperCase());
	}

	public User() {
		
	}

	@Override
	public User clone(){
		try {
			return (User) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

	public synchronized String getUsername() {
		return Username;
	}

	public synchronized void setUsername(String username) {
		Username = username;
	}

	public synchronized String getDisplay_Name() {
		return Display_Name;
	}

	public synchronized void setDisplay_Name(String display_Name) {
		Display_Name = display_Name;
	}

	public synchronized Bitmap getProfile_Pic(Context c) {
		return Profile_Pic == null? getDefaultP_Pic(c) : Profile_Pic;
	}
	
	public synchronized boolean hasProfile_Pic() {
		return Profile_Pic != null;
	}

	public synchronized void setProfile_Pic(Bitmap profile_Pic) {
		Profile_Pic = profile_Pic;
	}
	

	public synchronized Bitmap getDefaultP_Pic(Context c) {
		return Profile_Pic = ((BitmapDrawable)c.getResources().getDrawable(R.drawable.default_person)).getBitmap();
	}


	public synchronized Bitmap getCover_Pic(Context c) {
		return Cover_Pic == null? ((BitmapDrawable)c.getResources().getDrawable(R.drawable.default_person)).getBitmap() : Cover_Pic;
	}

	public synchronized void setCover_Pic(Bitmap cover_Pic) {
		Cover_Pic = cover_Pic;
	}

	public synchronized String getBirth_Date() {
		return Birth_Date;
	}

	public synchronized void setBirth_Date(String birth_Date) {
		Birth_Date = birth_Date;
	}

	public synchronized Type getSex() {
		return Sex;
	}

	public synchronized void setSex(String sex) {
		Sex = Type.valueOf(sex.toUpperCase());
	}

	public synchronized void setSex(Type sex) {
		Sex = sex;
	}
	
	public synchronized String getStatus() {
		return Status;
	}

	public synchronized void setStatus(String status) {
		Status = status;
	}

	public synchronized int getAge() {
		return Age == null? 0 : Integer.parseInt(Age);
	}

	public synchronized void setAge(String age) {
		Age = age;
	}

	public synchronized long getFollowing() {
		return Following == null? 0 : Long.parseLong(Following);
	}

	public synchronized void setFollowing(String following) {
		Following = following;
	}

	public synchronized long getFollowers() {
		return Followers == null? 0 : Long.parseLong(Followers);
	}

	public synchronized void setFollowers(String followers) {
		Followers = followers;
	}

	public void setCover_PicURL(String cOVER_PIC2) {
		Cover_PicURL = cOVER_PIC2;
	}

	public String getProfile_PicURL() {
		return Profile_PicURL;
	}

	public void setProfile_PicURL(String pROFILE_PIC2) {
		Profile_PicURL = pROFILE_PIC2;		
	}

	public String getCover_PicURL() {
		return Cover_PicURL;
	}

	public String getId() {
		// TODO Auto-generated method stub
		return id;
	}

	public void setId(String number) {
		id=number;
	}


}
