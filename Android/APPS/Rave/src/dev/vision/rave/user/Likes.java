package dev.vision.rave.user;

import java.util.HashMap;

public class Likes extends HashMap<String, Like> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8300291114373291115L;
	private long FEMALE_LIKES;
	private long MALE_LIKES;
	/**
	 * @return the fEMALE_LIKES
	 */
	public long getFEMALE_LIKES() {
		long temp = 0;
		for(Like l : values()){
			temp += l.From.Sex == User.Type.FEMALE? 1 : 0;
		}
		FEMALE_LIKES = temp;
		return FEMALE_LIKES;
	}
	/**
	 * @param fEMALE_LIKES the fEMALE_LIKES to set
	 */
	public void setFEMALE_LIKES(long fEMALE_LIKES) {
		FEMALE_LIKES = fEMALE_LIKES;
	}
	/**
	 * @return the mALE_LIKES
	 */
	public long getMALE_LIKES() {
		long temp = 0;
		for(Like l : values()){
			temp += l.From.Sex == User.Type.MALE? 1 : 0;
		}
		MALE_LIKES = temp;
		return MALE_LIKES;
	}
	/**
	 * @param mALE_LIKES the mALE_LIKES to set
	 */
	public void setMALE_LIKES(long mALE_LIKES) {
		MALE_LIKES = mALE_LIKES;
	}
}
