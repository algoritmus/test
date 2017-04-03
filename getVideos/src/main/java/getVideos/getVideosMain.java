package getVideos;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class getVideosMain {
	static WebDriver driver;
	static WebElement elem;
	
	public static void main(String[] args) {
		
		
		System.setProperty("webdriver.chrome.driver", "C:/Users/ATDESY/Documents/WebDrivers/chromedriver227.exe");
		driver = new ChromeDriver();
		try{
			
			driver.get("https://capgemini.skillport.com/skillportfe/main.action?assetid=119045#whatshappening");
			elem = driver.findElement(By.id("sp-search"));
			
			highlightElement();
			elem.sendKeys("selenium java");
			elem = driver.findElement(By.id("sp-search-icon"));
			highlightElement();
			elem.click();
			WebDriverWait wait = new WebDriverWait(driver,15);
			wait.until(ExpectedConditions.elementToBeClickable(By.id("asset_tab_atag_video_bin"))).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@class='launchAsset btn btn-primary' and @assettype='VIDEOS']")));
			List<WebElement> assets = driver.findElements(By.xpath("//button[@class='launchAsset btn btn-primary' and @assettype='VIDEOS']"));
			String parentWindowHandler = driver.getWindowHandle(); // Store your parent window
			for (WebElement we:assets){
				//String id = we.getAttribute("assetid").split(":")[2];
				
				Thread.sleep(1000);
				we.click();
				Thread.sleep(2000);
				
				
				String subWindowHandler = null;

				Set<String> handles = driver.getWindowHandles(); // get all window handles
				Iterator<String> iterator = handles.iterator();
				while (iterator.hasNext()){
				    subWindowHandler = iterator.next();
				}
				driver.switchTo().window(subWindowHandler); 
				
				wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//img[@src='images/download.png'"))).click();
				wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(),'MP4')]"))).click();
				driver.close();
				driver.switchTo().window(parentWindowHandler); 
			}
		}catch (Exception ex){
			ex.printStackTrace();
		}finally{
			driver.close();
			driver.quit();
		}
			
	}
	
	public static void highlightElement() {
	    
	    // draw a border around the found element
	    if (driver instanceof JavascriptExecutor) {
	        ((JavascriptExecutor)driver).executeScript("arguments[0].style.border='3px solid red'", elem);
	    }
	   
	}
	

	

}
