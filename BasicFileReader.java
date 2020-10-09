import javax.swing.*;
import java.awt.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;

public class BasicFileReader {

	//method relegated to read and return the contents from fileName 
	public String readFromFile(String fileName) {
		BufferedReader br = null;
		String content = "";
		try {
			br = new BufferedReader(new FileReader(fileName));
			String contentLine = br.readLine();
			while (contentLine != null) {
				content = content + contentLine + "\n";
				contentLine = br.readLine();
			}
		} catch (IOException ioe){
			content = "Error loading file";
			} 
		return content;
	}
	
	//method relegated to write the string [content] to [fileName]
	public static boolean writeToFile(String fileName, String content) {
		try {
		      File myObj = new File(fileName);
		      if (!myObj.exists()) {
		    	  myObj.createNewFile();
		      }
		      FileWriter myWriter = new FileWriter(myObj);
		      myWriter.write(content);
		      myWriter.close();
		      return true;
		    } catch (IOException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		      return false;
		    }
	}
	
	public BasicFileReader(){
		//JComponents
		JLabel fileTextLabel = new JLabel("File Text");
		fileTextLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		JTextArea fileText = new JTextArea(20,20);
		fileText.setText("Hello World");
	    fileText.setLineWrap(true);
	    fileText.setWrapStyleWord(true);
	    fileText.setMargin(new Insets(5, 5, 5,5));
	      
		JTextField fileNameField = new JTextField(20);
		fileNameField.setText("examplefilename.txt");
		JButton openFile = new JButton("Open File");
		JButton saveToFile = new JButton("Save File");
		
		JPanel fileChoosePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		fileChoosePanel.add(fileNameField);
		fileChoosePanel.add(openFile);
		fileChoosePanel.add(saveToFile);
		
		JPanel fileReaderPanel = new JPanel();
		fileReaderPanel.setLayout(new BorderLayout());
		fileReaderPanel.add(fileTextLabel, BorderLayout.NORTH);
		fileReaderPanel.add(new JScrollPane(fileText), BorderLayout.CENTER);
		fileReaderPanel.add(fileChoosePanel, BorderLayout.SOUTH);
		
		//Action listeners
		//openFile -> set fileText to content of fileNameField, calling method readFromFile
		openFile.addActionListener((e) -> {
			String content = readFromFile(fileNameField.getText().trim());
			fileText.setText(content);
	      });
		
		/*saveToFile -> save content of fileText to file titled [fileNameField] 
		 * if fileNameField exists, overwrite
		 * else, create new file entitled [fileNameField]
		 * */
		saveToFile.addActionListener(e -> {
			String content = fileText.getText();
			String fileName = fileNameField.getText().trim();
			writeToFile(fileName, content);
		});
		
		JFrame frame = new JFrame();
	    frame.setTitle("Basic File Reader");
	    frame.setLayout(new BorderLayout());
	    frame.add(fileReaderPanel, BorderLayout.CENTER);
	    frame.setSize(new Dimension(800, 650));
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setLocationRelativeTo(null);
	    frame.setResizable(false);
	    frame.setVisible(true);
	      
	      
	}
	
	public static void main(String[] args) {
		try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

            new BasicFileReader();

	}
}
