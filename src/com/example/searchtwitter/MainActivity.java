package com.example.searchtwitter;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

@SuppressLint({ "HandlerLeak", "UseSparseArrays" })
public class MainActivity extends Activity {
	
	public String JSonString;
	public String urlString = "http://search.twitter.com/search.json?q=&geocode=37.781157,-122.398720,10mi&rpp=100";
	//public String urlString = "http://search.twitter.com/search.json?q=sex&rpp=100";
	private TextView textView;
	private ListView listView;
	private EditText editText;
	private Button button;
	public Context context = this;
	private Map<Integer, Bitmap> images;
	
	DataBaseHelper dbHelper;
	private String filter;
	
	private Handler JSonHandler = new Handler() {
		
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			JSONTokener jsonTokener = new JSONTokener(JSonString);
			JSONArray jsonArray = null;
			try {
				jsonArray = ((JSONObject) jsonTokener.nextValue()).optJSONArray("results");
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject object = jsonArray.getJSONObject(i);
					Tweet tweet = new Tweet();
					tweet.setName(object.getString("from_user_name"));
					tweet.setImageURL(object.getString("profile_image_url"));
					tweet.setLocation(object.getString("location"));
					tweet.setText(object.getString("text"));
					dbHelper.saveTweet(tweet);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			new Thread(Image_Download).start();
		}
	};
	
	private Handler mainHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			SimpleAdapter adapter = new SimpleAdapter(context, getData(), R.layout.tweets, 
					new String[]{"image", "text", "location"}, 
					new int[]{R.id.itemImage, R.id.itemText, R.id.itemLocation});
			adapter.setViewBinder(new ItemViewBinder());
	        listView.setAdapter(adapter);
		}
	};
	
	Runnable URL_Request = new Runnable() {  
		  
		@Override  
		public void run() {  
		    // TODO Auto-generated method stub  
			JSonString = GetResponseString.getResp(urlString);
			JSonHandler.sendEmptyMessage(0);
		}  
	};
	
	Runnable Image_Download = new Runnable() {  
		  
		@Override  
		public void run() {  
		    // TODO Auto-generated method stub  
			List<Tweet> tweetsList = null;
	        if ((filter == null) || (filter.equalsIgnoreCase(""))) {
	        	tweetsList = dbHelper.getAllTweets();
	        } else {
	        	tweetsList = dbHelper.getFilteredTweets(filter);
	        }
			images = new HashMap<Integer, Bitmap>();
			
			for (Tweet tweet : tweetsList) {
	        	Bitmap image = fetchBitMap(tweet.getImageURL());
	        	images.put(tweet.getID(), image);
	        }
			
			mainHandler.sendEmptyMessage(0);
		}  
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		filter = "";
		dbHelper = new DataBaseHelper(this);
		//dbHelper.Clear();
		
		textView = (TextView) findViewById(R.id.titleTextView);
		listView = (ListView) findViewById(R.id.tweetsList);
		editText = (EditText) findViewById(R.id.searchText);
		textView.setText("Search Twitter");
		
		//urlString = "http://search.twitter.com/search.json?q=New%20York&rpp=100";
		
		new Thread(URL_Request).start();
		
		button = (Button) findViewById(R.id.searchButton);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				filter = editText.getText().toString();
				new Thread(Image_Download).start();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map;
 
        List<Tweet> tweetsList = null;
        if ((filter == null) || (filter.equalsIgnoreCase(""))) {
        	tweetsList = dbHelper.getAllTweets();
        } else {
        	tweetsList = dbHelper.getFilteredTweets(filter);
        }
        for (Tweet tweet : tweetsList) {
        	map = new HashMap<String, Object>();
        	map.put("image", images.get(tweet.getID()));
        	String text = tweet.getName() + ": " + tweet.getText();
            map.put("text", text);
            map.put("location", "Location: " + tweet.getLocation());
            list.add(map);
        }
         
        return list;
    }

	private Bitmap fetchBitMap(String url) {
		URL myFileUrl = null;
		Bitmap bitmap = null;
		try {
			myFileUrl = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		try {
			HttpURLConnection conn = (HttpURLConnection) myFileUrl
					.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}
}
