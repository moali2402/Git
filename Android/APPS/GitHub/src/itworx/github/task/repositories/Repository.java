package itworx.github.task.repositories;

import org.json.JSONException;
import org.json.JSONObject;

/**
	 * A dummy REPOSITORY representing a piece of content.
	 */
	public class Repository {
		
		private Owner owner = new Owner();

		private String id;
		private String name;
		private String full_name;
	    private String html_url;
	    private String description;	    
	    private String url;
	    private String forks_url;
	    private String keys_url;
	    private String created_at;
	    private String updated_at;
	    private String pushed_at;
	    private String git_url;
	    private String ssh_url;
	    private String clone_url;
	    private String svn_url;
	    private String homepage;
	    private String mirror_url;
	    private String language;
	    private String default_branch;


	    private boolean isPrivate;
	    private boolean fork;
	    private boolean has_issues;
	    private boolean has_downloads;
	    private boolean has_wiki;
	    private boolean has_pages;

	    private int size;
	    private int stargazers_count;
	    private int watchers_count;
	    private int forks_count;
	    private int open_issues_count;
	    private int forks;
	    private int open_issues;
	    private int watchers;
	    private Permissions permissions;
	    
	    public Repository() {
		}
		
		public Repository(String id) {
			this.id = id;
		}

		public Repository(JSONObject jo) throws JSONException {
			
			//Strings
			id 					= jo.optString("id");
			name 				= jo.optString("name");
			full_name 			= jo.optString("full_name");			
			html_url 			= jo.optString("html_url");
			description 		= jo.optString("description");
			url 				= jo.optString("url");
			forks_url 			= jo.optString("forks_url");
			keys_url 			= jo.optString("keys_url");
			
			created_at 			= jo.optString("created_at");
			updated_at 			= jo.optString("updated_at");
			
			//integers
			size 				= jo.optInt("size");
		    stargazers_count 	= jo.optInt("stargazers_count");
		    watchers_count 		= jo.optInt("watchers_count");
		    forks_count 		= jo.optInt("forks_count");
		    open_issues_count 	= jo.optInt("open_issues_count");
		    forks 				= jo.optInt("forks");
		    open_issues 		= jo.optInt("open_issues");
		    watchers 			= jo.optInt("watchers");
		    
		    //Booleans
		    isPrivate 			= jo.optBoolean("private");
		    fork      			= jo.optBoolean("fork");
		    has_issues 			= jo.optBoolean("has_issues");
		    has_downloads 		= jo.optBoolean("has_downloads");
		    has_wiki 			= jo.optBoolean("has_wiki");
		    has_pages 			= jo.optBoolean("has_pages");

		    //Owner
	    	owner 				= new Owner(jo.getJSONObject("owner"));
		    
	    	//Permissions
	    	permissions 		= new Permissions(jo.getJSONObject("permissions"));
		    
	    		
		    
			
		    /*
		    private String pushed_at;
		    private String git_url;
		    private String ssh_url;
		    private String clone_url;
		    private String svn_url;
		    private String homepage;
		    private String mirror_url;
		    private String language;
		    private String default_branch;
		    */

		}
		
		

		

		/**
		 * @return the owner
		 */
		public Owner getOwner() {
			return owner;
		}

		/**
		 * @param owner the owner to set
		 */
		public void setOwner(Owner owner) {
			this.owner = owner;
		}
		
		/**
		 * @param av the owner's Avatar to set
		 */
		public void setOwnerAvatar(String av) {
			this.owner.setAvatar_url(av);;
		}
		
		/**
		 * @param login the owner's login to set
		 */
		public void setOwnerLogin(String login) {
			this.owner.setLogin(login);
		}


		public String getOwnerLogin() {
			return	this.owner.getLogin();
		}
		
		public String getOwnerAvatar_url() {
			return this.owner.getAvatar_url();
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
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @param name the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * @return the full_name
		 */
		public String getFull_name() {
			return full_name;
		}

		/**
		 * @param full_name the full_name to set
		 */
		public void setFull_name(String full_name) {
			this.full_name = full_name;
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
		 * @return the description
		 */
		public String getDescription() {
			return description;
		}

		/**
		 * @param description the description to set
		 */
		public void setDescription(String description) {
			this.description = description;
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
		 * @return the forks_url
		 */
		public String getForks_url() {
			return forks_url;
		}

		/**
		 * @param forks_url the forks_url to set
		 */
		public void setForks_url(String forks_url) {
			this.forks_url = forks_url;
		}

		/**
		 * @return the keys_url
		 */
		public String getKeys_url() {
			return keys_url;
		}

		/**
		 * @param keys_url the keys_url to set
		 */
		public void setKeys_url(String keys_url) {
			this.keys_url = keys_url;
		}

		/**
		 * @return the created_at
		 */
		public String getCreated_at() {
			return created_at;
		}

		/**
		 * @param created_at the created_at to set
		 */
		public void setCreated_at(String created_at) {
			this.created_at = created_at;
		}

		/**
		 * @return the updated_at
		 */
		public String getUpdated_at() {
			return updated_at;
		}

		/**
		 * @param updated_at the updated_at to set
		 */
		public void setUpdated_at(String updated_at) {
			this.updated_at = updated_at;
		}

		/**
		 * @return the pushed_at
		 */
		public String getPushed_at() {
			return pushed_at;
		}

		/**
		 * @param pushed_at the pushed_at to set
		 */
		public void setPushed_at(String pushed_at) {
			this.pushed_at = pushed_at;
		}

		/**
		 * @return the git_url
		 */
		public String getGit_url() {
			return git_url;
		}

		/**
		 * @param git_url the git_url to set
		 */
		public void setGit_url(String git_url) {
			this.git_url = git_url;
		}

		/**
		 * @return the ssh_url
		 */
		public String getSsh_url() {
			return ssh_url;
		}

		/**
		 * @param ssh_url the ssh_url to set
		 */
		public void setSsh_url(String ssh_url) {
			this.ssh_url = ssh_url;
		}

		/**
		 * @return the clone_url
		 */
		public String getClone_url() {
			return clone_url;
		}

		/**
		 * @param clone_url the clone_url to set
		 */
		public void setClone_url(String clone_url) {
			this.clone_url = clone_url;
		}

		/**
		 * @return the svn_url
		 */
		public String getSvn_url() {
			return svn_url;
		}

		/**
		 * @param svn_url the svn_url to set
		 */
		public void setSvn_url(String svn_url) {
			this.svn_url = svn_url;
		}

		/**
		 * @return the homepage
		 */
		public String getHomepage() {
			return homepage;
		}

		/**
		 * @param homepage the homepage to set
		 */
		public void setHomepage(String homepage) {
			this.homepage = homepage;
		}

		/**
		 * @return the mirror_url
		 */
		public String getMirror_url() {
			return mirror_url;
		}

		/**
		 * @param mirror_url the mirror_url to set
		 */
		public void setMirror_url(String mirror_url) {
			this.mirror_url = mirror_url;
		}

		/**
		 * @return the language
		 */
		public String getLanguage() {
			return language;
		}

		/**
		 * @param language the language to set
		 */
		public void setLanguage(String language) {
			this.language = language;
		}

		/**
		 * @return the default_branch
		 */
		public String getDefault_branch() {
			return default_branch;
		}

		/**
		 * @param default_branch the default_branch to set
		 */
		public void setDefault_branch(String default_branch) {
			this.default_branch = default_branch;
		}

		/**
		 * @return the isPrivate
		 */
		public boolean isPrivate() {
			return isPrivate;
		}

		/**
		 * @param isPrivate the isPrivate to set
		 */
		public void setPrivate(boolean isPrivate) {
			this.isPrivate = isPrivate;
		}

		/**
		 * @return the fork
		 */
		public boolean isFork() {
			return fork;
		}

		/**
		 * @param fork the fork to set
		 */
		public void setFork(boolean fork) {
			this.fork = fork;
		}

		/**
		 * @return the has_issues
		 */
		public boolean isHas_issues() {
			return has_issues;
		}

		/**
		 * @param has_issues the has_issues to set
		 */
		public void setHas_issues(boolean has_issues) {
			this.has_issues = has_issues;
		}

		/**
		 * @return the has_downloads
		 */
		public boolean isHas_downloads() {
			return has_downloads;
		}

		/**
		 * @param has_downloads the has_downloads to set
		 */
		public void setHas_downloads(boolean has_downloads) {
			this.has_downloads = has_downloads;
		}

		/**
		 * @return the has_wiki
		 */
		public boolean isHas_wiki() {
			return has_wiki;
		}

		/**
		 * @param has_wiki the has_wiki to set
		 */
		public void setHas_wiki(boolean has_wiki) {
			this.has_wiki = has_wiki;
		}

		/**
		 * @return the has_pages
		 */
		public boolean isHas_pages() {
			return has_pages;
		}

		/**
		 * @param has_pages the has_pages to set
		 */
		public void setHas_pages(boolean has_pages) {
			this.has_pages = has_pages;
		}

		/**
		 * @return the size
		 */
		public int getSize() {
			return size;
		}

		/**
		 * @param size the size to set
		 */
		public void setSize(int size) {
			this.size = size;
		}

		/**
		 * @return the stargazers_count
		 */
		public int getStargazers_count() {
			return stargazers_count;
		}

		/**
		 * @param stargazers_count the stargazers_count to set
		 */
		public void setStargazers_count(int stargazers_count) {
			this.stargazers_count = stargazers_count;
		}

		/**
		 * @return the watchers_count
		 */
		public int getWatchers_count() {
			return watchers_count;
		}

		/**
		 * @param watchers_count the watchers_count to set
		 */
		public void setWatchers_count(int watchers_count) {
			this.watchers_count = watchers_count;
		}

		/**
		 * @return the forks_count
		 */
		public int getForks_count() {
			return forks_count;
		}

		/**
		 * @param forks_count the forks_count to set
		 */
		public void setForks_count(int forks_count) {
			this.forks_count = forks_count;
		}

		/**
		 * @return the open_issues_count
		 */
		public int getOpen_issues_count() {
			return open_issues_count;
		}

		/**
		 * @param open_issues_count the open_issues_count to set
		 */
		public void setOpen_issues_count(int open_issues_count) {
			this.open_issues_count = open_issues_count;
		}

		/**
		 * @return the forks
		 */
		public int getForks() {
			return forks;
		}

		/**
		 * @param forks the forks to set
		 */
		public void setForks(int forks) {
			this.forks = forks;
		}

		/**
		 * @return the open_issues
		 */
		public int getOpen_issues() {
			return open_issues;
		}

		/**
		 * @param open_issues the open_issues to set
		 */
		public void setOpen_issues(int open_issues) {
			this.open_issues = open_issues;
		}

		/**
		 * @return the watchers
		 */
		public int getWatchers() {
			return watchers;
		}

		/**
		 * @param watchers the watchers to set
		 */
		public void setWatchers(int watchers) {
			this.watchers = watchers;
		}
		
		
		@Override
		public String toString() {
			return "Repository [owner=" + owner + ", id=" + id + ", name="
					+ name + ", full_name=" + full_name + ", html_url="
					+ html_url + ", description=" + description + ", url="
					+ url + ", forks_url=" + forks_url + ", keys_url="
					+ keys_url + ", created_at=" + created_at + ", updated_at="
					+ updated_at + ", pushed_at=" + pushed_at + ", git_url="
					+ git_url + ", ssh_url=" + ssh_url + ", clone_url="
					+ clone_url + ", svn_url=" + svn_url + ", homepage="
					+ homepage + ", mirror_url=" + mirror_url + ", language="
					+ language + ", default_branch=" + default_branch
					+ ", isPrivate=" + isPrivate
					+ ", fork=" + fork + ", has_issues=" + has_issues
					+ ", has_downloads=" + has_downloads + ", has_wiki="
					+ has_wiki + ", has_pages=" + has_pages + ", size=" + size
					+ ", stargazers_count=" + stargazers_count
					+ ", watchers_count=" + watchers_count + ", forks_count="
					+ forks_count + ", open_issues_count=" + open_issues_count
					+ ", forks=" + forks + ", open_issues=" + open_issues
					+ ", watchers=" + watchers + ", permissions=" + permissions
					+ "]";
		}



		class Permissions {
		    boolean admin;
		    boolean push;
		    boolean pull;
		      
	        public Permissions(JSONObject jo){
	    	   admin = jo.optBoolean("admin");
		       push =  jo.optBoolean("push");
		       pull =  jo.optBoolean("pull");
		    }

			@Override
			public String toString() {
				return "Permissions [admin=" + admin + ", push=" + push
						+ ", pull=" + pull + "]";
			}		      
		 }
		
		/*

	    "collaborators_url": "https://api.github.com/repos/blackberry/WebWorks/collaborators{/collaborator}",
	    "teams_url": "https://api.github.com/repos/blackberry/WebWorks/teams",
	    "hooks_url": "https://api.github.com/repos/blackberry/WebWorks/hooks",
	    "issue_events_url": "https://api.github.com/repos/blackberry/WebWorks/issues/events{/number}",
	    "events_url": "https://api.github.com/repos/blackberry/WebWorks/events",
	    "assignees_url": "https://api.github.com/repos/blackberry/WebWorks/assignees{/user}",
	    "branches_url": "https://api.github.com/repos/blackberry/WebWorks/branches{/branch}",
	    "tags_url": "https://api.github.com/repos/blackberry/WebWorks/tags",
	    "blobs_url": "https://api.github.com/repos/blackberry/WebWorks/git/blobs{/sha}",
	    "git_tags_url": "https://api.github.com/repos/blackberry/WebWorks/git/tags{/sha}",
	    "git_refs_url": "https://api.github.com/repos/blackberry/WebWorks/git/refs{/sha}",
	    "trees_url": "https://api.github.com/repos/blackberry/WebWorks/git/trees{/sha}",
	    "statuses_url": "https://api.github.com/repos/blackberry/WebWorks/statuses/{sha}",
	    "languages_url": "https://api.github.com/repos/blackberry/WebWorks/languages",
	    "stargazers_url": "https://api.github.com/repos/blackberry/WebWorks/stargazers",
	    "contributors_url": "https://api.github.com/repos/blackberry/WebWorks/contributors",
	    "subscribers_url": "https://api.github.com/repos/blackberry/WebWorks/subscribers",
	    "subscription_url": "https://api.github.com/repos/blackberry/WebWorks/subscription",
	    "commits_url": "https://api.github.com/repos/blackberry/WebWorks/commits{/sha}",
	    "git_commits_url": "https://api.github.com/repos/blackberry/WebWorks/git/commits{/sha}",
	    "comments_url": "https://api.github.com/repos/blackberry/WebWorks/comments{/number}",
	    "issue_comment_url": "https://api.github.com/repos/blackberry/WebWorks/issues/comments/{number}",
	    "contents_url": "https://api.github.com/repos/blackberry/WebWorks/contents/{+path}",
	    "compare_url": "https://api.github.com/repos/blackberry/WebWorks/compare/{base}...{head}",
	    "merges_url": "https://api.github.com/repos/blackberry/WebWorks/merges",
	    "archive_url": "https://api.github.com/repos/blackberry/WebWorks/{archive_format}{/ref}",
	    "downloads_url": "https://api.github.com/repos/blackberry/WebWorks/downloads",
	    "issues_url": "https://api.github.com/repos/blackberry/WebWorks/issues{/number}",
	    "pulls_url": "https://api.github.com/repos/blackberry/WebWorks/pulls{/number}",
	    "milestones_url": "https://api.github.com/repos/blackberry/WebWorks/milestones{/number}",
	    "notifications_url": "https://api.github.com/repos/blackberry/WebWorks/notifications{?since,all,participating}",
	    "labels_url": "https://api.github.com/repos/blackberry/WebWorks/labels{/name}",
	    "releases_url": "https://api.github.com/repos/blackberry/WebWorks/releases{/id}",
	    */
}