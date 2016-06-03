package org.duckdns.algoritmus.jegyuzer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/request")
public class RequestHandler {

	private static final String dbURL="jdbc:mysql://192.168.0.21:3306/jegyuzer";
	private static Connection conn = null;
    private static Statement stmt = null;
    //private static final String HTML_START="<html><body>";
    //private static final String HTML_END="</body></html>";
    private static final String tableName="requests";
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String showRequests(){
		createConnection();
		List<List<String>> result = Utils.selectStarRequests(conn, stmt, tableName);
		return Utils.generateTable(result,"REQUESTS");
		
	}

	@POST
	@Path("/register")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_HTML)
	public String addRequest(   @FormParam("origin") String origin,
								@FormParam("destination") String destination,
								@FormParam("outbound") String outbound,
								@FormParam("inbound") String inbound,
								@FormParam("adults") int adults,
								@FormParam("infants") int infants,
								@FormParam("emailaddr") String emailaddr
								){
		
		
		createConnection();
		insertRequest(origin,destination,outbound,inbound,adults,infants,emailaddr);
		
		
		
		//return "<form  action='../request'><button type='submit'>OK, check it</button></form>";
		return null;
		
	}
	private void insertRequest(String origin, String destination, String outbound, String inbound, int adults,
			int infants, String emailaddr) {
		
		try {
			stmt = conn.createStatement();
			String sql="insert into " + tableName +" values (null,'"+emailaddr+"','"+origin+"','"+destination+
					"','"+outbound+"','"+inbound+"',"+adults+","+infants+")";
			System.out.println(sql);
			stmt.execute(sql);
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
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
