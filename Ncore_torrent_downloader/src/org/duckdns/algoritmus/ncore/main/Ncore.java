package org.duckdns.algoritmus.ncore.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
	
	public static Connection conn;
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
        
        
    	BufferedReader br = null;
		BufferedWriter bw = null;
    	

    	try{
			

    		
			conn = DBUtils.connectionToDerby(null);
			
			DBUtils.dropTable(conn, dropTable);
			DBUtils.createTable(conn);
			
    		String key = NcoreUtils.login(driver, "Algoritmus", "NCorejelszo123");
			NcoreUtils.findNewTorrents(driver, conn, key, "lucifer s02 720", "hdser", false);

			
			

			
			
			
			
			
	      
		}catch(Exception e){
			e.printStackTrace();
		}finally{
	        
			//driver.close();
	        
	        driver.quit();
	        long duration = (System.currentTimeMillis() - start) / 1000;
	        System.out.println("Running time: " + duration + "s");
		}
	}

	

}
