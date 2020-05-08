package server.models;

import java.net.Socket;
import java.nio.file.Path;

import server.controller.ParserController;

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
		new ParserController(this.client).realizarOperacoes(this.csvFilePath, this.saveFolderPath);
	}
}
