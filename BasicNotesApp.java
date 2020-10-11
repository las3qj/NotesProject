package basicNotes;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.util.Vector;

import javax.swing.*;



public class BasicNotesApp {
	BasicNotesDB dataBase;
	Vector<String> categories;
	
	JComboBox sCategory;
	JLabel sTag1, sTag2;
	JPanel sTagsPanel;
	JPanel selectionPanel;
	JLabel cNLabel;
	JTextField cNTag1, cNTag2;
	JPanel cNTagPanel;
	JTextArea cNTextArea;
	JPanel currNotePanel;
	JPanel finalPanel;
	JFrame frame;
	
	public BasicNotesApp(BasicNotesDB db){
		dataBase = db;
		simpleListener listener = new simpleListener();
		
		categories = new Vector<String>();
		categories.add("Category");
		Vector<String> cats = db.getCategoryNames();
		for(int i=0; i<cats.size(); i++)
			categories.add(cats.get(i));
		sCategory = new JComboBox(categories);
		sCategory.addActionListener(listener);
		
		sTag1 = new JLabel("Tag1");
		sTag2 = new JLabel("Tag2");
		sTagsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		sTagsPanel.add(sCategory);
		sTagsPanel.add(sTag1);
		sTagsPanel.add(sTag2);
		
		JTextArea fillerTextArea = new JTextArea(20,20);
		fillerTextArea.setText("Hello World");
		fillerTextArea.setLineWrap(true);
		fillerTextArea.setWrapStyleWord(true);
		fillerTextArea.setMargin(new Insets(5, 5, 5,5));
	    
		selectionPanel = new JPanel(new BorderLayout());
		selectionPanel.add(sTagsPanel, BorderLayout.NORTH);
		selectionPanel.add(fillerTextArea, BorderLayout.CENTER);
		GridBagConstraints sPConstraints = new GridBagConstraints();
		sPConstraints.gridx = 1;
		sPConstraints.gridy = 1;
		
		cNLabel = new JLabel("CurrNote");
		cNTag1 = new JTextField("Tag1");
		cNTag2 = new JTextField("Tag2");
		cNTagPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		cNTagPanel.add(cNLabel);
		cNTagPanel.add(cNTag1);
		cNTagPanel.add(cNTag2);
		
		cNTextArea = new JTextArea(20,20);
	    cNTextArea.setText("Hello World");
	    cNTextArea.setLineWrap(true);
	    cNTextArea.setWrapStyleWord(true);
	    cNTextArea.setMargin(new Insets(5, 5, 5,5));
	    
		currNotePanel = new JPanel(new BorderLayout());
		currNotePanel.add(cNTagPanel, BorderLayout.NORTH);
		currNotePanel.add(new JScrollPane(cNTextArea), BorderLayout.CENTER);
		
		GridBagConstraints cNPConstraints = new GridBagConstraints();
		cNPConstraints.gridx = 2;
		cNPConstraints.gridy = 1;
		
		finalPanel = new JPanel(new GridBagLayout());
		finalPanel.add(selectionPanel, sPConstraints);
		finalPanel.add(currNotePanel, cNPConstraints);
		
		//Final JFrame component
		frame = new JFrame();
	    frame.setTitle("Basic File Reader");
	    frame.setLayout(new BorderLayout());
	    frame.add(finalPanel, BorderLayout.CENTER); 
	    frame.setSize(new Dimension(800, 650));
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setLocationRelativeTo(null);
	    frame.setResizable(false);
	    frame.setVisible(true);
		
	}
	

	private class simpleListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == sCategory) {
				if((String)sCategory.getSelectedItem() != "Category") {
					Vector<String> tags = dataBase.getCategoryTags((String)sCategory.getSelectedItem());
					sTag1.setText(tags.get(0));
					sTag2.setText(tags.get(1));
				}
				else {
					sTag1.setText("Tag1");
					sTag2.setText("Tag2");
				}
			}
		}
	}
	
	
	
}
