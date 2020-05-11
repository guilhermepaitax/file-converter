package server.controller;

import java.io.FileWriter;
import java.io.PrintStream;
import java.io.Reader;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import javafx.scene.control.ProgressBar;

import java.util.List;
import server.models.ControlQueue;
import server.models.Parser;
import server.models.Writer;


public class ConversionController {
	// private LeituraCsv leituraCSV;
	// private ControlQueue reader;
	// private Parser parser;
	
	private Socket client;
	
	private ProgressBar progressBar;
	
	private int totalLines;
	
	public ConversionController(Socket client) {
		this.client = client;
	}
	
	public void startConversion(Path csvFilePath, Path saveFolderPath) {
		try {
			this.totalLines = Files.readAllLines(csvFilePath).size();
			int progessIncrm = totalLines / 100;
			
			Reader reader = Files.newBufferedReader(csvFilePath);
    		
    		CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();
    		
    		List<String[]> lines = csvReader.readAll();
    		
    		PrintStream printStream = new PrintStream(client.getOutputStream());
			
			Thread tLeitura = new Thread(new ControlQueue(lines, progressBar));
			Thread tParse = new Thread(new Parser());
			Thread tGravacao = new Thread(new Writer(new FileWriter(saveFolderPath + "\\saida.json")));

			tLeitura.start();
			printStream.println("Strart Read");
			tParse.start();
			printStream.println("Strart Parse");
			tGravacao.start();
			printStream.println("Strart Writer");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
