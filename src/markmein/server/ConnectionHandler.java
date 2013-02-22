package ie.markmein.server;

import static com.googlecode.javacv.cpp.opencv_highgui.CV_LOAD_IMAGE_GRAYSCALE;
import static com.googlecode.javacv.cpp.opencv_highgui.cvLoadImage;
import ie.markmein.face.FaceRecogniser;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ConnectionHandler implements Runnable {
	static int tempFilenameGen = 9000;
	private Socket socket;
	private FaceRecogniser recogniser;
	
	private InputStream inStream;
	private OutputStream outStream;
	
	public ConnectionHandler(Socket aSocket){
		socket = aSocket;
	}

	public void run(){
		try {
			
			int incommingFileSize, bytesRead;
			int currentByte = 0;
			byte[] incommingFile;
			String moduleOfferingId;
			
			inStream = socket.getInputStream();
			DataInputStream dInStream = new DataInputStream(inStream);
			incommingFileSize = dInStream.readInt();
			System.out.println("File size: " + incommingFileSize);
			incommingFile = new byte [incommingFileSize+1];
			moduleOfferingId = dInStream.readUTF();
			System.out.println("String :" + moduleOfferingId);
					
			FileOutputStream fos = new FileOutputStream(tempFilenameGen + ".jpg"); // destination path and name of file
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			
			bytesRead = dInStream.read(incommingFile, 0,incommingFile.length);
			currentByte = bytesRead;

			do{
				bytesRead =  dInStream.read(incommingFile, currentByte, (incommingFile.length-currentByte));
				if(bytesRead >= 0){
					currentByte += bytesRead;
				}
			}while(bytesRead > -1);
			System.out.println("Received");
			
			bos.write(incommingFile, 0 , currentByte);
			bos.flush();
			bos.close();
			
			//Validation
			System.out.println(moduleOfferingId);
			System.out.println(incommingFileSize);
			
			recogniser = new FaceRecogniser("./studentImgs/");
			String name = recogniser.recognise(cvLoadImage("9000.jpg", CV_LOAD_IMAGE_GRAYSCALE));
			System.out.println(name);
			outStream = socket.getOutputStream();
			DataOutputStream dOutStream = new DataOutputStream(outStream);
			dOutStream.writeUTF(name);
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
	}
}
