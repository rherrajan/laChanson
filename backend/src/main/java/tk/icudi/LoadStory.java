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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import org.commonmark.node.*;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

@Controller
public class LoadStory {

	@Autowired
	DataSource dataSource;
	
	@RequestMapping(value="/loadStory", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	StoryData loadStory(HttpServletRequest request) throws IOException, SQLException, URISyntaxException {
		
		Map<String, String> requestMap = toMap(IOUtils.toString(request.getInputStream(), StandardCharsets.UTF_8));
		
		String uuid = requestMap.get("uuid");
		System.out.println(" --- uuid " + uuid);
		StoryData story = new StoryData();
		story.player = getPlayerData(uuid);
		story.location.markup = createMarkup("markdown/index.md");
		
		return story;
	}

	private String createMarkup(String filename) throws IOException, URISyntaxException {
		String input = readFile(filename);
		return parseMarkdown(input);
	}

    private String parseMarkdown(String input) {
    	Parser parser = Parser.builder().build();
    	Node document = parser.parse(input);
    	HtmlRenderer renderer = HtmlRenderer.builder().build();
    	return renderer.render(document);
	}

//	private String readFile(String filename) throws IOException, URISyntaxException {
//		URL systemResource = ClassLoader.getSystemResource(filename);
//		URI uri = systemResource.toURI();
//		Path path = Paths.get(uri);
//		byte[] readAllBytes = Files.readAllBytes(path);
//		return new String(readAllBytes);
//	}

	private String readFile(String filename) throws IOException, URISyntaxException {
	    final DefaultResourceLoader loader = new DefaultResourceLoader();   
	    Resource resource = loader.getResource("classpath:" + filename);  
	    return IOUtils.toString(resource.getInputStream(), "UTF-8"); 
	}

	private PlayerData getPlayerData(String uuid) throws SQLException {

		try (Connection connection = dataSource.getConnection()) {
			Statement stmt = connection.createStatement();
			//stmt.executeUpdate("DROP TABLE players");
			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS players (uuid text, tick timestamp, location text)");
			stmt.executeUpdate("INSERT INTO players VALUES ('" + uuid + "', now(), 'start')");
			ResultSet rs = stmt.executeQuery("SELECT location FROM players");

			while (rs.next()) {
				PlayerData data = new PlayerData();
				data.location = rs.getString("location");
				return data;
			}
			
			throw new RuntimeException("could not find or create player data");
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
