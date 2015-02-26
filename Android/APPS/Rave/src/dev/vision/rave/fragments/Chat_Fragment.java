package dev.vision.rave.fragments;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.nostra13.universalimageloader.core.ImageLoader;

import dev.vision.rave.R;
import dev.vision.rave.messaging.Message;
import dev.vision.rave.messaging.Message.TYPE;
import dev.vision.rave.views.CircularImageResizable;
import dev.vision.rave.views.InScrollableListView;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.Spannable;
import android.text.Spannable.Factory;
import android.text.Spanned;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.LayoutInflater;
import android.view.ViewStub;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.TextView.BufferType;

public class Chat_Fragment extends ListFragment implements OnClickListener {
	
	ArrayList<Message> s;
	InScrollableListView li;
	static String[] smiley;
	EditText toSend;
	

	private int viewH=0;

	LinearLayout ll;
	ImageView userPic;
	private boolean group;
	private LayoutInflater inflater;
	private Bundle savedInstanceState;
	private Button sendGuest;
	private ImageView send;
	static Resources res;

	 @Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,
	   Bundle savedInstanceState) {
		/*
		 View layout = super.onCreateView(inflater, container,
		            savedInstanceState);
	     ListView lv = (ListView) layout.findViewById(android.R.id.list);
	     ViewGroup parent = (ViewGroup) lv.getParent();

	     // Remove ListView and add CustomView  in its place
	     int lvIndex = parent.indexOfChild(lv);
	     parent.removeViewAt(lvIndex);
		    
		*/	 
		 this.savedInstanceState= savedInstanceState;
		 this.inflater=inflater;

		 
		 View v = inflater.inflate(R.layout.message_chat, container,false);
		
		 res = getResources();
	    	ll = (LinearLayout) v.findViewById(R.id.iis);
	    	userPic = (ImageView) v.findViewById(R.id.copy);
	    	toSend = (EditText) v.findViewById(R.id.toSend);
			
			
		    
			send = (ImageView) v.findViewById(R.id.send);
			sendGuest = (Button) v.findViewById(R.id.sendGuest);
			
			
		 return v;
	 }
		//setContentView(R.layout.message_chat);
		@Override
	    public void onActivityCreated(Bundle savedInstanceState) 
	    {
	        super.onActivityCreated(savedInstanceState);
			li = (InScrollableListView) getListView();

			s = new ArrayList<Message>();
			Message mes = new Message(Message.TYPE.GUEST);
			mes.setMessage("ssss");
			s.add(mes);
			smiley = listFiles("png");
	       
			for(String s : smiley){
		    	try {
					Drawable d = Drawable.createFromStream(getActivity().getAssets().open("png/"+s), null);
					if(d!=null){
						if(s.contains("smile")){
							addPattern(emoticonsd, ":)", d);
							addPattern(emoticonsd, ":-)", d);
						}else if(s.contains("laugh")){
							addPattern(emoticonsd, ":D", d);
							addPattern(emoticonsd, ":d", d);
						}else if(s.contains("anger")){
							addPattern(emoticonsd, ":@", d);
						}else if(s.contains("cry")){
							addPattern(emoticonsd, ":'(", d);
						}else if(s.contains("sad")){
							addPattern(emoticonsd, ":(", d);
							addPattern(emoticonsd, ":-(", d);
						}else if(s.contains("question")){
							addPattern(emoticonsd, ":?", d);
						}else if(s.contains("surprise")){
							addPattern(emoticonsd, ":O", d);
						}
					}
					/*
						:-)   emo_im_happy
						:-(   emo_im_sad
						:-D   emo_im_laughing
						:'(   emo_im_cyring
						:-/   emo_im_undecided
						:-[   emo_im_embarrassed
						O:-)  emo_im_angel
						:-!   emo_im_foot_in_mouth
						:-$   emo_im_money_mouth
						B-)   emo_im_cool
						:-*   emo_im_kissing
						:O    emo_im_yelling
						=-O   emo_im_suprised
						:-P   emo_im_toungue_sticking_out
						;-)   emo_im_winking
						:-X   emo_im_lips_are_sealed
						o.O   emo_im_wtf
				    */
		    	} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			/*
			 * try {
				Drawable d = Drawable.createFromStream(getAssets().open("png/cool.PNG"), null);
			    addPattern(emoticonsd, ":)", d);
	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 */
			
			
			sendGuest.setOnClickListener(this);
			send.setOnClickListener(this);
			if(group)
				li.setAdapter(new Group_MessageAdapter(s));
			else
				li.setAdapter(new MessageAdapter(s));
			viewH =0;
		
		
	}
	
	
	@Override
	public void onResume(){
		super.onResume();
		if(group)
		li.setOnScrollListener(new onPauseScroll(null, true, true));
	}
	
	 public class onPauseScroll implements OnScrollListener{

			int mScrollState = OnScrollListener.SCROLL_STATE_IDLE;
			private int mScrollY=0;
			public onPauseScroll(ImageLoader imageLoader, boolean pauseOnScroll,
					boolean pauseOnFling) {
				//super(imageLoader, pauseOnScroll, pauseOnFling);
			}
			
			 @Override
			    public void onScrollStateChanged(AbsListView view, int scrollState) {
			        // Store the state to avoid re-laying out in IDLE state.
			       // super.onScrollStateChanged(view, scrollState);
			        mScrollState = scrollState;

			    }
			
			@SuppressLint("NewApi")
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {			
					
				    int stop = 0;
					int translationY = 0;
					
					
					

			        
			        // Nothing to do in IDLE state.
			       // if (mScrollState == OnScrollListener.SCROLL_STATE_IDLE)
			       //     return;
			        
			        for (int i=0; i < visibleItemCount; i++) {
			            View listItem = view.getChildAt(i);
			            if (listItem == null)
			                break;
			            
			            CircularImageResizable title = (CircularImageResizable) listItem.findViewById(R.id.userPic);
			            if(title!=null)
			            {	
			            			
			            float topMargin = 0;
			          //  if (i == 0) {
			                int top = listItem.getTop();
			                int height = listItem.getHeight();
			                int right = (((RelativeLayout.LayoutParams)title.getLayoutParams()).getRules()[RelativeLayout.ALIGN_PARENT_RIGHT]);
			                // if top is negative, the list item has scrolled up.
			                // if the title view falls within the container's visible portion,
			                //     set the top margin to be the (inverse) scrolled amount of the container.
			                // else
			                //     set the top margin to be the difference between the heights.
			                if (top < viewH+stop){
			                	userPic.setImageDrawable(title.getDrawable());
			                	if(right ==  RelativeLayout.TRUE)
			                		ll.setGravity(Gravity.RIGHT);
			                	else
				                	ll.setGravity(Gravity.LEFT);
	
			                	title.setVisibility(View.INVISIBLE);
			                	
					            if(i+1< visibleItemCount){
					            	View listItemAfter = view.getChildAt(i+1);
						            CircularImageResizable titleAfter = (CircularImageResizable) listItemAfter.findViewById(R.id.userPic);
				                	if((((RelativeLayout.LayoutParams)titleAfter.getLayoutParams()).getRules()[RelativeLayout.ALIGN_PARENT_RIGHT]) !=  right){
					                	topMargin = title.getHeight() < (top-30 + height-viewH-stop) ? 0 : (top+height-title.getHeight()-30);
					                	if(topMargin!=0)
					                		titleAfter.setVisibility(View.VISIBLE);
				                	}
				                	ll.setTranslationY(topMargin);
				                	
			                	}

			                }
			                /*
			                if (top < viewH+stop){
			                	topMargin = title.getHeight() < (top-30 + height-viewH-stop) ? -(top-viewH-stop) : (height - title.getHeight()-30);

			                }
			                // request Android to layout again.
				            title.setTranslationY(topMargin);
				            */
			          //  }
			 
			       
			           
			            }
			        }
			   	 
		
				}

			private int getActionBarHeight() {
				return mScrollState;
			//	return backgroundimage.getHeight();
			}		
		}

	@Override
	public void onClick(View v) {
		if(toSend.length()>0){
			Message mes = null;
			switch(v.getId()){
				case R.id.send:
					mes = new Message(Message.TYPE.USER);
					break;
				case R.id.sendGuest:
					mes = new Message(Message.TYPE.GUEST);
					break;
			}
			mes.setMessage(toSend.getText().toString());
			s.add(mes);
	
			((MessageAdapter)li.getAdapter()).notifyDataSetChanged();
		}
	}
	
	class Group_MessageAdapter extends BaseAdapter{
		ArrayList<Message> map;
		SparseBooleanArray sb = new SparseBooleanArray();
		public Group_MessageAdapter(ArrayList<Message> map){
			this.map=map;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return map.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return map.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		
		public Message.TYPE getPrevious(int pos){
			return pos==0? null : map.get(pos-1).getType();
		}
			
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			Message m = map.get(position);
			Object message = m.getMessage();
			Message.TYPE type = m.getType();
			final ViewHolder holder;
			View v;
			if(type == Message.TYPE.GUEST)
				v= inflater.inflate(R.layout.chat_row_guest_group, parent,false);
			else
				v= inflater.inflate(R.layout.chat_row_user_group, parent,false);
			
			holder = new ViewHolder();
			assert v != null;
			holder.userPic = (ImageView) v.findViewById(R.id.userPic);	
			if(type==getPrevious(position)){
				holder.userPic.setVisibility(View.INVISIBLE);
			}else{
				holder.userPic.setVisibility(View.VISIBLE);
			}
			
			holder.stub = (ViewStub) v.findViewById(R.id.layout_stub);

			if(message instanceof String){
				holder.stub.setLayoutResource(R.layout.text_message);
				holder.body = (TextView) holder.stub.inflate();
				Spannable s = getSmiledTextd(getActivity(), (CharSequence) message, type);
				holder.body.setText(s, BufferType.SPANNABLE);
			}else if(message instanceof Bitmap){
				
			}else if(message instanceof Bitmap){
				
			}else if(message instanceof Bitmap){
				
			}
			
			return v;
		}
		class ViewHolder {
			ViewStub stub;
			TextView body;
			ImageView userPic;
			View arrow;
		}	
		
	}

	class MessageAdapter extends BaseAdapter{
		ArrayList<Message> map;
		SparseBooleanArray sb = new SparseBooleanArray();
		public MessageAdapter(ArrayList<Message> map){
			this.map=map;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return map.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return map.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		
		public Message.TYPE getPrevious(int pos){
			return pos==0? null : map.get(pos-1).getType();
		}
			
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			Message m = map.get(position);
			Object message = m.getMessage();
			Message.TYPE type = m.getType();
			final ViewHolder holder;
			View v;
			

			if(type == Message.TYPE.GUEST)
				v= inflater.inflate(R.layout.chat_row_guest, parent,false);
			else
				v= inflater.inflate(R.layout.chat_row_user, parent,false);
			
			holder = new ViewHolder();
			assert v != null;
			if(type==getPrevious(position)){
			}else{
			}
			
			holder.stub = (ViewStub) v.findViewById(R.id.layout_stub);


			if(message instanceof String){
				holder.stub.setLayoutResource(R.layout.text_message);
				holder.body = (TextView) holder.stub.inflate();
				if(m.sp == null)
				m.sp = getSmiledTextd(getActivity(), (CharSequence) message, type);
				holder.body.setText(m.sp, BufferType.SPANNABLE);
			}else if(message instanceof Bitmap){
				
			}else if(message instanceof Bitmap){
				
			}else if(message instanceof Bitmap){
				
			}
			
			return v;
		}
		class ViewHolder {
			ViewStub stub;
			TextView body;
			//View arrow;
		}	
		
	}
	
	private static final Factory spannableFactory = Spannable.Factory
	        .getInstance();

	private static final Map<Pattern, Integer> emoticons = new HashMap<Pattern, Integer>();

	static {
	    //addPattern(emoticons, ":)", R.drawable.smile);
	    
	    //addPattern(emoticons, ":-)", R.drawable.emo_im_happy);
	    // ...
	}  
	
	private static void addPattern(Map<Pattern, Integer> map, String smile,
	        int resource) {
	    map.put(Pattern.compile(Pattern.quote(smile)), resource);
	}

	public static boolean addSmiles(Context context, Spannable spannable) {
	    boolean hasChanges = false;
	    for (Entry<Pattern, Integer> entry : emoticons.entrySet()) {
	        Matcher matcher = entry.getKey().matcher(spannable);
	        while (matcher.find()) {
	            boolean set = true;
	            for (ImageSpan span : spannable.getSpans(matcher.start(),
	                    matcher.end(), ImageSpan.class))
	                if (spannable.getSpanStart(span) >= matcher.start()
	                        && spannable.getSpanEnd(span) <= matcher.end())
	                    spannable.removeSpan(span);
	                else {
	                    set = false;
	                    break;
	                }
	            if (set) {
	                hasChanges = true;
	                spannable.setSpan(new ImageSpan(context, entry.getValue()),
	                        matcher.start(), matcher.end(),
	                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
	            }
	        }
	    }
	    return hasChanges;
	}
	
	public static Spannable getSmiledText(Context context, CharSequence text) {
	    Spannable spannable = spannableFactory.newSpannable(text);
	    addSmiles(context, spannable);
	    return spannable;
	}
	
	
	//
	
	private static final Map<Pattern, Drawable> emoticonsd = new HashMap<Pattern, Drawable>();

	
	private  String[]  listFiles(String dirFrom) {
         
        String fileList[] = null;
		try {
			fileList = getActivity().getAssets().list(dirFrom);
			return fileList;
		} catch (IOException e) {
			return fileList;
		}

            
    }
	
	private static void addPattern(Map<Pattern, Drawable> map, String smile,
	        Drawable d) {
	    map.put(Pattern.compile(Pattern.quote(smile)), d);
	}
	
	public static boolean addSmilesd(Context context, Spannable spannable, TYPE type) {
	    boolean hasChanges = false;
	    for (Entry<Pattern, Drawable> entry : emoticonsd.entrySet()) {
	        Matcher matcher = entry.getKey().matcher(spannable);
	        while (matcher.find()) {
	            boolean set = true;
	            for (ImageSpan span : spannable.getSpans(matcher.start(),
	                    matcher.end(), ImageSpan.class))
	                if (spannable.getSpanStart(span) >= matcher.start()
	                        && spannable.getSpanEnd(span) <= matcher.end())
	                    spannable.removeSpan(span);
	                else {
	                    set = false;
	                    break;
	                }
	            if (set) {
	                hasChanges = true;
	                Drawable dr = entry.getValue();
	                
	                if(type==TYPE.GUEST){
	                	Bitmap sprite = ((BitmapDrawable) dr).getBitmap();
	                
		
		                //Create a matrix to be used to transform the bitmap
		                Matrix mirrorMatrix = new Matrix();
		
		                //Set the matrix to mirror the image in the x direction
		                mirrorMatrix.preScale(-1.0f, 1.0f);
		
		                //Create a flipped sprite using the transform matrix and the original sprite
		                Bitmap fSprite = Bitmap.createBitmap(sprite, 0, 0, sprite.getWidth(), sprite.getHeight(), mirrorMatrix, false);
		                dr =new BitmapDrawable(res, fSprite);
	                }
	                dr.setBounds(0, 0, 70, 70);

	                spannable.setSpan(new ImageSpan(dr, DynamicDrawableSpan.ALIGN_BOTTOM),
	                        matcher.start(), matcher.end(),
	                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
	            }
	        }
	    }
	    return hasChanges;
	}
	
	public static Spannable getSmiledTextd(Context context, CharSequence text, TYPE type) {
	    Spannable spannable = spannableFactory.newSpannable(text);
	    addSmilesd(context, spannable, type);
	    return spannable;
	}
}
