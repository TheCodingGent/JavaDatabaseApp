import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class DatabaseInterface {
	
	 private Connection connection; 
	 PreparedStatement statement;
	 
	 // constructor, does nothing 
	 public DatabaseInterface()
	 {
	  connection = null;
	  statement = null;
	 }

	 public Boolean connect()
	 {
	  Boolean success = true;
	  
	  try {
	  @SuppressWarnings("unused")
	PreparedStatement statement = null;
	  
	  Class.forName("com.mysql.jdbc.Driver");
	  
	  connection = DriverManager.getConnection("jdbc:mysql://localhost/biopeakdb", "root", "ELEPhant_792");
	 
	  } catch (SQLException e) {
	   // TODO Auto-generated catch block
	   e.printStackTrace();
	   success = false;
	  } catch (ClassNotFoundException e) {
	   // TODO Auto-generated catch block
	   e.printStackTrace();
	   success = false;
	  }
	 
	  return (success);
	 }
	 
	 // runQuery method: input: a query string, output a resultset
	 
	 public ResultSet runQuery(String query) throws SQLException
	 {
	  ResultSet result;
	  
	  statement = connection.prepareStatement(query);
	  result = statement.executeQuery();
	  
	  return (result); 
	 }
	 
	 // disconnect method
	 public Boolean disconnect()
	 {
	  Boolean success = true;
	  
	  try {
	   
		  connection.close();
	  
	  }catch (Exception e) {
	   
		  success = false;
	  }
	  return (success);
	 }

}
