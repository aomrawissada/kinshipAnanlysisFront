package obj;

import tools.ObjManager;

public class StrData {
	
	private String sampleID;
	private int sampleYear;
	private int type;
	private String locus;
	private Float allele;
	private int readCount;
	private String sequence;
	private Float genotype1;
	private Float genotype2;
	
	public StrData() {
		ObjManager.getInstance().updatePersonInfo(this);
	}

	public String getSampleID() {
		return sampleID;
	}

	public int getSampleYear() {
		return sampleYear;
	}

	public int getType() {
		return type;
	}

	public String getLocus() {
		return locus;
	}

	public Float getAllele() {
		return allele;
	}

	public int getReadCount() {
		return readCount;
	}

	public String getSequence() {
		return sequence;
	}

	public Float getGenotype1() {
		return genotype1;
	}

	public Float getGenotype2() {
		return genotype2;
	}

	public void setSampleID(String sampleID) {
		this.sampleID = sampleID;
	}

	public void setSampleYear(int sampleYear) {
		this.sampleYear = sampleYear;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setLocus(String locus) {
		this.locus = locus;
	}

	public void setAllele(Float allele) {
		this.allele = allele;
	}

	public void setReadCount(int readCount) {
		this.readCount = readCount;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public void setGenotype1(Float genotype1) {
		this.genotype1 = genotype1;
	}

	public void setGenotype2(Float genotype2) {
		this.genotype2 = genotype2;
	}

}