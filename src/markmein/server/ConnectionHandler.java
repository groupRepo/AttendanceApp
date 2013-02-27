package ie.markmein.server;

//import static com.googlecode.javacv.cpp.opencv_highgui.CV_LOAD_IMAGE_GRAYSCALE;
//import static com.googlecode.javacv.cpp.opencv_highgui.cvLoadImage;
import ie.markmein.db.DBHandler;
import ie.markmein.face.FaceOperations;
import ie.markmein.face.FaceRecogniser;

import java.io.BufferedOutputStream;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import static com.googlecode.javacv.cpp.opencv_highgui.*;

public class ConnectionHandler implements Runnable {
	private final String TEMP_PICTURE_DIR = "./temp_pic/";
	private String temp_picture_file;
	
	private static int tempFilenameGen = 0;
	
	private FaceRecogniser recogniser;
	private DBHandler database = new DBHandler();
	
	private String moduleOfferingId;
	
	private Socket socket;
	private InputStream inStream;
	private OutputStream outStream;
	
	public ConnectionHandler(Socket aSocket){
		socket = aSocket;
		temp_picture_file = TEMP_PICTURE_DIR + tempFilenameGen + ".jpg";
		tempFilenameGen++;
	}

	public void run(){
		
		//!!!
		//boolean received
		receiveData();
		
		//getAttendanceList();
		
	}
	private boolean receiveData(){
		boolean a = false;
		
		int fileSize;
		byte fileArray[];
		
		try {
			int bytesRead, currentByte;

			inStream = socket.getInputStream();
			ObjectInputStream oInStream = new ObjectInputStream(inStream);
		
			// Receive filesize and module offering id
			moduleOfferingId = oInStream.readUTF();
			System.out.println("ModuleOfferingId: " + moduleOfferingId);
			fileSize = oInStream.readInt();
			System.out.println("File size: " + fileSize);
			
			fileArray = new byte[fileSize+10];
			
			// Receive the actual file
			FileOutputStream fos = new FileOutputStream(temp_picture_file); // destination path and name of file
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			
			currentByte = 0;
			bytesRead = oInStream.read(fileArray, 0,fileArray.length);
			currentByte = bytesRead;
			System.out.println("File size: " + currentByte);
			do{
				bytesRead =  oInStream.read(fileArray, currentByte, (fileArray.length-currentByte));
				if(bytesRead >= 0){
					currentByte += bytesRead;
				}
			}while(bytesRead > 0);
			System.out.println("File size: " + fileSize);
			
			bos.write(fileArray);
			bos.flush();
			bos.close();
			
			a = true;
			oInStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return a;
	}
	private ArrayList<String> getAttendanceList() throws ClientProtocolException, IOException, JSONException{
		// Returned variable
		ArrayList<String> a = new ArrayList<String>();
		
		// Other variables
		JSONArray ja = database.executeQuery(database.GET_MODULE_OFFERING_TRDATA, DBHandler.prepareParams("moid", moduleOfferingId));
		JSONObject jo = ja.getJSONObject(0);
		String trDataPath = jo.getString("trdatapath");
		
		// Set up recogniser and load in received file
		recogniser = new FaceRecogniser(trDataPath);
		
		a = recogniser.recogniseMany(temp_picture_file);
		return a;
	}
	
}
