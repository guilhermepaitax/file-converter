package com.paitax.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;

public class ControlQueue implements Runnable {

	private List<String[]> fileString;
	private static boolean finishRead = true;
	private static boolean finishParse = true;
	
	private static List<String[]> TaskQueueParse;
	private static List<String> TaskQueueWrite;
	
	@FXML
	private static ProgressBar progressBar;
	
	public static void setFinishAll() {
		Platform.runLater(new Runnable() {
            @Override public void run() {
            	progressBar.setProgress(100);	
            }
        });
	}
	
	public static void setFinishParse(boolean convert) {
		finishParse = convert;
	}
	
	public static boolean getFinishParse() {
		return finishParse;
	}
	
	public ControlQueue(List<String[]> file, ProgressBar pgBar) {
		TaskQueueParse = new Vector<String[]>();
		TaskQueueWrite = new Vector<String>();
		this.fileString = file;
		ControlQueue.progressBar = pgBar;
	}
	
	private void addTask(String[] task) {
		TaskQueueParse.add(task);
	}
	
	public static synchronized String[] getTask() {
		if (TaskQueueParse.size() > 0)
			return TaskQueueParse.remove(0);
		return null;
	}
	
	public static synchronized String getTaskWriter() {
		if (TaskQueueWrite.size() > 0)
			return TaskQueueWrite.remove(0);
		return null;
	}
	
	public static void addTaskWrite(String task) {
		TaskQueueWrite.add(task);
	}
	
	public static boolean isTerminatedWriter() {
		if(finishRead) {
			return true;
		} 
		if(finishParse) {
			return true;
		} 
		return  !TaskQueueParse.isEmpty();		
	}
	
	public static boolean isTerminated() {
		if(finishRead) {
			return true;
		}
		if(!TaskQueueParse.isEmpty()) {
			return true;
		}
		else {
			return !TaskQueueWrite.isEmpty();
		}	
	}
	
	@SuppressWarnings("static-access")
	private void receivesData() {
		System.out.println("Start Read " + System.currentTimeMillis());
		int qtdeTotalRegistro = fileString.size();
		int qtdeRegistros = 0;
		do {
			addTask(fileString.get(qtdeRegistros));
			qtdeRegistros++;
		}while(qtdeRegistros < qtdeTotalRegistro );
		finishRead = false;
		System.out.println("Finish Read " +System.currentTimeMillis());
	}
	
	@Override
	public void run() {
		receivesData();
	}

}
