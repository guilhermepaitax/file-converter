package server.controller;

import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

import server.models.Parser;
import server.models.Writer;


public class ConversionController {
	// private LeituraCsv leituraCSV;
	private Parser parser;
	private Writer writer;
	
	private Socket client;
	
	private int totalLines;
	
	public ConversionController(Socket client) {
		this.client = client;
	}
	
	public void startConversion(Path csvFilePath, Path saveFolderPath) {
		try {
			this.totalLines = Files.readAllLines(csvFilePath).size();
			int progessIncrm = totalLines / 100;
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
