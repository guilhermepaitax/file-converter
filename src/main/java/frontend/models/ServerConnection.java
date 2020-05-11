package frontend.models;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

import javafx.concurrent.Task;
import javafx.scene.control.ProgressBar;

public class ServerConnection {
	public void Connect(String host, int port, String csvFilePath, String saveFolderPath, ProgressBar progressBarFilaLeitura) {
		Task<Void> connectServer = getTask(host, port, csvFilePath, saveFolderPath, progressBarFilaLeitura);		
		progressBarFilaLeitura.progressProperty().bind(connectServer.progressProperty());
		new Thread(connectServer).start();					
	}
	
	public Task<Void> getTask(String host, int port, String csvFilePath, String saveFolderPath, ProgressBar progressBarFilaLeitura) {
		return new Task<Void>() {
			@Override
			protected Void call() {
				try {
					Socket client = new Socket(host, port);

					PrintStream printStream = new PrintStream(client.getOutputStream());
					printStream.println(csvFilePath + ";" + saveFolderPath);

					InputStream input = client.getInputStream();

					while (!client.isClosed()) {						
						Scanner scanner = new Scanner(input);
						String str = scanner.nextLine();
						System.out.println(str);
					}
					
					client.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}
		};

	}
}
