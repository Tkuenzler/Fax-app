package Clients;

import java.io.IOException;
import java.util.logging.Level;
import org.apache.commons.logging.LogFactory;
import com.gargoylesoftware.htmlunit.AjaxController;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;

import table.Record;

public class PlanLookup {
	WebClient webClient = new WebClient(BrowserVersion.CHROME);
	CookieManager cookieManager;
	public PlanLookup() {
		cookieManager = webClient.getCookieManager();
		cookieManager.setCookiesEnabled(true);
		webClient.setCookieManager(cookieManager);
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setUseInsecureSSL(false);
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		webClient.setAjaxController(new AjaxController(){
			@Override
		    public boolean processSynchron(HtmlPage page, WebRequest request, boolean async) {
		        return true;
		    }
		});
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
	    java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF); 
	    java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
	}
	public String GetCountyCode(String zip) {
		try {
			HtmlPage page1 = webClient.getPage("https://www.uscounties.com/zipcodes/search.pl?query="+zip);
			HtmlTable table = (HtmlTable) page1.getByXPath("//table").get(2);
			HtmlTableRow row = table.getRow(1);
			HtmlTableCell cell = row.getCell(4);
			return cell.asText();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return e.getMessage();
		} finally {
			webClient.close();
		}	
	}
	public void GetPlan(String contractId,String benefitId,String fip,Record record) {
		try {
			HtmlPage page1 = webClient.getPage("https://q1medicare.com/MedicareAdvantage-2020C-MedicareHealthPlanBenefits.php?state="+record.getState()+"&source=2020MAFinder&ZIP="+record.getZip()+"&countyCode="+fip
					+"&contractId="+contractId+"&planId="+benefitId);
			System.out.println(page1.asText());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			webClient.close();
		}	
	}
	public void GetPlan(String contractId,String benefitId,String fip,String state,String zip) {
		try {
			HtmlPage page1 = webClient.getPage("https://q1medicare.com/MedicareAdvantage-2020C-MedicareHealthPlanBenefits.php?state="+state+"&source=2020MAFinder&ZIP="+zip+"&countyCode="+fip
					+"&contractId="+contractId+"&planId="+benefitId);
			System.out.println(page1.getUrl().toString());
			System.out.println(page1.asText());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			webClient.close();
		}	
	}
}
