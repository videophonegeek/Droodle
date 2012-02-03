package com.ivoid.droodle;

import com.ivoid.droodle.R;
import com.ivoid.helpers.Base64;
import com.ivoid.helpers.Student;

import org.json.JSONObject;
import android.webkit.URLUtil;

import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity
{	
	private JSONObject json = null;
	private ProgressDialog progressDialog;
	private Map<String, String> creds;
	private String loginResult = "";
	private boolean saveCredentials;
	private Globals app;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{	
		super.onCreate(savedInstanceState);
		
		app = ((Globals)getApplicationContext());
		
		if (!Preferences.isLogged(this))
			setupLoginView();
		else
		{
			creds = Preferences.getCredentials(this);
			saveCredentials = false;
			tryLogin(); 	
		}
	}
	
	private void setupLoginView()
	{
		setContentView(R.layout.login);	
		
		Button login = (Button) findViewById(R.login.loginButton);
		
		final EditText username = (EditText) findViewById(R.login.username);
		final EditText password = (EditText) findViewById(R.login.password);
		final EditText www = (EditText) findViewById(R.login.url);
		
		login.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{	
				String  user = username.getText().toString().trim();
				String  pass = password.getText().toString().trim();
				String  url = www.getText().toString().trim();
				
				if ( URLUtil.isValidUrl(url) )
				{
					Map<String, String> mycreds = new HashMap<String, String>();
					mycreds.put("username", user);
			        mycreds.put("password", pass);
			        mycreds.put("url", url);
			        creds = mycreds;
			
					saveCredentials = true;
					
			        tryLogin();
				}
				else
					showDialog("Url must be a valid URL!");	
			}
		});
	}
	
	private void showDialog(String stuff)
	{ Toast.makeText(this, stuff,Toast.LENGTH_LONG).show(); }
	
	private void saveLogin()
	{ Preferences.saveLogin(this, creds); }
	
	private final Handler progressHandler = new Handler()
	{
		@Override
		public void handleMessage(android.os.Message msg) 
		{			
			progressDialog.dismiss();
			goHome();
		}
    };
	
    private boolean connectedToInternet()
    {
    	ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
    	NetworkInfo ni = cm.getActiveNetworkInfo();
    	if (ni == null) {
    	    // There are no active networks.
    	    return false;
    	}
    	return ni.isConnected();
    }
    
    private void tryLogin()
	{
		if (!connectedToInternet())
		{
			loginResult = "No Internet connection.";
			goHome();
		}

		else
		{
			progressDialog = ProgressDialog.show(this, "", "Fetching Student...", true);
			
			new Thread(new Runnable()
			{			
				@Override
				public void run()
				{		
					Base64 cryptor = new Base64();
					
					fetchJSON(
								cryptor.encode(creds.get("username")), 
								cryptor.encode(creds.get("password")), 
								creds.get("url")
							 );
					
					HttpResponse response = app.httphelper.getHttpResponse();
					
					if (response == null)
						loginResult = "Server unavailable, try again later.";
					
					else
					{
						app.httphelper.setHeader(response.getFirstHeader("Cookie")); 
						
						try
						{
							json=new JSONObject(EntityUtils.toString(response.getEntity()));
						} catch (Exception e){
							if (saveCredentials)
								loginResult = "Invalid username or password.";
							else
								loginResult = "Server error, try again later.";
						}
					}
					progressHandler.sendEmptyMessage(0);	
				}
			}).start();
		}
	}
	
	private void fetchJSON(String user, String pass, String url)
	{
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("username", user));
		nameValuePairs.add(new BasicNameValuePair("password", pass));
		nameValuePairs.add(new BasicNameValuePair("url", url));
		try {
			app.httphelper.post("getCourses", nameValuePairs);
		} catch (Exception e){}
	}

    private void goHome()
    {
    	if (loginResult.equals(""))//if the JSON was successfully fetched
		{			
    		if (saveCredentials)
				saveLogin();
    		
			Student student = new Student(json);
			app.student = student;
				
			Intent myIntent = new Intent (this, CoursesActivity.class);
			finish();  
			startActivity(myIntent);
		}
    	else
    	{	
    		showDialog(loginResult);
    		
    		if (saveCredentials)
    			loginResult="";
    		else
    		{
    			Handler mHandler = new Handler();
    			mHandler.postDelayed(new Runnable()
    			{
    				public void run()
    				{
    					finish();  
    					System.exit(0); 
    				}
    			}, 3000);
    		}
    	}		
    }
    
    @Override
    public void onBackPressed()
    {
    	Log.i("myid","Droodle is exiting.");
    	finish();  
    	System.exit(0); 
    }
}