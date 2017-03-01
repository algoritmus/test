package org.duckdns.algoritmus.main;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class NcoreUtils {
	
	public static void login(WebDriver driver,String userName, String password){
		 	driver.get("https://ncore.cc/torrents.php?csoport_listazas=osszes_film");
	        driver.findElement(By.xpath("//input[@class='beviteliMezo' and @name='nev']")).sendKeys("Algoritmus");
	        driver.findElement(By.xpath("//input[@class='beviteliMezo' and @name='pass']")).sendKeys("NCorejelszo123");
	        driver.findElement(By.xpath("//input[@type='submit']")).click();
	}
	
	public static List<String> getTorrents(WebDriver driver, double imdbLimit, int minimumSeed){
		
		List<String> result = new ArrayList<String>();
		List<WebElement> torrents = driver.findElements(By.xpath("//div[@class='box_torrent']"));
		
		for (WebElement torrent:torrents){
			double imdbScore = 0;
			String imdbString = "0";
			StringBuilder sb = new StringBuilder();
			try{
				String feltoltve = "|" + torrent.findElement(By.xpath(".//div[@class='box_feltoltve2']")).getText();
				
				String meret = "|" +  torrent.findElement(By.xpath(".//div[@class='box_meret2']")).getText();
			
				String seed = torrent.findElement(By.xpath(".//div[@class='box_s2']")).getText();
				int seedNumber = Integer.parseInt(seed);
				seed = "|" + seed;
				
				String img_att = torrent.findElement(By.xpath(".//img[@class='infobar_ico']")).getAttribute("onmouseover");
				String img_link = img_att.split(",")[0].replace("mutat(","").replaceAll("'", "");
				
				
				WebElement torrent_txt = torrent.findElement(By.xpath(".//div[@class='torrent_txt']/a"));
				String title =  "|" + torrent_txt.getAttribute("title");
				String id = "|" +  torrent_txt.getAttribute("onclick").replace("torrent(", "").replace("); return false;","");

				imdbString = torrent.findElement(By.xpath(".//div[@class='siterank']/a")).getText().replace("[imdb: ","").replace("]", "");
			
				imdbScore = Double.parseDouble(imdbString);
				if(imdbScore >= imdbLimit){
					if(seedNumber >= minimumSeed){
						
						//System.out.println(title +" - "+id+" - "+ imdbScore + ":" + imdbString + " " + feltoltve + " " + meret + " " + seed);
						sb.append(imdbString).append(id).append(title).append(feltoltve).append(meret).append(seed).append("|"+img_link);
						result.add(sb.toString());
					}
				}
			}
			catch(Exception e){
				//e.printStackTrace();
				
			}
		}
		
		return result;
		
	}

	public static int getNumberOfPages(WebDriver driver){
		
		List<WebElement> pages = driver.findElements(By.xpath("//div[@id='pager_bottom']/a"));
		String lastHref = pages.get(pages.size()-1).getAttribute("href");
		String[] data = lastHref.split("=");
		String[] data1 = data[1].split("\\&");
		//String[] data2 = data1[0].split("=");
		return Integer.parseInt(data1[0]);
	}
	
	public static void pager(WebDriver driver, int i,String tipus, String titleFilter){
		
		
		String url = "https://ncore.cc/torrents.php?oldal=" + i + "&miben=name&tipus=kivalasztottak_kozott&kivalasztott_tipus=" + tipus + "&mire=" + titleFilter;
		//System.out.println("Loading page number: " + i);
		driver.get(url);
	}
	
	public static String getCurrentTimeStamp() {
	    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    Date now = new Date();
	    String strDate = sdfDate.format(now);
	    return strDate;
	}
}
