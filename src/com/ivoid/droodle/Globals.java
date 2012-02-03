package com.ivoid.droodle;

import android.app.Application;

import com.ivoid.helpers.Student;
import com.ivoid.helpers.Course;
import com.ivoid.helpers.Assignment;
import com.ivoid.helpers.HttpHelper;

public class Globals extends Application
{
	public Student    student    = null;
	public Course     course     = null;
	public Assignment assignment = null;
	public HttpHelper httphelper = new HttpHelper("http://droodle-api.appspot.com/api/");
}
