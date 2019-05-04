package obj;

import java.util.ArrayList;

import tools.ObjManager;

public class PersonInfo{
	
	private String sampleID;
	private int sampleYear;
	private String PID;
	private String name;
	private String Surname;
	private String gender;
	private String Region;
	private int Age;
	private String Race;
	
	private ArrayList<StrData> strList = new ArrayList<StrData>();
	
	public PersonInfo() {
		ObjManager.getInstance().insertPersonInfo(this);
	}
	
	public boolean insertData(StrData newData) {
		if(newData.getSampleID()!=this.sampleID) {
			return false;
		}
		for(StrData strData : strList) {
			if(strData.getLocus() == newData.getLocus()) {
				return false;
			}
		}
		strList.add(newData);
		return true;
	}
	
	public ArrayList<StrData> getStrList() {
		return strList;
	}

	public void setStrList(ArrayList<StrData> strList) {
		this.strList = strList;
	}

	public String getSampleID() {
		return sampleID;
	}

	public int getSampleYear() {
		return sampleYear;
	}

	public String getPID() {
		return PID;
	}

	public String getName() {
		return name;
	}

	public String getSurname() {
		return Surname;
	}

	public String getGender() {
		return gender;
	}

	public String getRegion() {
		return Region;
	}

	public int getAge() {
		return Age;
	}

	public String getRace() {
		return Race;
	}

	public void setSampleID(String sampleID) {
		this.sampleID = sampleID;
	}

	public void setSampleYear(int sampleYear) {
		this.sampleYear = sampleYear;
	}

	public void setPID(String pID) {
		PID = pID;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSurname(String surname) {
		Surname = surname;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public void setRegion(String region) {
		Region = region;
	}

	public void setAge(int age) {
		Age = age;
	}

	public void setRace(String race) {
		Race = race;
	}
	

}
