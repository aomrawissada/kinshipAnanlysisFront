package obj;

import tools.ObjManager;

public class AlleleFreq{
	
	private String regionName;
	private String locus;
	private Float allele;
	private Float freq;
	
	public AlleleFreq() {
		ObjManager.getInstance().updateRegion(this);
	}

	public String getRegionName() {
		return regionName;
	}

	public String getLocus() {
		return locus;
	}

	public Float getAllele() {
		return allele;
	}

	public Float getFreq() {
		return freq;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public void setLocus(String locus) {
		this.locus = locus;
	}

	public void setAllele(Float allele) {
		this.allele = allele;
	}

	public void setFreq(Float freq) {
		this.freq = freq;
	}
	
}
