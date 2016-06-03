package org.duckdns.algoritmus.jegyuzer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/result")
public class Resulthandler {
	
	private static final String dbURL="jdbc:mysql://192.168.0.21:3306/jegyuzer";
	private static Connection conn = null;
    private static Statement stmt = null;
    private static final String tableName="results";
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String showRequests(){
		createConnection();
		return Utils.getAllResultsAsJson(conn,stmt,tableName);
	}
	
	private void createConnection() {
		try
        {
            Class.forName("org.mariadb.jdbc.Driver").newInstance();
            //Get a connection
            conn = DriverManager.getConnection(dbURL,"jegyuzer","Zsani123"); 
        }
        catch (Exception except)
        {
            except.printStackTrace();
        }
		
	}


}
