package analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import obj.PersonInfo;
import obj.StrData;
import tools.ObjManager;

public final class NoParentTest{
	
	private static NoParentTest instance;
	
	private NoParentTest() {};
	
	public static NoParentTest getInstance() {
        if (instance == null) {
            synchronized (NoParentTest.class) {
                if (instance == null) {
                    instance = new NoParentTest();
                }
            }
        }
        return instance;
    }
	
	public Map<String, Float> getPiList(ArrayList<String> list,String region){
		Map<String, Float> PiList = new HashMap<String,Float>();
		for(String s : list) {
			String[] ss = s.split(" ");
			PiList.put(ss[0], getPi(ss[0],Float.valueOf(ss[1]),Float.valueOf(ss[2]),
					Float.valueOf(ss[3]),Float.valueOf(ss[4]),region));
		}
		return PiList;
	}

	public Map<String, Float> getPiList(PersonInfo parent,PersonInfo child) {
		Map<String, Float> PiList = new HashMap<String,Float>();
		if(parent.getRegion().equals(child.getRegion())) {
			for(StrData pStrData : parent.getStrList()) {
				String locus = pStrData.getLocus();
				for(StrData cStrData : child.getStrList()) {
					if(locus.equals(cStrData.getLocus())) {
						PiList.put(locus,
								getPi(locus,pStrData.getGenotype1(),pStrData.getGenotype2(),
								cStrData.getGenotype1(),cStrData.getGenotype2(),parent.getRegion()));
					}
				}
			}
		}else {
			for(StrData pStrData : parent.getStrList()) {
				String locus = pStrData.getLocus();
				for(StrData cStrData : child.getStrList()) {
					if(locus.equals(cStrData.getLocus())) {
						PiList.put(locus,
								getPiMixed(locus,pStrData.getGenotype1(),pStrData.getGenotype2(),
								cStrData.getGenotype1(),cStrData.getGenotype2(),parent.getRegion(),child.getRegion()));
					}
				}
			}
		}
		
		return PiList;
	}
	
