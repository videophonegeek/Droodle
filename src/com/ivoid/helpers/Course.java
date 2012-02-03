package com.ivoid.helpers;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Course 
{
	private String title;
	private String link;
	private Assignment[] assignments;
	private boolean fetched;
	
	public Course(JSONObject course)
	{
		try
		{
			title = course.getString("title");
			link = course.getString("link");
		} catch(JSONException e){}
		
		fetched = false;
	}
	
	private void fetchJSON(HttpHelper httphelper) 
	{	
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("link", link));
		try {
			httphelper.post("getAssignments", nameValuePairs);
		} catch (Exception e) {}
	}	
	
	public void populateAssignments(HttpHelper httphelper)
	{	
		fetched = true;
		
		fetchJSON(httphelper);
		
		JSONArray jArray = null;
		
		try{
			jArray = new JSONArray( EntityUtils.toString ( httphelper.getHttpResponse().getEntity() ) );
		} catch (Exception e) {
			fetched = false;
			return;
		}
		
		short len = (short) jArray.length();
		assignments = new Assignment[len];
		
		try
		{
			Assignment assignment = null;
			
			for (short s = 0; s<len; s++)
	      	{
				assignment = new Assignment(jArray.getJSONObject(s));
				assignments[s] = assignment;
	      	}
		} catch(JSONException e){}
	}
	
	//Accessors
	public boolean wasFetched()
	{ return fetched; }
	
	public String getTitle()
	{ return title; }
	
	public Assignment[] getAssignments()
	{ return assignments; }	
}
