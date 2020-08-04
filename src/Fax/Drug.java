package Fax;

import javax.swing.JOptionPane;

import PBM.InsuranceFilter;
import PBM.InsuranceType;
import table.Record;

public enum Drug {
	//Local Anesthetic
	LidocaineTetracine("Pliaglis (Lidocaine 7% Tetracaine 7% Cream)","300 Grams","Apply a thin layer (2-3 gms) to affected area 3-4 times a day. Peel off after waiting the required application time as directed (max application time of 60 minutes).","LOCAL ANESTHETIC",new String[] {"71800063115"}),
	Lidocaine300("Lidocaine 5% Ointment ","300 Grams","Apply 2-3 gms to affected area(s) 3-4 times a day. (1 gram = 1 dime size).","LOCAL ANESTHETIC",new String[] {"65162091853","52565000855","68462041827"}),
	Lidocaine250("Lidocaine 5% Ointment ","250 Grams","Apply 2-3 gms to affected area(s) 3-4 times a day. (1 gram = 1 dime size).","LOCAL ANESTHETIC",new String[] {"65162091853","52565000855","68462041827"}),
	LidoPrilo360("Lidocaine 2.5% Prilocaine 2.5% Cream","360 Grams"," Apply 2-3 grams to the affected area 3-4 times daily.","LOCAL ANESTHETIC",new String[] {"50383066730","591207030","70512003030","115146845","52565000730"}),
	LidoPrilo240("Lidocaine 2.5% Prilocaine 2.5% Cream","240 Grams"," Apply 2-3 grams to the affected area 3-4 times daily.","LOCAL ANESTHETIC",new String[] {"50383066730","591207030","70512003030","115146845","52565000730"}),
	//Neurpathic Pain
	Doxepin("Doxepin 5% Cream","180 Grams","Apply a thin film (1-2 gms) to painful area 3 times daily. Wait at least 3-4 hours between applications. (1 gram = 1 dime size)","NEUROPATHIC PAIN",new String[] {"378811745"}),
	//Inflammation Management-Non-Sterodial
	Diclofenac3("Diclofenac 3% Topical Gel","300 Grams","Apply 2-3 gms to affected area(s) 3-4 times a day (1gram = 1 dime size).","INFLAMMATION MANAGEMENT: NON-STEROIDAL",new String[] {"00168084401","115148361","68462035594"}),
	Diclofenac1("Diclofenac 1% Topical Gel","300 Grams","Apply 2-3 gms to affected area(s) 3-4 times a day (1gram = 1 dime size).","INFLAMMATION MANAGEMENT: NON-STEROIDAL",new String[] {""}),
	//Inflammation Management-Sterodial
	Diflorasone360("Diflorasone Diacetate Ointment 0.05%","360 Grams"," Apply 2-3 gms to affected area(s) 3-4 times daily (1 gm =1 dime size).","INFLAMMATION MANAGEMENT: STEROID",new String[] {"71800000960","70512003160"}),
	Diflorasone180("Diflorasone Diacetate Ointment 0.05%","180 Grams"," Apply 1-2 gms to affected area(s) 2-3 times daily (1 gm =1 dime size).","INFLAMMATION MANAGEMENT: STEROID",new String[] {"71800000960","70512003160"}),
	Hydrocorisone("Hydrocortisone Butyrate Lotion 0.1%","236 mls","Apply 4 mls to the affected skin area 2 times daily, and rub in gently.","INFLAMMATION MANAGEMENT: STEROID",new String[] {"70512003204"}),
	Fluocinonide("Fluocinonide 0.1% Topical Cream ","240 Grams"," Apply 1-2 gm to affected area 3-4 times daily as directed. Maximum use 60g per week.","INFLAMMATION MANAGEMENT: STEROID",new String[] {"00168045704"}),
	
