package com.ivoid.droodle;

import java.util.Calendar;
import java.util.Date;

import com.ivoid.droodle.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class AssignmentActivity extends Activity
{
	private TextView   description;
	private TextView   grade;
	private TextView   comment;
	private TextView   available_from;
	private TextView   due;
	private TextView   turned_in;
	private TextView   status;
	private Globals app;
	
	private void showDialog(String stuff)
	{ Toast.makeText(this, stuff,Toast.LENGTH_LONG).show(); }
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.assignment);
		
		app = ((Globals)getApplicationContext());
		
		((TextView) findViewById(R.assignment.bar)).setText(app.assignment.getTitle());
		
		if (!app.assignment.wasFetched())
			app.assignment.populateAssignment(app.httphelper);
		if (!app.assignment.wasFetched())
		{
			showDialog("Assignment failed to fetch.");
			finish();
		}
		
		status = (TextView) findViewById(R.assignment.status);
		due = (TextView) findViewById(R.assignment.due);
		description = (TextView) findViewById(R.assignment.description);
		grade = (TextView) findViewById(R.assignment.grade);
		comment = (TextView) findViewById(R.assignment.comment);
		available_from = (TextView) findViewById(R.assignment.available_from);
		turned_in = (TextView) findViewById(R.assignment.turned_in); 
		
		description.setText(app.assignment.getDescription());
		status.setText(app.assignment.getStatus());
		due.setText(app.assignment.getDue());
		available_from.setText(app.assignment.getAvailableFrom());
		turned_in.setText(app.assignment.getTurnedIn());
		grade.setText(app.assignment.getGrade());
		comment.setText(app.assignment.getComment());
	}
	
	private boolean setReminder()
	{
		Calendar now = Calendar.getInstance();
		Calendar due = Calendar.getInstance();

		Date dueDate = new Date(app.assignment.getDue());
		due.setTime(dueDate);

		Intent intent = new Intent(Intent.ACTION_EDIT);
		intent.setType("vnd.android.cursor.item/event");
		
		if (due.compareTo(now)!=-1)
		{
			intent.putExtra("endTime", due.getTimeInMillis());
			due.set(Calendar.DAY_OF_YEAR, due.get(Calendar.DAY_OF_YEAR)-1);
			due.set(Calendar.HOUR, 12);
			due.set(Calendar.MINUTE, 0);
			intent.putExtra("beginTime", due.getTimeInMillis());
		}
		
		intent.putExtra("allDay", false);
		intent.putExtra("title", app.assignment.getTitle());
		startActivity(intent);
		return true;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		if (!app.assignment.getDone() && !app.assignment.getNoDate())
		{
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.assignment_menu, menu);
		}
        return super.onCreateOptionsMenu(menu);
    }
	
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case (R.menu.ReminderMenuItem):
			{
				setReminder();
				return true;
			}
		}
		return super.onOptionsItemSelected(item);
	}
}
