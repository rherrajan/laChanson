package tk.icudi;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoadStory {

	@Autowired
	DataSource dataSource;
	
	@RequestMapping(value="/loadStory", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	StoryData loadStory(HttpServletRequest request) throws IOException, SQLException, URISyntaxException {
		
		Map<String, String> requestMap = toMap(IOUtils.toString(request.getInputStream(), StandardCharsets.UTF_8));
		
		String uuid = requestMap.get("uuid");
		StoryData story = new StoryData();
		story.player = createPlayerData(uuid);
		System.out.println(" --- continue at: " + story.player.location);
		story.location.markup = createMarkup("markdown" + story.player.location + ".md", story.player);
		
		return story;
	}

	@RequestMapping(value="/progressStory", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	StoryData progressStory(HttpServletRequest request, @RequestParam(required=false) String action, @RequestParam String destination) throws IOException, SQLException, URISyntaxException {
		
		Map<String, String> requestMap = toMap(IOUtils.toString(request.getInputStream(), StandardCharsets.UTF_8));
		String uuid = requestMap.get("uuid");
		
//		String source = getPlayerData(uuid).location;
		
		System.out.println(" --- uuid: " + uuid);
//		System.out.println(" --- action: " + action);
//		System.out.println(" --- source: " + source);
		System.out.println("  --- destination: " + destination);
		
		String destinationKnowledgeFound = calucalateDestinationKnowledgeFound(destination);
		
		StoryData story = new StoryData();
		story.player = updatePlayerData(uuid, destination, destinationKnowledgeFound);
		story.location.markup = createMarkup("markdown" + destination + ".md", story.player);
		return story;
	}

	private String calucalateDestinationKnowledgeFound(String destination) throws IOException, URISyntaxException {
		String metadata = getMetadata("markdown" + destination + ".md");
		int know = metadata.indexOf("knowledge: ");
		String destinationKnowledgeFound;
		if(know != -1) {
			destinationKnowledgeFound = metadata.substring(know + "knowledge: ".length(), metadata.indexOf("\n", know));
			System.out.println("  --- foundKnowledge: " + destinationKnowledgeFound);	
		} else {
			destinationKnowledgeFound = "";
		}
		return destinationKnowledgeFound;
	}
	
	private String getMetadata(String filename) throws IOException, URISyntaxException {
		String input = readFile(filename);
		return extractMetadata(input);
	}

	private String createMarkup(String filename, PlayerData player) throws IOException, URISyntaxException {
		String input = readFile(filename);
		String data = removeMetadata(input);
		data = removeUnavailableOptions(data, player);
		return parseMarkdown(data);
	}

	private String removeUnavailableOptions(String data, PlayerData player) {
		int knowlegdeSection = data.indexOf("{knowledge:");
		if(knowlegdeSection == -1){
			return data;
		} else {
			int beginEnding = data.indexOf("}", knowlegdeSection);
			String knownlegdeNeeded = data.substring(knowlegdeSection +"{knowledge:".length(), beginEnding).trim();
			
			int endEnding = data.indexOf("{/knowledge}", beginEnding);
			String prefix = data.substring(0, knowlegdeSection);
			String suffix = data.substring(endEnding + "{/knowledge}".length());
			
			boolean hasNeededKnowledge = player.knowledge.contains(knownlegdeNeeded);
			if(hasNeededKnowledge) {
				String restrictedData = data.substring(beginEnding + "}".length(), endEnding);
				data = prefix + restrictedData + suffix;
			} else {
				data = prefix + suffix;
				//data = StringUtils.removeFirst(data, "\\{knowledge:[a-z]+.*\\{/knowledge\\}");
			}
		}
		
		return data;
	}

	private String removeMetadata(String input) {
		int metaBlockStart = input.indexOf("---");
		int metaBlockEnd = input.indexOf("---", metaBlockStart + "---".length());
		int markupStart = input.indexOf("\n",metaBlockEnd + "---".length());
		return input.substring(markupStart);
	}

	private String extractMetadata(String input) {
		int metaBlockStart = input.indexOf("---");
		int metaBlockEnd = input.indexOf("---", metaBlockStart + "---".length());
		int markupStart = input.indexOf("\n",metaBlockEnd + "---".length());
		return input.substring(0, markupStart);
	}
	
    private String parseMarkdown(String input) {
    	Parser parser = Parser.builder().build();
    	Node document = parser.parse(input);
    	HtmlRenderer renderer = HtmlRenderer.builder().build();
    	return renderer.render(document);
	}

	private String readFile(String filename) throws IOException, URISyntaxException {
	    final DefaultResourceLoader loader = new DefaultResourceLoader();   
	    Resource resource = loader.getResource("classpath:" + filename);  
	    return IOUtils.toString(resource.getInputStream(), "UTF-8"); 
	}

	private PlayerData createPlayerData(String uuid) throws SQLException {

		try (Connection connection = dataSource.getConnection()) {
			Statement stmt = connection.createStatement();
			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS players (uuid text, tick timestamp, location text, knowledge text, PRIMARY KEY (uuid))");
			
			ResultSet rs = stmt.executeQuery("SELECT * FROM players WHERE uuid='" + uuid + "'");
//			ResultSet rs = stmt.executeQuery("SELECT location FROM players");
			System.out.println(" --- rs: " + rs);

			if (!rs.next()) {
				System.out.println(" --- new player: " + uuid);
				stmt.executeUpdate("INSERT INTO players VALUES ('" + uuid + "', now(), '/dorfeingang/index')");
				rs = stmt.executeQuery("SELECT * FROM players WHERE uuid='" + uuid + "'");
//				rs = stmt.executeQuery("SELECT location FROM players");
				rs.next();
			} else {
				System.out.println(" --- old player in " + rs.getString("location"));
			}

			PlayerData data = toPlayerData(rs);
			return data;
			
			//throw new RuntimeException("could not find or create player data");
		}

	}

	private PlayerData toPlayerData(ResultSet rs) throws SQLException {
		PlayerData data = new PlayerData();
		data.location = rs.getString("location");
		data.knowledge = rs.getString("knowledge");
		System.out.println("  --- reading knowledge from db: " + data.knowledge);
		return data;
	}

	private PlayerData getPlayerData(String uuid) throws SQLException {
		try (Connection connection = dataSource.getConnection()) {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM players WHERE uuid='" + uuid + "'");
			while (rs.next()) {
				return toPlayerData(rs);
			}
			
			throw new RuntimeException("could not find player data");
		}
	}
	
	private PlayerData updatePlayerData(String uuid, String destination, String foundKnowledge) throws SQLException {
		PlayerData data = getPlayerData(uuid);
		data.location = destination;
		data.addKnowledge(foundKnowledge);
		
		try (Connection connection = dataSource.getConnection()) {
			Statement stmt = connection.createStatement();
			System.out.println("  --- saving knowledge to db: " + data.knowledge);
			stmt.executeUpdate("UPDATE players SET location='" + data.location + "', tick=now(), knowledge='" + data.knowledge + "' WHERE uuid='" + uuid + "'");
			return data;
		}
	}
	
	private Map<String, String> toMap(String mapString) {
		Map<String, String> resultMap = new HashMap<String, String>();
		String[] pairs = mapString.split(",");
		for (int i=0;i<pairs.length;i++) {
		    String pair = pairs[i];
		    String[] keyValue = pair.split(":");
		    resultMap.put(keyValue[0], keyValue[1]);
		}
		
		return resultMap;
	}

	@ModelAttribute
	public void setVaryResponseHeader(HttpServletResponse response) {
	    response.setHeader("Access-Control-Allow-Origin", "*");	  
//	    response.setHeader("Access-Control-Request-Headers", "Origin, X-Requested-With, Content-Type, Accept");	  
	}   
		
}
