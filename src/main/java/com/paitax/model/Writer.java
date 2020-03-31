package com.paitax.model;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Writer implements Runnable {

	FileWriter writeFile;
	public Writer(FileWriter wf) {
	  this.writeFile = wf;
	}
	
	private String getData() {
		return new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss.SSS z").format(new Date(System.currentTimeMillis()));
	}
	
	@Override
	public void run() {

		System.out.println("Start write" + getData());
		do {

			String task = ControlQueue.getTaskWriter();

			if (task != null) {
				try {
					writeFile.write(task+ "\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		} while (ControlQueue.isTerminatedWriter());
		try {
			writeFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		ControlQueue.setFinishAll();
		System.out.println("Finish write" + getData());
	}
	
}
