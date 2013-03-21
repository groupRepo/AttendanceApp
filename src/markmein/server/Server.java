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
    }
}