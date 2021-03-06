package eu.markmein.db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

public class DBHandler {

	private static HttpClient mHttpClient;
	private static final String URL = "http://80.93.22.88/phpDatabase/";
	
	/**
	 * key: code //module offering code
	 */
	public static final String GET_MODULE_OFFERING_TRDATA = "module/getModuleOfferingTRDATA.php";
	/**
	 * key: code //module offering code
	 */
	public static final String GET_STUDENTS_OF_MODULE_OFFERING = "student/getStudetsOfModuleOffering.php";
	/**
	 * key: lecturerId //lecturer id
	 */
	public static final String GET_LECTURERES_CLASSES = "lecturer/getModuleOfferings.php";
	/**
	 * key: moduleOfferingId date time Type
	 */
	public static final String ADD_ATTENDANCE = "attendance/addAttendance.php";
	/**
	 * key: attendanceId studentId
	 */
	public static final String ADD_STUDENT_ATTENDANCE = "attendance/addStudentAttendance.php";
	/**
	 * key moduleOfferingId date time
	 */
	public static final String GET_ATTENDANCE_ID = "attendance/getAttendanceId.php";
	
	public DBHandler() {
		mHttpClient = new DefaultHttpClient();
	}
	
	public JSONArray executeQuery(String aQuery, ArrayList<NameValuePair> aParams) throws ClientProtocolException, IOException, JSONException{
		HttpPost request = new HttpPost(URL + aQuery);
		UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(aParams, "utf-8");
		request.setEntity(formEntity);
		HttpResponse response = null;
		response = mHttpClient.execute(request);
		
		BufferedReader in = null;
		in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		StringBuffer sb = new StringBuffer("");
		String line = "";
		String NL = System.getProperty("line.separator");
		while ((line = in.readLine()) != null) {
			sb.append(line + NL);
		}
		in.close();
		JSONArray result = null;
		try{
			result = new JSONArray(sb.toString());
		} catch(Exception e){
			
		}
		
		return result;
	}
	public static ArrayList<NameValuePair> prepareParams(String aKey, String aValue){
		ArrayList<NameValuePair> p = new ArrayList<NameValuePair>();
		
		p.add(new BasicNameValuePair(aKey, aValue));
		return p;
	}
	public static ArrayList<NameValuePair> prepareParams(ArrayList<String> aKey, ArrayList<String> aValue){
		ArrayList<NameValuePair> p = new ArrayList<NameValuePair>();
		
		int limit = aKey.size();
		for(int i = 0; i < limit; i++){
			p.add(new BasicNameValuePair(aKey.get(i), aValue.get(i)));
		}
		return p;
	}
}
