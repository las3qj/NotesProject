package basicNotes;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BasicNotesDB {
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
                + "	tag1 text, \n"
                + "	tag2 text\n"
                + ");";
        // SQL statement for creating a new table -- notes
        String sql2 = "CREATE TABLE IF NOT EXISTS notes (\n"
        		+ " id integer PRIMARY KEY, \n"
        		+ "note text, \n"
        		+ "tag1 text, \n"
        		+ "tag2 text \n"
        		+ ");";
        
        try (Connection conn = this.connect();
                Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql1);
            stmt.execute(sql2);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
	
	//Private Categories methods
	/**
     * Insert a new category into the categories table
     *
     * @param name
     * @param tag1
     * @param tag2
     */
	private void insertCategory(String name, String tag1, String tag2) {
        String sql = "INSERT INTO categories(name,tag1,tag2) VALUES(?,?,?)";

        try (Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, tag1);
            pstmt.setString(3, tag2);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
	
	/**
     * Update data of a category specified by the name
     *
     * @param name name of the category
     * @param tag1 first tag in the category
     * @param tag2 second tag in the category
     */
    private void updateCategory(String name, String tag1, String tag2) {
        String sql = "UPDATE categories SET tag1 = ? , "
                + "tag2 = ? "
                + "WHERE name = ?";

        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setString(1, tag1);
            pstmt.setString(2, tag2);
            pstmt.setString(3, name);
            // update 
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * select all rows in the categories table
     */
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
    
    //Notes methods
	/**
     * Insert a new note into the notes table
     *
     * @param id
     * @param name
     * @param tag1
     * @param tag2
     */
    private void insertNote(int id, String note, String tag1, String tag2) {
        String sql = "INSERT INTO notes(id,note,tag1,tag2) VALUES(?,?,?,?)";

        try (Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, note);
            pstmt.setString(3, tag1);
            pstmt.setString(4, tag2);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

	/**
     * Update data of a category specified by the name
     *
     * @param name name of the category
     * @param tag1 first tag in the category
     * @param tag2 second tag in the category
     */
    private void updateNote(int id, String note, String tag1, String tag2) {
        String sql = "UPDATE notes SET note = ?"
        		+ "tag1 = ? , "
                + "tag2 = ? "
                + "WHERE id = ?";

        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setString(1, note);
            pstmt.setString(2, tag1);
            pstmt.setString(3, tag2);
            pstmt.setInt(4, id);
            // update 
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * select all rows in the notes table
     */
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

    //Public methods
    /**
     * Inserts a new note into notes after creating an id
     * 
     * @param note
     * @param tag1
     * @param tag2
     */
    public void publishNote(String note, String tag1, String tag2) {
    	String sql1 = "SELECT COUNT(*) FROM notes";
    	int id = 1;
    	
    	try (Connection conn = this.connect();
                Statement stmt  = conn.createStatement();
                ResultSet rs    = stmt.executeQuery(sql1)){
    		id += rs.getInt(1);
            
           } catch (SQLException e) {
               System.out.println(e.getMessage());
           }
    	insertNote(id, note, tag1, tag2);
    }
    
    /**
     * Deletes current tables and creates new, empty ones
     **/
    public void reset() {
    	deleteCategories();
    	deleteNotes();
    	createNewTables();
    }

    //From here we need to determine how exactly the app will interact with this database interface
    //i.e. we really need to spend some time working on creating the design of the app
}
