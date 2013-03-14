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
		try {
			HttpClient httpClient = getHttpClient();
			HttpPost httpRequest = new HttpPost(url);
			UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(params, "utf-8");
			httpRequest.setEntity(urlEncodedFormEntity);
			HttpResponse httpResponse = httpClient.execute(httpRequest);
			in = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
			StringBuffer stringBuffer = new StringBuffer("");
			String line = "";
			String NL = System.getProperty("line.separator");
			while ((line = in.readLine()) != null) {
				stringBuffer.append(line + NL);
			}
			in.close();
			result = stringBuffer.toString();
			return result;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					Log.e("log_tag", "Error converting result "+e.toString()); 
				}
			}
		}
	}

	public String executeHttpGet(String url){
		try{
			HttpClient httpClient = new DefaultHttpClient();
			URI website = new URI(url);
			HttpGet httpGet = new HttpGet();
			httpGet.setURI(website);
			HttpResponse httpResponse = httpClient.execute(httpGet);
			in = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
			StringBuffer stringBuffer = new StringBuffer("");
			String line = "";
			String newLine = System.getProperty("line.separator");
			while((line = in.readLine()) != null){
				stringBuffer.append(line + newLine);
			}
			in.close();
			result = stringBuffer.toString();
			return result;
		}catch(Exception e){
			Log.e("Error", "In executeHttpGet 1 " + e.toString());
		}finally{
			if(in != null){
				try{
					in.close();
					return result;
				}catch(Exception e){
					Log.e("Error", "In executeHttpPost 2 " + e.toString());
				}
			}
		}
		return null;
	}
}
