import java.awt.Insets;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import javafx.application.*;
import javafx.stage.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.*;
import javafx.scene.*;
public class EditorGUI extends Application{
	public static void main(String[] args) {
		launch(args);
	}
	@Override
	public void start(Stage stage) throws Exception {
		BorderPane background = new BorderPane();
		VBox options = new VBox();
		TextArea textArea = new TextArea();
		Button loadFile = new Button("Load File");
		loadFile.setMaxWidth(100);
		Button saveFile = new Button("Save as");
		saveFile.setMaxWidth(100);
		Button errorLog = new Button("Error Log");
		errorLog.setMaxWidth(100);
		Button preview = new Button("Preview");
		preview.setMaxWidth(100);
		
		loadFile.setOnAction(MouseClicked -> {
			FileChooser fileChooser = new FileChooser(); 
			File file = fileChooser.showOpenDialog(stage);
			if(file != null) {
				EditorModel model = new EditorModel(file);
				EditorController controller = new EditorController(model);
				textArea.clear();
				try {
					Scanner in = new Scanner(new FileReader(file));
					while(in.hasNextLine()) {
						textArea.appendText(in.nextLine());
						textArea.appendText("\n");
					}
					model.setContent(textArea.getText());
				} catch (FileNotFoundException e) {
					
				}
			}
		});
		
		saveFile.setOnAction(MouseClicked -> {
			
		});
		
		errorLog.setOnAction(MouseClicked -> {
			Stage errorWindow = new Stage();
			BorderPane errorPane = new BorderPane();
			TextArea error = new TextArea();
			error.setPrefSize(400, 400);
			errorPane.setCenter(error);
			Scene errorScene = new Scene(errorPane);
			errorWindow.setScene(errorScene);
			errorWindow.show();
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
	
	
	

}
