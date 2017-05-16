package org.duckdns.algoritmus.ncore.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.util.List;
import java.util.logging.Level;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class Ncore {
	
	static Connection conn;
	static String keyword;
	static String type;
	static boolean getAll;
	static String key;
	static String dropTable = "";
	static List<String> searchList;
	public static void main(String[] args) {
		
		
		long start = System.currentTimeMillis();
		
		java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF); 
		if (args.length > 0){
			dropTable = args[0];
		}
		//String output = args[1];
		

		//ChromeDriverManager.getInstance().setup();
		//PhantomJsDriverManager.getInstance().setup();
	
    	
        //WebDriver driver = new PhantomJSDriver();
        //WebDriver driver = new ChromeDriver();
        
        WebDriver driver = new HtmlUnitDriver();
        
        
    	
		
    	

    	try{
    		readSearchListFromFile();
   		
			conn = DBUtils.connectionToDerby(null);
			
			DBUtils.dropTable(conn, dropTable);
			DBUtils.createTable(conn);
			
    		key = NcoreUtils.login(driver, "Algoritmus", "NCorejelszo123");
			for(String line:searchList){
				keyword = line.split(",")[0];
				type = line.split(",")[1];
				getAll = Boolean.parseBoolean(line.split(",")[2]);
				NcoreUtils.findNewTorrents(driver, conn, key, keyword, type, getAll);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
        
	        driver.quit();
	        long duration = (System.currentTimeMillis() - start) / 1000;
	        System.out.println("Running time: " + duration + "s");
		}
	}
	private static void readSearchListFromFile() throws FileNotFoundException, IOException {
		
		searchList = Files.readAllLines(Paths.get("torrents.csv"), Charset.defaultCharset() );
		
	}

	

}
