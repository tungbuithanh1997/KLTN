package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;

import Recommend.SANPHAM;

public class DatabaseUtils {
	private static Connection connection;
	
	public static void initConnection() throws ClassNotFoundException, SQLException
	{
		connection = MySQLConnUtils.getMySQLConnection();
	}
	
	public static void loadRatings() throws SQLException
	{
	      Statement statement = connection.createStatement();
	 
	      String sql = "Select * from danhgia";
	 
	      ResultSet rs = statement.executeQuery(sql);
	 
	      while (rs.next()) {         
	          String empNo = rs.getString(2);
	          System.out.println("--------------------");
	          System.out.println(rs.getString(1));
	          System.out.println(rs.getString(2));
	      }
	     // connection.close();
	}
	
	public static ArrayList<SANPHAM> getListSanPhamByListID(ArrayList<Integer> listId) throws SQLException
	{
		ArrayList<SANPHAM> listResult = new ArrayList<>();
		
		Statement statement = connection.createStatement();
		StringBuilder builder = new StringBuilder();

		for( int i = 0 ; i < listId.size(); i++ ) {
		    builder.append("?,");
		}
		
		String query = "SELECT * FROM sanpham sp, nhanvien nv WHERE (sp.MANV = nv.MANV AND MASP IN ("
				+ builder.deleteCharAt( builder.length() -1 ).toString() + "))";
		PreparedStatement preparedstatement = connection.prepareStatement(query);
		int index = 1;
		for( Integer o : listId ) 
		{
			preparedstatement.setInt(index++, o ); // or whatever it applies 
		}    
		//System.out.println("quere: " + preparedstatement);
		
		 ResultSet rs =preparedstatement.executeQuery();
		 while (rs.next()) {         
			listResult.add(new SANPHAM(rs.getString(1),rs.getString(2),rs.getString(3),
					rs.getString(4),rs.getString(5),rs.getString(6),
					rs.getString(7),rs.getString(8),rs.getString(9),
					rs.getString(10),rs.getString(11),rs.getString(12),
					rs.getString(13),rs.getString(14),rs.getString(16)));
	      }
		 	
		return listResult;
	}
	
	public static ArrayList<SANPHAM> getListSanPhamGoiYAnonymous() throws SQLException
	{
		ArrayList<SANPHAM> listResult = new ArrayList<>();
		Statement statement = connection.createStatement();
		
		String query = "SELECT * FROM sanpham sp, nhanvien nv WHERE sp.MANV = nv.MANV ORDER BY LUOTMUA DESC LIMIT 20";
		
		 ResultSet rs = statement.executeQuery(query);
		 while (rs.next()) {         
			listResult.add(new SANPHAM(rs.getString(1),rs.getString(2),rs.getString(3),
					rs.getString(4),rs.getString(5),rs.getString(6),
					rs.getString(7),rs.getString(8),rs.getString(9),
					rs.getString(10),rs.getString(11),rs.getString(12),
					rs.getString(13),rs.getString(14),rs.getString(16)));
	      }
		return listResult;
	}
}
