package server.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.NoSuchElementException;
import java.util.Scanner;

import server.models.ClientThread;

public class ClientController implements Runnable {
	private Socket client;

	public ClientController(Socket client) {
		this.client = client;
	}
	
	@Override
	public void run() {
		while (!client.isClosed()) {
			try {
				InputStream input;
				input = client.getInputStream();

				Scanner scanner = new Scanner(input);
				String filesPaths = scanner.nextLine();
				String[] paths = filesPaths.split(";");

				Path csvFilePath = Paths.get(paths[0]);
				Path saveFolderPath = Paths.get(paths[1]);
				
				Thread threadClient = new Thread(new ClientThread(client, csvFilePath, saveFolderPath));
				threadClient.start();

			} catch (NoSuchElementException e) {
				e.getStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.getStackTrace();
			}
		}
	}
}
