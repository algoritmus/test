package org.duckdns.algoritmus.jegyuzer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

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

	public static String getDataFromDbAsJson(){
		
		
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static String getAllResultsAsJson(Connection conn,Statement stmt,String tableName) {
		JSONArray jArray = new JSONArray();
		JSONObject jObject = new JSONObject(); 
		try
        {
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("select * from "+tableName);
            ResultSetMetaData rsmd = results.getMetaData();
            int numberCols = rsmd.getColumnCount();
            
            while(results.next())
            {
            	List<String> record= new ArrayList<String>();
            	record.add(results.getTimestamp(1).toString());
            	jObject.put(rsmd.getColumnLabel(1),results.getTimestamp(1).toString());
            	for (int i=2; i<numberCols; i++)
                {
            		jObject.put(rsmd.getColumnLabel(i),results.getTimestamp(i));
                }
            	jObject.put(rsmd.getColumnLabel(numberCols),results.getTimestamp(numberCols));
            	jArray.add(jObject);
            }
            results.close();
            stmt.close();
            
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
		return jArray.toString();
	}

}
