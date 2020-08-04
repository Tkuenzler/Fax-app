package PivotTable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONException;
import org.json.JSONObject;

import Fax.Drug;
import subframes.FileChooser;
import table.Record;

public class LoadData {
	public static final String BACH_LIST = "http://ltf5469.tam.us.siteprotect.com/Data/Bach.txt";
	public static final String LAKE_IDA_LIST = "http://ltf5469.tam.us.siteprotect.com/Data/Lake%20Ida.txt";
	public static final String MARK_LIST = "http://ltf5469.tam.us.siteprotect.com/Data/Mark.txt";
	public static final String FUSION_LIST = "http://ltf5469.tam.us.siteprotect.com/Data/Fusion.txt";
	public JSONObject data;
	public LoadData() {
		
	}
	public String getList() {
		String[] pharmacies = {"Bach","Lake Ida","Mark","Fusion"};
		String pharmacy = (String) JOptionPane.showInputDialog(new JFrame(), "What pharmacy would you like to load data from?", "Pharmacy:", JOptionPane.QUESTION_MESSAGE, null, pharmacies, pharmacies[0]);
		switch(pharmacy) {
			case "Bach":
				return BACH_LIST;
			case "Lake Ida":
				return LAKE_IDA_LIST;
			case "Mark":
				return MARK_LIST;
			case "Fusion":
				return FUSION_LIST;
			default: return null;
		}
	}
	public void GetData(String list) throws IOException, JSONException{
		HttpURLConnection connection;
		connection = (HttpURLConnection) new URL(list).openConnection();
		StringBuilder sb = new StringBuilder();
		BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String line = null;
		while((line=br.readLine())!=null) {
			sb.append(line);
		}
		data = new JSONObject(sb.toString());
		br.close();
	}
	public void LoadAjudicationData() {
		String[] pharmacies = {"Bach","Lake Ida","Mark","Fusion"};
		String pharmacy = (String) JOptionPane.showInputDialog(new JFrame(), "What pharmacy would you like to load data from?", "Pharmacy:", JOptionPane.QUESTION_MESSAGE, null, pharmacies, pharmacies[0]);
		File file = null;
		switch(pharmacy) {
			case "Bach":
				file = FileChooser.OpenXlsxFile("Load Bach Data");
				LoadBachList(file);
				break;
			case "Lake Ida":
				file = FileChooser.OpenCsvFile("Load Lake Ida Data");
				LoadLakeIdaList(file);
				break;
			case "Mark":
				file = FileChooser.OpenXlsxFile("Load Mark Data");
				LoadMarkFile(file);
				return;
			case "Fusion":
				file = FileChooser.OpenXlsxFile("Load Mark Data");
				LoadFusionFile(file);
				return;
		}
	}
	public void SaveData() {
		BufferedWriter bw = null;
		File file = FileChooser.SaveTxtFile();
		try {
			bw = new BufferedWriter(new FileWriter(file));
			bw.write(data.toString());
			bw.close();
		} catch(IOException e) {
		
		} finally {
			try {
				bw.close();
			} catch(IOException e) {
				e.printStackTrace();
			}	
		} 
	}
	private void LoadFusionFile(File file) {
		XSSFWorkbook myExcelBook = null;
		JSONObject obj = new JSONObject();
		try {
			myExcelBook = new XSSFWorkbook(new FileInputStream(file)); 
			XSSFSheet sheet = myExcelBook.getSheetAt(0);
			Iterator<Row> iterator = sheet.iterator();
			boolean header = false;
			while (iterator.hasNext()) {
				if(!header) {
	       		 iterator.next();
	       		 header = true;
				}
				Row row = iterator.next();
				System.out.println("COLUMNS: "+row.getLastCellNum());
				if(row.getLastCellNum()!=10)
					continue;
				if(!Entry.IsValidNDC(row))
					continue;
				if(!Entry.IsBin(row, "610097"))
					continue;
				Entry entry = new Entry();
				entry.LoadMarkList(row);
				if(entry.getPcn()==null)
					entry.setPcn("");
				if(entry.getGrp()==null)
					entry.setGrp("");
				AddEntry(obj,entry);	
			}
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		} catch(JSONException e) {
			e.printStackTrace();
		} finally {
			try {
				myExcelBook.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.data = obj;
	}
	private void LoadMarkFile(File file) {
		XSSFWorkbook myExcelBook = null;
		JSONObject obj = new JSONObject();
		try {
			myExcelBook = new XSSFWorkbook(new FileInputStream(file)); 
			XSSFSheet sheet = myExcelBook.getSheetAt(0);
			Iterator<Row> iterator = sheet.iterator();
			boolean header = false;
			while (iterator.hasNext()) {
				if(!header) {
	       		 iterator.next();
	       		 header = true;
				}
				Row row = iterator.next();
				System.out.println(row.getLastCellNum());
				if(row.getLastCellNum()!=39)
					continue;
				if(!Entry.IsMarkRowProfitable(row))
					continue;
				if(!Entry.IsValidNDC(row))
					continue;
				if(!Entry.IsBin(row, "610097"))
					continue;
				Entry entry = new Entry();
				entry.LoadMarkList(row);
				if(entry.getPcn()==null)
					entry.setPcn("");
				if(entry.getGrp()==null)
					entry.setGrp("");
				AddEntry(obj,entry);	
			}
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		} catch(JSONException e) {
			e.printStackTrace();
		} finally {
			try {
				myExcelBook.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.data = obj;
	}
	private void LoadLakeIdaList(File file) {
		BufferedReader br = null;
		JSONObject obj = new JSONObject();
		Entry entry = null;
		try {
			br = new BufferedReader(new FileReader(file));
			String line = null;
			String header = br.readLine();
			while((line=br.readLine())!=null) {
				String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
				entry = new Entry();
				entry.LoadLakeIda(data);
				if(entry.getPcn()==null)
					entry.setPcn("");
				if(entry.getGrp()==null)
					entry.setGrp("");
				AddEntry(obj,entry);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch(JSONException e) {
			e.printStackTrace();
		} 
		finally {
			try {
				if (br != null)br.close();
				System.out.println("CLOSED");
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		this.data = obj;
	}
	private void LoadBachList(File file) {
		XSSFWorkbook myExcelBook = null;
		JSONObject obj = new JSONObject();
		try {
			myExcelBook = new XSSFWorkbook(new FileInputStream(file)); 
			XSSFSheet sheet = myExcelBook.getSheetAt(0);
			Iterator<Row> iterator = sheet.iterator();
			boolean header = false;
			while (iterator.hasNext()) {
				if(!header) {
	       		 iterator.next();
	       		 header = true;
				}
				Row row = iterator.next();
				System.out.println(row.getLastCellNum());
				if(row.getLastCellNum()!=8)
					continue;
				Entry entry = new Entry();
				entry.LoadBachList(row);
				if(entry.getPcn()==null)
					entry.setPcn("");
				if(entry.getGrp()==null)
					entry.setGrp("");
				AddEntry(obj,entry);	
			}
			
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		} catch(JSONException e) {
			e.printStackTrace();
		} finally {
			try {
				myExcelBook.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.data = obj;
	}
	private JSONObject LoadProductList(Record record) {
		if(!data.has(record.getBin()))
			return null;
		try {
			JSONObject bin = data.getJSONObject(record.getBin());
			if(!bin.getJSONObject("PCNs").has(translatePCN(record))) 
				return bin;
			JSONObject pcn = bin.getJSONObject("PCNs").getJSONObject(translatePCN(record));
			if(!pcn.getJSONObject("GRPs").has(translateGRP(record))) 
				return pcn;	
			JSONObject grp = pcn.getJSONObject("GRPs").getJSONObject(translateGRP(record));
			return grp;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	private String translatePCN(Record record) {
		if(record.getBin().equalsIgnoreCase("610014") && record.getPcn().equalsIgnoreCase("No Data Returned"))
			return "PEU";
		else if(record.getPcn().equalsIgnoreCase("No Data Returned"))
			return "";
		else if(record.getBin().equalsIgnoreCase("017010") && !record.getPcn().equalsIgnoreCase("CIMCARE"))
			return ""+Integer.parseInt(record.getPcn());
		else if(record.getBin().equalsIgnoreCase("610127"))
			return ""+Integer.parseInt(record.getPcn());
		else if(record.getBin().equalsIgnoreCase("015581"))
			return ""+Integer.parseInt(record.getPcn());
		else if(record.getBin().equalsIgnoreCase("610502") && !record.getPcn().equalsIgnoreCase("MEDDAET"))
			return ""+Integer.parseInt(record.getPcn());
		else
			return record.getPcn();
		
	}
	private String translateGRP(Record record) {
		if(record.getGrp().equalsIgnoreCase("No Data Returned"))
			return "";
		else if(record.getPcn().equalsIgnoreCase("BCTX"))
			return ""+Integer.parseInt(record.getGrp());
		
		else if(record.getBin().equalsIgnoreCase("610127"))
			return ""+Integer.parseInt(record.getGrp());
		else
			return record.getGrp();
	}
	public Drug[] GetDrugs(Record record) {
		try {
			JSONObject list = LoadProductList(record);
			if(list==null)
				return null;
			JSONObject data = list.getJSONObject("Data");
			JSONObject products = list.getJSONObject("Products");
			double totalCount = data.getInt("Count");
			Iterator keys = products.keys();
			TreeMap<String, Double> treemap = new TreeMap<String, Double>();
			while (keys.hasNext()) {
			    String key = (String)keys.next();
			    if(key.equalsIgnoreCase("Data") || key.equalsIgnoreCase("UNKNOWN") || key.equalsIgnoreCase("WOUND CARE"))
			    	continue;
			    JSONObject product = products.getJSONObject(key);
			    JSONObject productData = product.getJSONObject("Data");
			    double count = productData.getInt("Count");
			    double value = (((count/totalCount)*100)*productData.getDouble("AverageProfit"))/100;
			    treemap.put((String)key, value);
			}
			String[] therapies = GetTherapies(treemap);
			String[] names = new String[3];
			Drug[] drugs = new Drug[3];
			for(int i = 0;i<therapies.length;i++) {
				if(therapies[i]==null)
					continue;
				String name = GetTopDrug(products,therapies[i],totalCount);
				if(name!=null)
					names[i] = name;
			}
			for(int i = 0;i<names.length;i++) {
				if(names[i]==null)
					continue;
				else 
					drugs[i] = Drug.GetDrug(names[i]);
			}
			return drugs;
		} catch(JSONException e) {
			e.printStackTrace();
		}
		System.out.println("RETURNING NULL");
		return null;
	}
	public Drug GetTopical(Record record) {
		if(record.getBin().equalsIgnoreCase("020115"))
			return Drug.Clobetasol360;
		try {
			JSONObject list = LoadProductList(record);
			if(list==null)
				return null;
			JSONObject data = list.getJSONObject("Data");
			JSONObject products = list.getJSONObject("Products");
			double totalCount = data.getInt("Count");
			Iterator categoryKeys = products.keys();
			TreeMap<String, Double> treemap = new TreeMap<String, Double>();
			while (categoryKeys.hasNext()) {
			    String categoryKey = (String)categoryKeys.next();
			    switch(categoryKey) {
			    	case "LOCAL ANESTHETIC":
			    	case "NEUROPATHIC PAIN":
			    	case "INFLAMMATION MANAGEMENT: NON-STEROIDAL":
			    	case "INFLAMMATION MANAGEMENT: STEROID":
			    		break;
			    	default:
			    		continue;
			    }
			    JSONObject drugs = products.getJSONObject(categoryKey);
			    Iterator drugKeys = drugs.keys();
			    while(drugKeys.hasNext()) {
			    	 String drugKey = (String)drugKeys.next();
			    	 if(drugKey.equalsIgnoreCase("Data"))
			    		 continue;
			    	 JSONObject drug = drugs.getJSONObject(drugKey);
					 JSONObject productData = drug.getJSONObject("Data");
					 double count = productData.getInt("Count");
					 double value = (((count/totalCount)*100)*productData.getDouble("AverageProfit"))/100;
					 treemap.put((String)drugKey, value);
			    }
			}
			String name = GetTop(treemap);
			return Drug.GetDrug(name);
		} catch(JSONException e) {
			e.printStackTrace();
		}
		System.out.println("RETURNING NULL");
		return null;
	}
	public Drug GetOral(Record record) {
		try {
			JSONObject list = LoadProductList(record);
			if(list==null)
				return null;
			JSONObject data = list.getJSONObject("Data");
			JSONObject products = list.getJSONObject("Products");
			double totalCount = data.getInt("Count");
			Iterator categoryKeys = products.keys();
			TreeMap<String, Double> treemap = new TreeMap<String, Double>();
			while (categoryKeys.hasNext()) {
			    String categoryKey = (String)categoryKeys.next();
			    switch(categoryKey) {
			    	case "MUSCLE RELAXANT":
			    	case "NSAID":
			    		break;
			    	default:
			    		continue;
			    }
			    JSONObject drugs = products.getJSONObject(categoryKey);
			    Iterator drugKeys = drugs.keys();
			    while(drugKeys.hasNext()) {
			    	 String drugKey = (String)drugKeys.next();
			    	 if(drugKey.equalsIgnoreCase("Data"))
			    		 continue;
			    	 JSONObject drug = drugs.getJSONObject(drugKey);
					 JSONObject productData = drug.getJSONObject("Data");
					 double count = productData.getInt("Count");
					 double value = (((count/totalCount)*100)*productData.getDouble("AverageProfit"))/100;
					 treemap.put((String)drugKey, value);
			    }
			}
			String name = GetTop(treemap);
			return Drug.GetDrug(name);
		} catch(JSONException e) {
			e.printStackTrace();
		}
		System.out.println("RETURNING NULL");
		return null;
	}
	private void AddEntry(JSONObject obj, Entry entry) throws JSONException {
		if(entry.profit<0)
			return;
		AddBinProductData(obj,entry);
	}
	private void AddGrpProductData(JSONObject obj,Entry entry) throws JSONException {
		if(!obj.has("GRPs"))
			obj.put("GRPs", new JSONObject());
		JSONObject grps = obj.getJSONObject("GRPs");
		if(!grps.has(entry.getGrp()))
			grps.put(entry.getGrp(), new JSONObject());
		JSONObject grp = grps.getJSONObject(entry.getGrp());
		addData(grp,entry);	
		if(!grp.has("Products"))
			grp.put("Products", new JSONObject());
		JSONObject products = grp.getJSONObject("Products");
		if(!products.has(entry.getDrugGroup()))
			products.put(entry.getDrugGroup(), new JSONObject());
		JSONObject drugGroup = products.getJSONObject(entry.getDrugGroup());
		addData(drugGroup,entry);	
		if(!drugGroup.has(entry.getDrugName()))
			drugGroup.put(entry.getDrugName(), new JSONObject());
		JSONObject drug = drugGroup.getJSONObject(entry.getDrugName());
		addData(drug,entry);
	}
	private void AddPcnProductData(JSONObject obj,Entry entry) throws JSONException {
		if(!obj.has("PCNs"))
			obj.put("PCNs", new JSONObject());
		JSONObject pcns = obj.getJSONObject("PCNs");
		if(!pcns.has(entry.getPcn()))
			pcns.put(entry.getPcn(), new JSONObject());
		JSONObject pcn = pcns.getJSONObject(entry.getPcn());
		addData(pcn,entry);	
		if(!pcn.has("Products"))
			pcn.put("Products", new JSONObject());
		JSONObject products = pcn.getJSONObject("Products");
		if(!products.has(entry.getDrugGroup()))
			products.put(entry.getDrugGroup(), new JSONObject());
		JSONObject drugGroup = products.getJSONObject(entry.getDrugGroup());
		addData(drugGroup,entry);	
		if(!drugGroup.has(entry.getDrugName()))
			drugGroup.put(entry.getDrugName(), new JSONObject());
		JSONObject drug = drugGroup.getJSONObject(entry.getDrugName());
		addData(drug,entry);
		AddGrpProductData(pcn,entry);
	}
	private void AddBinProductData(JSONObject obj,Entry entry) throws JSONException {
		if(!obj.has(entry.getBin()))
			obj.put(entry.getBin(), new JSONObject());
		JSONObject bins = obj.getJSONObject(entry.getBin());
		addData(bins,entry);	
		if(!bins.has("Products"))
			bins.put("Products", new JSONObject());
		JSONObject products = bins.getJSONObject("Products");
		if(!products.has(entry.getDrugGroup()))
			products.put(entry.getDrugGroup(), new JSONObject());
		JSONObject drugGroup = products.getJSONObject(entry.getDrugGroup());
		addData(drugGroup,entry);	
		if(!drugGroup.has(entry.getDrugName()))
			drugGroup.put(entry.getDrugName(), new JSONObject());
		JSONObject drug = drugGroup.getJSONObject(entry.getDrugName());
		addData(drug,entry);
		AddPcnProductData(bins,entry);
	}
	
	private void addData(JSONObject obj,Entry entry) throws JSONException {
		JSONObject data = null;
		if(!obj.has("Data"))
			data = new JSONObject();
		else
			data = obj.getJSONObject("Data");
		if(!data.has("SumOfProfit"))	
			data.put("SumOfProfit", 0);
		if(!data.has("Count"))
			data.put("Count", 0);
		if(!data.has("AverageProfit"))
			data.put("AverageProfit", 0);
		double profit = data.getDouble("SumOfProfit");
		profit += entry.getProfit();
		data.put("SumOfProfit", profit);
		double count = data.getDouble("Count");
		count++;
		data.put("Count", count);	
		data.put("AverageProfit", profit/count);
		obj.put("Data", data);
	}
	
	private String GetTopDrug(JSONObject products,String category,double totalCount) throws JSONException {
		JSONObject obj = products.getJSONObject(category);
		Iterator keys = obj.keys();
		TreeMap<String, Double> treemap = new TreeMap<String, Double>();
		while (keys.hasNext()) {
		    String key = (String)keys.next();
		    if(key.equalsIgnoreCase("Data"))
		    	continue;
		    JSONObject productData = obj.getJSONObject(key).getJSONObject("Data");
		    double count = productData.getInt("Count");
		    double value = (((count/totalCount)*100)*productData.getDouble("AverageProfit"))/100;
		    treemap.put((String)key, value);
		}
		return GetTop(treemap);
	}
	private String[] GetTherapies(TreeMap<String,Double> treemap) {
		String[] therapies = new String[3];
		Map<String, Double> sortedMap = sortByValues(treemap);
		Set<java.util.Map.Entry<String, Double>> set = sortedMap.entrySet();
		// Get iterator
		Iterator<java.util.Map.Entry<String, Double>> it = set.iterator();
		    // Show TreeMap elements
		    int count = 0;
		    while(it.hasNext()) {
		      Map.Entry pair = (Map.Entry)it.next();
		      therapies[count] = (String) pair.getKey();
		      count++;
		      if(count==3)
		    	  return therapies;
		    }
		 return therapies;
	}
	private String GetTop(TreeMap<String,Double> treemap) {
		Map<String, Double> sortedMap = sortByValues(treemap);
		Set<java.util.Map.Entry<String, Double>> set = sortedMap.entrySet();
		// Get iterator
		Iterator<java.util.Map.Entry<String, Double>> it = set.iterator();
		// Show TreeMap elements
		while(it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();
		    return (String)pair.getKey();
		}
		return null;
	}
	public static <K, V extends Comparable<V>> Map<K, V> 
	  sortByValues(final Map<K, V> map) {
	    Comparator<K> valueComparator = 
	             new Comparator<K>() {
	      public int compare(K k1, K k2) {
	        int compare = 
	              map.get(k2).compareTo(map.get(k1));
	        if (compare == 0) 
	          return 1;
	        else 
	          return compare;
	      }
	    };
	 
	    Map<K, V> sortedByValues = 
	      new TreeMap<K, V>(valueComparator);
	    sortedByValues.putAll(map);
	    return sortedByValues;
	    }
}
