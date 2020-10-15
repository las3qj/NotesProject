package basicNotes;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public class BasicNotesDB {
	private int catTags;
	private int noteTags;
	private String insertCatSQL;
	private String insertNoteSQL;
	private String updateCatSQL;
	private String updateNoteSQL;
	private String selectCatTagsSQL;
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
            	if(i==0)
            		catTags = (rs.getInt("tags"));
            	else if(i==1)
            		noteTags = (rs.getInt("tags"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
	}
	
	//---------------------------SQL Statement Update Methods---------------------------
	
	/**
	 * sets the insert SQL strings for categories to the appropriate number of tags in each table
	 */
	private void setCatInsertSQL() {
		//Create the SQL string for inserting a new category
		String c1 = "INSERT INTO categories(name,";
		String c2 = ") VALUES(?,";
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
		String n1 = "INSERT INTO notes(id,content,";
		String n2 = ") VALUES(?,?,";
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
	 * sets the update SQL string for categories to the appropriate number of tags in each table
	 */
	private void setCatUpdateSQL() {
		//Create the SQL string for updating a category
		String c1 = "UPDATE categories SET ";
		for(int i=0; i<catTags; i++) {
			if(i==0) {
				c1+=("tag1 = ? ");
			}
			else if(i==catTags-1) {
				c1+=(", tag"+catTags+" = ? WHERE name = ?");
			}
			else {
				c1+=(", tag"+(i+1)+" = ? ");
			}
		}
		updateCatSQL=c1;
	}
	
	/**
	 * sets the update SQL string for notes to the appropriate number of tags in each table
	 */
	private void setNoteUpdateSQL() {
		//Create the SQL string for updating a note
		String c1 = "UPDATE notes SET ";
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
	 * sets the select from tag SQL string for notes to the appropriate number of tags in the table
	 */
	private void setSelectNoteFromTagSQL() {
		//Create the SQL string for selecting notes from a tag
		String c1 = "select id, content, ";
		String c2 = " from notes where ";
		for(int i=0; i<noteTags; i++) {
			if(i==0) {
				c1+=("tag1");
				c2+=("tag1 = ?");
			}
			else if(i==noteTags-1) {
				c1+=(", tag"+(noteTags));
				c2+=(" or tag"+(noteTags)+" = ?");
			}
			else {
				c1+=(", tag"+(i+1));
				c2+=(" or tag"+(i+1)+" = ?");
			}
		}
		selectNoteFromTagSQL=c1+c2;
	}
	
	/**
	 * sets the select SQL string for notes to the appropriate number of tags in the table
	 */
	/*
	private void setSelectNoteTags() {
		//Create the SQL string for selecting tags from a note
		String c1 = "select ";
		for(int i=0; i<noteTags; i++) {
			if(i==0) {
				c1+=("tag1");
			}
			else if(i==noteTags-1) {
				c1+=(", tag"+(noteTags)+" from notes where id = ?");
			}
			else {
				c1+=(", tag"+(i+1));
			}
		}
		selectNoteTags=c1;
	}*/
	
	//---------------------------Essential DB methods---------------------------
	
	/**
     * Connect to the basicnotes.db database
     *
     * @return the Connection object
     */
    private Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:C://sqlite/db/basicnotes.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
    
    /**
     * Create the appropriate tables in basicnotes.db
     *
     * @param fileName the database file name
     */
	private void createNewTables() {
        // SQLite connection string
        //String url = "jdbc:sqlite:C://sqlite/db/basicnotes.db";
        
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
		try (Connection conn = this.connect();
                Statement stmt = conn.createStatement()) {
			//for every int of difference between current max and newMax
			for(int i=catTags+1; i<=newMax; i++) {
				String sql = "alter table categories add column tag"+i+" text";
	            // alter categories table
	            stmt.execute(sql);
			}
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
		//Update the insert strings accordingly
		catTags=newMax;
		setCatInsertSQL();
		setCatUpdateSQL();
		setSelectCatTagsSQL();
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
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
		//Update the insert strings accordingly
		noteTags=newMax;
		setNoteInsertSQL();
		setNoteUpdateSQL();
		setSelectNoteFromTagSQL();
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
            for(int i=0; i<tags.size(); i++) {
                pstmt.setString(i+2, tags.get(i));
            }
            pstmt.executeUpdate();
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
    private void updateCategoryTags(String name, Vector<String> tags) {
    	//if there are more tags in the vector than in the categories table
        if(tags.size()>this.catTags) {
        	incCategoryTags(tags.size());
        }
        
        String sql = updateCatSQL;
        //use the insertCatSQL and loop through the tags vector to update category
        try (Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
        	int i=0;
        	while(i<tags.size()) {
        		pstmt.setString(i+1, tags.get(i));
        		i++;
        	}
        	pstmt.setString(i+1, name);
            pstmt.executeUpdate();
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
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
    }

    /**
     * Delete a category specified by the name
     *
     * @param name
     */
    private void deleteCategory(String name) {
        String sql = "DELETE FROM categories WHERE name = ?";

        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setString(1, name);
            // execute the delete statement
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Delete the category table
     **/
    private void deleteCategories() {
    	String sql = "DROP TABLE IF EXISTS categories";
    	
    	try (Connection conn = this.connect();
    			Statement stmt = conn.createStatement()) {
    		stmt.execute(sql);
    	}	catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * select all rows in the categories table -- vestigial method as of now
     */
    /*
    private void selectAllCategories(){
        String sql = "SELECT name, tag1, tag2 FROM categories";
        
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
            for(int i=0; i<tags.size(); i++) 
                pstmt.setString(i+3, tags.get(i));
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
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
        
        String sql = updateNoteSQL;
        //use the insertNoteSQL and loop through the tags vector to update note
        try (Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
        	int i=0;
        	while(i<tags.size()) {
        		pstmt.setString(i+1, tags.get(i));
        		i++;
        	}
        	pstmt.setInt(i+1, id);
            pstmt.executeUpdate();
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
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
    }

    /**
     * Delete a note specified by the id
     *
     * @param id
     */
    private void deleteNote(int id) {
        String sql = "DELETE FROM notes WHERE id = ?";

        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setInt(1, id);
            // execute the delete statement
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Clear the note table
     **/
    private void deleteNotes() {
    	String sql = "DROP TABLE IF EXISTS notes";
    	
    	try (Connection conn = this.connect();
    			Statement stmt = conn.createStatement()) {
    		stmt.execute(sql);
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
    	}	catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

	//---------------------------Public (General) Methods---------------------------
    
	/**
	 * Constructor
	 */
	public BasicNotesDB(boolean reset){
		//delete tables, create new tables, and populate tagtable
		if(reset) {
			reset();
			createNewTables();
			populateTagTable();
			catTags = 2;
			noteTags = 2;
		}
		else {
			//set catTags and noteTags to the current tags from the table
			getCurrTags();
		}
		setCatInsertSQL();
		setNoteInsertSQL();
		setCatUpdateSQL();
		setNoteUpdateSQL();
		setSelectCatTagsSQL();
		setSelectNoteFromTagSQL();
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
               
           } catch (SQLException e) {
               System.out.println(e.getMessage());
           }
    	return results;
    }

    /**
     * Creates test categories
     */
    public void testCategories() {
    	Vector<String> authors = new Vector<String>();
    	authors.add("Nietzsche");
    	authors.add("Rorty");
    	insertCategory("Genius", authors);
    	authors.add("Emerson");
    	updateCategoryTags("Genius", authors);
    	updateCategoryName("Genius","Author");
    	Vector<String> content = new Vector<String>();
    	content.add("Quote");
    	content.add("Thought");
    	insertCategory("Content", content);
    	Vector<String> genre = new Vector<String>();
    	genre.add("Philosophy");
    	genre.add("TV");
    	genre.add("Literature");
    	insertCategory("Genre", genre);
    }
    
  //---------------------------Public Notes Methods---------------------------
    
    /**
     * Inserts a new note into notes after creating an id
     * 
     * @param content - the content of the note to be inserted
     * @param tags - the Vector<String> of the tags to be inserted
     */
    public void publishNote(String content, Vector<String> tags) {
    	String sql1 = "SELECT COUNT(*) FROM notes";
    	int id = 1;
    	
    	try (Connection conn = this.connect();
                Statement stmt  = conn.createStatement();
                ResultSet rs    = stmt.executeQuery(sql1)){
    		id += rs.getInt(1);
            
           } catch (SQLException e) {
               System.out.println(e.getMessage());
           }
    	insertNote(id, content, tags);
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
        try (Connection conn = this.connect();
        		PreparedStatement pstmt = conn.prepareStatement(sql)) {
            	pstmt.setString(1, tag);
            	pstmt.setString(2, tag);
            	ResultSet rs    = pstmt.executeQuery();
                // loop through the result set
                while (rs.next()) {
                	Vector<String> tags = new Vector<String>();
                	for(int i=0; i<noteTags; i++) {
                		tags.add(rs.getString("tag"+(i+1)));
                	}
                    results.add(new Note(rs.getString("content"),rs.getInt("id"),tags));
                }
               
           } catch (SQLException e) {
               System.out.println(e.getMessage());
           }
    	return results;
    }
    
    //HYPER-INEFFICIENT METHOD
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
    
    public void testNotes() {
    	Vector<String> tags = new Vector<String>();
    	tags.add("Nietzsche");
    	tags.add("Quote");
    	insertNote(1, "all concepts in which an entire process is semiotically summarized elude definition;"
    			+ " only that which has no history is definable", tags);
    	tags = new Vector<String>();
    	tags.add("Quote");
    	tags.add("Rorty");
    	insertNote(2, "Rather, he saw self-knowledge as self-creation. The process of coming to know oneself,"
    			+ " confronting one's contingency, tracking one's causes home, is identical with the process"
    			+ "of inventing a new language -- that is, of thinking up some new metaphors.", tags);
    	tags = new Vector<String>();
    	tags.add("Philosophy");
    	tags.add("Nietzsche");
    	insertNote(3, "Nietzsche does not argue that truth does not exist, but merely that truth is not *out there*"
    			+ " -- that is, there is no truth beyond our interpretations. Truth is multiple, perspectival", 
    			tags);
    	tags = new Vector<String>();
    	tags.add("Quote");
    	tags.add("Nietzsche");
    	insertNote(4, "What, then, is truth? A mobile army of metaphors, metonyms, and anthropomorphisms--in short,"
    			+ "a sum of human relations, which have been enhanced, transposed, and embellished poetically and "
    			+ "rhetoircally, and which after long use seem firm, canonical, and obligatory to a people: truths"
    			+ " are illusions about which one has forgotten that this is what they are; metaphors which are"
    			+ " worn out and without sensuous power; coins which have lost their pictures and now matter only as"
    			+ "metal, no longer as coins", tags);
    	tags.add("Philosophy");
    	updateNoteTags(4,tags);
    	tags = new Vector<String>();
    	tags.add("Nietzsche");
    	tags.add("Quote");
    	insertNote(5, "Every great pig has a retroactive force: all history is again placed in the scales for"
    			+ " his sake, and a thousand secrets of the past crawl out of their hideouts--into his sun.", tags);
    	this.updateNoteContent(5, "Every great human being has a retroactive force: all history is again placed "
    			+ "in the scales for his sake, and a thousand secrets of the past crawl out of their hideouts"
    			+ "--into his sun.");
    }
    //From here we need to determine how exactly the app will interact with this database interface
    //i.e. we really need to spend some time working on creating the design of the app
}
