package com.ivoid.droodle;

import java.util.ArrayList;

import com.ivoid.droodle.R;
import com.ivoid.helpers.Course;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CoursesActivity extends ListActivity
{
	private ArrayList<String> mList;
	private ArrayAdapter<String> mListAdapter; 
	private ProgressDialog progressDialog;
	private int coursePicked;
	private Globals app;
	
	public void onCreate(Bundle savedInstanceState)
	{	
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.courses);
		
		app = ((Globals)getApplicationContext());
		
		((TextView) findViewById(R.courses.bar)).setText(app.student.getName());
		
		mList = new ArrayList<String>(); 
		
		for (Course c: app.student.courses)
			mList.add(c.getTitle()); 
	
		mListAdapter = new ArrayAdapter<String>(this, R.layout.list_item, mList);
		setListAdapter(mListAdapter);
	}
	
	private void showDialog(String stuff)
	{ Toast.makeText(this, stuff,Toast.LENGTH_LONG).show(); }
	
	private void logout()
	{	
		Preferences.setLogged(this, false);
		Intent i = new Intent(this, LoginActivity.class);
		finish();
		startActivity(i);
	}

	private final Handler progressHandler = new Handler()
	{
		@Override
		public void handleMessage(android.os.Message msg) 
		{			
			progressDialog.dismiss();
			goAssignments();
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
    
    @Override
	protected void onListItemClick(ListView l, View v, final int position, long id) 
	{
		super.onListItemClick(l, v, position, id);
		
		coursePicked = position;
		
		if (connectedToInternet())
		{
			if (!app.student.courses[position].wasFetched())
			{
				progressDialog = ProgressDialog.show(this, "", "Fetching Course...", true);
				
				new Thread( new Runnable() {
					public void run()
					{
						app.student.courses[position].populateAssignments(app.httphelper);
						progressHandler.sendEmptyMessage(0);
					}
				}).start();
			}
			else goAssignments();
		}
		else
			showDialog("No Internet connection.");
			
	}
	
	private void goAssignments()
	{
		if (app.student.courses[coursePicked].wasFetched())
		{
			app.course = app.student.courses[coursePicked];
			Intent intent = new Intent(this, AssignmentsActivity.class);	
			startActivity(intent);
		}
		else
			showDialog("Course fetch failed.");
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.courses_menu, menu);
        
        return super.onCreateOptionsMenu(menu);
    }
	
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case (R.menu.LogoutMenuItem):
			{
				logout();
				return true;
			}
		}
		return super.onOptionsItemSelected(item);
	}
 
    public void onBackPressed()
    {
    	finish();   
    }
}