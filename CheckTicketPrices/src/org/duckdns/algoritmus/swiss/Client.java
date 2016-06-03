package org.duckdns.algoritmus.swiss;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Client {

	static int bp=0;
	static String url="https://www.swiss.com/us/en/Book";
	private static final String dbURL="jdbc:mysql://192.168.0.21:3306/jegyuzer";
	private static Connection conn = null;
    private static Statement stmt = null;
    private static List<String> outbound = new ArrayList<String>();
    private static List<String> inbound = new ArrayList<String>();
    private static List<String> infos = new ArrayList<String>();
    private static ResultSet requests=null;
	
    public static void main(String[] args) {
		
    	createConnection();
    	
    	WebDriver driver = new FirefoxDriver();

		driver.get(url);
		WebDriverWait wait= new WebDriverWait(driver, 30);
		
		
		
		
		getRequests();
		
        try {
			while(requests.next())
			{
			
				
				clearAndSetFormFields(wait,"SearchODCalenderModel_SearchCriteria_Origin",requests.getString(3),false);
				clearAndSetFormFields(wait,"SearchODCalenderModel_SearchCriteria_Destination",requests.getString(4),false);
				clearAndSetFormFields(wait,"datefrom",requests.getString(5),false);
				clearAndSetFormFields(wait,"dateto",requests.getString(6),false);
				setFormFields(wait,"SearchPaxClassAirlineModel_PaxSelectionModel_SearchCriteria_Adults",requests.getString(7),true);

			
				
				List<WebElement> rows = waitForData(wait);
				//System.out.println("ZHR - BUD");
				extractData(rows,"outbound");
				goToReturnFlightPage(wait, rows);

				rows = waitForData(wait);
				//System.out.println("BUD - ZHR");
				extractData(rows,"inbound");
				//System.out.println("\n-------------------------------\nBest commbo price :" + bp);
				infos.addAll(outbound);
				infos.addAll(inbound);
				writeResultToDatabase(infos);
				driver.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			
			try {
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private static void writeResultToDatabase(List<String> infos) {
		
		try {
			stmt = conn.createStatement();
	        for(String line:infos){
	        	String[] data=line.split(",");
	        	String sqlStmt="insert into results values(null,'"+requests.getString(2)+"','";
	        	if(data[3].equals("o")){
	        		sqlStmt+=requests.getString(3)+"','"+requests.getString(4)+"','";
	        	}else{
	        		sqlStmt+=requests.getString(4)+"','"+requests.getString(3)+"','";
	        	}
	        	sqlStmt+=requests.getString(5)+"','"
						+requests.getString(6)+"',"
						+requests.getString(7)+","
						+requests.getString(8)+",'"
						+data[0]+"','"
						+data[1]+"',"
						+data[2]+")";
	        	
				//System.out.println(sqlStmt);	
	        	stmt.execute(sqlStmt);
	        }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private static void getRequests() {
		
		
		try
        {
            stmt = conn.createStatement();
            requests = stmt.executeQuery("select * from requests");
            
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
		
	}

	private static void createConnection() {
		try
        {
            Class.forName("org.mariadb.jdbc.Driver").newInstance();
            //Get a connection
            conn = DriverManager.getConnection(dbURL,"jegyuzer","Zsani123"); 
        }
        catch (Exception except)
        {
            except.printStackTrace();
        }
		
	}
	private static List<WebElement> waitForData(WebDriverWait wait) {
		List<WebElement> rows = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//div[@class='book_bundle_row has-2classes  js-book-bundling--row']")));
		return rows;
	}

	private static void goToReturnFlightPage(WebDriverWait wait, List<WebElement> rows) {
		WebElement field;
		rows.get(0).findElement(By.xpath(".//button[@data-type='economy']/span[@class='book-bundle-button--price']")).click();
		field=wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='stickybasket']/div/div/div[4]/div/button")));
		field.click();
	}

	private static void extractData(List<WebElement> rows, String direction) {
		//int bestPrice=9999;
		
		
		
		for (int i=0; i<rows.size();i++){
			WebElement d = rows.get(i).findElement(By.xpath(".//div[@class='book-bundle-flightentry--departure']/strong"));
			String departure=d.getText();

			d = rows.get(i).findElement(By.xpath(".//div[@class='book-bundle-flightentry--arrival']/strong"));
			String arrival=d.getText();

			d = rows.get(i).findElement(By.xpath(".//span[@class='book-bundle-button--price']"));
			String price=d.getText();
			
			if(price.equals("")){
				price="9999";
				
			}
			if (direction.equals("outbound")){	
				outbound.add(i, departure + "," + arrival + "," + price+",o");
			}else{
				inbound.add(i, departure + "," + arrival + "," + price+",i");
			}
		}
		
	}

	private static void clearAndSetFormFields(WebDriverWait wait,String id, String value,boolean sumbit) {
		WebElement field=wait.until(ExpectedConditions.presenceOfElementLocated(By.id(id)));
		field.clear();
		field.sendKeys(value);
		if (sumbit){
			field.submit();
		}
	}
	
	private static void setFormFields(WebDriverWait wait,String id, String value,boolean sumbit) {
		WebElement field=wait.until(ExpectedConditions.presenceOfElementLocated(By.id(id)));
		field.sendKeys(value);
		if (sumbit){
			field.submit();
		}
	}
	
}
