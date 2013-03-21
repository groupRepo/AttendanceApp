package eu.markmein.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class DataRec implements Runnable {

	@Override
	public void run() {
		ServerSocket servsock;
		try {
			servsock = new ServerSocket(2221);
			while(true){
				System.out.println("Waiting for Data.");
				Socket newCon = servsock.accept();
				System.out.println("Connection accepted from :" + newCon);
				Runnable r = new ConnectionHandler(newCon);
				Thread t = new Thread(r);
				t.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
