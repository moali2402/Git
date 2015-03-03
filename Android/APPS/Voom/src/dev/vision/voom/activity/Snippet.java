package dev.vision.voom.activity;

public class Snippet {
	/*
	ParseQuery<Contact> query = Contact.getUpdatedQuery();
	//query.whereNotEqualTo("objectId", currentUserId);
	query.findInBackground(new FindCallback<Contact>() {
	    public void done(List<Contact> userList, com.parse.ParseException e) {
	        if (e == null) {
	        	names.clear();
	            for (final Contact c : userList) {
					
	                	c.getProfile().findInBackground(new FindCallback<Profiles>() {
							
							@Override
							public void done(List<Profiles> p, ParseException e) {
								
								if(e == null && p.size() >0){
	
									if( p.size() > 1 ){
										for(Profiles profile : p){
										  if(!profile.isDefault()){
					                        names.add(c.getUsername() + " : " +profile.getDisplayName());
										  }
										}
									}else{
										Profiles profile = p.get(0);
				                        names.add(c.getUsername() + " : " +profile.getDisplayName());
									}
			                        namesArrayAdapter.notifyDataSetChanged();
								}
							}
						});
	               
	            }
	            
	
	            
	
	            usersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	                @Override
	                public void onItemClick(AdapterView<?> a, View v, int i, long l) {
	                    openConversation(names, i);
	                }
	            });
	
	        } else {
	            Toast.makeText(getApplicationContext(),
	                "Error loading user list",
	                    Toast.LENGTH_LONG).show();
	        }
	    }
	});
*/
	
/*
try {
	List<Profiles> p = c.getProfile().find();
    if(p.size() >0){
        Toast.makeText(ListUsersActivity.this, c.getUsername() + " : " + p.size(), Toast.LENGTH_SHORT).show();

		if( p.size() > 1 ){
			for(Profiles profile : p){
			  if(!profile.isDefault()){
                names.add(c.getUsername() + " : " +profile.getDisplayName());
			  }
			}
		}else{
			Profiles profile = p.get(0);
            names.add(c.getUsername() + " : " +profile.getDisplayName());
		}
        namesArrayAdapter.notifyDataSetChanged();
	}
} catch (ParseException e1) {
	e1.printStackTrace();
}

*/
}