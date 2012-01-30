package com.ivoid.helpers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Student
{	
	private String name;
	public Course[] courses;
	
	public Student(JSONObject student)
	{
		JSONArray tmp = null;
		try
		{
			name=student.getString("student");
			tmp=student.getJSONArray("courses");
		} catch(JSONException e){}
		
		populateCourses(tmp);
	}
	
	
	private void populateCourses(JSONArray jArray)
	{
		short len = (short) jArray.length();
		courses = new Course[len];
		
		try
		{
			Course course = null;
			
	      	for (short s = 0; s<len; s++)
	      	{
				course = new Course(jArray.getJSONObject(s));
				courses[s] = course;
	      	}
		} catch(JSONException e){}
	}
	
	//Accessors
	public String getName()
	{ return name; }
}
