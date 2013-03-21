package eu.markmein.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ListRec implements Runnable {
	@Override
	public void run() {
		ServerSocket servsock;
		try {
			servsock = new ServerSocket(2222);
			while(true){
				System.out.println("Waiting for List.");
				Socket newCon = servsock.accept();
				System.out.println("Connection accepted from :" + newCon);
				Runnable r = new ConnectionHandler2(newCon);
				Thread t = new Thread(r);
				t.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
