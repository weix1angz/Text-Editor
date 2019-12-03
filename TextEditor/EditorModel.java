import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class EditorModel {
	File file;
	String fileContent;
	
	public EditorModel(File file) {
		this.file = file; 
	}
	
	
	public String getContent() {
		return fileContent;
	}
	
	public void setContent(String str) {
		fileContent = str;
	}
}
