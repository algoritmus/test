package org.duckdns.algoritmus.ncore.main;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class NcoreUtils {
	static String torrentName = null;
	static String torrentId = null;
	static String imdbId = null;
	static String season = "";
	static String episode = "";
	static String filter = null;
	
	public static String login(WebDriver driver,String userName, String password){
		 	driver.get("https://ncore.cc/torrents.php?csoport_listazas=osszes_film");
	        driver.findElement(By.xpath("//input[@class='beviteliMezo' and @name='nev']")).sendKeys("Algoritmus");
	        driver.findElement(By.xpath("//input[@class='beviteliMezo' and @name='pass']")).sendKeys("NCorejelszo123");
	        driver.findElement(By.xpath("//input[@type='submit']")).click();
	        return driver.findElement(By.xpath("//link[@rel='alternate']")).getAttribute("href").split("key=")[1];
	        
	}
	
	public static void findNewTorrents(WebDriver driver, Connection conn, String key, String filter, String type, boolean getAll){

		try{
			filter = escapeFilterString(filter);
			doSearch(driver, filter, type);
			List<WebElement> torrents = extractTorrentList(driver);
			
			for(WebElement torrent:torrents){
				extractTorrentInformations(torrent);
				extractSeasonAndEpisodeInformation();

				if(!DBUtils.isTorrentAlreadyDownloaded(conn, imdbId, season, episode)){
					DBUtils.insertTorrent(conn, imdbId, torrentName, season, episode);
					downloadTorrentFile(key, torrentId, torrentName);
				}else{
					//System.out.println("Already downloaded:" + key);
				};

				if(!getAll){
					break;
				}
			}//end of for loop		

		}
		catch(Exception e){
				e.printStackTrace();
				
		}

	}// end of searchTorrents

	private static void extractSeasonAndEpisodeInformation() {
		Pattern pattern = Pattern.compile("^.*(S\\d\\d)\\.*(E\\d\\d)");
		Matcher matcher = pattern.matcher(torrentName);
		
		if (matcher.find()){
		    season = matcher.group(1);
		    episode = matcher.group(2);
		    
		   
		}else{
			season = "SXX";
			episode = "EXX";
		}
	}

	private static String escapeFilterString(String filter) {
		filter=filter.replaceAll(" ", "%20");
		return filter;
	}

	private static void extractTorrentInformations(WebElement torrent) {
		WebElement torrent_txt = torrent.findElement(By.xpath(".//div[@class='torrent_txt']/a"));
		torrentName = torrent_txt.getAttribute("title");
		torrentId = torrent_txt.getAttribute("href").split("id=")[1];
		imdbId = torrent.findElement(By.xpath(".//div[@class='siterank']/a")).getAttribute("href").split("title/")[1].replace("/", "");
	}

	private static List<WebElement> extractTorrentList(WebDriver driver) {
		return driver.findElements(By.xpath("//div[@class='box_torrent']"));
	}

	private static void doSearch(WebDriver driver, String filter, String type) {
		driver.get("https://ncore.cc/torrents.php?&tipus=all_own&miben=name&mire=" + filter + "&tipus=kivalasztottak_kozott&kivalasztott_tipus=" + type);
	}
	
	public static String getCurrentTimeStamp() {
	    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    Date now = new Date();
	    String strDate = sdfDate.format(now);
	    return strDate;
	}
	
	private static void downloadTorrentFile(String key, String torrentId, String name){
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
