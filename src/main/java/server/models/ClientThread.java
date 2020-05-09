package server.models;

import java.net.Socket;
import java.nio.file.Path;

import server.controller.ConversionController;

public class ClientThread implements Runnable {
	private Socket client;
	private Path csvFilePath;
	private Path saveFolderPath;
	
	public ClientThread(Socket client, Path csvFilePath, Path saveFolderPath) {
		this.client = client;
		this.csvFilePath = csvFilePath;
		this.saveFolderPath = saveFolderPath;
	}
	
	@Override
	public void run() {		
		new ConversionController(this.client).startConversion(this.csvFilePath, this.saveFolderPath);
	}
}
