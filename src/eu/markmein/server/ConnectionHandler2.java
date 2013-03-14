package eu.markmein.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import java.net.Socket;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONObject;

import eu.markmein.db.DBHandler;

public class ConnectionHandler2 implements Runnable {

	private Socket socket;
	private DBHandler database = new DBHandler();
	
	private InputStream is;
	private ObjectInputStream ois;
	
	//private OutputStream os;
	//private ObjectOutputStream oos;
	
	private String moduleOfferingId;
	private ArrayList<String> studentIds;
	private String type;
	public ConnectionHandler2(Socket aSocket) {
		socket = aSocket;
	}

	@Override
	public void run() {
		if(ReceiveData()){
			String time, date;
			date = new Date(System.currentTimeMillis()).toString();
			time = new Time(System.currentTimeMillis()).toString();
			
			try {
				ArrayList<String> keys = new ArrayList<String>(Arrays.asList(new String[]{"moduleOfferingId", "date", "time", "Type"}));
				ArrayList<String> values = new ArrayList<String>(Arrays.asList(new String[]{moduleOfferingId, date, time, type}));
				database.executeQuery(DBHandler.ADD_ATTENDANCE, DBHandler.prepareParams(keys, values));
				String attendanceId;
				
				keys = new ArrayList<String>(Arrays.asList(new String[]{"moduleOfferingId", "date", "time"}));
				values =new ArrayList<String>(Arrays.asList(new String[]{moduleOfferingId, date, time}));
				JSONArray ja = database.executeQuery(DBHandler.GET_ATTENDANCE_ID, DBHandler.prepareParams(keys, values));
				JSONObject jo = ja.getJSONObject(0);
				attendanceId = jo.getString("id");
				System.out.println("Attendance id: " + attendanceId);
				for(String id: studentIds){
					keys = new ArrayList<String>(Arrays.asList(new String[]{"attendanceId", "studentId"}));
					values = new ArrayList<String>(Arrays.asList(new String[]{attendanceId, id}));
					System.out.println("Adding student: " + id + " to attendance: " + attendanceId);
					database.executeQuery(DBHandler.ADD_STUDENT_ATTENDANCE, DBHandler.prepareParams(keys, values));
				}
				//os = socket.getOutputStream();
				//oos = new ObjectOutputStream(os);
				//oos.writeBoolean(true);
				socket.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private boolean ReceiveData(){
		boolean a = false;
		try {
			is = socket.getInputStream();
			ois = new ObjectInputStream(is);
			
			type = ois.readUTF();
			System.out.println("Type of class: " + type);
			moduleOfferingId = ois.readUTF();
			System.out.println("ModuleOfferingId: " + moduleOfferingId);
			studentIds = (ArrayList<String>) ois.readObject();
			System.out.println("List Received: " + studentIds.toString());
			socket.shutdownInput();
			a = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return a;
	}

}
