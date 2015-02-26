package itworx.github.task;

import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
 
public class Utils {
	
    public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
              int count=is.read(bytes, 0, buffer_size);
              if(count==-1)
                  break;
              os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }
    
    public static boolean isNetworkConnected(Context c) {
    	ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo ni = cm.getActiveNetworkInfo();
	    if (ni == null)
	    	return false;
	    else
	    	return true;
	}
}