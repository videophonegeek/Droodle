package com.ivoid.helpers;

import java.io.IOException;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

public class HttpHelper
{
	private DefaultHttpClient httpClient;
	private String 		 	  serverURL;
	private Header 		 	  httpHeader;
	private HttpResponse 	  httpResponse;
	
	public HttpHelper(String serverurl)
	{
		httpClient   = new DefaultHttpClient();
		serverURL    = serverurl;
		httpHeader 	 = null;
		httpResponse = null;
	}
	
	public void post(String get, List<NameValuePair> nameValuePairs) 
	throws ClientProtocolException, IOException
	{
		HttpPost httpPost = new HttpPost(serverURL+get);
		httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		
		if (httpHeader != null)	
			httpPost.setHeader(httpHeader);
		
		HttpResponse response = httpClient.execute(httpPost);
		
		httpResponse 		  = response;
	}
	
	public void setHeader(Header h)
	{ httpHeader = h; }
	
	public HttpResponse getHttpResponse()
	{ return httpResponse; }
	
	public Header getHeader()
	{ return httpHeader; }
}