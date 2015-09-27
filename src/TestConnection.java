import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;



public class TestConnection {

	public static void main (String [] args) throws SQLException {
		
		DatabaseInterface db = new DatabaseInterface();
		
		System.out.println(db.connect());
		
		ResultSet res = db.runQuery("SELECT * FROM subject WHERE Online = FALSE");
		ArrayList<String> ar = new ArrayList<String>();
		
		 while (res.next())
         {
			 String name = res.getString("Name");
			    System.out.println(name);
			    ar.add(name);
         }   
		
	}	
}
	

