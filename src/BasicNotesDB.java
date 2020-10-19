package basicNotes;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class BasicNotesDB {
	private int catTags;
	private int noteTags;
	private String conn;
	private String selectNoteSQL;
	private String insertCatSQL;
	private String insertNoteSQL;
	private String updateNoteTagsSQL;
	private String updateNoteSQL;
	private String selectCatTagsSQL;
	private String selectAllTagsSQL;
	private String updateCatTagsSQL;
	private String selectNoteFromTagSQL;
	
	//------------------------------Private methods-----------------------------
	
	//---------------------------Construction methods---------------------------
	
	/**
	 * Gets the current number of tag columns in both tables and sets catTags and noteTags accordingly
	 */
	private void getCurrTags() {
		//query current number of tag columns in both tables
		String sql = "SELECT tags FROM taginfo";
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            
            // loop through the result set to get current number of tags for cat and notes
        	int i=0;
            while (rs.next()) {
            	if(i==0) {
            		catTags = (rs.getInt("tags"));
            	}
            	else if(i==1) {
            		noteTags = (rs.getInt("tags"));
            	}
            	i++;
            }
            //System.out.println("here"+catTags);
            conn.commit();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
	}
	
	//---------------------------SQL Statement Update Methods---------------------------
	
	private void setSelectNoteSQL() {
		//Create the SQL string for selecting a new note
		String c1 = "Select id, content, ";
		for(int i=0; i<noteTags; i++) {
			if(i==0) 
				c1+=("tag1");
			else if(i==noteTags-1) {
				c1+=(", tag"+(i+1)+" from notes where id = ?");
			}
			else {
				c1+=(", tag"+(i+1));
			}
		}
		selectNoteSQL=c1;
	}
	/**
	 * sets the insert SQL strings for categories to the appropriate number of tags in each table
	 */
	private void setCatInsertSQL() {
		//Create the SQL string for inserting a new category
		String c1 = "INSERT INTO categories(name,tags,";
		String c2 = ") VALUES(?,?,";
		for(int i=0; i<catTags; i++) {
			if(i==0) {
				c1+=("tag1");
				c2+=("?");
			}
			else if(i==catTags-1) {
				c1+=(",tag"+catTags);
				c2+=",?)";
			}
			else {
				c1+=(",tag"+(i+1));
				c2+=",?";
			}
		}
		insertCatSQL=c1+c2;
	}
	/**
	 * sets the insert SQL strings for notes to the appropriate number of tags in each table
	 */
	private void setNoteInsertSQL() {
		//Create the SQL string for inserting a new note
		String n1 = "INSERT INTO notes(id,content,tags,";
		String n2 = ") VALUES(?,?,?,";
		for(int i=0; i<noteTags; i++) {
			if(i==0) {
				n1+=("tag1");
				n2+=("?");
			}
			else if(i==noteTags-1) {
				n1+=(",tag"+noteTags);
				n2+=",?)";
			}
			else {
				n1+=(",tag"+(i+1));
				n2+=",?";
			}
		}
		insertNoteSQL=n1+n2;
	}
	
	/**
	 * sets the update SQL string for note tags to the appropriate number of tags in each table
	 */
	private void setUpdateNoteTagsSQL() {
		//Create the SQL string for updating a note
		String c1 = "UPDATE notes SET tags = ?, ";
		for(int i=0; i<noteTags; i++) {
			if(i==0) {
				c1+=("tag1 = ? ");
			}
			else if(i==noteTags-1) {
				c1+=(", tag"+noteTags+" = ? WHERE id = ?");
			}
			else {
				c1+=(", tag"+(i+1)+" = ? ");
			}
		}
		updateNoteTagsSQL=c1;
	}
	
	/**
	 * sets the update SQL string for notes to the appropriate number of tags in each table
	 */
	private void setUpdateNoteSQL() {
		//Create the sql string for updating a note
		String c1 = "UPDATE notes SET content = ?, tags = ?, ";
		for(int i=0; i<noteTags; i++) {
			if(i==0) {
				c1+=("tag1 = ? ");
			}
			else if(i==noteTags-1) {
				c1+=(", tag"+noteTags+" = ? WHERE id = ?");
			}
			else {
				c1+=(", tag"+(i+1)+" = ? ");
			}
		}
		updateNoteSQL=c1;
	}
	
	/**
	 * sets the select SQL string for categories to the appropriate number of tags in the table
	 * may not work as intended where catTags==1
	 */
	private void setSelectCatTagsSQL() {
		//Create the SQL string for selecting tags from a category
		String c1 = "select ";
		for(int i=0; i<catTags; i++) {
			if(i==0) {
				c1+=("tag1");
			}
			else if(i==catTags-1) {
				c1+=(", tag"+(catTags)+" from categories where name = ?");
			}
			else {
				c1+=(", tag"+(i+1));
			}
		}
		selectCatTagsSQL=c1;
	}
	
	/**
	 * sets the SQL for selectAllTags according to proper number of tags in categories
	 */
	private void setSelectAllTagsSQL() {
		String c1 = "select ";
		for(int i=0; i<catTags; i++) {
			if(i==0) 
				c1+= "tag1";
			else if(i==catTags-1) 
				c1+= ", tag"+(i+1)+" from categories";
			else
				c1+= ", tag"+(i+1);
		}
		selectAllTagsSQL=c1;
	}
	
	/**
	 * sets the update category tags sql string for the appropriate number of tags in table
	 */
	private void setUpdateCatTagsSQL() {
		String c1 = "UPDATE categories set tags = ?, ";
		for(int i=0; i<catTags; i++) {
			if(i==0)
				c1+="tag1 = ?";
			else if(i==(catTags-1))
				c1+=", tag"+(catTags)+" = ? WHERE name = ?";
			else
				c1+=", tag"+(i+1)+" = ?";
		}
		updateCatTagsSQL=c1;
	}
	
	/**
	 * sets the select from tag SQL string for notes to the appropriate number of tags in the table
	 */
	private void setSelectNoteFromTagSQL() {
		//Create the SQL string for selecting notes from a tag
		String c1 = "select id, content, ";
		String c2 = " from notes where ";
		for(int i=0; i<noteTags; i++) {
			if(i==0) {
				c1+=("tag1");
			}
			else if(i==noteTags-1) {
				c1+=(", tag"+(noteTags));
			}
			else {
				c1+=(", tag"+(i+1));
			}
		}
		selectNoteFromTagSQL=c1+c2;
	}
	
	//---------------------------Essential DB methods---------------------------
	
	/**
     * Connect to the basicnotes.db database
     *
     * @return the Connection object
     */
    private Connection connect() {
        // SQLite connection string
        String url = conn;
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
            conn.setAutoCommit(false);
        } catch (SQLException e) {
        	JFrame f = new JFrame();
            JOptionPane.showMessageDialog(f, "Failed to connect to db");
        }
        return conn;
    }
    
    /**
     * Create the appropriate tables in basicnotes.db
     *
     * @param fileName the database file name
     */
	private void createNewTables() {
        
        // SQL statement for creating a new table -- categories
        String sql1 = "CREATE TABLE IF NOT EXISTS categories (\n"
                + "	name text PRIMARY KEY,\n"
        		+ " tags integer,\n"
                + "	tag1 text,\n"
                + "	tag2 text\n"
                + ");";
        // SQL statement for creating a new table -- notes
        String sql2 = "CREATE TABLE IF NOT EXISTS notes (\n"
        		+ "id integer PRIMARY KEY, \n"
        		+ "content text, \n"
        		+ "tags integer, \n"
        		+ "tag1 text, \n"
        		+ "tag2 text \n"
        		+ ");";
        // SQL statement for creating a new table -- tagsinfo
        String sql3 = "CREATE TABLE IF NOT EXISTS taginfo (\n"
        		+ "name text, \n"
        		+ "tags integer \n"
        		+ ");";
        try (Connection conn = this.connect();
                Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql1);
            stmt.execute(sql2);
            stmt.execute(sql3);
            conn.commit();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
	
	//---------------------------AlterTable methods---------------------------
	/**
	 * increases the number of tag columns in categories, adding new column(s) as "tag"+newMax
	 * @param newMax is how many tag columns should be in categories by method close
	 */
	private void incCategoryTags(int newMax) {
		//EDIT
		try (Connection conn = this.connect();
                Statement stmt = conn.createStatement()) {
			//for every int of difference between current max and newMax
			for(int i=catTags+1; i<=newMax; i++) {
				String sql = "ALTER TABLE categories ADD COLUMN tag"+newMax+" text";
	            // alter categories table
	            stmt.execute(sql);
			}
			conn.commit();
			conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
		//update the tagsinfo table accordingly
		updateTags(newMax, "categories");
		//Update the insert strings accordingly
		getCurrTags();
		setCatInsertSQL();
		setSelectAllTagsSQL();
		setSelectCatTagsSQL();
		setUpdateCatTagsSQL();
	}
	
	/**
	 * increases the number of tag columns in notes, adding new column(s) as "tag"+newMax
	 * @param newMax is the integer of the tag column(s) after the method executes
	 */
	private void incNotesTags(int newMax) {
		try (Connection conn = this.connect();
                Statement stmt = conn.createStatement()) {
			//for every int of difference between current max and newMax
			for(int i=noteTags+1; i<=newMax; i++) {
				String sql = "alter table notes add column tag"+i+" text";
	            // alter notes table
	            stmt.execute(sql);
			}
			conn.commit();
			conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
		//update the tagsinfo table accordingly
		this.updateTags(newMax, "notes");
		//Update the insert strings accordingly
		noteTags=newMax;
		setNoteInsertSQL();
		setUpdateNoteTagsSQL();
		setUpdateNoteSQL();
		setSelectNoteFromTagSQL();
		setSelectNoteSQL();
	}
	
	//---------------------------Private Categories methods---------------------------
	/**
     * Insert a new category into the categories table
     *
     * @param name is the name of the category to add
     * @param tags is a Vector<String> of the tags to add
     */
	private void insertCategory(String name, Vector<String> tags) {

        //if there are more tags in the vector than in the categories table
        if(tags.size()>this.catTags) {
        	incCategoryTags(tags.size());
        }
        //use the insertCatSQL and loop through the tags vector to insert new category
        String sql = insertCatSQL;
        try (Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, tags.size());
            for(int i=0; i<tags.size(); i++) {
                pstmt.setString(i+3, tags.get(i));
            }
            pstmt.executeUpdate();
            conn.commit();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * Update name of the category specified
     * 
     * @param name the current name of the category to update
     * @param newName the new name for the updated category
     */
    private void updateCategoryName(String name, String newName) {
    	String sql = "UPDATE categories SET name = ? where name = ?";
        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
        		pstmt.setString(1, newName);
            	pstmt.setString(2, name);
                pstmt.executeUpdate();
                conn.commit();
                conn.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
    }
    
    private int getNumCategoryTags(String name) {
    	String sql = "select tags from categories where name = ?";
    	int result=0;
        try (Connection conn = this.connect();
        		PreparedStatement pstmt = conn.prepareStatement(sql)) {
            	pstmt.setString(1, name);
            	ResultSet rs    = pstmt.executeQuery();
                // loop through the result set
                while (rs.next()) {
                	for(int i=0; i<catTags; i++) {
                		result=rs.getInt("tags");
                	}
                }                
               conn.commit();
               conn.close();
           } catch (SQLException e) {
               System.out.println(e.getMessage());
           }
    	return result;
    }

    /**
     * Delete the category table
     **/
    private void deleteCategories() {
    	String sql = "DROP TABLE IF EXISTS categories";
    	
    	try (Connection conn = this.connect();
    			Statement stmt = conn.createStatement()) {
    		stmt.execute(sql);
    		conn.commit();
    		conn.close();
    	}	catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * select all rows in the categories table -- vestigial method as of now
     */
    
    /*
    private void selectAllCategories(){
        String sql = "SELECT name, tags, tag1, tag2 FROM categories";
        
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            
            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getString("name") +  "\t" + 
                                   rs.getString("tag1") + "\t" +
                                   rs.getString("tag2"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    } */
    
  //---------------------------Private Notes methods---------------------------
	/**
     * Insert a new note into the notes table
     *
     * @param id
     * @param content
     * @param tag1
     * @param tag2
     */
    private void insertNote(int id, String content, Vector<String> tags) {
        //if there are more tags in the vector than in the notes table
        if(tags.size()>this.noteTags) {
        	incNotesTags(tags.size());
        }
        //use the insertNoteSQL and loop through the tags vector to insert new note
        String sql = insertNoteSQL;
        try (Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, content);
            pstmt.setInt(3, tags.size());
            for(int i=0; i<tags.size(); i++) 
                pstmt.setString(i+4, tags.get(i));
            
            pstmt.executeUpdate();
            conn.commit();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * Helper function for testNotes(), inserts a new Note according to its given parameters
     * @param content - content for the note
     * @param id - known id of the note
     * @param ts - Vector of tags for the note
     */
    private void addTestNote(String content, int id, Vector<String> ts) {
    	Vector<String> tags = new Vector<String>();
    	tags.add(ts.get(0));
    	Note n = new Note(content, id, new Vector<String>(tags));
    	this.publishNote(n);
    	for(int i=1; i<ts.size(); i++) {
    		tags.add(ts.get(i));
    		n = new Note(content, id, new Vector<String>(tags));
    		this.updateNote(n);
    	}
    }

	/**
     * Update tags of a note specified by the name
     *
     * @param id - the id of the note in the database
     * @param tags - a vector containing the tags for the updated note
     */
    private void updateNoteTags(int id, Vector<String> tags) {
    	//if there are more tags in the vector than in the notes table
        if(tags.size()>this.noteTags) {
        	incNotesTags(tags.size());
        }
        
        String sql = updateNoteTagsSQL;
        //use the insertNoteSQL and loop through the tags vector to update note
        try (Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
        	int i=0;
        	pstmt.setInt(1, tags.size());
        	while(i<tags.size()) {
        		pstmt.setString(i+2, tags.get(i));
        		i++;
        	}
        	pstmt.setInt(i+2, id);
            pstmt.executeUpdate();
            conn.commit();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * Update the selected note with the content in newContent
     * @param id - the id of the note to update
     * @param newContent - the content String with which to update the note
     */
    private void updateNoteContent(int id, String newContent) {
    	String sql = "UPDATE notes SET content = ? where id = ?";
        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
        		pstmt.setString(1, newContent);
            	pstmt.setInt(2, id);
                pstmt.executeUpdate();
                conn.commit();
                conn.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
    }
    
    /**
     * Return the number of tags in each note
     * @param id of the note being querried
     * @return the number of tags of the particular notes
     */
    private int getNumNoteTags(int id) {
    	String sql = "select tags from notes where id = ?";
    	int result=0;
        try (Connection conn = this.connect();
        		PreparedStatement pstmt = conn.prepareStatement(sql)) {
            	pstmt.setInt(1, id);
            	ResultSet rs    = pstmt.executeQuery();
                // loop through the result set
                while (rs.next()) {
                	for(int i=0; i<noteTags; i++) {
                		result=rs.getInt("tags");
                	}
                }                
               conn.commit();
               conn.close();
           } catch (SQLException e) {
               System.out.println(e.getMessage());
           }
    	return result;
    }
    
    /**
     * Return the notes in notes with tag 'tag' in col 'tagcol'
     * @param tag - the name of the tag being searched for
     * @param tagcol - the tagcol being searched
     */
    private Vector<Note> getNotesFromTag(String tag, String tagcol){
    	String sql = selectNoteFromTagSQL + tagcol + " = ?";
    	Vector<Note> results = new Vector<Note>();
    	try (Connection conn = this.connect();
        		PreparedStatement pstmt = conn.prepareStatement(sql)) {
            	pstmt.setString(1, tag);
            	ResultSet rs    = pstmt.executeQuery();
                // loop through the result set
                while (rs.next()) {
                	Vector<String> tags = new Vector<String>();
                	String content = rs.getString("content");
                	int id = rs.getInt("id");
                	for(int i=0; i<noteTags; i++) {
                		tags.add(rs.getString("tag"+(i+1)));
                	}
                	Note newN = new Note(content, id, tags);
                	results.add(newN);
                }                
               conn.commit();
               conn.close();
           } catch (SQLException e) {
               System.out.println(e.getMessage());
           }
    	return results;
    }

    /**
     * Clear the note table
     **/
    private void deleteNotes() {
    	String sql = "DROP TABLE IF EXISTS notes";
    	
    	try (Connection conn = this.connect();
    			Statement stmt = conn.createStatement()) {
    		stmt.execute(sql);
    		conn.commit();
    		conn.close();
    	}	catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * select all rows in the notes table -- currently vestigial method
     */
    /*
    private void selectAllNotes(){
        String sql = "SELECT id, note, tag1, tag2 FROM notes";
        
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            
            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getInt("id") +  "\t" + 
                				   rs.getString("note") +  "\t" +
                                   rs.getString("tag1") + "\t" +
                                   rs.getString("tag2"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }*/
    
    //---------------------------Private TagInfo methods---------------------------
    
    /**
     * Populates the taginfo table with default values 
     * (2 tags in both tables)
     */
	private void populateTagTable() {
		String sql = "INSERT INTO taginfo(name,tags) VALUES(?,?)";
        try (Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "categories");
            pstmt.setInt(2, 2);
            pstmt.executeUpdate();
            pstmt.setString(1, "notes");
            pstmt.setInt(2, 2);
            pstmt.executeUpdate();
            conn.commit();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
	}
	
	/**
	 * Updates the taginfo table for the relevant table
	 * @param tags - the new tags count for the relevant table
	 * @param name - the name with which to identify the relevant table
	 */
	private void updateTags(int tags, String name) {
    	String sql = "UPDATE taginfo SET tags = ? where name = ?";
        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
        		pstmt.setInt(1, tags);
            	pstmt.setString(2, name);
                pstmt.executeUpdate();
                conn.commit();
                conn.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
	}
	
	/**
	 * Deletes all info from the taginfo table -- drops taginfo
	 */
    private void deleteTagInfo() {
    	String sql = "DROP TABLE IF EXISTS taginfo";
    	
    	try (Connection conn = this.connect();
    			Statement stmt = conn.createStatement()) {
    		stmt.execute(sql);
    		conn.commit();
    		conn.close();
    	}	catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

	//---------------------------Public (General) Methods---------------------------
    
	/**
	 * Constructor
	 */
	public BasicNotesDB(String url, boolean tests, boolean reset){
		//delete tables, create new tables, and populate tagtable
		boolean firstRun=false;
		
		if(tests) 
			conn = url+"/testnotes.db";
		
		else 
			conn = url+"/mynotes.db";
		
		if(reset) 
			reset();
		
		createNewTables();	
		if(getCategoryNames().size()==0||reset)
			firstRun=true;
		
		if(firstRun) {
			populateTagTable();
		}
		
		getCurrTags();
		setCatInsertSQL();
		setNoteInsertSQL();
		setSelectAllTagsSQL();
		setUpdateNoteTagsSQL();
		setUpdateNoteSQL();
		setSelectCatTagsSQL();
		setUpdateCatTagsSQL();
		setSelectNoteFromTagSQL();
		setSelectNoteSQL();
		
		if(tests&&firstRun) {
			this.testCategories();
			this.testNotes();		
		}
	}
    
    /**
     * Deletes current tables and creates new, empty ones
     **/
    public void reset() {
    	deleteCategories();
    	deleteNotes();
    	deleteTagInfo();
    }
    
	//---------------------------Public Category Methods---------------------------
    
    /**
     * Returns a vector containing all category names in categories
     **/
    public Vector<String> getCategoryNames(){
        String sql = "SELECT name FROM categories";
        Vector<String> results = new Vector<String>();
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            
            // loop through the result set
            while (rs.next()) {
                results.add(rs.getString("name"));
            }
            conn.commit();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return results;
    }
    
    /**
     * Gets all tags in the categories table
     * @return Vector<String> containing all tags in the categories table
     */
    public Vector<String> getAllTags(){
    	String sql = selectAllTagsSQL;
    	Vector<String> results = new Vector<String>();
    	try( Connection conn = this.connect();
    			Statement stmt = conn.createStatement();
    			ResultSet rs = stmt.executeQuery(sql)) {
            // loop through the result set
            while (rs.next()) {
            	for(int i=0; i<catTags; i++) {
            		String tag = rs.getString("tag"+(i+1));
            		if(tag!=null)
            			results.add(tag);
            	}
            }
            conn.commit();
            conn.close();
    	} catch (SQLException e) {
    		System.out.println(e.getMessage());
    	}
    	return results;
    }
    
    /**
     * Returns a vector containing all tags from the given category
     * @param name name of the category
     * @return Vector<String> containing all tags in the category
     */
    public Vector<String> getCategoryTags(String name){
    	String sql = selectCatTagsSQL;
    	Vector<String> results = new Vector<String>();
        try (Connection conn = this.connect();
        		PreparedStatement pstmt = conn.prepareStatement(sql)) {
            	pstmt.setString(1, name);
            	ResultSet rs    = pstmt.executeQuery();
                // loop through the result set
                while (rs.next()) {
                	for(int i=0; i<catTags; i++) {
                		results.add(rs.getString("tag"+(i+1)));
                	}
                }                
               conn.commit();
               conn.close();
           } catch (SQLException e) {
               System.out.println(e.getMessage());
           }
    	return results;
    }
    
    /**
     * Adds a new category empty of tags
     * @param name - the name of the category to add
     */
    public void addCategory(String name) {
    	Vector<String> tags = new Vector<String>();
    	this.insertCategory(name, tags);
    }
    
    /**
     * Adds a new tag to an existing category in the database
     * @param category - the name of the category to be modified
     * @param tag - the tag to add to the category
     */
    public void addCategoryTag(String category, String tag) {
    	int tags = getNumCategoryTags(category);
        //if there will then be more tags than in the categories table
        if(tags+1>catTags) {
        	incCategoryTags(tags+1);
        }
        //execute update with this information
        String sql2 = "update categories set tag"+(tags+1)+"= ?, tags = ? where name = ?";
    	try (Connection conn = this.connect();
    		PreparedStatement pstmt2 = conn.prepareStatement(sql2)){
    		pstmt2.setString(1, tag);
    		pstmt2.setInt(2, (tags+1));
    		pstmt2.setString(3, category);
            pstmt2.executeUpdate();
            conn.commit();
            conn.close();
    	} catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * Delete a category specified by the name
     *
     * @param name
     */
    public void deleteCategory(String name) {
        String sql = "DELETE FROM categories WHERE name = ?";

        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setString(1, name);
            // execute the delete statement
            pstmt.executeUpdate();
            conn.commit();
            conn.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * Update tags of a category specified by the name
     *
     * @param name name of the category to update
     * @param tags is a Vector<String> of the tags to add
     */
    public void updateCategoryTags(String name, Vector<String> tags) {
    	//if there are more tags in the vector than in the categories table
        if(tags.size()>this.catTags) {
        	incCategoryTags(tags.size());
        }
        
        String sql = updateCatTagsSQL;
        //use the insertCatSQL and loop through the tags vector to update category
        try (Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
        	pstmt.setInt(1, tags.size());
        	int i=0;
        	while(tags.size()<catTags)
        		tags.add(null);
        	
        	while(i<catTags) {
        		String tag=tags.get(i);
        		pstmt.setString(i+2, tag);
        		i++;
        	}
        	pstmt.setString(i+2, name);
            pstmt.executeUpdate();
            conn.commit();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Creates test categories
     */
    public void testCategories() {
    	this.addCategory("Tips for Using");
    	Vector<String> tips = new Vector<String>();
    	tips.add("Category panel");
    	this.updateCategoryTags("Tips for Using", new Vector<String>(tips));
    	tips.add("Selection panel");
    	this.updateCategoryTags("Tips for Using", new Vector<String>(tips));
    	tips.add("Current note panel");
    	this.updateCategoryTags("Tips for Using", new Vector<String>(tips));
    	Vector<String> authors = new Vector<String>();
    	this.addCategory("Authors");
    	authors.add("Beauvoir");
    	updateCategoryTags("Authors", new Vector<String>(authors));
    	authors.add("Camus");
    	updateCategoryTags("Authors", new Vector<String>(authors));
    	authors.add("Emerson");
    	updateCategoryTags("Authors", new Vector<String>(authors));
    	authors.add("Freud");
    	updateCategoryTags("Authors", new Vector<String>(authors));
    	authors.add("Melville");
    	updateCategoryTags("Authors", new Vector<String>(authors));
    	authors.add("Nietzsche");
    	updateCategoryTags("Authors", new Vector<String>(authors));
    	authors.add("Rorty");
    	updateCategoryTags("Authors", new Vector<String>(authors));
    	authors.add("Sartre");
    	updateCategoryTags("Authors", new Vector<String>(authors));
    	authors.add("Steinbeck");
    	updateCategoryTags("Authors", new Vector<String>(authors));
    	Vector<String> content = new Vector<String>();
    	this.addCategory("Content");
    	content.add("Idea");
    	updateCategoryTags("Content", new Vector<String>(content));
    	content.add("Quote");
    	updateCategoryTags("Content", new Vector<String>(content));
    	content.add("Summary");
    	updateCategoryTags("Content", new Vector<String>(content));
    	content.add("Thought");
    	updateCategoryTags("Content", new Vector<String>(content));
    	Vector<String> genre = new Vector<String>();
    	this.addCategory("Medium");
    	genre.add("Literature");
    	updateCategoryTags("Medium", new Vector<String>(genre));
    	genre.add("Philosophy");
    	updateCategoryTags("Medium", new Vector<String>(genre));
    	genre.add("TV");
    	updateCategoryTags("Medium", new Vector<String>(genre));
    	this.addCategory("Course");
    	Vector<String> course = new Vector<String>();
    	course.add("Master Thinkers");
    	updateCategoryTags("Course", new Vector<String>(course));
    	course.add("Origin C. Thought");
    	updateCategoryTags("Course", new Vector<String>(course));
    }
    
  //---------------------------Public Notes Methods---------------------------
    
    /**
     * Inserts a new note into notes after creating an id
     * 
     * @param content - the content of the note to be inserted
     * @param tags - the Vector<String> of the tags to be inserted
     * @return id - returns the id of the published note
     */
    public int publishNote(Note n) {
    	String sql1 = "SELECT COUNT(*) FROM notes";
    	int id = 1;
    	while(this.getNoteFromId(id).getId()==id) {
    		id++;
    	}
    	/*
    	try (Connection conn = this.connect();
                Statement stmt  = conn.createStatement();
                ResultSet rs    = stmt.executeQuery(sql1)){
    		id += rs.getInt(1);
    		System.out.println(id);
            conn.commit();
            conn.close();
           } catch (SQLException e) {
               System.out.println(e.getMessage());
           }*/
    	insertNote(id, n.getContent(), n.getTags());
    	return id;
    }
    
    /**
     * Return the Note identified by the id
     * @param id- identification of the note
     * @return Note object for the note
     */
    public Note getNoteFromId(int idi) {
    	String sql = selectNoteSQL;
    	Note newN= new Note();
    	try (Connection conn = this.connect();
        		PreparedStatement pstmt = conn.prepareStatement(sql)) {
            	pstmt.setInt(1, idi);
            	ResultSet rs    = pstmt.executeQuery();
                // loop through the result set
                while (rs.next()) {
                	Vector<String> tags = new Vector<String>();
                	String content = rs.getString("content");
                	int id = rs.getInt("id");
                	for(int i=0; i<noteTags; i++) {
                		tags.add(rs.getString("tag"+(i+1)));
                	}
                	newN = new Note(content, id, tags);
                }                
               conn.commit();
               conn.close();
           } catch (SQLException e) {
               System.out.println(e.getMessage());
           }
    	return newN;
    }
    
    /**
     * Update the selected note with the content and tags
     * @param id - the id of the note to be updated
     * @param newContent - the new content to update
     * @param tags - the Vector<String> of tags to add to the updated note
     */
    public void updateNote(Note note) {
		Vector<String> tags = note.getTags();
    	//if there are more tags in the vector than in the notes table
        if(tags.size()>this.noteTags) {
        	incNotesTags(tags.size());
        }
        for(int i=tags.size(); i<noteTags; i++) 
        	tags.add(null);
        
        
    	String sql = updateNoteSQL;
        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
        		pstmt.setString(1, note.getContent());
        		pstmt.setInt(2, tags.size());
        		int i=0;
            	while(i<tags.size()) {
            		pstmt.setString(i+3, tags.get(i));
            		i++;
            	}
            	pstmt.setInt(i+3, note.getId());
                pstmt.executeUpdate();
                conn.commit();
                conn.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
    }
    
    /**
     * Returns all notes corresponding to the requested tag
     * @param tag String representing a tag in the dataBase
     * @return a vector of the Note class representation of every note with 'tag' amongst its tags
     * returns empty vector if no notes correspond to this tag
     */
    public Vector<Note> getNotesFromTag(String tag){
    	String sql = selectNoteFromTagSQL;
    	Vector<Note> results = new Vector<Note>();
    	for(int i=0; i<this.noteTags; i++) {
    		results.addAll(this.getNotesFromTag(tag, "tag"+(i+1)));
    	}
    	return results;
    }
    
    /**
     * Find all notes of all tags within a given category - calls getCategoryTags(cat) and getNotesFromTag() for all tags
     * 
     * @param cat is a string representing the category to pull notes corresponding to
     * @return A Vector<String> of all notes with a tag in the given category
     */
    public Vector<Note> getNotesFromCategory(String cat){
    	Vector<String> tags = new Vector<String>();
    	tags = getCategoryTags(cat);
    	Vector<Note> notes = new Vector<Note>();
    	for(int i=0; i<tags.size(); i++) {
    		notes.addAll(this.getNotesFromTag(tags.get(i)));
    	}
    	return notes;
    }
    
    /**
     * Adds a new tag to an existing category in the database
     * @param category - the name of the category to be modified
     * @param tag - the tag to add to the category
     */
    public void addNoteTag(int id, String tag) {
    	int tags = getNumNoteTags(id);
        //if there will then be more tags than in the notes table
        if(tags+1>noteTags) {
        	incNotesTags(tags+1);
        }
        //execute update with this information
        String sql2 = "update notes set tag"+(tags+1)+"= ?, tags = ? where id = ?";
    	try (Connection conn = this.connect();
    		PreparedStatement pstmt2 = conn.prepareStatement(sql2)){
    		pstmt2.setString(1, tag);
    		pstmt2.setInt(2, (tags+1));
    		pstmt2.setInt(3, id);
            pstmt2.executeUpdate();
            conn.commit();
            conn.close();
    	} catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * Delete a note specified by the id
     *
     * @param id
     */
    public void deleteNote(int id) {
        String sql = "DELETE FROM notes WHERE id = ?";

        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setInt(1, id);
            // execute the delete statement
            pstmt.executeUpdate();
            conn.commit();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public void testNotes() {
    	Vector<String> tags = new Vector<String>();
    	tags.add("Nietzsche");
    	tags.add("Origin C. Thought");
     	tags.add("Philosophy");
    	tags.add("Quote");
    	this.addTestNote("all concepts in which an entire process is semiotically summarized elude definition;"
    			+ " only that which has no history is definable", 1, tags);
    	tags = new Vector<String>();
    	tags.add("Camus");
    	tags.add("Philosophy");
    	tags.add("Quote");
    	this.addTestNote("Although 'The Myth of Sisyphus' poses mortal problems, it sums itself up for me as a lucid"
    			+ "invitation to live and to create, in the very midst of the desert.", 2, tags);
    	tags = new Vector<String>();
    	tags.add("Camus");
    	tags.add("Philosophy");
    	tags.add("Quote");
    	this.addTestNote("Each atom of that stone, each mineral flake of that night-filled mountain, in itself forms"
    			+ " a world. The struggle itself toward the heights is enough to fill a man's heart. One must imagine"
    			+ "Sisyphus happy.", 3, tags);
    	tags = new Vector<String>();
    	tags.add("Literature");
    	tags.add("Steinbeck");
    	tags.add("Quote");
    	this.addTestNote("The writers of today, even I, have a tendency to celebrate the destruction of the spirit"
    			+ " and god knows it is destroyed often enough. But the beacon thing is that sometimes it is not.", 4, tags);
    	tags = new Vector<String>();
    	tags.add("Literature");
    	tags.add("Steinbeck");
    	tags.add("Quote");
    	this.addTestNote("But it becomes important out of all proportion to its importance. And I suppose that is essential."
    			+ "The dunghill beetle must be convinced of the essential quality in rolling his ball of dung. . .",5,tags);
    	tags = new Vector<String>();
    	tags.add("Literature");
    	tags.add("Steinbeck");
    	tags.add("Quote");
    	this.addTestNote("Now this I must say and say right here and so sharply and so memorably that it will not be "
    			+ "forgotten in the rather terrible and disheartening things which are to come in this book; so that "
    			+ "although East of Eden is not Eden, it is not insuperably far away.",6,tags);
    	tags = new Vector<String>();
    	tags.add("Literature");
    	tags.add("Melville");
    	tags.add("Quote");
    	this.addTestNote("Now small fowls flew screaming over the yet yawning gulf; a sullen white surf beat against its"
    			+ " steep sides; then all collapsed, and the great shroud of the sea rolled on as it rolled five"
    			+ " thousand years ago.", 7, tags);
    	tags = new Vector<String>();
    	tags.add("Literature");
    	tags.add("Melville");
    	tags.add("Quote");
    	this.addTestNote(". . .all this to explain, would be to dive deeper than Ishmael can go. . .", 8, tags);
    	tags = new Vector<String>();
    	tags.add("Freud");
    	tags.add("Master Thinkers");
    	tags.add("Quote");
    	this.addTestNote("In writing this work I have discovered afresh the most banal truths", 9, tags);
    	tags = new Vector<String>();
    	tags.add("Freud");
    	tags.add("Master Thinkers");
    	tags.add("Quote");
    	this.addTestNote("One can hardly be wrong in concluding that the idea of life having a purpose stands"
    			+ " and falls with the religious system.", 10, tags);
    	tags = new Vector<String>();
    	tags.add("Freud");
    	tags.add("Master Thinkers");
    	tags.add("Quote");
    	this.addTestNote("And we may well heave a sigh of relief at the thought that it is nevertheless"
    			+ " vouchsafed to a few to salvage without effort from the whirlpool of their own feelings"
    			+ "the deepest truths, towards which the rest of us have to find our way through tormenting"
    			+ "uncertainty and with restless groping.", 11, tags);
    	tags = new Vector<String>();
    	tags.add("Master Thinkers");
    	tags.add("Nietzsche");
    	tags.add("Origin C. Thought");
     	tags.add("Philosophy");
    	tags.add("Quote");
    	this.addTestNote("The same impulse that calls art into existence, the complement and apotheoisis of existence"
    			+ ", also created the Olympian world with which the Hellenic 'will' held up a transfiguring mirror"
    			+ "to itself. Thus the gods provide a justification for the life of man by living it themselves", 12, tags);
    	tags = new Vector<String>();
    	tags.add("Nietzsche");
    	tags.add("Origin C. Thought");
     	tags.add("Philosophy");
    	tags.add("Quote");
    	this.addTestNote("Every great human being has a retroactive force: all history is again placed in the scales for his"
    			+ "sake, and a thousand secrets of the past crawl out of their hideouts--into his sun.", 13, tags);
    	tags = new Vector<String>();
    	tags.add("Master Thinkers");
     	tags.add("Philosophy");
    	tags.add("Quote");
    	tags.add("Rorty");
    	this.addTestNote("But if we could ever become reconciled to the idea that most of reality is indifferent"
    			+ " to our descriptions of it, and that the human self is created by the use of a vocabulary"
    			+ " rather than being adequately or inadequately expressed in a vocabulary, then we should at least"
    			+ " have assimilated what was true in the Romantic idea that truth is made rather than found.", 14, tags);
    	tags = new Vector<String>();
    	tags.add("Master Thinkers");
     	tags.add("Philosophy");
    	tags.add("Quote");
    	tags.add("Rorty");
    	this.addTestNote("Rather, he saw self-knowledge as self-creation. The process of coming to know onself, "
    			+ "confronting one's contingency, tracking one's causes home, is identical with the process of "
    			+ "inventing a new language--that is, thinking up some new metaphors", 15, tags);
    	tags = new Vector<String>();
    	tags.add("Master Thinkers");
     	tags.add("Philosophy");
    	tags.add("Quote");
    	tags.add("Rorty");
    	this.addTestNote("What ties Nietzsche and Freud together is this attempt -- the attempt to see a blind impress "
    			+ "as not unworthy of programming our lives or our poems.", 16, tags);
    	tags = new Vector<String>();
    	tags.add("Master Thinkers");
     	tags.add("Philosophy");
    	tags.add("Quote");
    	tags.add("Rorty");
    	this.addTestNote("liberal societies of our century have produced more and more people who are able to recognize"
    			+ " the contingency of the vocabulary in which they state their highest hopes -- the contingency of"
    			+ " their own consciences -- and yet have remained faithful to those consciences", 17, tags);
    	tags = new Vector<String>();
    	tags.add("Emerson");
    	tags.add("Master Thinkers");
     	tags.add("Philosophy");
    	tags.add("Quote");
    	this.addTestNote("The secret of the illusoriness is in the necessity of a succession of moods or objects. "
    			+ "Gladly we would anchor, but the anchorage is quicksand", 18, tags);
    	tags = new Vector<String>();
    	tags.add("Emerson");
    	tags.add("Master Thinkers");
     	tags.add("Philosophy");
    	tags.add("Quote");
      	this.addTestNote("I have learned that I cannot dispose of other people’s facts; but I possess such a key to "
      			+ "my own as persuades me, against all their denials, that they also have a key to theirs.", 19, tags);
    	tags = new Vector<String>();
    	tags.add("Emerson");
    	tags.add("Master Thinkers");
     	tags.add("Philosophy");
    	tags.add("Quote");
      	this.addTestNote("Thus inevitably does the universe wear our color, and every object fall successively into the "
      			+ "subject itself.", 20, tags);
    	tags = new Vector<String>();
    	tags.add("Emerson");
    	tags.add("Master Thinkers");
     	tags.add("Philosophy");
    	tags.add("Quote");
      	this.addTestNote("These roses under my window make no reference to former roses or to better ones; "
      			+ "they are for what they are", 21, tags);
    	tags = new Vector<String>();
    	tags.add("Beauvoir");
    	tags.add("Master Thinkers");
     	tags.add("Philosophy");
    	tags.add("Quote");
    	this.addTestNote("Man's gods are in such a faraway heaven that in truth, for him, there are no gods: "
    			+ "the little girl lives among gods with a human face.", 22, tags);
    	tags = new Vector<String>();
    	tags.add("Beauvoir");
    	tags.add("Master Thinkers");
     	tags.add("Philosophy");
    	tags.add("Quote");
    	this.addTestNote("It is a terrible frustration not to be able to imprint the movements of one's heart on"
    			+ " the face of the earth.", 23, tags);
    	tags = new Vector<String>();
    	tags.add("Literature");
    	tags.add("Quote");
     	tags.add("Sartre");
    	this.addTestNote("Through layers and layers of existence, it veils itself, thin and firm, and when you"
    			+ " want to seize it, you find only existants, you butt against existants devoid of sense.", 24, tags);
    	tags = new Vector<String>();
    	tags.add("Literature");
    	tags.add("Quote");
     	tags.add("Sartre");
    	this.addTestNote("And I, too, wanted to be. That is all I wanted; this is the last word. . . And at that very"
    			+ "moment, on the other side of existence, in this other world which you can see in the distance, but"
    			+ " without ever approaching it, a little melody began to sing and dance", 25, tags);
    	
    	getCurrTags();
    }

}
