package ie.markmein.server;   
//import ie.markmein.db.DBHandling;

import java.io.*;
import java.net.*;

public class Server {

    public static void main(String[] args) throws IOException {
        // create socket
        ServerSocket servsock = new ServerSocket(2221);
        
        while(true){
        	System.out.println("Waiting for connection.");
        	Socket newCon = servsock.accept();
        	System.out.println("Connection accepted from :" + newCon);
        	Runnable r = new ConnectionHandler(newCon);
        	Thread t = new Thread(r);
        	t.start();
        }
    }
}