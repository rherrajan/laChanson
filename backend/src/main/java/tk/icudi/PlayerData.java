package tk.icudi;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;

public class PlayerData {

	public String location;
	public String knowledge;
	public String additionalKnowledge;
	
	public void addKnowledge(String foundKnowledge) {
		
		if(StringUtils.isEmpty(foundKnowledge)) {
			return;
		}
		System.out.println("  --- old knowledge: " + knowledge);
		System.out.println("  --- addKnowledge: " + foundKnowledge);	
		
		if(StringUtils.isEmpty(knowledge)) {
			System.out.println("  --- first knowledge: " + foundKnowledge);				
			JSONArray resultArray = new JSONArray();
			resultArray.put(foundKnowledge);
			knowledge = resultArray.toString();
			additionalKnowledge = foundKnowledge;
		} else {
			System.out.println("  --- adding to existing: " + foundKnowledge);	
			JSONArray resultArray = new JSONArray(knowledge);
			if(!resultArray.toString().contains(foundKnowledge)) {
				resultArray.put(foundKnowledge);
				additionalKnowledge = foundKnowledge;
			}
			knowledge = resultArray.toString();
		}
	}

}
