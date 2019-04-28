package tk.icudi;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

public class PlayerData {

	public String location;
	public String knowledge;
	public String additionalKnowledge;
	
	public void addKnowledge(String foundKnowledge) {
		if(StringUtils.isEmpty(knowledge)) {
			Set<String> knowHow = new HashSet<String>();
			knowHow.add(foundKnowledge);
			knowledge = foundKnowledge.toString();
			additionalKnowledge = foundKnowledge;
		}
	}

}
