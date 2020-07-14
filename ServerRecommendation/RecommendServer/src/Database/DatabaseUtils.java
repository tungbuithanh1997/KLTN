package Database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import Recommend.SANPHAM;

public class DatabaseUtils {
	private static Connection connection;
	static NeighborhoodBased nb = new NeighborhoodBased();
	
	public static void initConnection() throws ClassNotFoundException, SQLException
	{
		connection = MySQLConnUtils.getMySQLConnection();
		//loadRatings();
		loadNeighborhoodBasedRS();
	}
	
	public static void loadNeighborhoodBasedRS() throws SQLException
	{
		DatabaseUtils.loadRatings(nb.getUtilityMatrix(), nb.getUserRatingInfo());
		
		nb.normalizedMatrix();
		nb.calculateSimilarityMatrix();
		
		//nb.printNormalizedMatrix();
		//System.out.println("_________________");	
		//nb.printSimilarityMatrix();
	}
	
	public NeighborhoodBased getNb() {
		return nb;
	}

	public static ArrayList<SANPHAM> predictForUserNumItem(int userId, int numItem) throws SQLException
	{
		if (!nb.getUserRatingInfo().containsKey(userId))
			return getListSanPhamGoiYAnonymous();
		
		ArrayList<Rate> resultRecommend = nb.predictItemsForUser(userId, numItem);
		ArrayList<Integer> listIds = new ArrayList<>();
		int size = Math.min(numItem, resultRecommend.size());
		for (int i = 0; i < size; i++)
			listIds.add(resultRecommend.get(i).itemId);
		//System.out.println("SIZE:" + listIds.size());
		if (listIds.size() > 0)
		return getListSanPhamByListID(listIds);
		return getListSanPhamGoiYAnonymous();
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
		
		String query = "SELECT * FROM sanpham sp, nhanvien nv WHERE sp.MANV = nv.MANV ORDER BY LUOTMUA DESC LIMIT 50";
		
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
	
	public static void loadRatings(float[][] outputMatrix, HashMap<Integer, RowInfo> outputRowInfo) throws SQLException
	{
	      Statement statement = connection.createStatement();
	 
	      String sql = "Select * from danhgia";
	 
	      ResultSet rs = statement.executeQuery(sql);
	 
	      while (rs.next()) {         
	    	  int userId = Integer.valueOf(rs.getString(2));
	    	  int itemId = Integer.valueOf(rs.getString(1));
	    	  float rate = Float.valueOf(rs.getString(5));
	    	  outputMatrix[userId][itemId] = rate;
	    	  if (!outputRowInfo.containsKey(userId))
	    		  outputRowInfo.put(userId, new RowInfo());
	    	  outputRowInfo.get(userId).sum += rate;
	    	  outputRowInfo.get(userId).numValue +=1;
	    	 // System.out.println("Rate: "+ rate +" userid: " + userId + "itemID: "+ itemId);
	      }
	      
	     
	     // connection.close();
	      /*
	      for (int i=0; i< 20; i++)
	      {
	    	  for (int j=0; j<20; j++)
	    		  System.out.print(outputMatrix[i][j] + "\t");
	    	  System.out.println();
	      }*/
		
	}
	
	public static void loadRatings(String filePath,float[][] outputMatrix, HashMap<Integer, RowInfo> outputRowInfo) throws SQLException, IOException
	{
		File file = new File(filePath); 
		  
		  BufferedReader br = new BufferedReader(new FileReader(file)); 
		  
		  String st; 
		  while ((st = br.readLine()) != null) 
		  {
			  String stringValues[] = st.split("\t");
			  int userId = Integer.valueOf(stringValues[0]);
	    	  int itemId = Integer.valueOf(stringValues[1]);
	    	  float rate = Float.valueOf(stringValues[2]);
	    	  outputMatrix[userId][itemId] = rate;
	    	  if (!outputRowInfo.containsKey(userId))
	    		  outputRowInfo.put(userId, new RowInfo());
	    	  outputRowInfo.get(userId).sum += rate;
	    	  outputRowInfo.get(userId).numValue +=1;
		  }
		  
		  /*
	      while (rs.next()) {         
	         // String empNo = rs.getString(2);
	          //System.out.println("--------------------");
	         // System.out.println(rs.getString(1));
	          //System.out.println(rs.getString(2));
	    	  int userId = Integer.valueOf(rs.getString(2));
	    	  int itemId = Integer.valueOf(rs.getString(1));
	    	  float rate = Float.valueOf(rs.getString(5));
	    	  outputMatrix[userId][itemId] = rate;
	    	  if (!outputRowInfo.containsKey(userId))
	    		  outputRowInfo.put(userId, new RowInfo());
	    	  outputRowInfo.get(userId).sum += rate;
	    	  outputRowInfo.get(userId).numValue +=1;
	      }*/
		
	}
}
