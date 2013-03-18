package eu.markmein;

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

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class DBHandler {
	
	public View mLoginFormView;
	public View mLoginStatusView;

	private static HttpClient mHttpClient;
	private static final String URL = "http://80.93.22.88/phpDatabase/";

	public static final String VALIDATE_LOGIN = "users/validateLogin.php";
	public static final String GET_MODULE_OFFERING_TRDATA = "module/getModuleOfferingTRDATA.php";
	public static final String GET_STUDENTS_OF_MODULE_OFFERING = "student/getStudetsOfModuleOffering.php";
	public static final String GET_LECTURERES_CLASSES = "lecturer/getModuleOfferings.php";
	
	public static final String GET_MODULE_ATTENDANCE_RECORDS = "module/moduleAttendanceRecords.php";
	public static final String GET_BEST_WORST_LECTURE_RECORDS = "lecturer/getBestWorstAttendedLecture.php";
	public static final String GET_BEST_WORST_LAB_RECORDS = "lecturer/getBestWorstAttendedLab.php";
	
	public static final String GET_STUDENTS_ID_NAME = "student/getAllStudentsIdName.php";
	public static final String GET_STUDENT_INFO = "student/getStudentDetails.php";
	public static final String GET_STUDENT_MODULES_ATTENDANCES = "student/getStudentModulesAttendance.php";
	
	public static final String GET_STUDENT_CLASSES = "student/getStudentClasses.php";
	public static final String GET_STUDENT_ABSENCES = "student/getStudentAbsences.php";
	public static final String SUBMIT_EXPLANATION = "student/submitExplaination.php";

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
		JSONArray result = new JSONArray(sb.toString());
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
