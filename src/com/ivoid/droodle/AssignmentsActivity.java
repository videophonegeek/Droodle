package com.ivoid.droodle;

import java.util.ArrayList;

import com.ivoid.droodle.R;
import com.ivoid.helpers.Assignment;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class AssignmentsActivity extends ListActivity
{	
	private ArrayList<String> assignmentNameList;
	private ArrayAdapter<String> assignmentListAdapter;
	private Globals app;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.assignments);
		
		app = ((Globals)getApplicationContext());
		
		((TextView) findViewById(R.assignments.bar)).setText(app.course.getTitle());
		
		assignmentNameList    = new ArrayList<String>();
		assignmentListAdapter = new ArrayAdapter<String>(this, R.layout.list_item, assignmentNameList);
		
		for (Assignment a: app.course.getAssignments())
			assignmentNameList.add(a.getTitle());
		
		setListAdapter(assignmentListAdapter);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) 
	{
		super.onListItemClick(l, v, position, id);
		Intent i = new Intent(this, AssignmentActivity.class);
		app.assignment = app.course.getAssignments()[position];
		startActivity(i);
	}
	
	@Override
	public void onBackPressed()
    {	
		app.course = null;
		finish();
    }
}