package basicNotes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;
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
 * [X] 6. Determine the nature of the double-click requirement
 * [X] 7. Allow a category to be selected
 * . . .
 * and so on and so forth
 * . . .
 * [X] 8. Allow categories in dataBase to have a greater number of tags
 * [X] 9. Allow adding of categories
 * [X] 10. Allow adding of tags
 * [X] 11. Require tags to be sorted alphabetically within notes and categories both
 * 12. Union and intersection
 * 13. Adjustable size!
 * . . .
 * [X] 9. Move on to the currentNote panel
 * . . .
 * m-1. Make the NotePreviews more [X] uniform and []visually appealing
 * [X] m. Organize the formatting and sizing of the app to be uniform across and between panels
 * . . .
 * Improve, improve, improve!
 */


public class BasicNotesApp {
	BasicNotesDB dataBase;
	Vector<String> categories;
	
	//categoryPanel components
	JPanel intCatPanel;
	JScrollPane catScrollPane;
	JPanel extCatPanel;
	JTree cTree;
	DefaultMutableTreeNode top;
	DefaultTreeModel cTModel;
	
	//selectionPanel components
	/*JComboBox sCategory;
	JLabel sTag1, sTag2;*/
	//JPanel sTagsPanel;
	JLabel oneNote;
	JPanel selectionPanel, sNotesPanel;
	JScrollPane sScrollPane;
	
	//currNotePanel components
	JPanel cNTagPanel;
	JButton addTag;
	JScrollPane cNTScrollPane;
	JPanel cNTopPanel;
	JPanel cNBotPanel;
	JPanel cNMidPanel;
	JButton cNSave, cNNew;
	JTextArea cNTextArea;
	JPanel currNotePanel;
	
	Note currNote;
	Vector<String> curTags;
	
	//final components
	JPanel finalPanel;
	JFrame frame;
	
	//Deleting utilities
	JPopupMenu deleteMenu;
	Component selectedComp;
	TreePath selectedNode;
	
	//Action listeners and tree listeners
	basicListener bListener;
	
	//Final variables
	static int WIDTH;
	static int HEIGHT;
	final double CATWEIGHT=0.2;
	final double SELWEIGHT=0.3;
	final double CURWEIGHT=0.5;
	