	Clobetasol360("Clobetasol 0.05% Ointment","360 Grams","Apply 2-3 grams to affected area(s) 3-4 times daily.","INFLAMMATION MANAGEMENT: STEROID",new String[] {"00168016260","713065660","51672125903","70710140103","52565003960","68462053065","70700010617"}),
	Clobetasol180("Clobetasol 0.05% Ointment","180 Grams","Apply 1-2 grams to affected area(s) 3-4 times daily. Maximum use 50g perweek","INFLAMMATION MANAGEMENT: STEROID",new String[] {"00168016260","713065660","51672125903","70710140103","52565003960","68462053065","70700010617"}),
	Desoximetasone360("Desoximetasone 0.05% Cream","360 Grams","Apply 2-3 grams to affected area(s) 3-4 times daily.","INFLAMMATION MANAGEMENT: STEROID",new String[] {"51672127103"}),
	Desoximetasone120("Desoximetasone 0.05% Cream","120 Grams","Apply 1-2 grams to affected area(s) 1-2 times daily.","INFLAMMATION MANAGEMENT: STEROID",new String[] {"51672127103"}),
	Triamcinolone("Triamcinolone Acetonide 0.147 mg/gm Topical Aerosol","400 Grams","Apply 2-3 sprays to affected area(s) 3-4 times daily (2 second spray = 1 gram)","INFLAMMATION MANAGEMENT: STEROID",new String[] {"71800015631"}),
	CalcipotreneBetaMethasoone("Calciporene BetaMethasone","","","INFLAMMATION MANAGEMENT: STEROID",new String[] {"00781716535"}),
	//Psoriasis
	Calcipotrene360("Calcipotriene 0.005% Topical Cream","360 Grams","Apply 2-3 gm to affected area 3-4 times daily as directed.","PSORIASIS",new String[] {"00781711783","16714076302","68462050166"}),
	Calcipotrene240("Calcipotriene 0.005% Topical Cream","240 Grams","Apply 2-3 gm to affected area 2-3 times daily as directed.","PSORIASIS",new String[] {"00781711783","16714076302","68462050166"}),
	//SCAR
	SilKPad("Sil-K Pad","4 Patches","Cut pad to fit scar with 1/4 inch beyond the scar on all sides. Apply patch and leave on for 8-12 hours per day then remove (Discard the pad and replace with a new one every 7 days)","SCAR",new String[] {"70350261501"}),
	//NSAID
	Fenoprofen("Fenoprofen 200mg Capsule","180 Capsules","Take 1-2 capsules by mouth up to 3 times daily","NSAID",new String[] {"69336012410"}),
	Ketoprofen("Ketoprofen 25 mg Capsule","180 Capsules","Take 1-2 capsules by mouth up to 3 times daily","NSAID",new String[] {"69336012710"}),
	NaproxenOralSuspension("Naproxen Oral Suspenion (125MG/5ML) ","946 ML","Take 10-20ml by mouth twice daily as needed for pain.","NSAID",new String[] {"68134020116","70868020016"}),
	Naproxen375("Naproxen CR 375 mg","60 Tablets","Take 1-2 tablet by mouth daily for pain.","NSAID",new String[] {"47781015301"}),
	Indomethacin("Indomethacin Oral Capsule 20 MG","180 Capsules","Take 2 capsules by mouth three times daily for pain","NSAID",new String[] {}),
	Ibuprofen800("Ibuprofen 800mg","60 Tablets","Take 2 tablets by mouth daily for pain.","NSAID",new String[] {}),
	//Muscle Relaxants
	Chlorzoxazone250("Chlorzoxazone 250mg Tablet","120 Tablets","Take 1 tablet by mouth 3 or 4 times daily for muscle spasm.","MUSCLE RELAXANT",new String[] {"69499033060"}),
	Chlorzoxazone375("Chlorzoxazone 375mg Tablet","120 Tablets","Take 1 tablet by mouth 3 or 4 times daily for muscle spasm.","MUSCLE RELAXANT",new String[] {"13811071710"}),
	Cyclobenzaprine("Cyclobenzaprine 7.5mg Tablet","120 Tablets","Take one tablet by mouth up to 4 times daily as need muscle spasm.","MUSCLE RELAXANT",new String[] {"69420100101","591333001","71800000301","52817033110"}),
	Metaxalone("Metaxalone 800mg Tablet","120 Tablets","Take 1 tablet by mouth 3-4 times daily.","MUSCLE RELAXANT",new String[] {"00591234101","591234101","64980047201"}),
	
	//Skin Emollinent
	Phlag("Phlag Rx Skin Emulsion","240 ML","Use 1-2 sprays on affected area and let dry re apply upto 4 times a day or as needed.","LOCAL ANESTHETIC", new String[] {"70569002502"}),
	
	//Fungal
	Econazole("Econazole Nitrate 1% Cream","340 Grams","Apply 3-6 grams to affected area(s) 3 times daily (1 gm=1 dime size)","ANTIFUNGAL",new String[] {"52565002285"}),
	Mupirocin("Mupirocin 2% Cream","360 Grams","Apply 2-3 grams to the affected area(s) 3-4 times daily.","ANTIFUNGAL",new String[] {"68462056435"}),
	Voriconazole("Voriconazole Inj 200mg","60 Vials","Fill Soaking device with warm water empty contentfs of 1-2 vials into soaking.","ANTIFUNGAL",new String[] {}),
	Vancomycin("Vancomycin 250 mg Capsule","180 Capsules","Fill basin with warm water add 6 cups into basin, wait 1 hour for capsule.","ANTIFUNGAL",new String[] {}),
	Gentamicin("Gentamicin 0.1% Cream","1800 Grams","Add 30g to foot bath and soak affected area for 15-20 minutes 1-2 times daily.","ANTIFUNGAL",new String[] {}),
	
