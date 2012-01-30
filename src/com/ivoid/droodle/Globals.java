package com.ivoid.droodle;

import com.ivoid.helpers.Student;
import com.ivoid.helpers.Course;
import com.ivoid.helpers.Assignment;
import com.ivoid.helpers.HttpHelper;

public class Globals
{
	public static Student    student    = null;
	public static Course     course     = null;
	public static Assignment assignment = null;
	public static HttpHelper httphelper = new HttpHelper("http://droodle-api.appspot.com/api/"); 
}