	public BasicNotesApp(BasicNotesDB db){
		JFrame f = new JFrame();
		WIDTH = Integer.parseInt(JOptionPane.showInputDialog(f, "Requested app width", "1500"));
		HEIGHT = Integer.parseInt(JOptionPane.showInputDialog(f, "Requested app height", "1000"));
		curTags=new Vector<String>();
		dataBase = db;
		
		bListener = new basicListener();
		
		//categoryPanel components (cComponents) are now instantiated
		GridBagConstraints cConstraints = new GridBagConstraints();
		cConstraints.gridx = 0;
		cConstraints.gridy = 0;
		cConstraints.weightx=CATWEIGHT;
		cConstraints.weighty=1;
		cConstraints.fill=GridBagConstraints.BOTH;
		cConstraints.anchor=GridBagConstraints.LINE_START;
		//invoke createCategoryPanel() to create the categoryPanel
		createCategoryPanel();
		
		//selectionPanel components (sComponents) are now now instantiated
		GridBagConstraints sPConstraints = new GridBagConstraints();
		sPConstraints.gridx = 1;
		sPConstraints.gridy = 0;
		sPConstraints.weightx=SELWEIGHT;
		sPConstraints.weighty=1;
		sPConstraints.fill=GridBagConstraints.BOTH;
		sPConstraints.anchor=GridBagConstraints.CENTER;
		//invoke createSelectionPanel() to create the selectionPanel
		createSelectionPanel();
				
		//currNotePanel components (cNCompoonents) are now instantiated
		GridBagConstraints cNPConstraints = new GridBagConstraints();
		cNPConstraints.gridx = 2;
		cNPConstraints.gridy = 0;
		cNPConstraints.weightx=CURWEIGHT;
		cNPConstraints.weighty=1;
		cNPConstraints.fill=GridBagConstraints.BOTH;
		//invoke createCurrentNotePanel() to create the currNotePanel
		createCurrentNotePanel();
		
		createDeleteMenu();
		
		finalPanel = new JPanel(new GridBagLayout());
		extCatPanel.setPreferredSize(new Dimension((int)(CATWEIGHT*WIDTH), HEIGHT));
		extCatPanel.setBackground(Color.DARK_GRAY);
		finalPanel.add(extCatPanel, cConstraints);
		selectionPanel.setPreferredSize(new Dimension((int)(SELWEIGHT*WIDTH), HEIGHT));
		selectionPanel.setBackground(Color.DARK_GRAY);
		finalPanel.add(selectionPanel, sPConstraints);
		currNotePanel.setPreferredSize(new Dimension((int)(CURWEIGHT*WIDTH), HEIGHT));
		currNotePanel.setBackground(Color.DARK_GRAY);
		finalPanel.add(currNotePanel, cNPConstraints);
	    GridBagConstraints fC = new GridBagConstraints();
	    fC.weightx=1;
	    fC.weighty=1;
	    fC.fill=GridBagConstraints.BOTH;
	    fC.anchor=GridBagConstraints.CENTER;
		
		//Final JFrame component
		frame = new JFrame();
	    frame.setTitle("Basic Notes");
	    frame.setLayout(new GridBagLayout());
	    frame.add(finalPanel, fC); 
	    frame.setSize(new Dimension(WIDTH, HEIGHT));
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setLocationRelativeTo(null);
	    frame.setResizable(false);
	    frame.setVisible(true);
		
	}
	/**
	 * creates JPanel categoryPanel and related components
	 */
	private void createCategoryPanel() {
		intCatPanel = new JPanel(new GridBagLayout());
		createCTreeNodes();
		intCatPanel.setBackground(Color.white);
		catScrollPane = new JScrollPane(intCatPanel);
		catScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		GridBagConstraints c = new GridBagConstraints();
		c.fill=GridBagConstraints.BOTH;
		c.weightx=1;
		c.weighty=1;
		c.anchor=GridBagConstraints.FIRST_LINE_START;
		extCatPanel = new JPanel(new GridBagLayout());
		extCatPanel.setBackground(Color.LIGHT_GRAY);
		extCatPanel.add(catScrollPane, c);
	}
	
	/**
	 * creates JPanel selectionPanel and related components
	 */
	private void createSelectionPanel() {
		selectionPanel = new JPanel(new GridBagLayout());
		sNotesPanel = new JPanel();
		sNotesPanel.setLayout(new BoxLayout(sNotesPanel, BoxLayout.PAGE_AXIS));
		sNotesPanel.setBackground(Color.DARK_GRAY);
		sScrollPane = new JScrollPane(sNotesPanel);
		sScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		sScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		GridBagConstraints s = new GridBagConstraints();
		s.weightx=1;
		s.weighty=1;
		s.fill = GridBagConstraints.BOTH;
		s.anchor=GridBagConstraints.FIRST_LINE_START;
		selectionPanel.add(sScrollPane, s);
		sScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
		selectionPanel.setBackground(Color.white);
	}
	
	/**
	 * creates JPanel currNotePanel and related components
	 */
	private void createCurrentNotePanel() {
		createCNTop();
		GridBagConstraints top = new GridBagConstraints();
		top.weightx=1;
		top.weighty=0.03;
		top.gridx=0;
		top.gridy=0;
		top.fill=GridBagConstraints.BOTH;
		cNTopPanel.setPreferredSize(new Dimension(WIDTH, (int)(HEIGHT*top.weighty)));
		
		createCNBot();
		GridBagConstraints bot = new GridBagConstraints();
		bot.weightx=1;
		bot.weighty=0.02;
		bot.gridx=0;
		bot.gridy=2;
		bot.fill=GridBagConstraints.BOTH;
		cNBotPanel.setPreferredSize(new Dimension(WIDTH, (int)(HEIGHT*bot.weighty)));
		
		createCNMid();
	    GridBagConstraints mid = new GridBagConstraints();
	    mid.weightx=1;
	    mid.weighty=0.95;
	    mid.gridx=0;
	    mid.gridy=1;
	    mid.fill=GridBagConstraints.BOTH;
	    cNMidPanel.setPreferredSize(new Dimension(WIDTH, (int)(HEIGHT*mid.weighty)));
	    
		currNotePanel = new JPanel(new GridBagLayout());
		currNotePanel.add(cNTopPanel, top);
		currNotePanel.add(cNMidPanel, mid);
		currNotePanel.add(cNBotPanel, bot);
	}
	
