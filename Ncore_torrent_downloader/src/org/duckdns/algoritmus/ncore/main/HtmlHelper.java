package org.duckdns.algoritmus.ncore.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class HtmlHelper {
	
	public static void createHtmlReportFile(BufferedWriter bw, String fileName){
		try {
			bw = new BufferedWriter(new FileWriter(new File(fileName)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void addHtmlReportHeader(BufferedWriter bw, double imdbLimit, String titleFilter, int minimumSeed, String tipus, String time){
		try{
			
			bw.write("<html><body>\n");
			bw.write("<div><b>IMBD LIMIT: </b>" + imdbLimit + "</div>\n");
			bw.write("<div><b>TITLE CONTAINS: </b>" + titleFilter + "</div>\n");
			bw.write("<div><b>TIPUS: </b>" + tipus + "</div>\n");
			bw.write("<div><b>MIN SEED: </b>" + minimumSeed + "</div>\n");
			bw.write("<div><b>Created: </b>" + time + "</div>\n");
			bw.write("<div><table border='1'><tr><th>COVER</th><th>IMDB</th><th>ID</th><th>TITLE</th><th>FELTOLTVE</th><th>MERET</th><th>SEED</th></tr>\n");

		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
	
	public static void addHtmltableRows(List<String> content,BufferedWriter bw){
		
		for (String s:content){
			String[] field = s.split("\\|");
			try {
				String link = "<a href='https://ncore.cc/torrents.php?action=download&id=" + field[1] + "'>" + field[1] + "</a>";
				bw.write("<tr><td><img src='" + field[6] + "'></td><td>" + field[0] + "</td><td>" + link + "</td><td>" + field[2] + "</td><td>" + field[3] + "</td><td>" + field[4] + "</td><td>" + field[5] + "</td></tr>\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public static void addHtmlClosure(BufferedWriter bw){
		try {
			bw.write("</table></div></body></html>");
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
