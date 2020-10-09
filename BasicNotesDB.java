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
	public void createNewTables() {
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
        		+ "quote text, \n"
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
	
	//Categories methods
	/**
     * Insert a new category into the categories table
     *
     * @param name
     * @param tag1
     * @param tag2
     */
	public void insertCategory(String name, String tag1, String tag2) {
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
    public void updateCategory(String name, String tag1, String tag2) {
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
    public void selectAllCategories(){
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
    public void deleteCategory(String name) {
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

    //Notes methods
	/**
     * Insert a new note into the notes table
     *
     * @param id
     * @param name
     * @param tag1
     * @param tag2
     */
    public void insertNote(int id, String note, String tag1, String tag2) {
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
    public void updateNote(int id, String note, String tag1, String tag2) {
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
    public void selectAllNotes(){
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
    public void deleteNote(int id) {
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

}
