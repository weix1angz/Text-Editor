
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javafx.application.*;
import javafx.stage.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.control.*;
import javafx.scene.*;
public class EditorGUI extends Application{
	EditorModel model = null;
	EditorController controller = null;
	boolean ifLoadFile = false;
	public static void main(String[] args) {
		launch(args);
	}
	@Override
	public void start(Stage stage) throws Exception {
		BorderPane background = new BorderPane();
		VBox options = new VBox();
		TextArea textArea = new TextArea();
		textArea.setFont(Font.font("Menlo"));
		Button loadFile = new Button("Load File");
		loadFile.setMaxWidth(100);
		Button saveFile = new Button("Save as");
		saveFile.setMaxWidth(100);
		Button errorLog = new Button("Error Log");
		errorLog.setMaxWidth(100);
		Button preview = new Button("Preview");
		preview.setMaxWidth(100);
		
		
		
		
		// load file button handler
		loadFile.setOnAction(MouseClicked -> {
			FileChooser fileChooser = new FileChooser(); 
			File file = fileChooser.showOpenDialog(stage);
			if(file != null) {
				ifLoadFile = true;
				try {
					model = new EditorModel(file);
				} catch (FileNotFoundException e1) {
				}
				controller = new EditorController(model);
				textArea.clear();
				try {
					Scanner in = new Scanner(new FileReader(file));
					while(in.hasNextLine()) {
						textArea.appendText(in.nextLine());
						textArea.appendText("\n");
						
					}
					in.close();
				} catch (FileNotFoundException e) {
					
				}
			}
		});
		
		// save file button handler
		saveFile.setOnAction(MouseClicked -> {
			FileChooser fileChooser = new FileChooser();
			FileChooser.ExtensionFilter extension=new 
					FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
			fileChooser.getExtensionFilters().add(extension);
			File file = fileChooser.showSaveDialog(stage);
			if(file!=null)
			{
				if(model.ifInvalid()) {
					SaveFile(model.getDafultContent(),file);
				}else {
					SaveFile(model.getFormattedContent(),file);
				}
			}
			
		});
		
		// error log button handler
		errorLog.setOnAction(MouseClicked -> {
			Stage errorWindow = new Stage();
			BorderPane errorPane = new BorderPane();
			TextArea error = new TextArea();
			error.setPrefSize(400, 400);
			errorPane.setCenter(error);
			error.setText(model.getErrorMessage());
			Scene errorScene = new Scene(errorPane);
			errorWindow.setScene(errorScene);
			errorWindow.show();
		});
		
		// preview button handler
		preview.setOnAction(MouseClicked -> {
			if(ifLoadFile) {
				if(model.ifInvalid()) {
					textArea.clear();
					textArea.setText(model.getDafultContent());
				}else {
					textArea.clear();
					textArea.setText(model.getFormattedContent());
				}
			}
			
		});
		
		
		textArea.setPrefSize(600, 500);
		options.getChildren().addAll(loadFile, saveFile, errorLog, preview);
		
		background.setCenter(textArea);
		background.setLeft(options);
		Scene scene = new Scene(background, 700, 500);
		stage.setScene(scene);
		stage.setTitle("TEXT EDITOR");
		
		stage.show();
		
		
	
	}
	private void SaveFile(String text,File file)
	{
		try
		{
				FileWriter writer=new FileWriter(file);
				writer.write(text);
				writer.close();
		}
		catch (IOException e)
		{
			
		}
		
	}
	
	
	

}
