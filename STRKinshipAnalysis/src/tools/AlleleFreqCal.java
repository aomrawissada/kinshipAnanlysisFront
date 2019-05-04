package tools;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import mysql.MysqlConnector;


public class AlleleFreqCal {
	
	private Date lastUpdate;
	private MysqlConnector connector = MysqlConnector.getInstance();
	
	private ArrayList<Data> dataList = new ArrayList<Data>();
	
	private static AlleleFreqCal instance;
	
	private AlleleFreqCal() {};
	
	public static AlleleFreqCal getInstance() {
		if (instance == null) {
             instance = new AlleleFreqCal();
            }
        return instance;
	}
	
	private boolean isUpToDate() {
		if(lastUpdate == null) {
			return false;
		}
		if(Calendar.getInstance().getTime().getTime()-lastUpdate.getTime()<3600000) {
			return true;
		}else {
			return false;
		}
	}
	
	public void updateFreq() {
		if(isUpToDate()) {
			System.out.println("No update");
			return;
		}
		dataList = new ArrayList<Data>();
		if(connector.queryAlleleForCalculateFreq()) {
			calculateFreq();
		}
		setLastUpdate(Calendar.getInstance().getTime());
	}
	
	private void calculateFreq() {
		ArrayList<String> regions = new ArrayList<String>();
		for(Data d : dataList) {
			if(!regions.contains(d.region)) {
				regions.add(d.region);
			}
		}
		for(String r : regions) {
			ArrayList<String> locus = new ArrayList<String>();
			for(Data d : dataList) {
				if(r.equals(d.region)) {
					if(!locus.contains(d.locus)) {
						locus.add(d.locus);
					}
				}
			}
			for(String l : locus) {
				int counter = 0;
				for(Data d : dataList) {
					if(r.equals(d.region) && l.equals(d.locus)) {
						counter+= d.n;
					}
				}
				for(Data d : dataList) {
					if(r.equals(d.region) && l.equals(d.locus)) {
						d.setFreq(((float)d.n)/counter);
						connector.updateFreq(d.freq, d.region, d.locus, d.allele);
					}
				}
			}
		}
	}
	
	public void addData(String region,String locus, Float allele) {
		boolean found = false;
		for(Data d : dataList) {
			if(d.region.equals(region) && d.locus.equals(locus) && d.allele.equals(allele)) {
				found = true;
				d.count();
			}
		}
		if(!found){
			dataList.add(new Data(region,locus,allele));
		}
	}
	
	public class Data{
		String region;
		String locus;
		Float allele;
		int n;
		Float freq;
		
		public Data(String region,String locus,Float allele) {
			this.region = region;
			this.locus = locus;
			this.allele = allele;
			this.n = 1;
		}

		public void count() {
			n++;
		}

		public void setFreq(Float freq) {
			this.freq = freq;
		}
	}
	
	public Date getLastUpdate() {
		return lastUpdate;
	}

	private void setLastUpdate(Date date) {
		this.lastUpdate = date;
	}
	
	public void printData() {
		for(Data d : dataList) {
			System.out.println(d.region+" "+d.locus+" "+d.freq);
		}
	}
	
}
