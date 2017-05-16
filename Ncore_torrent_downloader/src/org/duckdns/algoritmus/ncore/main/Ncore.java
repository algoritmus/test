package org.duckdns.algoritmus.ncore.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class Ncore {
	
	static Connection conn;
	static String keyword;
	static String type;
	static boolean getAll;
	static String key;
	public static void main(String[] args) {
		
		
		long start = System.currentTimeMillis();
		
		java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF); 
		String dropTable = args[0];
		//String output = args[1];
		

		//ChromeDriverManager.getInstance().setup();
		//PhantomJsDriverManager.getInstance().setup();
	
    	
        //WebDriver driver = new PhantomJSDriver();
        //WebDriver driver = new ChromeDriver();
        
        WebDriver driver = new HtmlUnitDriver();
        
        
    	
		
    	

    	try{
    		readSearchItems();
   		
			conn = DBUtils.connectionToDerby(null);
			
			DBUtils.dropTable(conn, dropTable);
			DBUtils.createTable(conn);
			
    		key = NcoreUtils.login(driver, "Algoritmus", "NCorejelszo123");
			NcoreUtils.findNewTorrents(driver, conn, key, keyword, type, getAll);
	      
		}catch(Exception e){
			e.printStackTrace();
		}finally{
        
	        driver.quit();
	        long duration = (System.currentTimeMillis() - start) / 1000;
	        System.out.println("Running time: " + duration + "s");
		}
	}
	private static void readSearchItems() throws FileNotFoundException, IOException {
		BufferedReader br = new BufferedReader(new FileReader("torrents.csv"));
		String line = null;
		while (( line = br.readLine()) != null){
			keyword = line.split(",")[0];
			type = line.split(",")[0];
			getAll = Boolean.parseBoolean(line.split(",")[0]);
			
			
		}
	}

	

}
