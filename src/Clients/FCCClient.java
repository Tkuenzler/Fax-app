package Clients;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import subframes.FileChooser;

public class FCCClient {
	public static String GetComplaint(String phone) throws IOException {
		String url = "https://opendata.fcc.gov/resource/3xyp-aqkj.json?caller_id_number="+phone;
		HttpGet get = new HttpGet(url);
		CloseableHttpClient client = HttpClients.createDefault();
		get.setHeader("Accept", "application/json");
		get.setHeader("X-App-Token", "ehto61eu5cat7g2MS1yx0qh6h");
		CloseableHttpResponse response = client.execute(get);
		int status_code = response.getStatusLine().getStatusCode();
		HttpEntity entity = response.getEntity();
		String responseString = EntityUtils.toString(entity, "UTF-8");
		client.close();
		response.close();
		if(status_code==200)
			return responseString;
		else
			return "INVALID STATUS CODE "+status_code;
	}
	public static void GetComplaintsFromNumbers() {
		File file = FileChooser.OpenCsvFile("Phone number list");
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			String line = null;
			String header = br.readLine();
			while((line=br.readLine())!=null) {
				if(line.equalsIgnoreCase(""))
					continue;
				else if(line.length()==10) {
					String phone = line.substring(0, 3)+"-"+line.substring(3, 6)+"-"+line.substring(6, 10);
					System.out.println(phone);
					System.out.println(GetComplaint(phone));
				}
			}
		}  catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
				System.out.println("CLOSED");
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}
