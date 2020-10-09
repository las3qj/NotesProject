import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;
import java.util.stream.Stream;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class BasicFileReader {

	public void readFromFile(String fileName) {
		
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
		
		JPanel fileChoosePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		fileChoosePanel.add(fileNameField);
		fileChoosePanel.add(openFile);
		
		JPanel fileReaderPanel = new JPanel();
		fileReaderPanel.setLayout(new BorderLayout());
		fileReaderPanel.add(fileTextLabel, BorderLayout.NORTH);
		fileReaderPanel.add(new JScrollPane(fileText), BorderLayout.CENTER);
		fileReaderPanel.add(fileChoosePanel, BorderLayout.SOUTH);
		
		//Action listeners
		//openFile -> set fileText to content of fileNameField
		openFile.addActionListener((e) -> {
			BufferedReader br = null;
			String content = "";
			try {
				br = new BufferedReader(new FileReader(fileNameField.getText()));
				String contentLine = br.readLine();
				while (contentLine != null) {
					content = content + contentLine + "\n";
					contentLine = br.readLine();
				}
			} catch (IOException ioe){
				content = "Error loading file";
				} 
			fileText.setText(content);
			
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
