package Clients;

import java.io.IOException;
import java.util.logging.Level;

import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.AjaxController;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import Properties.EmdeonProperties;
import objects.Emdeon;

public class EZScriptRxClient {
	WebClient webClient = new WebClient(BrowserVersion.CHROME);
	CookieManager cookieManager;
	HtmlPage lookUpPage;
	
	private class ElementIds {
		public static final String LOGIN_FORM = "loginform";
		public static final String USERNAME_INPUT = "log";
		public static final String PASSWORD_INPUT = "pwd";
		public static final String LOGIN_BUTTON = "wp-submit";
	}
	public EZScriptRxClient() {
		setWebClientOptions();
	}
	private void setWebClientOptions() {
		cookieManager = webClient.getCookieManager();
		cookieManager.setCookiesEnabled(true);
		webClient.setCookieManager(cookieManager);
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setUseInsecureSSL(false);
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
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
	public boolean Login(String user,String password) {
		HtmlPage page1;
		try {
			page1 = webClient.getPage("https://login.ezscriptrx.com/wp-login.php");
			System.out.println(page1.asText());
			HtmlForm form = page1.getFormByName(ElementIds.LOGIN_FORM);
						
			HtmlTextInput user_name = form.getInputByName(ElementIds.USERNAME_INPUT);
			HtmlPasswordInput pwd = form.getInputByName(ElementIds.PASSWORD_INPUT);
			user_name.setValueAttribute(user);
			pwd.setValueAttribute(password);
			
			HtmlSubmitInput button = form.getInputByName(ElementIds.LOGIN_BUTTON);
			
			lookUpPage = button.click();
			
			System.out.println(lookUpPage.asXml());
			//System.out.println(lookUpPage.asText());
			System.out.println("LOGGED IN");
			return true;
			
		} catch (FailingHttpStatusCodeException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (ElementNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}
}