	private Float getPi(String locus,Float p1, Float p2,Float c1, Float c2,String regionName) {
//		System.out.println("getPi");
		Float pi = null;
		System.out.println("getPi: IN :"+locus+" "+p1.toString()+" "+p2.toString()+" "+c1.toString()+" "+c2.toString()+" "+regionName);
		if(p1.equals(p2)) {
			if(p1.equals(c1)) {
				if(c1.equals(c2)) {
					pi = 1f/ObjManager.getInstance().getFreq(regionName, locus, p1);
				}else {
					pi = 1f/(2*(ObjManager.getInstance().getFreq(regionName, locus, p1)));
				}
			}else if(p1.equals(c2)) {
				pi = 1f/(2*(ObjManager.getInstance().getFreq(regionName, locus, p1)));
			}else {
				pi = 0f;
			}
		}else if(c1.equals(c2)) {
			if(p1.equals(c1)) {
				pi = 1f/(2*(ObjManager.getInstance().getFreq(regionName, locus, p1)));	
			}else if(p2.equals(c1)) {
				pi = 1f/(2*(ObjManager.getInstance().getFreq(regionName, locus, p2)));	
			}
		}else if(p1.equals(c1)){
			if(p2.equals(c2)) {
				pi = (1f/(4*(ObjManager.getInstance().getFreq(regionName, locus, p1)))) + 
						(1f/(4*(ObjManager.getInstance().getFreq(regionName, locus, p2))));
			}else {
				pi = 1f/(4*(ObjManager.getInstance().getFreq(regionName, locus, p1)));
			}
		}else if(p1.equals(c2)) {
			if(p2.equals(c1)) {
				pi = (1f/(4*(ObjManager.getInstance().getFreq(regionName, locus, p1)))) + 
						(1f/(4*(ObjManager.getInstance().getFreq(regionName, locus, p2))));
			}else {
				pi = 1f/(4*(ObjManager.getInstance().getFreq(regionName, locus, p1)));
			}
		}else if(p2.equals(c1)) {
			pi = 1f/(4*(ObjManager.getInstance().getFreq(regionName, locus, p2)));
		}else if(p2.equals(c2)) {
			pi = 1f/(4*(ObjManager.getInstance().getFreq(regionName, locus, p2)));
		}else {
			pi = 0f;
		}
		System.out.println(">>>"+pi.toString());
		return pi;
	}
	
	
	private Float getPiMixed(String locus,Float p1, Float p2,Float c1, Float c2,String pRegionName,String cRegionName) {
		Float pi = null;
		ObjManager manager = ObjManager.getInstance();
		
		if(p1.equals(p2)) {
			if(p1.equals(c1)) {
				if(c1.equals(c2)) {
					pi = 1/manager.getFreq(pRegionName, locus, p1);
				}else {
					pi = manager.getFreq(cRegionName, locus, c2)/
							((manager.getFreq(pRegionName, locus, p1)*manager.getFreq(cRegionName, locus, c2))+
							 (manager.getFreq(pRegionName, locus, c2)*manager.getFreq(cRegionName, locus, p1)));
				}
			}else if(p1.equals(c2)) {
				pi = manager.getFreq(cRegionName, locus, c1)/
						((manager.getFreq(pRegionName, locus, p1)*manager.getFreq(cRegionName, locus, c1))+
						 (manager.getFreq(pRegionName, locus, c1)*manager.getFreq(cRegionName, locus, p1)));
			}else {
				pi = 0f;
			}
		}else if(c1.equals(c2)) {
			if(p1.equals(c1)) {
				pi = 1f/(2*(manager.getFreq(pRegionName, locus, p1)));	
			}else if(p2.equals(c1)) {
				pi = 1f/(2*(manager.getFreq(pRegionName, locus, p2)));	
			}
		}else if(p1.equals(c1)){
			if(p2.equals(c2)) {
				pi = (manager.getFreq(cRegionName, locus, c1)+manager.getFreq(cRegionName, locus, c2))/
						(2*( (manager.getFreq(pRegionName, locus, p1)*manager.getFreq(cRegionName, locus, c2)) +
							 (manager.getFreq(pRegionName, locus, p2)*manager.getFreq(cRegionName, locus, c1))	));

			}else {
				pi = (0.5f * manager.getFreq(cRegionName, locus, c2))/
						( manager.getFreq(pRegionName, locus, p1) * manager.getFreq(cRegionName, locus, c2)) +
						( manager.getFreq(pRegionName, locus, c2) * manager.getFreq(cRegionName, locus, c1)) ;
			}
		}else if(p1.equals(c2)) {
			if(p2.equals(c1)) {
				pi = (manager.getFreq(cRegionName, locus, c1)+manager.getFreq(cRegionName, locus, c2))/
						(2*( (manager.getFreq(pRegionName, locus, p1)*manager.getFreq(cRegionName, locus, c2)) +
							 (manager.getFreq(pRegionName, locus, p2)*manager.getFreq(cRegionName, locus, c1))	));
			}else {
				pi = (0.5f * manager.getFreq(cRegionName, locus, c1))/
						( manager.getFreq(pRegionName, locus, p1) * manager.getFreq(cRegionName, locus, c1)) +
						( manager.getFreq(pRegionName, locus, c1) * manager.getFreq(cRegionName, locus, p1)) ;
			}
		}else if(p2.equals(c1)) {
			pi = (0.5f * manager.getFreq(cRegionName, locus, c2))/
					( manager.getFreq(pRegionName, locus, c1) * manager.getFreq(cRegionName, locus, c2)) +
					( manager.getFreq(pRegionName, locus, c2) * manager.getFreq(cRegionName, locus, c1)) ;
		}else if(p2.equals(c2)) {
			pi = (0.5f * manager.getFreq(cRegionName, locus, c1))/
					( manager.getFreq(pRegionName, locus, c2) * manager.getFreq(cRegionName, locus, c1)) +
					( manager.getFreq(pRegionName, locus, c1) * manager.getFreq(cRegionName, locus, c2)) ;
		}else {
			pi = 0f;
		}
		System.out.println(">>>"+pi.toString());
		return pi;
	}
	

}
