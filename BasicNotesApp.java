package basicNotes;

import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

/**
 * 
 * The immediate tasks to reconcile are as follows (in order of tackling, I think)
 * [X] 1. Implement an action listener for the tree so that the selectionPanel displays a note corresponding to the selected tag
 * [X] 2. Create a formatting system for the displayed note to fit into a horizontal constraint
 * [X] 3. Allow multiple notes to display when a tag is selected
 * [X] 4. Allow a category to be selected, displaying all notes from that category
 * 5. Allow multiple tags to be selected, or even multiple categories
 * 6. Determine the nature of the double-click requirement
 * 7. Allow a category to be selected
 * . . .
 * and so on and so forth
 * . . .
 * n. Allow adding of new tags and categories
 * n+1. Move on to the currentNote panel
 * . . .
 * m. Organize the formatting and sizing of the app to be uniform across and between panels
 * . . .
 * Improve, improve, improve!
 */


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
	
	//Action listeners and tree listeners
	treeListener tListener;
	
	//Final variables
	
	
	public BasicNotesApp(BasicNotesDB db){
		dataBase = db;
		
		tListener = new treeListener();
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
	    frame.setSize(new Dimension(1500, 1000));
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
		cTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		cTree.addTreeSelectionListener(tListener);
		cTree.setRootVisible(false);
		GridBagConstraints cTConstraints = new GridBagConstraints();
		cTConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
		cTConstraints.ipadx=5;
		cTConstraints.ipady=5;
		cTConstraints.weightx=1;
		cTConstraints.weighty=1;
		categoryPanel = new JPanel(new GridBagLayout());
		categoryPanel.add(cTree, cTConstraints);
		categoryPanel.setPreferredSize(new Dimension(150,400));
		categoryPanel.setBackground(Color.white);
	}
	
	/**
	 * creates JPanel selectionPanel and related components
	 */
	private void createSelectionPanel() {
		selectionPanel = new JPanel(new GridBagLayout());
		selectionPanel.setPreferredSize(new Dimension(300,400));
		selectionPanel.setBackground(Color.lightGray);
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
	
	/**
	 * Currently examines the node last selected and sets the JLabel oneNote to be the text of the first note
	 * with the corresponding tag (of the selected node)
	 */
	private class treeListener implements TreeSelectionListener{
		public void valueChanged(TreeSelectionEvent e) {
	        DefaultMutableTreeNode node = (DefaultMutableTreeNode)
	                           cTree.getLastSelectedPathComponent();

	        if (node == null) return;

	        Object nodeInfo = node.getUserObject();
	        if (node.isLeaf()) {
	            String tag = (String)nodeInfo;
	            Vector<String> notes = dataBase.getNotesFromTag(tag);
	            GridBagConstraints c = new GridBagConstraints();
	            c.weightx=1;
	            c.weighty= 1.0 / (double)notes.size();
	            c.gridx=0;
	            c.gridy=0;
	            c.insets = new Insets(5,5,5,5);
	            selectionPanel.removeAll();
	            selectionPanel.repaint();
	            for(int i = 0; i<notes.size()-1; i++) {
	            	String note = notes.get(i);
	            	NotePreview newNote = new NotePreview(note);
	            	selectionPanel.add(newNote, c);
	            	c.gridy++;
	            }
	        }       
	    }
	}
	
	private class NotePreview extends JPanel{
		private String noteContent;
		private JLabel noteLabel;
		
		public NotePreview(String content) {
			noteContent = content;
			noteLabel = new JLabel("<html>"+noteContent+"</html>");
			noteLabel.setPreferredSize(new Dimension(280,130));
			this.setLayout(new GridBagLayout());
			this.setPreferredSize(new Dimension(290,140));
			this.setBackground(Color.white);
			GridBagConstraints nLConstraints  = new GridBagConstraints();
			nLConstraints.insets = new Insets(5,5,5,5);
			nLConstraints.anchor = nLConstraints.CENTER;
			this.add(noteLabel, nLConstraints);
		}
	}
	
}
