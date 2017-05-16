package org.duckdns.algoritmus.ncore.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtils {
	
	public static Connection connectionToDerby(String dbFile) throws SQLException {
	    // -------------------------------------------
	    // URL format is
	    // jdbc:derby:<local directory to save data>
	    // -------------------------------------------
		String dbUrl = null;
		
		if(dbFile == null){
			dbUrl = "jdbc:derby:c:\\Users\\ATDESY\\MyDB\\demo;create=true";
		}else {
			dbUrl = "jdbc:derby:" + dbFile + ";create=true";
		}
		
	    Connection conn = DriverManager.getConnection(dbUrl);
	    return conn;
	  }
	
	public static void createTable(Connection conn) {
		try{
			Statement stmt = conn.createStatement();
			//auto increment ?
		    // create table
			stmt.executeUpdate("Create table torrents (id INT not null primary key GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1), imdbid varchar(30), name varchar(200), season varchar(3), episode varchar(3))");
		}catch(SQLException e){
			if(e.getSQLState().equals("X0Y32")){
				//Already exists
				return;
			}
			e.printStackTrace();
		}
	}
	
	public static void dropTable(Connection conn, String dropTable) {
		
		try{
			if(dropTable.equals("deleteTable")){
				Statement stmt = conn.createStatement();
				stmt.executeUpdate("Drop table torrents");
			}
		}catch(SQLException e){
			if(e.getSQLState().equals("42Y55")){
				//Does not exists
				return;
			}
			e.printStackTrace();
		}
	}
	
	public static void insertTorrent(Connection conn, String imdbId, String name, String season, String episode)  {
	    try{
	    	Statement stmt = conn.createStatement();
	    //auto increment ?
	    	stmt.executeUpdate("insert into torrents (imdbid,name,season,episode) values ('" + imdbId + "','" + name +"','"+ season + "','" + episode + "')");
	    }catch(SQLException e){
			
			e.printStackTrace();
		}
	}

	public static boolean isTorrentAlreadyDownloaded(Connection conn, String imdbId, String season, String episode) throws SQLException {
	    Statement stmt = conn.createStatement();
	    //auto increment ?
	    ResultSet rs = stmt.executeQuery("select count(*) from torrents where imdbid='" + imdbId + "' and season='" + season + "' and episode='" + episode + "'");
	    rs.next();
	    if(rs.getInt(1) != 0){
	    	return true; 
	    }else{
	    	return false;
	    }
	   
	  }	
}
