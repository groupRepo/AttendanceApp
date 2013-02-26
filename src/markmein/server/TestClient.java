package ie.markmein.server;


import java.io.BufferedInputStream;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;


public class TestClient {

	public static void main(String[] args) throws InterruptedException {
		 Socket sock;
         try {
             sock = new Socket("localhost", 2221); 
             System.out.println("Connecting...");

              // sendfile
                   File myFile = new File ("/home/kubac65/Dropbox/Private/Java_workspace/ImagesManipulationTest/2.jpg"); 
                   System.out.println(myFile.length());
                   byte [] mybytearray  = new byte [(int)myFile.length()];
                   FileInputStream fis = new FileInputStream(myFile);
                   BufferedInputStream bis = new BufferedInputStream(fis);
                   bis.read(mybytearray,0,mybytearray.length);
                   OutputStream os = sock.getOutputStream();
                   ObjectOutputStream dOutStream = new ObjectOutputStream(os);
                   System.out.println("Sending...");
                   dOutStream.writeUTF("DUPA");
                   dOutStream.writeInt((int) myFile.length());
                   dOutStream.write(mybytearray,0,mybytearray.length);
                   dOutStream.flush();
                   InputStream is = sock.getInputStream();
                   DataInputStream dis = new DataInputStream(is);
                   sock.shutdownOutput();
                   System.out.println("Sent");
                   
                //  String ret = dis.readUTF();
                //  System.out.println(ret);
                 sock.close();
         } catch (UnknownHostException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
         } catch (IOException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
         }
     }
}
