package org.duckdns.algoritmus.jegyuzer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Utils {

	public static String generateTable(List<List<String>> result,String title) {
		
		String table="<table border='1'><tr><th colspan=#'>" + title + "</th></tr>";
		int cols=0;
		int rows=0;
		//for(List<String> record:result){
		for(int i=0;i<result.size();i++){
			String row = "<tr>";
			rows++;
			List<String> record=result.get(i);
			for(String cell:record){
				cols++;
				if(i == 0){
					row+="<th>"+cell+"</th>";	
				}else{
					row+="<td>"+cell+"</td>";	
				}
			}
			row+="</tr>";
			table+=row;
		}
		table+="</table>";
		table=table.replace("colspan=#", "colspan="+ cols/rows);
		return table;
	}

	public static List<List<String>> selectStarRequests(Connection conn,Statement stmt,String tableName) {
		List<List<String>> result= new ArrayList<List<String>>();
		try
        {
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("select * from "+tableName);
            ResultSetMetaData rsmd = results.getMetaData();
            int numberCols = rsmd.getColumnCount();
            
            List<String> header= new ArrayList<String>();
            
            for (int i=1; i<=numberCols; i++)
            {
                //print Column Names
                
                header.add(rsmd.getColumnLabel(i));
               
            }
            result.add(header);
            

            while(results.next())
            {
            	List<String> record= new ArrayList<String>();
            	record.add(results.getTimestamp(1).toString());
            	for (int i=2; i<=numberCols; i++)
                {
            		record.add(results.getString(i));
                   
                }
                result.add(record);
            }
            results.close();
            stmt.close();
            
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
		return result;
	}

}
