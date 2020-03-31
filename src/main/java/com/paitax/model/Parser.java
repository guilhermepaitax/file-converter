package com.paitax.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.Gson;

public class Parser implements Runnable {

	@Override
	public void run() {
		System.out.println("Start conversion" + getData());
		do {

			String[] task = ControlQueue.getTask();

			if (task != null) {
				String[] campos = task;
				JsonModel newJson = new JsonModel();
				setFields(campos, newJson);
				ControlQueue.addTaskWrite(new Gson().toJson(newJson));
			}

		} while (ControlQueue.isTerminated());
		ControlQueue.setFinishParse(false);
		System.out.println("Finish conversion" + getData());
	}
	
	private void setFields(String[] campos, JsonModel json) {
		json.setNumber(Long.parseLong(campos[0])); 
		json.setGender(campos[1]);
		json.setNameSet(campos[2]);
		json.setTitle(campos[3]);
		json.setGivenName(campos[4]);
		json.setSurname(campos[5]);
		json.setStreetAddress(campos[6]);
		json.setCity(campos[7]);
		json.setState(campos[8]);
		json.setZipCode(campos[9]);
		json.setCountryFull(campos[10]);
		json.setEmailAddress(campos[11]);
		json.setUsername(campos[12]);
		json.setPassword(campos[13]);
		json.setTelephoneNumber(campos[14]);
		json.setBirthday(campos[15]);
		json.setCCType(campos[16]);
		json.setCCNumber(Long.parseLong(campos[17]));
		json.setCVV2(Integer.parseInt(campos[18]));
		json.setCCExpires(campos[19]);
		json.setNationalID(campos[20]);
		json.setColor(campos[21]);
		json.setKilograms(Double.parseDouble(campos[22]));
		json.setCentimeters(Integer.parseInt(campos[23]));
		json.setGUID(campos[24]);
	}
	
	private String getData() {
		return new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss.SSS z").format(new Date(System.currentTimeMillis()));
	}
	
}
