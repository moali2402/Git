package dev.vision.relationshipninjas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import dev.vision.relationshipninjas.API.API;
import dev.vision.relationshipninjas.Classes.Relationships;

public class Static {
	public static Relationships rs =new Relationships();

	static void login(Context c, String email, String pass){
		SharedPreferences sh = c.getSharedPreferences(c.getPackageName(), Context.MODE_PRIVATE);
		sh.edit().putString("email", email)
				.putString("pass",pass)
				.putBoolean("logged", true)
				.commit();
	}
	
	static boolean isLogged(Context c){
		SharedPreferences sh = c.getSharedPreferences(c.getPackageName(), Context.MODE_PRIVATE);
		API.user.setEmail(sh.getString("email", ""));
		API.user.setPass(sh.getString("pass",""));

		return sh.getBoolean("logged", false);
	}

	public static void logout(Context c){
		SharedPreferences sh = c.getSharedPreferences(c.getPackageName(), Context.MODE_PRIVATE);
		sh.edit().putBoolean("logged", false).commit();
		
		c.startActivity(new Intent().setClass(c, Login.class));
	}
	
	
	
}
