package dev.vision.rave.user;

import android.graphics.Bitmap;
import android.location.Location;

public class Post {
	
	public enum Type{
		IMAGE,VIDEO,TEXT;
    }
	
	String ID;
	String Link;
	Type type;
	Bitmap Image;
	String Body;
	String Date;
	User From = null;
	Likes likes = new Likes();
	Comments comments = new Comments();
	Location location;
	String ImageUrl = null;
	long likesSize = 0;
	private String ImagePath;
	private String ImageName;
	public int progress =0;
	
	public synchronized String getID() {
		return ID;
	}
	public synchronized void setID(String iD) {
		ID = iD;
	}
	public synchronized String getLink() {
		return Link;
	}
	public synchronized void setLink(String link) {
		Link = link;
	}
	public synchronized Type getType() {
		return type;
	}
	public synchronized void setType(String t) {
		type = Type.valueOf(t.toUpperCase());
	}
	public synchronized Bitmap getImage() {
		return Image;
	}
	public synchronized void setImage(Bitmap image) {
		Image = image;
	}
	public synchronized String getBody() {
		return Body;
	}
	public synchronized void setBody(String body) {
		Body = body;
	}
	public synchronized String getDate() {
		return Date;
	}
	public synchronized void setDate(String date) {
		Date = date;
	}
	public synchronized User getFrom() {
		return From;
	}
	public synchronized void setFrom(User from) {
		From = from;
	}
	public synchronized Likes getLikes() {
		return likes;
	}
	public synchronized long getLikesSize() {
		return likesSize;
	}
	
	
	public synchronized void setLikes(Likes likes) {
		this.likes = likes;
		likesSize = likes.size();
	}
	
	public synchronized Comments getComments() {
		return comments;
	}
	public synchronized void setComments(Comments comments) {
		this.comments = comments;
	}
	public synchronized Location getLocation() {
		return location;
	}
	public synchronized void setLocation(Location location) {
		this.location = location;
	}
	public void setImageUrl(String string) {
		this.ImageUrl = string;
	}
	public String getImageUrl() {
		return this.ImageUrl;
	}
	public void like(User user) {
		likes.put(user.getUsername(), new Like(user));
		likesSize = likes.size();
	}
	public void unlike(User user) {
		likes.remove(user.getUsername());
		likesSize = likes.size();
	}
	public boolean isliked(User user) {
		return likes.containsKey(user.getUsername());
	}
	public String getImagePath() {
		// TODO Auto-generated method stub
		return ImagePath;
	}
	public String getImageName() {
		// TODO Auto-generated method stub
		return ImageName;
	}
	public void setImagePath(String string) {
		this.ImagePath = string;
	}
	public void setImageName(String string) {
		this.ImageName = string;
	}
}
