package eu.markmein;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;

public class HTTPUtils  {
	HttpClient mHttpClient = null;
	BufferedReader in = null;
	String result = null;

	public HttpClient getHttpClient() {
		if (mHttpClient == null) {
			mHttpClient = new DefaultHttpClient();
			Log.e("Error", "In getHttpClient 1");
			return mHttpClient;
		}
		return null;
	}

	public String executeHttpPost(String url, ArrayList<NameValuePair> params) throws Exception {
		//Log.e("Error", "In executeHttpPost 1");
		try {
			//Log.e("Error", "In executeHttpPost 2");
			HttpClient httpClient = getHttpClient();
			//Log.e("Error", "In executeHttpPost 3");
			HttpPost httpRequest = new HttpPost(url);
			//Log.e("Error", "In executeHttpPost 4");
			UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(params, "utf-8");
			//Log.e("Error", "In executeHttpPost 5 "+ urlEncodedFormEntity.toString());
			httpRequest.setEntity(urlEncodedFormEntity);
			//Log.e("Error", "In executeHttpPost 6 " + httpRequest.getURI()+ " " +httpRequest.getParams().getParameter("uID"));
			HttpResponse httpResponse = httpClient.execute(httpRequest);
			//Log.e("Error", "In executeHttpPost 7");
			in = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
			StringBuffer stringBuffer = new StringBuffer("");
			String line = "";
			String NL = System.getProperty("line.separator");
			//Log.e("Error", "In executeHttpPost 8");
			while ((line = in.readLine()) != null) {
				stringBuffer.append(line + NL);
			}
			in.close();
			result = stringBuffer.toString();
			//Log.e("Error", "In executeHttpPost 9");
			return result;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					Log.e("log_tag", "Error converting result "+e.toString()); 
					e.printStackTrace();
				}
			}
		}
	}

	public String executeHttpGet(String url){
		Log.e("Error", "In executeHttpGet 1");
		try{
			Log.e("Error", "In executeHttpPost 2");
			HttpClient httpClient = new DefaultHttpClient();
			Log.e("Error", "In executeHttpGet 3 " + httpClient.toString());
			URI website = new URI(url);
			Log.e("Error", "In executeHttpGet 4 " + website.toURL().toString());
			HttpGet httpGet = new HttpGet();
			Log.e("Error", "In executeHttpGet 5");
			httpGet.setURI(website);
			Log.e("Error", "In executeHttpGet 6");
			HttpResponse httpResponse = httpClient.execute(httpGet);
			Log.e("Error", "In executeHttpGet 7");
			in = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
			Log.e("Error", "In executeHttpGet 8");
			StringBuffer stringBuffer = new StringBuffer("");
			String line = "";
			Log.e("Error", "In executeHttpGet 9");
			String newLine = System.getProperty("line.separator");
			Log.e("Error", "In executeHttpGet 10");
			while((line = in.readLine()) != null){
				stringBuffer.append(line + newLine);
			}
			Log.e("Error", "In executeHttpGet 11");
			in.close();
			result = stringBuffer.toString();
			Log.e("Error", "In executeHttpGet 12");
			return result;
		}catch(Exception e){
			Log.e("Error", "In executeHttpGet 13 " + e.toString());
		}finally{
			if(in != null){
				try{
					in.close();
					return result;
				}catch(Exception e){
					Log.e("Error", "In executeHttpPost 14 " + e.toString());
				}
			}
		}
		return null;
	}
}
