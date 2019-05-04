package tools;

import java.util.ArrayList;

import obj.AlleleFreq;
import obj.PersonInfo;
import obj.Region;
import obj.StrData;

public final class ObjManager {
	
	private int type;
	private String parentID,childID,parentID2;
	private ArrayList<PersonInfo> persons = new ArrayList<PersonInfo>();;
	private ArrayList<Region> regions = new ArrayList<Region>();
	
	private static ObjManager instance;
	
	private ObjManager() {};
	
	public static ObjManager getInstance() {
        if (instance == null) {
            synchronized (ObjManager.class) {
                if (instance == null) {
                    instance = new ObjManager();
                }
            }
        }
        return instance;
    }
	
	public void insertPersonInfo(PersonInfo newInfo) {
		for(PersonInfo p : persons) {
			if(p.getSampleID() == newInfo.getSampleID()) {
				return;
			}
		}
		persons.add(newInfo);
	}
	
	public void insertRegion(Region newRegion) {
		for(Region r : regions) {
			if(r.getRegionName()==newRegion.getRegionName()) {
				return;
			}
		}
		regions.add(newRegion);
	}
	
	public void updatePersonInfo(StrData newData) {
		for(PersonInfo p : persons) {
			if(p.getSampleID()==newData.getSampleID()) {
				p.insertData(newData);
				return;
			}
		}
	}
	
	public void updateRegion(AlleleFreq newFreq) {
		for(Region r : regions) {
			if(r.getRegionName()==newFreq.getRegionName()) {
				r.inSertAlleleFreq(newFreq);
				return;
			}
		}
	}
	
	public Float getFreq(String region,String locus,Float genotype) {
		for(Region r : regions) {
			if(r.getRegionName().equals(region)) {
				for(AlleleFreq a : r.getData()) {
					if(a.getLocus().equals(locus) && a.getAllele().equals(genotype)) {
						System.out.println("freq : "+a.getFreq());
						return a.getFreq();
					}
				}
			}
		}
		System.out.println("getFreq : NULL");
		return null;
	}

	public ArrayList<PersonInfo> getPersons() {
		return persons;
	}

	public ArrayList<Region> getRegions() {
		return regions;
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getParentID() {
		return parentID;
	}

	public String getChildID() {
		return childID;
	}

	public String getParentID2() {
		return parentID2;
	}

	public void setParentID(String parentID) {
		this.parentID = parentID;
	}

	public void setChildID(String childID) {
		this.childID = childID;
	}

	public void setParentID2(String parentID2) {
		this.parentID2 = parentID2;
	}
	
	
	
	
}
