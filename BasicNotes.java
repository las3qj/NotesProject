package basicNotes;

import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class BasicNotes {

	public static void main(String[] args) {
		JFrame f = new JFrame();
		String path = JOptionPane.showInputDialog(f, "Database pathName", "jdbc:sqlite:C://sqlite/db/basicnotes.db");
		int test = (JOptionPane.showConfirmDialog(f, "Populate with test data?"));
		if(path==null||test==3)
			return;
		BasicNotesDB dataBase = new BasicNotesDB(path,test==0);
		
			
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
