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
import javax.swing.tree.DefaultMutableTreeNode;



public class BasicNotesApp {
	BasicNotesDB dataBase;
	Vector<String> categories;
	
	//categoryPanel components
	JPanel categoryPanel;
	JTree cTree;
	
	//selectionPanel components
	/*JComboBox sCategory;
	JLabel sTag1, sTag2;*/
	//JPanel sTagsPanel;
	JLabel oneNote;
	JPanel selectionPanel;
	
	//currNotePanel components
	JLabel cNLabel;
	JTextField cNTag1, cNTag2;
	JPanel cNTagPanel;
	JTextArea cNTextArea;
	JPanel currNotePanel;
	
	
	//final components
	JPanel finalPanel;
	JFrame frame;
	
	simpleListener listener;
	
	public BasicNotesApp(BasicNotesDB db){
		dataBase = db;
		
		//invoke createCategoryPanel() to create the categoryPanel
		createCategoryPanel();
		//categoryPanel components (cComponents) are now instantiated
		GridBagConstraints cConstraints = new GridBagConstraints();
		cConstraints.gridx = 1;
		cConstraints.gridy = 1;
		
		//invoke createSelectionPanel() to create the selectionPanel
		createSelectionPanel();
		//selectionPanel components (sComponents) are now now instantiated
		GridBagConstraints sPConstraints = new GridBagConstraints();
		sPConstraints.gridx = 2;
		sPConstraints.gridy = 1;
				
		//invoke createCurrentNotePanel() to create the currNotePanel
		createCurrentNotePanel();
		//currNotePanel components (cNCompoonents) are now instantiated
		GridBagConstraints cNPConstraints = new GridBagConstraints();
		cNPConstraints.gridx = 3;
		cNPConstraints.gridy = 1;
		
		finalPanel = new JPanel(new GridBagLayout());
		finalPanel.add(categoryPanel, cConstraints);
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
	/**
	 * creates JPanel categoryPanel and related components
	 */
	private void createCategoryPanel() {
		DefaultMutableTreeNode top = new DefaultMutableTreeNode("delete");
		createCTreeNodes(top);
		cTree = new JTree(top);
		cTree.setRootVisible(false);
		categoryPanel = new JPanel(new BorderLayout());
		categoryPanel.add(cTree, BorderLayout.CENTER);
	}
	
	/**
	 * creates JPanel selectionPanel and related components
	 */
	private void createSelectionPanel() {
	    //callDeadCode()
		//String note = dataBase.getNotesFromTag("Nietzsche").get(0);
		//oneNote = new JLabel(note);
		oneNote = new JLabel("Basic example of a note");
		selectionPanel = new JPanel(new BorderLayout());
		selectionPanel.add(oneNote);
	}
	
	/**
	 * creates JPanel currNotePanel and related components
	 */
	private void createCurrentNotePanel() {
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
	}
	
	/**
	 * Populates the top node of cTree with categories and tags from dataBase
	 * 
	 * @param top is the top node for JTree cTree
	 */
	private void createCTreeNodes(DefaultMutableTreeNode top) {
		DefaultMutableTreeNode category = null;
	    DefaultMutableTreeNode tag = null;
	    
	    //pull all categories from dataBase
	    Vector<String> cats = dataBase.getCategoryNames();
	    Vector<String> tags;
	    
	    for(int i = 0; i<cats.size(); i++) {
	    	category = new DefaultMutableTreeNode(cats.get(i));
	    	top.add(category);
	    	tags = dataBase.getCategoryTags(cats.get(i));
	    	
	    	//pull tags for each category in dataBase
	    	for(int j = 0; j<tags.size(); j++) {
	    		tag = new DefaultMutableTreeNode(tags.get(j));
	    		category.add(tag);
	    	}
	    }
	}
	
	private void callDeadCode() {
		/* dead code
		sCategory = new JComboBox(categories);
		sCategory.addActionListener(listener);
		sTag1 = new JLabel("Tag1");
		sTag2 = new JLabel("Tag2");
		sTagsPanel.add(sCategory);
		sTagsPanel.add(sTag1);
		sTagsPanel.add(sTag2);
		listener = new simpleListener();
		categories = new Vector<String>();
		categories.add("Category");
		Vector<String> cats = dataBase.getCategoryNames();
		for(int i=0; i<cats.size(); i++)
			categories.add(cats.get(i));
		
		sTagsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		selectionPanel.add(sTagsPanel, BorderLayout.NORTH);*/
	}
	
	private class simpleListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			/*if(e.getSource() == sCategory) {
				if((String)sCategory.getSelectedItem() != "Category") {
					Vector<String> tags = dataBase.getCategoryTags((String)sCategory.getSelectedItem());
					sTag1.setText(tags.get(0));
					sTag2.setText(tags.get(1));
				}
				else {
					sTag1.setText("Tag1");
					sTag2.setText("Tag2");
				}
			}*/
		}
	}
	
	
	
}
