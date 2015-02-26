package dev.vision.split.it.extras;

import android.graphics.drawable.BitmapDrawable;
import dev.vision.split.it.R;
import dev.vision.split.it.Table;
import dev.vision.split.it.user.ME;

public class APP {
	public static final String DEBUG_ID = "00000";
	public static ME ME = new ME();

	public static String[] names = new String[]{"Emma W", "Keira M", "Mo A", "Peter N", "Richard P", "James R"};
	int[] pic = new int[]{R.drawable.bby, };
	
	public static Table getMyTable(){
		return ME.getTable();
	}
	
	
	
}
