package frontend.controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

public class MainController {

    @FXML
    private TextField txtCSVFile;

    @FXML
    private Button btnCSVFile;

    @FXML
    private TextField txtExitFolder;

    @FXML
    private Button btnExitFolder;

    @FXML
    private Button btnConvert;

    @FXML
    private ProgressBar progressBarLeitura;

    @FXML
    private ProgressBar progressBarConversao;

    @FXML
    private ProgressBar progressBarGravacao;

    @FXML
    private ProgressBar progressBarFilaLeitura;

    @FXML
    private ProgressBar progressBarFilaConversao;

    @FXML
    private TextArea txtAreaTempos;

    @FXML
    private TextArea txtAreaEstados;

    @FXML
    void initialize() {
    	// Get CSV File to Convert
    	btnCSVFile.setOnMouseClicked((MouseEvent e)-> {	
			txtCSVFile.setText(FileSearch());
		});
    	
    	// Get Folder Path to Save File
    	btnExitFolder.setOnMouseClicked((MouseEvent e)-> {	
    		txtExitFolder.setText(FolderSearch());
		});

    	// Convert File
    	btnConvert.setOnMouseClicked((MouseEvent e)-> {	
    		File file = new File(txtCSVFile.getText());
    		
    		if(file.exists() && !txtCSVFile.getText().equals("")) {
    			Cervert();
    		}
    		else {
    			Alert error = new Alert(Alert.AlertType.ERROR);
                error.setHeaderText("Erro");
        		error.setContentText("Arquivo não encontrado!");
        		error.showAndWait();
    		}
		});
    }
    
    private String FileSearch() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new ExtensionFilter("CSV", "*.csv"));
		File file = fileChooser.showOpenDialog(new Stage());	
		if(file != null) {
			return file.getAbsolutePath();	
		}
		return null;
	}
    
    private String FolderSearch() {
    	DirectoryChooser directoryChooser = new DirectoryChooser();
    	File file = directoryChooser.showDialog(new Stage());
		if(file != null) {
			return file.getAbsolutePath();	
		}
		return null;
	}
    
    private void Cervert() {
    	
    }
}
