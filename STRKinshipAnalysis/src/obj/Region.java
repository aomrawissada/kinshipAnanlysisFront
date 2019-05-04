package obj;

import java.util.ArrayList;

public class Region {
	
	private String regionName;
	ArrayList<AlleleFreq> data = new ArrayList<AlleleFreq>();
	
	public Region() {}
	
	public boolean inSertAlleleFreq(AlleleFreq newFreq) {
		if(newFreq.getRegionName()!=this.regionName) {
			return false;
		}
		for(AlleleFreq a : data) {
			if(a.getAllele()==newFreq.getAllele()) {
				return false;
			}
		}
		data.add(newFreq);
		return true;
	}
	
	public String getRegionName() {
		return regionName;
	}
	public ArrayList<AlleleFreq> getData() {
		return data;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public void setData(ArrayList<AlleleFreq> data) {
		this.data = data;
	}
	
	
}
