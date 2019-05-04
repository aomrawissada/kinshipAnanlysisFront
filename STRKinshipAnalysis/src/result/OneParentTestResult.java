package result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class OneParentTestResult {
	
	String parent;
	String child;
	Map<String, Float> piList = new HashMap<String, Float>();
	
	Double LR;
	Double postProb;
	
	public OneParentTestResult(String parent, String child, Map<String, Float> piList) {
		super();
		this.parent = parent;
		this.child = child;
		this.piList = piList;
	}

	public String getString() {
		calPostProb();
		String string = "Post.prob = "+postProb+"\n";
		if(postProb.compareTo(99.0)>0){
			return string+getCase1String();
		}else if(postProb.compareTo(0.1)<=0){
			return string+getCase2String();
		}else {
			return string+getCase3String();
		}
	}
	
	private String getCase1String() {
		return parent+" ไม่ถูกคัดออกจากการเป็นพ่อ-แม่ของ "+child+"\n"+
					"โดยความเชื่อมั่นที่"+parent+"เป็นพ่อ-แม่ของ"+child+"เท่ากับ"+postProb+
					"เมื่อคํานวณจากฐานข้อมูลประชากรไทย โดยสันนิษฐานค่า prior probability = 0.5";
	}
	
	private String getCase2String() {
		ArrayList<String> temp = new ArrayList<String>();
		for(Entry<String, Float> e : piList.entrySet()) {
			if(e.getValue().equals(0.0f)) {
				temp.add(e.getKey());
			}
		}
		String str = parent+"ไมใช่พ่อแม่ของ"+child+"โดยมีตำแหน่งที่เข้ากันไม่ได้ "+temp.size()+"ตำแหน่ง ได้แก่ ";
		for(String s : temp) {
			str= str+s+" ";
		}
		return str;
	}
	
	private String getCase3String() {
		return "ไม่สามารถสรุปได้ว่า "+parent+" เป็นพ่อแม่ของ "+child+" หรือไม่";
	}
	
	private void calLR() {
		LR = 1.0;
		for(String s : piList.keySet()) {
			System.out.println(s + " : " + piList.get(s));
			LR *= piList.get(s);
		}
	}
	
	private void calPostProb() {
		calLR();
		calPostProb(0.5f);
	}
	
	private void calPostProb(Float pr) {
		postProb = ( LR*pr / ((LR*pr)+(1-pr)) )*100 ;
	}
	

}
