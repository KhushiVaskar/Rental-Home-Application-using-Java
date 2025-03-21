package pdea;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class RentalAppl {

	public static void main(String[] args) throws SQLException {
		//JDBC1 obj = new JDBC1();
		//obj.createTable("kvdb", "Flats_app");
		Scanner sc = new Scanner(System.in);
		JDBC1 jdbc = new JDBC1();
		while(true) {
			System.out.println("1.Add Flat");
			System.out.println("2.View Flat");
			System.out.println("3.Book Flat");
			System.out.println("4.Delete Flat");
			System.out.println("5.Exit");
			System.out.println("Choose one option");
			int choice = sc.nextInt();
			switch(choice) {
			case 1:
				System.out.println("Enter Flat Number ");
				int flat_no = sc.nextInt();
				sc.nextLine();
				System.out.println("Enter location ");
				String location = sc.nextLine();
				System.out.println("Enter rent ");
				double rent = sc.nextDouble();
				Flat flat = new Flat(flat_no,rent,location);
				//flat.displayFlat();
				JDBC1.insertTable("kvdb", "Flats_app", flat);
				break;
				
			case 2:
				JDBC1.retrieveTable("kvdb", "Flats_app");
				break;
				
			case 3:
				System.out.println("Enter Flat number to Book ");
				int bookFlat_no=sc.nextInt();
				JDBC1.updateTable("kvdb", "Flats_app", bookFlat_no);
				break;
			
			case 4:
				System.out.println("Enter Flat number to delete");
				int deleteFlat_no=sc.nextInt();
				JDBC1.deleteTblRow("kvdb", "Flats_app",deleteFlat_no);
				System.out.println("Flat is now vacant...");
				break;
				
			case 5:
				System.out.println("Exiting Application....");
				sc.close();
				System.exit(0);
				
			default:
				System.out.println("Invalid choice");
			}
		}

	}

}


class Flat{
	private int flat_no;
	private double rent;
	private boolean isFlatAvailable;
	private String location;
	
	public Flat(int flat_no,double rent,String location) {
		this.location=location;
		this.flat_no=flat_no;
		this.rent=rent;
		this.isFlatAvailable=true;
	}//end constructor
	public int getflat_no() {
		return flat_no;
	}
	public void setflat_no(int flat_no) {
		this.flat_no=flat_no;
	}
	public String getlocation() {
		return location;
	}
	public void setlocation(String location) {
		this.location=location;
	}
	public double getrent() {
		return rent;
	}
	public void setrent(double rent) {
		this.rent=rent;
	}
	public boolean getisFlatAvailable() {
		return isFlatAvailable;
	}
	public void setisFlatAvailable(boolean isFlatAvailable) {
		this.isFlatAvailable=isFlatAvailable;
	}
	
}//end flat

class JDBC1{
	/*public void createTable(String dbName,String tblName) throws SQLException {
		Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/"+dbName,"postgres","123");
		String query = "CREATE TABLE IF NOT EXISTS "+ tblName +"(" +
		"flat_no INT PRIMARY KEY," +
		"location VARCHAR(100) NOT NULL," +
		"rent int not null," +
		"isFlatAvailable boolean default true)";
		Statement stmt=con.createStatement();
		stmt.executeLargeUpdate(query);
	}
	}*/
	
	public static void insertTable(String dbName,String tblName,Flat flat) throws SQLException {
		Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/"+dbName,"postgres","123");
		String query = "insert into "+tblName+" values(?,?,?,?)";
		PreparedStatement pstmt =  con.prepareStatement(query);
			pstmt.setInt(1,flat.getflat_no());
			pstmt.setString(2,flat.getlocation());
			pstmt.setDouble(3, flat.getrent());
			pstmt.setBoolean(4, flat.getisFlatAvailable());
			pstmt.executeUpdate();
			pstmt.close();
			con.close();
		}
	
	public static void retrieveTable(String dbName,String tblName) throws SQLException {
		Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/"+dbName,"postgres","123");
		String query = "select * from "+tblName;
		Statement stmt = con.createStatement();
		ResultSet rs=stmt.executeQuery(query);
		while(rs.next()) {
			System.out.println("Flat number is- "+rs.getInt("flat_no"));
			System.out.println("Flat location is- "+rs.getString("location"));
			System.out.println("Flat rent is- "+rs.getDouble("rent"));
			System.out.println("Is Flat Available- "+rs.getBoolean("isFlatAvailable"));
		}
	}
	public static void updateTable(String dbName,String tblName,int flat_no) throws SQLException {
		Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/"+dbName,"postgres","123");
		String query="select isFlatAvailable from "+tblName+" where flat_no=?";
		PreparedStatement pstmt=con.prepareStatement(query);
		pstmt.setInt(1, flat_no);
		ResultSet rs=pstmt.executeQuery();
		if(rs.next() && rs.getBoolean("isFlatAvailable")) {
			String update="update "+tblName+" set isFlatAvailable = false where flat_no=? ";
			PreparedStatement ps = con.prepareStatement(update);
			ps.setInt(1, flat_no);
			ps.executeUpdate();
			System.out.println("Flat "+flat_no+" booked successfully");
		}else {
			System.out.println("Flat is not available");
		}
	}
	public static void deleteTblRow(String dbName,String tblName,int flat_no)throws SQLException{
		String query = "delete from "+tblName+" where flat_no=?";
		Connection con=DriverManager.getConnection("jdbc:postgresql://localhost:5432/"+dbName,"postgres","123");
		PreparedStatement pstmt=con.prepareStatement(query);
		pstmt.setInt(1,flat_no);
		int updateRowCount=pstmt.executeUpdate();
		pstmt.close();
		con.close();
	
}
}
