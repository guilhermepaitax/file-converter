package frontend.models;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.Socket;

import javafx.concurrent.Task;
import javafx.scene.control.ProgressBar;
//import servidor.models.Feedback;

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
					// Feedback feedback = null;
					while (!client.isClosed()) {
						if (input.available() > 0) {
							byte[] arrBytes = new byte[3000];
							input.read(arrBytes);

							ByteArrayInputStream bis = new ByteArrayInputStream(arrBytes);
							ObjectInputStream ois = new ObjectInputStream(bis);

							Object obj = ois.readObject();
							// feedback = (Feedback) obj;							
							
							// System.out.println(feedback.toString());
							
							// updateProgress(feedback.getPbarLeituraValor(), 1F);																				
						}
					}
					
					client.close();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				return null;
			}
		};

	}
}
