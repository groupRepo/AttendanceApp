package eu.markmein.server;   
//import ie.markmein.db.DBHandling;

import java.io.*;

public class Server {

    public static void main(String[] args) throws IOException {
    	
    	
    	Runnable r1 = new DataRec();
    	Thread t1 = new Thread(r1);
    	t1.start();
    	
    	Runnable r2 = new ListRec();
    	Thread t2 = new Thread(r2);
    	t2.start();
    	/*
        // create socket
        @SuppressWarnings("resource")
		ServerSocket servsock = new ServerSocket(2221);
        
        
        while(true){
        	System.out.println("Waiting for connection.");
        	Socket newCon = servsock.accept();
        	System.out.println("Connection accepted from :" + newCon);
        	Runnable r = new ConnectionHandler(newCon);
        	Thread t = new Thread(r);
        	t.start();
        }*/
    }
}