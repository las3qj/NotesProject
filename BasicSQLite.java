import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;


public class BasicSQLite {

	
	/**
     * Connect to the test.db database
     *
     * @return the Connection object
     */
    private Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:C://sqlite/db/tests.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
    
    /**
     * Connect to a sample database
     *
     * @param fileName the database file name
     */
	public static void createNewTable() {
        // SQLite connection string
        String url = "jdbc:sqlite:C://sqlite/db/tests.db";
        
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS categories (\n"
                + "	name text PRIMARY KEY,\n"
                + "	tag1 text, \n"
                + "	tag2 text\n"
                + ");";
        
        try (Connection conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
	
	/**
     * Insert a new row into the warehouses table
     *
     * @param name
     * @param tag1
     * @param tag2
     */
	public void insert(String name, String tag1, String tag2) {
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
     * Update data of a warehouse specified by the id
     *
     * @param name name of the category
     * @param tag1 first tag in the category
     * @param tag2 second tag in the category
     */
    public void update(String name, String tag1, String tag2) {
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
    public void selectAll(){
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
    public void delete(String name) {
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
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    	 createNewTable();
    	 BasicSQLite sQLite = new BasicSQLite();
    	 
         // insert three new rows
    	 /*
         sQLite.insert("Author", "Neetche", "Rorty");
         sQLite.insert("Type", "Quotation", "Paraphrase");
         sQLite.insert("Theme", "Language", "Creation"); 
         
    	 sQLite.selectAll();
    	 // update authors
    	 sQLite.update("Author", "Nietzsche", "Rorty");
    	 */
    	 
    	 /*
    	 sQLite.insert("Autour", "Neetche", "Rotty");
    	 sQLite.selectAll();
    	 
    	 sQLite.delete("Autour");
    	 sQLite.selectAll();
    	 */
    }
}