	//Constipation
	Lactulose("Lactulose","60 Packets","Dissolve a 10 gram packet in 4 oz of water 2 times daily.","CONSTIPATION",new String[] {"69067001015"}),
	//Dietary
	Xvite("Xvite Tablet","60 Tablets","Take one table by mouth twice daily.","DIETARY SUPPLEMENT",new String[] {"69067004030"}),
	OmegaEthylEster("Omega-3 Acid Ethyl Esters","120 Capsules","Take one capsule four times a day.","",new String[] {"6050531707"}),
	//Cleaning
	AlcoholPad("Alcohol Pad","300"," Cleanse the skin by using 1 sterile pad on the affected area directly before applying any topical Cream / Gel / Solution","CLEANING",new String[] {"42423027101","47781015301","91237000128"});
	String name,qty,sig,therapy;
	String[] ndc;
	Drug(String name,String qty, String sig,String therapy,String[] ndc) {
		this.name = name;
		this.qty = qty;
		this.sig = sig;
		this.ndc = ndc;
		this.therapy = therapy;
	}
	public String getName() {
		return name;
	}
	public String getQty() {
		return qty;
	}
	public String getSig() {
		return sig;
	}
	public String[] getNDCs() {
		return ndc;
	}
	public String getTherapy() {
		return therapy;
	}
	public boolean IsSameDrug(Drug drug) {
		return this.getName().equalsIgnoreCase(drug.getName());
	}
	public static String GetADrug(String message) {
		Drug[] drugs = Drug.class.getEnumConstants();
		String[] names = new String[drugs.length];
		for(int i = 0;i<names.length;i++) {
			names[i] = drugs[i].getName()+" "+drugs[i].getQty();
		}
		Object selected = JOptionPane.showInputDialog(null, message, "Selection", JOptionPane.DEFAULT_OPTION, null, names, names[0]);
		if(selected==null)
			return "";
		return selected.toString();
	}
	public static Drug GetDrug(String name) {
		Drug[] drugs = Drug.class.getEnumConstants();
		for(Drug drug: drugs) {
			String name_qty = drug.getName()+" "+drug.getQty();
			if(name.equalsIgnoreCase(name_qty))
				return drug;
		}
		return null;
	}
	public static Drug GetDrugByNdc(String ndc) {
		Drug[] drugs = Drug.class.getEnumConstants();
		for(Drug drug: drugs) {
			for(String ndcs: drug.getNDCs()) {
				if(ndc.equalsIgnoreCase(ndcs))
					return drug;
			}
		}
		return null;
	}
	public static Drug GetTopicalScript(Record record) {
		switch(record.getBin()) {
			case "610014":
			case "003858":
			case "400023":
				return Diflorasone360;
			case "017010":
				return Diflorasone360;
			//Argus
			case "600428":
			{
				switch(record.getPcn()) {
					case "02960000":
						return Phlag;
					default:
						return Clobetasol360;
				}
				
			}
			//Medimpact 
			case "015574":
			//Optum Rx
			case "610097": {
				switch(record.getGrp().toUpperCase()) {
					case "SHCA":
						return Clobetasol360;
					default:
						return Diflorasone360;
				}
			}
			case "610494":
				return Diflorasone360;
			case "610011":
				return Diflorasone360;
			//Prime Therapeutics
			case "610455":
				return Diflorasone360;
			case "011552":{
				if(record.getPcn().equalsIgnoreCase("BCTX") || record.getPcn().equalsIgnoreCase("ILDR"))
					return null; //Prime Private
			}
			//Caremark	
			case "020099":
				return Diflorasone360;
			case "020107":
			case "020115":
			case "610502":
				return Clobetasol360;
			case "004336": {
					//Private Caremark
					if(record.getPcn().equalsIgnoreCase("ADV")) {
						if(InsuranceFilter.Filter(record).equalsIgnoreCase(InsuranceType.PRIVATE_VERIFIED))
							return Phlag;
						else 
							return Clobetasol360;
					}
					//Wellcare and Silver Scripts
					else if(record.getGrp().equalsIgnoreCase("RXCVSD") || record.getGrp().equalsIgnoreCase("788257"))
						return Drug.Clobetasol360;
					//Good MEDDADV?
					else if(record.getPcn().equalsIgnoreCase("MEDDADV")) {
						switch(record.getGrp().toUpperCase()) {
							case "RX6270":
							case "FCHP":
								return Diflorasone360;
							default:
								return Clobetasol360;
						}
					}

			}
			default: 
				System.out.println("DEFAULT: "+record.getBin()+" "+record.getGrp()+" "+record.getPcn());
				return Drug.Diflorasone360;
		}
	}
	public static Drug GetOralScript(Record record) {
		switch(record.getBin()) {
			case "610455":
				return NaproxenOralSuspension;
			case "610011":
			{
				switch(record.getPcn().toUpperCase()) {
					case "IRX":
						return Fenoprofen;
				}
			}
			case "011552":
				return Ketoprofen;
			case "610014":
			case "003858":
			case "400023":
				return Ketoprofen;
			case "610097":
				return Ketoprofen;
			case "004336":
			case "020099":
			case "020107":
			case "020115":
			case "610502":
				return Cyclobenzaprine;
			default:
				return null;
		}
	}
}
