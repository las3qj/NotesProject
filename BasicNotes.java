package basicNotes;

import java.util.Vector;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class BasicNotes {

	public static void main(String[] args) {
		BasicNotesDB dataBase = new BasicNotesDB(true);
		
		//hard reset the tables
		//dataBase.reset();
		dataBase.testCategories();
		dataBase.testNotes();
		
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
