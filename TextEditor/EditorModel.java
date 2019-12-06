import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class EditorModel {
	
	public static final int MAX_LENGTH = 80;
	File file;
	Scanner in;
	
	List<String> commandList;
	
	StringBuilder errorMessage = new StringBuilder("Error Message: \n");
	
	// here is the content without command and will be printed
	// if there is an error.
	boolean invalidCommandsEncounterd = false;
	String contentWithoutCommand = new String();
	String formattedContent = new String();
	
	boolean rightJustification = false;
	boolean centerJustification = false;
	
	boolean doubleSpace = false;
	
	boolean indentFirstLine = false;
	boolean indentMultipleLines = false;
	
	boolean twoColumns = false;
	
	boolean blankLine = false;
	
	public EditorModel(File file) throws FileNotFoundException {
		this.file = file; 
		in = new Scanner(new FileReader(file));
		contentWithoutCommands();
		String[] commands= {"-r", "-c", "-l", "-t", "-n", 
				"-d", "-s", "-i", "-b", "-n", "-2", 
				"-1", "-e"};
		commandList = Arrays.asList(commands);
		preProcessFile();
		postProcessing();
	}
	public void contentWithoutCommands() {		
		// merging lines
		String newLine = new String();
		while(in.hasNextLine()) {
			String line = in.nextLine().trim();
			if(line.length() > 0 && line.charAt(0) != '-') {
				String[] words = line.split(" ");
				for(String word : words) {
					
					if(newLine.length() + word.length() <= MAX_LENGTH) {
						newLine += word + " ";
					}else {
						contentWithoutCommand += new String(newLine) + "\n";
						newLine = new String(word);
					}
				}
			}else if(line.length() > 0 && line.charAt(0) == '-') {
				
				if(line.substring(0,2).equals("-i")){
					indentFirstLine = true;
				}
			}
			
		}
		contentWithoutCommand += new String(newLine) + "\n";
		
	}
	// dealing with line indentation, blank lines, 
	// 
	public void preProcessFile() throws FileNotFoundException {
		in = new Scanner(new FileReader(file));
		String newLine = new String();
		while(in.hasNextLine()) {
			String line = in.nextLine().trim();
			if(blankLine) {
				if(rightJustification == true) {
					newLine = rightJust(newLine);
				}
				if(centerJustification == true) {
					newLine = centerJust(newLine);
				}
				formattedContent += new String(newLine) + "\n";
				formattedContent += "\n";
				newLine = new String();
				blankLine = false;
			}
			if(line.length() > 0 && line.charAt(0) != '-') {
				String[] words = line.split(" ");
				for(String word : words) {
					if(indentFirstLine) {
						// indent the first line
						newLine += "     ";
						indentFirstLine = false;
					}
				
					if(newLine.length() + word.length() <= MAX_LENGTH) {
						newLine += word + " ";
					}else {
						if(rightJustification == true) {
							newLine = rightJust(newLine);
						}
						if(centerJustification == true) {
							newLine = centerJust(newLine);
						}
						formattedContent += new String(newLine) + "\n";
					
						newLine = new String(word + " ");
						if(indentMultipleLines)
							newLine = "          " + newLine + " ";
						
					}
					
				}
				
			// possible command line 
			}else if(line.length() > 0 && line.charAt(0) == '-') {
				String command = line.substring(0, 2);
				if(!commandList.contains(command)) {
					invalidCommandsEncounterd = true;
					errorMessage.append("Invalid Command: " + command + "\n"
					+ "Dafault output saved, no longer processing");
					
				}else {
					switch(command) {
						case "-r":
							rightJustification = true;
							centerJustification = false;
							break;
						case "-c":
							centerJustification = true;
							rightJustification = false;
							break;
						case "-l":
							rightJustification = false;
							centerJustification = false;
							break;
							
						case "-d":
							doubleSpace = true;
							break;
						case "-s":
							doubleSpace = false;
							break;	
						case "-i":
							indentFirstLine = true;
							break;
						case "-b":
							indentMultipleLines = true;
							break;
						case "-n":
							indentMultipleLines = false;
							break;
							
						case "-2":
							twoColumns = true;
							break;
						case "-1":
							twoColumns = false;
							break;
							
						case "-e":
							blankLine = true;
							
							break;
						
					}
				}
			}
			
		}
		if(rightJustification == true) {
			newLine = rightJust(newLine);
		}
		if(centerJustification == true) {
			newLine = centerJust(newLine);
		}
		formattedContent += newLine + "\n";
		System.out.print(formattedContent);
		
	}
	
	
	public void postProcessing() {
		if(doubleSpace) {
			formattedContent = doubleSpace();
		}
		if(twoColumns) {
			formattedContent = twoCol(formattedContent);
		}
	}
	
	
	public String twoCol(String line) 
	{
		//clean up the string and split by space
		String[] word = line.replaceAll("\n", "").replaceAll("\r", "").split(" ");
		//find out the total length of the cleaned string
		int total = 0;
		for(int i = 0; i < word.length; i ++) {
			total = total + word[i].length();
		}
		int size = 0; int length = 0;
		// find out the column size
		for(int i = 0; i < word.length; i ++) {
			size++;
			if(length > total / 2 ) {break;}
		}
		//column1
		String[] firsthalf = new String[size+2];
		int firsthalfindex = 0;
		//column2
		String[] secondhalf = new String[size+2];
		int secondhalfindex = 0;
		//initialization of colmun1 and coumn1
		for(int i = 0 ; i < size+2; i ++) {
			firsthalf[i] = "";
			secondhalf[i] = "";
		}
		int sum = 0;
		for(int i = 0; i < word.length; i ++) {
			// current length
			sum = sum + word[i].length();
			// if current length is great than half of the total length go to column2
			if(sum >total/ 2) {
				
				if(secondhalf[secondhalfindex].length() + word[i].length() < 35) {

					secondhalf[secondhalfindex] = secondhalf[secondhalfindex]  + word[i] + " ";
					
				}else {
					secondhalfindex = secondhalfindex +1;
					secondhalf[secondhalfindex] = secondhalf[secondhalfindex] + word[i] + " ";
				}			
			}else {
			
				if(firsthalf[firsthalfindex].length() + word[i].length() < 35){					
					firsthalf[firsthalfindex] = firsthalf[firsthalfindex] + word[i] + " ";					
				}else {
					firsthalfindex = firsthalfindex +1 ;
					firsthalf[firsthalfindex] = firsthalf[firsthalfindex] + word[i] + " ";
				}
			}
			
		}
		// fill the coulumn1 to 45 char with space
		for(int i = 0; i < size+2; i ++) {
			if(firsthalf[i].length() == 0) {
				break;
			}
			while(firsthalf[i].length() <45) {
				firsthalf[i] = firsthalf[i] + " ";
			}

		}
		// combine two column into one string ready for return 
		String ss = "";
        for(int i = 0; i < size+2 ; i ++) {
            if(firsthalf[i].length() != 0 || secondhalf[i].length() != 0) {
                ss = ss + firsthalf[i] + secondhalf[i] + "\n";
            }else {
            	break;
            }
        }
        System.out.println(ss);
		return ss;
	}
	
	public String doubleSpace() {
		String[] lines = formattedContent.split("\n");
		String newContent = "";
		for(String line : lines) {
			if(line == "") {
				newContent += "\n";
			}else {
				line += "\n\n";
				newContent += line;
			}
		}
		return newContent;
	}
	public String getDafultContent() {
		return contentWithoutCommand;
	}
	
	public String getFormattedContent() {
		return formattedContent;
	}
	
	public boolean ifInvalid() {
		return invalidCommandsEncounterd;
	}
	
	public String getErrorMessage() {
		return errorMessage.toString();
	}
	
	public String rightJust(String newLine) {
		newLine = newLine.substring(0, newLine.length()-1);
		int gap = MAX_LENGTH - newLine.length();
		String gap1 = "";
		while(gap >= 0) {
			gap1 += " ";
			gap--;
		}
		newLine = gap1 + newLine;
		return newLine;
	}
	
	public String centerJust(String newLine) {
		int gap = MAX_LENGTH - newLine.length();
		newLine = newLine.substring(0, newLine.length()-1);
		if(gap%2 == 1) {
			newLine = newLine + " ";
			gap--;
		}
		while(gap >= 0) {
			newLine = " " + newLine + " ";
			gap -= 2;
		}
		return newLine;
	}
	
	
	
	
}