	/**
	 * Creates the topmost section of currNotePanel
	 */
	private void createCNTop() {
		cNTagPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		cNTagPanel.setBackground(Color.DARK_GRAY);

		cNTScrollPane = new JScrollPane(cNTagPanel);
		cNTScrollPane.setBackground(Color.DARK_GRAY);
		cNTopPanel = new JPanel(new BorderLayout());
		cNTopPanel.add(cNTScrollPane, BorderLayout.CENTER);
		cNTopPanel.setOpaque(false);
	}
	
	/**
	 * Creates middle section of currNotePanel
	 */
	private void createCNMid() {
		cNTextArea = new JTextArea();
	    cNTextArea.setText("Hello World");
	    cNTextArea.setLineWrap(true);
	    cNTextArea.setWrapStyleWord(true);
	    cNTextArea.setMargin(new Insets(5, 5, 5,5));
	    Font f =cNTextArea.getFont();
	    cNTextArea.setFont(new Font(f.getName(), f.getStyle(), f.getSize()+5));
	    GridBagConstraints cNT = new GridBagConstraints();
	    cNT.weightx=1;
	    cNT.weighty=1;
	    cNT.fill=GridBagConstraints.BOTH;
	    
	    cNMidPanel=new JPanel(new GridBagLayout());
	    cNMidPanel.add(new JScrollPane(cNTextArea), cNT);
	}
	
	/**
	 * Creates bottom section of currNotePanel
	 */
	private void createCNBot() {
		cNBotPanel = new JPanel(new GridBagLayout());
		cNSave = new JButton("Save Note to Database");
		cNSave.addActionListener(bListener);
		GridBagConstraints save = new GridBagConstraints();
		save.anchor=GridBagConstraints.LINE_START;
		save.insets=new Insets(10,0,10,0);
		save.gridx=1;
		save.weighty=1;
		save.weightx=0.9;
		cNNew = new JButton("Create New Note");
		cNNew.addActionListener(bListener);
		GridBagConstraints newB = new GridBagConstraints();
		newB.gridx=0;
		newB.anchor=GridBagConstraints.CENTER;
		newB.insets=new Insets(10,0,10,0);
		newB.weighty=1;
		newB.weightx=0.1;
		addTag = new JButton("Add");
		addTag.addActionListener(bListener);
		cNBotPanel.add(cNNew, newB);
		cNBotPanel.add(cNSave,save);
		cNBotPanel.setBackground(Color.DARK_GRAY);
	}
	
