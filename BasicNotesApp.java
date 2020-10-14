package basicNotes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 * 
 * The immediate tasks to reconcile are as follows (in order of tackling, I think)
 * [X] 1. Implement an action listener for the tree so that the selectionPanel displays a note corresponding to the selected tag
 * [X] 2. Create a formatting system for the displayed note to fit into a horizontal constraint
 * [X] 3. Allow multiple notes to display when a tag is selected
 * [X] 4. Allow a category to be selected, displaying all notes from that category
 * [X] 4.b Allow a scrolling pane for notes while maintaining unity of size between notes
 * [X] 5. Allow multiple tags to be selected, or even multiple categories
 * 5a. Ensure that null tag values in the database are actually the empty string to avoid errors in parsing
 * [X] 6. Determine the nature of the double-click requirement
 * [X] 7. Allow a category to be selected
 * . . .
 * and so on and so forth
 * . . .
 * n. Allow adding of new tags and categories
 * n+1. Move on to the currentNote panel
 * . . .
 * m-1. Make the NotePreviews more uniform and visually appealing
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
	JPanel selectionPanel, sNotesPanel;
	JScrollPane sScrollPane;
	
	//currNotePanel components
	JLabel cNLabel;
	JLabel cNTag1, cNTag2;
	JPanel cNTagPanel;
	JTextArea cNTextArea;
	JPanel currNotePanel;
	
	//final components
	JPanel finalPanel;
	JFrame frame;
	
	//Action listeners and tree listeners
	basicListener bListener;
	
	//Final variables
	
	
	public BasicNotesApp(BasicNotesDB db){
		dataBase = db;
		
		bListener = new basicListener();
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
		cTree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
		cTree.addTreeSelectionListener(bListener);
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
		selectionPanel = new JPanel(new BorderLayout());
		sNotesPanel = new JPanel(new GridBagLayout());
		sScrollPane = new JScrollPane(sNotesPanel);
		sScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		selectionPanel.add(sScrollPane, BorderLayout.CENTER);
		selectionPanel.setPreferredSize(new Dimension(300,400));
	}
	
	/**
	 * creates JPanel currNotePanel and related components
	 */
	private void createCurrentNotePanel() {
		cNLabel = new JLabel("CurrNote");
		cNTag1 = new JLabel("Tag1");
		cNTag2 = new JLabel("Tag2");
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
	
	/**
	 * Currently examines the node last selected and sets the JLabel oneNote to be the text of the first note
	 * with the corresponding tag (of the selected node)
	 */
	private class basicListener implements TreeSelectionListener, MouseListener{
		@Override
		public void valueChanged(TreeSelectionEvent e) {

	        TreePath[] sPaths = cTree.getSelectionPaths();;
	        DefaultMutableTreeNode sNode;
	        Vector<Note> sNotes = new Vector<Note>();
	        for(int i=0; i<sPaths.length; i++) {
	        	sNode = ((DefaultMutableTreeNode)sPaths[i].getLastPathComponent());
	        	if(sNode.isLeaf()) 
	        		sNotes.addAll(dataBase.getNotesFromTag(((String)sNode.getUserObject())));
	        	
	        	else{
	        		sNotes.addAll(dataBase.getNotesFromCategory((String)sNode.getUserObject()));
	        	}
	        }
	        sNotesPanel.removeAll();
	        if(sNotes.size()==0) {
	            selectionPanel.revalidate();
	            selectionPanel.repaint();
	        	return;
	        }
	        GridBagConstraints c = new GridBagConstraints();
            c.weightx=1;
            c.weighty= 1.0 / (double)(sNotes.size());
            c.gridx=0;
            c.gridy=0;
            c.anchor = GridBagConstraints.PAGE_START;
            c.insets = new Insets(5,5,5,5);
            for(int i = 0; i<(sNotes.size()); i++) {
            	NotePreview newNote = new NotePreview(sNotes.get(i));
               	sNotesPanel.add(newNote, c);
            	c.gridy++;
            	//System.out.println(i-1 +" notes added");
            }
            selectionPanel.revalidate();
            selectionPanel.repaint();  
	    }

		
		@Override
		public void mouseClicked(MouseEvent click) {
			if(click.getComponent().getClass().equals(NotePreview.class)) {
				Note note = ((NotePreview)click.getSource()).getNote();
				cNTextArea.setText(note.getContent());
				cNTag1.setText(note.getTags().get(0));
				cNTag2.setText(note.getTags().get(1));
				cNLabel.setText(""+note.getId());
				currNotePanel.revalidate();
				currNotePanel.repaint();
			}
			
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
	
		
	}
	
	private class NotePreview extends JPanel{
		private Note note;
		private JLabel noteLabel;
		
		public NotePreview(Note n) {
			note = new Note(n);
			noteLabel = new JLabel("<html>"+note.getContent()+"</html>");
			noteLabel.setPreferredSize(new Dimension(255,130));
			this.setLayout(new GridBagLayout());
			//this.setPreferredSize(new Dimension(265,140));
			this.setBackground(Color.white);
			GridBagConstraints nLConstraints  = new GridBagConstraints();
			nLConstraints.insets = new Insets(5,5,5,5);
			nLConstraints.anchor = nLConstraints.CENTER;
			this.addMouseListener(bListener);
			this.add(noteLabel, nLConstraints);
		}
		public Note getNote() {
			return note;
		}
		public String getContent() {
			return note.getContent();
		}
	}
	
	
}
