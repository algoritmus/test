package org.duckdns.algoritmus.main;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
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
	
	public static CommandLine cmd;

	public static void main(String[] args) {
		
		
		long start = System.currentTimeMillis();
		
		java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF); 
		Options options = new Options();

        Option oImdb = new Option("i", "imdb", true, "imdb limit");
        oImdb.setRequired(true);
        options.addOption(oImdb);

        Option oTipus = new Option("t", "tipus", true, "torrent tipus");
        oTipus.setRequired(false);
        options.addOption(oTipus);
        
        Option oTitleFilter = new Option("f", "title", true, "torrent title filter");
        oTitleFilter.setRequired(false);
        options.addOption(oTitleFilter);

        Option oSeed = new Option("s", "seed", true, "minimum seed number");
        oSeed.setRequired(false);
        options.addOption(oSeed);
        
        Option oFileName = new Option("o", "file", true, "report file name");
        oFileName.setRequired(false);
        options.addOption(oFileName);
        
        Option oPage = new Option("p", "page", true, "how any page to search (0-all)");
        oPage.setRequired(false);
        options.addOption(oPage);
        
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("Ncore.jar", options);

            System.exit(1);
            return;
        }

		//ChromeDriverManager.getInstance().setup();
		//PhantomJsDriverManager.getInstance().setup();
	
    	
        //WebDriver driver = new PhantomJSDriver();
        //WebDriver driver = new ChromeDriver();
        
        WebDriver driver = new HtmlUnitDriver();
        
        
		String tipus = cmd.getOptionValue("tipus", "xvid_hun,xvid,dvd_hun,dvd,dvd9_hun,dvd9,hd_hun,hd");
    	double imdbLimit = Double.parseDouble(cmd.getOptionValue("imdb"));
		String titleFilter = cmd.getOptionValue("title","");
		int minimumSeed = Integer.parseInt(cmd.getOptionValue("seed","10"));
		String fileName = cmd.getOptionValue("file","Report.html");
		
		int page = Integer.parseInt(cmd.getOptionValue("page","10"));
		
		
    	BufferedWriter bw = null;
    	int counter = 0;
    	try{
			
			
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName),"UTF-8"));
			String time = NcoreUtils.getCurrentTimeStamp();
			HtmlHelper.addHtmlReportHeader(bw, imdbLimit, titleFilter, minimumSeed, tipus, time);
			NcoreUtils.login(driver, "Algoritmus", "NCorejelszo123");
			
			if(page == 0){
				NcoreUtils.pager(driver, 1, tipus, titleFilter);
				page = NcoreUtils.getNumberOfPages(driver);
			}
	      
	       for (int i = 1; i<=page; i++){
	    	   System.out.println("Processing page " + i + "/" + page);
	    	   NcoreUtils.pager(driver, i, tipus, titleFilter);
	    	   List<String> content = NcoreUtils.getTorrents(driver, imdbLimit, minimumSeed);
	    	   counter+=content.size();
	    	   HtmlHelper.addHtmltableRows(content, bw);
	    	   
	       }

	       HtmlHelper.addHtmlClosure(bw);
	       System.out.println(counter + " talalat.");
	       
		}catch(Exception e){
			e.printStackTrace();
		}finally{
	        try {
				bw.close();
			} catch (IOException e) {
			
				e.printStackTrace();
			}
			//driver.close();
	        
	        driver.quit();
	        long duration = (System.currentTimeMillis() - start) / 1000;
	        System.out.println("Running time: " + duration + "s");
		}
	}

}