	/**
	 * instantiates the DeleteMenu
	 */
	private void createDeleteMenu() {
		deleteMenu = new JPopupMenu("Delete");
		JMenuItem deleteItem = new JMenuItem("Delete");
		deleteItem.setActionCommand("Delete");
		deleteItem.addActionListener(bListener);
		deleteMenu.add(deleteItem);
	}
	
	
	/**
	 * Populates the top node of cTree with categories and tags from dataBase
	 * 
	 * @param top is the top node for JTree cTree
	 */
	private void createCTreeNodes() {
		top = new DefaultMutableTreeNode("delete");
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
	    		if(tags.get(j)==null)
	    			break;
	    		tag = new DefaultMutableTreeNode(tags.get(j));
	    		category.add(tag);
	    	}
	    	tag = new DefaultMutableTreeNode("Add tag");
	    	category.add(tag);
	    }
	    category = new DefaultMutableTreeNode("Add category");
	    top.add(category);
	    
		cTModel = new DefaultTreeModel(top);
		cTree = new JTree(cTModel);
		cTree.addMouseListener(bListener);
		Font f = cTree.getFont();
		cTree.setFont(new Font(f.getName(), f.getStyle(), f.getSize()+5));
		cTree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
		cTree.addTreeSelectionListener(bListener);
		cTree.setRootVisible(false);
		GridBagConstraints cTConstraints = new GridBagConstraints();
		cTConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
		cTConstraints.insets=new Insets(10,10,0,0);
		cTConstraints.weightx=1;
		cTConstraints.weighty=1;
		intCatPanel.removeAll();
		intCatPanel.add(cTree, cTConstraints);
	}
	
	/**
	 * Add new tag to the tree and to the database
	 * @param tag - the tag to be added to the tree and database
	 */
	private void addCatTag(String tag, TreePath sPath) {
		if(tag==null)
			return;
		
		DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)sPath.getParentPath().getLastPathComponent();
		String category = sPath.getParentPath().getLastPathComponent().toString();
		//get the newly sorted tags list
		Vector<String> tags = dataBase.getCategoryTags(category);
		tags = insertTag(tags, tag);
		//INEFFICIENT - find tag index in tags
		int i = tags.indexOf(tag);
		DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(tag);
		cTModel.insertNodeInto(childNode, parentNode, i);
		dataBase.updateCategoryTags(category, tags);
	}
	
	/**
	 * Add new category to the tree and to the database
	 * @param cat - the category to be added to the tree and database
	 */
	private void addCategory(String cat, TreePath sPath) {
		if(cat==null)
			return;
		DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode)sPath.getParentPath().getLastPathComponent();
		DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(cat);
		DefaultMutableTreeNode addTag = new DefaultMutableTreeNode("Add tag");
		cTModel.insertNodeInto(childNode, rootNode, rootNode.getChildCount()-1);
		cTModel.insertNodeInto(addTag, childNode, childNode.getChildCount());
		dataBase.addCategory(cat);
	}
	
	/**
	 * Returns whether this tag is present in the categories table
	 * @param tag - the tag to check
	 * @return true if in table, false if not
	 */
	private boolean isInCatTags(String tag) {
		Vector<String> tags = dataBase.getAllTags();
		for(int i=0; i<tags.size(); i++) {
			if(tags.get(i).compareTo(tag)==0)
				return true;
		}
		return false;
	}
	
	/**
	 * Using linked list and binary search, insert the newTag alphabetically
	 * @param n - the note whose tags to alter
	 * @param newTag - the new tag to insert
	 * @return tags - a vector of sorted tags where newTag has been inserted
	 */
	private Vector<String> insertTag(Vector<String> ts, String newTag) {
		LinkedList<String> tags = new LinkedList<String>(ts);
		String s= null;
		while(tags.remove(s));

		//if size is 0, add newTag
		if(tags.size()==0) {
			tags.add(newTag);
		}
		//if size is 1, add newTag to first or last on compare
		else if(tags.size()==1) {
			if(tags.getFirst().compareTo(newTag)<0)
				tags.addLast(newTag);
			else if(tags.getFirst().compareTo(newTag)>0)
				tags.addFirst(newTag);
		}
		else {
			//else do a binary search insert on the linked list
			int start = 0;
			int last = tags.size()-1;
			int prev;
			int next;
			boolean done=(start==last);
			while(!done) {					
				prev=(start+last)/2;
				next=prev+1;
				if(tags.get(prev).compareTo(newTag)<0) {
					if(tags.get(next).compareTo(newTag)>0) {
						tags.add(next, newTag);
						done=true;
					}
					//if at end
					else if(next==last) {
						tags.add(next+1,newTag);
						done=true;
					}
					else
						start=next;
				}
				else {
					if(start==0) {
						tags.add(0, newTag);
						done=true;
					}
					else
						last=prev;
				}
			}
			
		}
		
		return(new Vector<String>(tags));
	}
	
	/**
	 * Populate selectionPanel with the Notes in the Vector<Note> sNotes
	 * @param sNotes - a Vector<Note> of notes to add to the selectionPanel
	 */
	private void populateSNotes(Vector<String> tags) {
		Vector<Note> notes = new Vector<Note>();
		for(int i=0; i<tags.size(); i++)
			notes.addAll(dataBase.getNotesFromTag(tags.get(i)));
		notes= removeDuplicates(notes);
        sNotesPanel.removeAll();
        if(notes.size()==0) {
            selectionPanel.revalidate();
            selectionPanel.repaint();
        	return;
        }
        for(int i = 0; i<(notes.size()); i++) {
        	JPanel wrapper = new JPanel(new GridBagLayout());
        	wrapper.setBackground(Color.DARK_GRAY);
        	GridBagConstraints c = new GridBagConstraints();
        	c.insets= new Insets(5,0,5,5);
        	c.weightx=1;
        	c.weighty=1;
        	c.fill=GridBagConstraints.BOTH;
        	c.anchor=GridBagConstraints.LINE_START;
        	
        	NotePreview newNote = new NotePreview(notes.get(i));
        	wrapper.add(newNote,c);
        	wrapper.setMaximumSize(new Dimension((int)(WIDTH*SELWEIGHT)-95, newNote.getPreferredSize().height+10));
        	wrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        	//sNotesPanel.add(Box.createHorizontalGlue());
           	sNotesPanel.add(wrapper);
        	//System.out.println(i-1 +" notes added");
        }
        selectionPanel.revalidate();
        selectionPanel.repaint();  
	}
	
	/**
	 * Removes all duplicate notes from the vector
	 * @param notes - Vector<Note> to be checked
	 * @return - Vector<Note> removed of any duplicates
	 */
	private Vector<Note> removeDuplicates(Vector<Note> notes){
		Vector<Integer> ids = new Vector<Integer>();
		for(int i=0; i<notes.size(); i++) {
			boolean unique=true;
			for(int j=0; j<ids.size(); j++) {
				if(ids.get(j)==notes.get(i).getId()) {
					notes.remove(i);
					i--;
					unique=false;
					break;
				}
			}
			if(unique)
				ids.add(notes.get(i).getId());
		}
		return notes;
	}
	
	/**
	 * Currently examines the node last selected and sets the JLabel oneNote to be the text of the first note
	 * with the corresponding tag (of the selected node)
	 */
	private class basicListener implements TreeSelectionListener, MouseListener, ActionListener{
		/**
		 * //if a tree element has been selected
		 */
		@Override
		public void valueChanged(TreeSelectionEvent e) {

			
	        TreePath[] sPaths = cTree.getSelectionPaths();
	        if(sPaths==null)
	        	return;
	        
	        DefaultMutableTreeNode sNode;
	        curTags.clear();
	        for(int i=0; i<sPaths.length; i++) {
	        	sNode = ((DefaultMutableTreeNode)sPaths[i].getLastPathComponent());
	        	if(sNode.isLeaf()) {
	        		if(((String)sNode.getUserObject()).equals("Add tag")) {
	        			JFrame f = new JFrame();
	        			addCatTag(JOptionPane.showInputDialog(f, "Enter tag name"), cTree.getSelectionPath());
	        		}
	        		else if(((String)sNode.getUserObject()).equals("Add category")) {
	        			JFrame f = new JFrame();
	        			addCategory(JOptionPane.showInputDialog(f, "Enter category name"), cTree.getSelectionPath());
	        		}
	        		else {
	        			curTags.add((String)sNode.getUserObject());
	        			//sNotes.addAll(dataBase.getNotesFromTag(((String)sNode.getUserObject())));
	        		}
	        	}
	        	
	        	else{
	        		curTags.addAll(dataBase.getCategoryTags((String)sNode.getUserObject()));
	        		//sNotes.addAll(dataBase.getNotesFromCategory((String)sNode.getUserObject()));
	        	}
	        }
	        populateSNotes(curTags);
	    }

		/**
		 * If an action has been performed (button pressed)
		 * Currently handles cNNew, cNSave, addTag
		 */
		public void actionPerformed(ActionEvent e) {
			//For cNNew button
			if(e.getSource().equals(cNNew)) {
				Note newNote = new Note();
				currNote = newNote;
				populateCNPanel(newNote);
			}
			//For cNSave button
			else if(e.getSource().equals(cNSave)) {
				Vector<String> tags = currNote.getTags();
				boolean empty=true;
				for(int i=0; i<tags.size(); i++) 
					if(tags.get(i)!=null)
						empty=false;
				
				if(empty) {
					JFrame f = new JFrame();
					JOptionPane.showMessageDialog(f, "Add a tag before saving");
				}
				
				else if(currNote.getId()==-1) {
					currNote.setContent(cNTextArea.getText());
					currNote.setId(dataBase.publishNote(currNote));
				}
				
				else {
					currNote.setContent(cNTextArea.getText());
					dataBase.updateNote(currNote);
				}
				populateSNotes(curTags);
			}
			//For addTag button
			else if(e.getSource().equals(addTag)) {
				JFrame f = new JFrame();
    			String tag = (JOptionPane.showInputDialog(f, "Enter tag name"));
    			if(tag==null)
    				return;
    			//Ensure the tag is in the categories table, too
    			if(!isInCatTags(tag)) {
    				JOptionPane.showMessageDialog(f, "Tag not in categories");
    				return;
    			}
    			Vector<String> tags = insertTag(currNote.getTags(),tag);
    			currNote.setTags(tags);
    			populateCNTags(tags);
			}
			//For deleteItem
			else if(e.getActionCommand().compareTo("Delete")==0) {
				if(selectedComp.getClass().equals(JTree.class))
					deleteTreeComponent();
				else if(selectedComp.getClass().equals(NotePreview.class))
					deleteNotePreview();
				else if(selectedComp.getClass().equals(JLabel.class))
					deleteNoteTag();
			}
		}
		
		/**
		 * Delete the tag or category (and its corresponding tags) according to selectedComp
		 */
		private void deleteTreeComponent() {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)selectedNode.getLastPathComponent();
			DefaultMutableTreeNode parent = (DefaultMutableTreeNode)selectedNode.getParentPath().getLastPathComponent();
			JFrame f = new JFrame();
			Vector<Note> notes = new Vector<Note>();
			Vector<String> tags = new Vector<String>();
			if(node.isLeaf()) {
				String tag = node.toString();
				String cat = parent.toString();
				Vector<String> ts = dataBase.getCategoryTags(cat);
				if(JOptionPane.showConfirmDialog(f, ("Delete tag "+tag+"?"))!=0) 
					return;
				notes.addAll(dataBase.getNotesFromTag(tag));
				tags.add(tag);
				ts.remove(tag);
				//update the category;
				dataBase.updateCategoryTags(cat, ts);
				//update the tree
				cTModel.removeNodeFromParent(node);
				
			}
			else {
				String cat = node.toString();
				if(JOptionPane.showConfirmDialog(f, ("Delete category "+cat+"?"))!=0) 
					return;
				tags.addAll(dataBase.getCategoryTags(cat));
				notes.addAll(dataBase.getNotesFromCategory(cat));
				//delete the category
				dataBase.deleteCategory(cat);
				//update the tree
				for(int i=node.getChildCount()-1; i>=0; i--) {
					cTModel.removeNodeFromParent((MutableTreeNode)node.getChildAt(i));
				}
			}
			//for every note with this any of these tags, remove these tags and update the note
			for(int i=0; i<notes.size(); i++) {
				Note n = notes.get(i);
				Vector<String> ts = n.getTags();
				ts.removeAll(tags);
				n.setTags(ts);
				dataBase.updateNote(n);
			}
			//update the node
			top.removeAllChildren();
			
			//update curTags
			createCTreeNodes();
			extCatPanel.revalidate();
			extCatPanel.repaint();
			curTags.removeAll(tags);
			populateSNotes(curTags);
			if(currNote!=null)
				currNote=dataBase.getNoteFromId(currNote.getId());
			populateCNPanel(currNote);
			
			
			
		}
		/**
		 * Delete the notePreview corresponding to selectedComp
		 */
		private void deleteNotePreview(){
			JFrame f = new JFrame();
			if(JOptionPane.showConfirmDialog(f, "Delete this note?")!=0) 
				return;
			NotePreview sel = (NotePreview)selectedComp;
			dataBase.deleteNote(sel.getNote().getId());
			populateSNotes(curTags);
			populateCNPanel(null);
		}
		
		/**
		 * Delete the selected tag corresponding to selectedComp
		 */
		private void deleteNoteTag() {
			JLabel tag = (JLabel)selectedComp;
			String t = tag.getText();
			Vector<String> v= currNote.getTags();
			v.remove(t);
			currNote.setTags(v);
			populateCNTags(currNote.getTags());
		}
		
		/**
		 * Populate the currentNote panel with information from note n
		 * @param n - the note to populate currNotePanel with
		 */
		private void populateCNPanel(Note n) {
			currNote = n;
			if(n==null) {
				cNTextArea.setText("");
				cNTagPanel.removeAll();
				currNotePanel.revalidate();
				currNotePanel.repaint();
			}
			else {
				cNTextArea.setText(n.getContent());
				Vector<String> tags = n.getTags();
				populateCNTags(tags);
			}
		}
		
		private void populateCNTags (Vector<String> tags) {
			cNTagPanel.removeAll();
			//JPanel cNTags = new JPanel(new Flow)
			GridBagConstraints t = new GridBagConstraints();
			t.insets=new Insets(5,5,5,5);
			t.weightx=1;
			t.weighty=1;
			for(int i=0; i<tags.size(); i++) {
				JLabel cNTag = new JLabel(tags.get(i));
				JPanel cNPanel = new JPanel(new GridBagLayout());
				cNTag.addMouseListener(bListener);
				t.fill=GridBagConstraints.BOTH;
				t.anchor=GridBagConstraints.CENTER;
				Font font = cNTag.getFont();
				cNTag.setFont(new Font(font.getName(), font.getStyle(), font.getSize()+5));
				cNPanel.setBackground(Color.white);
				cNPanel.add(cNTag,t);
				cNTagPanel.add(cNPanel);
			}
			cNTagPanel.add(addTag);
			currNotePanel.revalidate();
			currNotePanel.repaint();
		}
		/**
		 * If there has been a mouseClick event (functioning for NotePreview classes)
		 */
		@Override
		public void mouseClicked(MouseEvent click) {
			selectedComp= click.getComponent();
			if(click.getButton()==MouseEvent.BUTTON3)
				rightClick(click);
			else if(click.getComponent().getClass().equals(NotePreview.class)) {
				Note note = ((NotePreview)click.getSource()).getNote();
				populateCNPanel(note);
			}
			
		}
		
		private void rightClick(MouseEvent click) {
			if(selectedComp.getClass().equals(JTree.class)) {
				selectedNode= cTree.getClosestPathForLocation(click.getX(), click.getY());
				String node = selectedNode.getLastPathComponent().toString();
				if(!(node.compareTo("Add tag")==0||node.compareTo("Add category")==0))
					deleteMenu.show(selectedComp, click.getX(), click.getY());
			}
			else
				deleteMenu.show(selectedComp, click.getX(), click.getY());
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
			noteLabel.setPreferredSize(new Dimension((int)(SELWEIGHT*BasicNotesApp.WIDTH),(int)(BasicNotesApp.HEIGHT/8)));
			this.setLayout(new GridBagLayout());
			this.setBackground(Color.white);
			GridBagConstraints nLConstraints  = new GridBagConstraints();
			nLConstraints.insets = new Insets(5,5,5,5);
			nLConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
			nLConstraints.weightx=1;
			nLConstraints.weighty=1;
			nLConstraints.fill=GridBagConstraints.BOTH;
			this.addMouseListener(bListener);
			this.add(deleteMenu);
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
