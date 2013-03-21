package eu.markmein.server;

//import static com.googlecode.javacv.cpp.opencv_highgui.CV_LOAD_IMAGE_GRAYSCALE;
//import static com.googlecode.javacv.cpp.opencv_highgui.cvLoadImage;
import static com.googlecode.javacv.cpp.opencv_highgui.cvLoadImage;

import java.io.BufferedOutputStream;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
//import java.io.ObjectOutputStream;
//import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.googlecode.javacv.cpp.opencv_core.IplImage;

import eu.markmein.db.DBHandler;
import eu.markmein.face.FaceRecogniser;
import eu.markmein.face.ImageOperations;

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
		//get socket
		socket = aSocket;
		//set filename for the 
		temp_picture_file = TEMP_PICTURE_DIR + tempFilenameGen + ".jpg";
		tempFilenameGen++;
	}

	public void run(){

		//!!!
		//boolean received
		if(receiveData()){
			ArrayList<String> presentStudents = null;
			try {

				presentStudents = getPresentStudentList();

				outStream = socket.getOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(outStream);			
				oos.writeObject(presentStudents);
				socket.close();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for(String s : presentStudents){
				System.out.println(s);
			}
		}	
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
			System.out.println("Bytes read in first go.: " + currentByte);
			do{
				bytesRead =  oInStream.read(fileArray, currentByte, (fileArray.length-currentByte));
				if(bytesRead >= 0){
					currentByte += bytesRead;
				}
			}while(bytesRead > 0);
			System.out.println("Total bytes read: " + currentByte);

			
			// Write file
			bos.write(fileArray);
			bos.flush();
			bos.close();

			a = true;
			//oInStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return a;
	}
	private ArrayList<String> getPresentStudentList() throws ClientProtocolException, IOException, JSONException{
		// Returned variable
		ArrayList<String> a = new ArrayList<String>();

		// Other variables
		JSONArray ja = database.executeQuery(DBHandler.GET_MODULE_OFFERING_TRDATA, DBHandler.prepareParams("code", moduleOfferingId));
		JSONObject jo = ja.getJSONObject(0);
		String trDataPath = jo.getString("trDataDir");

		// Set up recogniser and load in the model 
		recogniser = new FaceRecogniser(trDataPath);

		//Extract separate faces
		ArrayList<IplImage> faces = ImageOperations.extractFaces(cvLoadImage(temp_picture_file), ImageOperations.getFacesCoords(cvLoadImage(temp_picture_file)));
	
		System.out.println("Faces detected: " + faces.size());
		a = recogniser.recogniseMany(faces);
		System.out.println("Faces recognised: " + a.size());
		return a;
	}
}
