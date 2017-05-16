package org.duckdns.algoritmus.ncore.main;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class NcoreUtils {
	
	public static String login(WebDriver driver,String userName, String password){
		 	driver.get("https://ncore.cc/torrents.php?csoport_listazas=osszes_film");
	        driver.findElement(By.xpath("//input[@class='beviteliMezo' and @name='nev']")).sendKeys("Algoritmus");
	        driver.findElement(By.xpath("//input[@class='beviteliMezo' and @name='pass']")).sendKeys("NCorejelszo123");
	        driver.findElement(By.xpath("//input[@type='submit']")).click();
	        return driver.findElement(By.xpath("//div[@class='torrent_lenyilo']//a")).getAttribute("href").split("key=")[1]; 
	}
	
	public static void searchTorrents(WebDriver driver, Connection conn, String key, String filter, String type, boolean getAll){
		//https://ncore.cc/torrents.php?oldal=2&tipus=all_own&mire=lucifer&miben=name
		filter=filter.replaceAll(" ", "%20");
		driver.get("https://ncore.cc/torrents.php?&tipus=all_own&miben=name&mire=" + filter + "&tipus=kivalasztottak_kozott&kivalasztott_tipus=" + type);
		List<WebElement> torrents = driver.findElements(By.xpath("//div[@class='box_torrent']"));
		
		for(WebElement torrent:torrents){
			try{
								
				WebElement torrent_txt = torrent.findElement(By.xpath(".//div[@class='torrent_txt']/a"));
				String name = torrent_txt.getAttribute("title");
				String torrentId = torrent_txt.getAttribute("href").split("id=")[1];
				String imdbId = torrent.findElement(By.xpath(".//div[@class='siterank']/a")).getAttribute("href").split("title/")[1].replace("/", "");
				
				
				String season = "";
				String episode = "";
				
				Pattern pattern = Pattern.compile("^.*(S\\d\\d)\\.*(E\\d\\d)");
				Matcher matcher = pattern.matcher(name);
				
				if (matcher.find()){
				    season = matcher.group(1);
				    episode = matcher.group(2);
				    
				   
				}
				System.out.println(name + "," + season + "," + episode + "," + imdbId);
				
				//checking if it needs to download or not
				if(!DBUtils.isTorrentExists(conn, imdbId, season, episode)){
					DBUtils.insertTorrent(conn, imdbId, name, season, episode);
					System.out.println("Torrent does not exists yet:" + key );
					System.out.println(name + "," + season + "," + episode + "," + imdbId);
					downloadTorrentFile(key, torrentId, name);
				}else{
					System.out.println("Already downloaded:" + key);
				};
				
				
				
	
					
			}
			catch(Exception e){
					//e.printStackTrace();
					
			}
			if(!getAll){
				break;
			}
		}//end of for loop		

	}// end of searchTorrents

	
	
	
	public static String getCurrentTimeStamp() {
	    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    Date now = new Date();
	    String strDate = sdfDate.format(now);
	    return strDate;
	}
	
	public static void downloadTorrentFile(String key, String torrentId, String name){
		FileOutputStream fos = null;
		try{
			URL website = new URL("https://ncore.cc/torrents.php?action=download&id=" + torrentId + "&key=" + key);
			ReadableByteChannel rbc = Channels.newChannel(website.openStream());
			fos = new FileOutputStream(name + ".torrent");
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		}catch (Exception e){
			
		}finally{
			try {
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
