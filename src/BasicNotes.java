package basicNotes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class BasicNotes {

	public static void main(String[] args) {
		JFrame f = new JFrame();
		String pathName = JOptionPane.showInputDialog(f, "Database pathName", "C://notesApp//db");
		Path path = Paths.get(pathName);
		String p1 = "jdbc:sqlite:";
		try {
			Files.createDirectories(path);
		} catch (IOException e){
			JOptionPane.showMessageDialog(f, "Failed to create directory");
		}
		int test = (JOptionPane.showConfirmDialog(f, "Load testing table?"));
		String mes="Reset/clear data?";
		if(test==0)
			mes = "Reset default test values?";
		int reset = (JOptionPane.showConfirmDialog(f, mes));
		if(path==null||test==3||reset==3)
			return;
		BasicNotesDB dataBase = new BasicNotesDB(p1+pathName,test==0,reset==0);
		
			
		//hard reset the tables
		//dataBase.testCategories();
		//dataBase.testNotes();

		try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
		
		SwingUtilities.invokeLater(() -> {
            new BasicNotesApp(dataBase);
        });


	}

}
