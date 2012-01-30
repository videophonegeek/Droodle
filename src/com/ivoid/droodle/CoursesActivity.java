package com.ivoid.droodle;

import java.util.ArrayList;

import com.ivoid.droodle.R;
import com.ivoid.helpers.Course;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
	
	public void onCreate(Bundle savedInstanceState)
	{	
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.courses);
		
		((TextView) findViewById(R.courses.bar)).setText(Globals.student.getName());
		
		mList = new ArrayList<String>(); 
		
		for (Course c: Globals.student.courses)
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

	@Override
	protected void onListItemClick(ListView l, View v, final int position, long id) 
	{
		super.onListItemClick(l, v, position, id);
		
		if (!Globals.student.courses[position].wasFetched())
		{
			progressDialog = ProgressDialog.show(this, "", "Fetching Course...", true);
			
			Globals.student.courses[position].populateAssignments();

			progressDialog.dismiss();
		}
		
		if (Globals.student.courses[position].wasFetched())
		{
			Globals.course = Globals.student.courses[position];
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