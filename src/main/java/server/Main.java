package server;

import java.net.ServerSocket;
import java.net.Socket;

import server.controller.ClientController;

public class Main {

	public static void main(String[] args) {
		String host = "localhost";
		int port = 3333;
		try {
			System.out.println("Host: " + host + " Port: " + port);
			System.out.print("---- Starting Server\n");

			ServerSocket server = new ServerSocket(port);
			System.out.print("---- Server Started\n");
			System.out.print("---- Waiting for connections\n");
			while (true) {
				Socket client = server.accept();
				System.out.print("---- Connected\n");

				new Thread(new ClientController(client)).start();					

				System.out.println("---- Starting Conversion\n");
			}
		} catch (Exception ex) {
			ex.getStackTrace();
		}
	}

}
