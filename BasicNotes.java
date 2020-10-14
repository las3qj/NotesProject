package basicNotes;

import java.util.Vector;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class BasicNotes {

	public static void main(String[] args) {
		BasicNotesDB dataBase = new BasicNotesDB();
		
		//hard reset the tables
		//dataBase.reset();
		//dataBase.testCategories();
		/*Vector<String> tags = dataBase.getCategoryTags("Content");
		for(int i=0; i<tags.size(); i++)
			System.out.println(tags.get(i));*/
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
