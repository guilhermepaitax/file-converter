package com.paitax;

import java.io.BufferedReader;
import java.util.ArrayList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.paitax.model.ControlQueue;
import com.paitax.model.Parser;
import com.paitax.model.Writer;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class MainController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField txtCSVFile;

    @FXML
    private Button btnCSVFile;

    @FXML
    private TextField txtExitFolder;

    @FXML
    private Button btnExitFolder;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Button btnConvert;

    @FXML
    private Label lblProgress;

    @FXML
    private Label lblRead;

    @FXML
    private Label lblConvert;

    @FXML
    private Label lblSave;
    
    @FXML
    private Label lblTotal;
    
    private ExecutorService pool; 
    
    private static final int quantidadeCpu = Runtime.getRuntime().availableProcessors();

    @FXML
    void initialize() {
    	
    	progressBar.setProgress(0);		
		pool = Executors.newFixedThreadPool(quantidadeCpu);
    	
    	btnCSVFile.setOnMouseClicked((MouseEvent e)->{	
			txtCSVFile.setText(FileSearch());
		});
    	
    	btnExitFolder.setOnMouseClicked((MouseEvent e)->{	
    		txtExitFolder.setText(FolderSearch());
		});
    	
    	btnConvert.setOnMouseClicked((MouseEvent e)->{	
    		// long timeStarted = System.currentTimeMillis();
    		lblProgress.setVisible(true);
    		progressBar.setVisible(true);
    		progressBar.setProgress(0);
    		// progressBar.setProgress(0.05F);
    		
    		File file = new File(txtCSVFile.getText());
    		
    		if(file.exists() && !txtCSVFile.getText().equals("")) {
    			startConversion();
    		}
    		else {
    			Alert error = new Alert(Alert.AlertType.ERROR);
                error.setHeaderText("Erro");
        		error.setContentText("Arquivo não encontrado!");
        		error.showAndWait();
    		}
    		
    		/*
    		JsonArray result = ReadFile(txtCSVFile.getText());
    		long timeRead = System.currentTimeMillis();
    		progressBar.setProgress(0.33F);
    		
    		if (result == null) { Clean(); return; }
    		
    		String jsonContent = ConvertFile(result);
			progressBar.setProgress(0.66F);
    		long timeConvert = System.currentTimeMillis();
			
			
			if (!SaveFile(jsonContent, txtExitFolder.getText() + "//file.json")) { Clean(); return; }
			progressBar.setProgress(1F);
			long timeFinish = System.currentTimeMillis();
			
			lblTotal.setText("Tempo Total: " + (timeFinish - timeStarted));
			lblSave.setText("Tempo Gravação: " + (timeFinish - timeConvert));
			lblConvert.setText("Tempo Converção: " + (timeConvert - timeRead));
			lblRead.setText("Tempo Leitura: " + (timeRead - timeStarted) );
			
			progressBar.setProgress(1F);
			Alert sucess = new Alert(Alert.AlertType.CONFIRMATION);
			sucess.setHeaderText("Sucesso");
			sucess.setContentText("Arquivo convertido com sucesso!");
			sucess.showAndWait();
    		*/
    		Clean();
		});

    }
    
    private void startConversion() {
    	Reader reader;
    	
    	try {
    		reader = Files.newBufferedReader(Paths.get(txtCSVFile.getText()));
    		
    		CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();
    		
    		List<String[]> lines = csvReader.readAll();
    	    progressBar.setProgress(-1);
    	    
    	    pool.execute(new ControlQueue(lines, progressBar));
    	    pool.execute(new Parser());
    	    pool.execute(new Writer(new FileWriter(txtExitFolder.getText() + "\\saida.json")));
    		
    	} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public void Clean() {
    	lblProgress.setVisible(false);
		progressBar.setVisible(false);
		progressBar.setProgress(0F);
    }
    
    public String FileSearch() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new ExtensionFilter("CSV", "*.csv"));
		File file = fileChooser.showOpenDialog(new Stage());	
		if(file != null) {
			return file.getAbsolutePath();	
		}
		return null;
	}
    
    public String FolderSearch() {
    	DirectoryChooser directoryChooser = new DirectoryChooser();
    	File file = directoryChooser.showDialog(new Stage());
		if(file != null) {
			return file.getAbsolutePath();	
		}
		return null;
	}
    
    public JsonArray ReadFile(String file) {
    	JsonArray result = new JsonArray();
    	
    	try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
    		
    		String line;
    		boolean flag = true;
    		List<String> columns = null;
    		
    		while ((line = reader.readLine()) != null) {
    			if (flag) {
    				flag = false;
    				
    				columns = Arrays.asList(line.split(","));
    			} else {
    				JsonObject obj = new JsonObject();
    				List<String> chunks = Arrays.asList(line.split(","));
    				
    				for (int i = 0; i < columns.size(); i++) {
						obj.addProperty(columns.get(i), chunks.get(i));
					}
    				
    				result.add(obj);
    			}
    		}
    		
    		return result;
    	} catch(FileNotFoundException fnfe) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setHeaderText("Erro");
    		error.setContentText("Arquivo não encontrado!");
    		error.showAndWait();
            return null;
        } catch(IOException io) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setHeaderText("Erro");
    		error.setContentText("Erro ao ler o arquivo!");
    		error.showAndWait();
            return null;
        }
    }
    
    public String ConvertFile(JsonArray datasets) {
    	Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();
    	return gson.toJson(datasets);
    }
    
    public Boolean SaveFile(String content, String filename) {
    	try {
    		FileWriter fileWriter = new FileWriter(filename);
    		fileWriter.write(content);
    		fileWriter.close();
    		return true;
    	} catch (IOException e) {
    		Alert error = new Alert(Alert.AlertType.ERROR);
            error.setHeaderText("Erro");
    		error.setContentText("Erro ao salvar o arquivo!");
    		error.showAndWait();
    		return false;
	    }
    	
    	
    }
}
