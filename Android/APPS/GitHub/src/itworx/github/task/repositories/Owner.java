package itworx.github.task.repositories;

import org.json.JSONObject;

public class Owner {

	
    String login="";
    String id="";
    String avatar_url="";
    String gravatar_id;
    String url;
    String html_url;
    String followers_url;
    String following_url;
    String gists_url;
    String starred_url;
    String subscriptions_url;
    String organizations_url;
    String repos_url;
    String events_url;
    String received_events_url;
    String type;
    boolean site_admin;
    
	public Owner() {}
	
	public Owner(JSONObject jo) {
		
		//Strings
		login 				= jo.optString("login");
		id 					= jo.optString("id");
		avatar_url 			= jo.optString("avatar_url");			
		html_url 			= jo.optString("html_url");
		gravatar_id 		= jo.optString("gravatar_id");
		url 				= jo.optString("url");
		followers_url 		= jo.optString("followers_url");
		following_url 		= jo.optString("following_url");
		gists_url 			= jo.optString("gists_url");
		starred_url 		= jo.optString("starred_url");
		subscriptions_url 	= jo.optString("subscriptions_url");
		organizations_url 	= jo.optString("organizations_url");
		repos_url 			= jo.optString("repos_url");
		events_url 			= jo.optString("events_url");
		received_events_url = jo.optString("received_events_url");
		type 				= jo.optString("type");
		
		//Boolean
		site_admin 				= jo.optBoolean("site_admin");		
	}
	
	/**
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}
	/**
	 * @param login the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the avatar_url
	 */
	public String getAvatar_url() {
		return avatar_url;
	}
	/**
	 * @param avatar_url the avatar_url to set
	 */
	public void setAvatar_url(String avatar_url) {
		this.avatar_url = avatar_url;
	}
	/**
	 * @return the gravatar_id
	 */
	public String getGravatar_id() {
		return gravatar_id;
	}
	/**
	 * @param gravatar_id the gravatar_id to set
	 */
	public void setGravatar_id(String gravatar_id) {
		this.gravatar_id = gravatar_id;
	}
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * @return the html_url
	 */
	public String getHtml_url() {
		return html_url;
	}
	/**
	 * @param html_url the html_url to set
	 */
	public void setHtml_url(String html_url) {
		this.html_url = html_url;
	}
	/**
	 * @return the followers_url
	 */
	public String getFollowers_url() {
		return followers_url;
	}
	/**
	 * @param followers_url the followers_url to set
	 */
	public void setFollowers_url(String followers_url) {
		this.followers_url = followers_url;
	}
	/**
	 * @return the following_url
	 */
	public String getFollowing_url() {
		return following_url;
	}
	/**
	 * @param following_url the following_url to set
	 */
	public void setFollowing_url(String following_url) {
		this.following_url = following_url;
	}
	/**
	 * @return the gists_url
	 */
	public String getGists_url() {
		return gists_url;
	}
	/**
	 * @param gists_url the gists_url to set
	 */
	public void setGists_url(String gists_url) {
		this.gists_url = gists_url;
	}
	/**
	 * @return the starred_url
	 */
	public String getStarred_url() {
		return starred_url;
	}
	/**
	 * @param starred_url the starred_url to set
	 */
	public void setStarred_url(String starred_url) {
		this.starred_url = starred_url;
	}
	/**
	 * @return the subscriptions_url
	 */
	public String getSubscriptions_url() {
		return subscriptions_url;
	}
	/**
	 * @param subscriptions_url the subscriptions_url to set
	 */
	public void setSubscriptions_url(String subscriptions_url) {
		this.subscriptions_url = subscriptions_url;
	}
	/**
	 * @return the organizations_url
	 */
	public String getOrganizations_url() {
		return organizations_url;
	}
	/**
	 * @param organizations_url the organizations_url to set
	 */
	public void setOrganizations_url(String organizations_url) {
		this.organizations_url = organizations_url;
	}
	/**
	 * @return the repos_url
	 */
	public String getRepos_url() {
		return repos_url;
	}
	/**
	 * @param repos_url the repos_url to set
	 */
	public void setRepos_url(String repos_url) {
		this.repos_url = repos_url;
	}
	/**
	 * @return the events_url
	 */
	public String getEvents_url() {
		return events_url;
	}
	/**
	 * @param events_url the events_url to set
	 */
	public void setEvents_url(String events_url) {
		this.events_url = events_url;
	}
	/**
	 * @return the received_events_url
	 */
	public String getReceived_events_url() {
		return received_events_url;
	}
	/**
	 * @param received_events_url the received_events_url to set
	 */
	public void setReceived_events_url(String received_events_url) {
		this.received_events_url = received_events_url;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the site_admin
	 */
	public boolean isSite_admin() {
		return site_admin;
	}
	/**
	 * @param site_admin the site_admin to set
	 */
	public void setSite_admin(boolean site_admin) {
		this.site_admin = site_admin;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Owner [login=" + login + ", id=" + id + ", avatar_url="
				+ avatar_url + ", gravatar_id=" + gravatar_id + ", url=" + url
				+ ", html_url=" + html_url + ", followers_url=" + followers_url
				+ ", following_url=" + following_url + ", gists_url="
				+ gists_url + ", starred_url=" + starred_url
				+ ", subscriptions_url=" + subscriptions_url
				+ ", organizations_url=" + organizations_url + ", repos_url="
				+ repos_url + ", events_url=" + events_url
				+ ", received_events_url=" + received_events_url + ", type="
				+ type + ", site_admin=" + site_admin + "]";
	}
	
	
  
}
