package tk.icudi;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DBDebugger {

	@Autowired
	DataSource dataSource;
	

	@RequestMapping("/database")
	ResponseEntity<String> database(@RequestParam String name) {
		
		try (Connection connection = dataSource.getConnection()) {
			JSONObject result = readFromDB(connection, name);
		    
		    final HttpHeaders httpHeaders= new HttpHeaders();
		    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		    return new ResponseEntity<String>(result.toString(), httpHeaders, HttpStatus.OK);
		    

		} catch (Exception e) {
			
			e.printStackTrace();
			
			
			JSONObject error = new JSONObject();
			try {
				error.put("error", e.toString());
			} catch (JSONException e1) {
				throw new RuntimeException("error while generation error message for: " + e, e1);
			}
			
		    final HttpHeaders httpHeaders= new HttpHeaders();
		    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		    return new ResponseEntity<String>(error.toString(), httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
		    
		}

	}

	private JSONObject readFromDB(Connection connection, String name) throws SQLException, JSONException {
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM " + name);
		final int columnCount = rs.getMetaData().getColumnCount();
		
		System.out.println(" --- columnCount: " + columnCount);
		JSONArray resultArray = new JSONArray();
		while (rs.next()) {
			Object[] row = new Object[columnCount];
			for (int i = 1; i <= columnCount; ++i) {
			    row[i - 1] = rs.getString(i);
			}
			resultArray.put(row);
		}
		
		JSONObject result = new JSONObject();
		result.put(name, resultArray);
		return result;
	}

	@ModelAttribute
	public void setVaryResponseHeader(HttpServletResponse response) {
	    response.setHeader("Access-Control-Allow-Origin", "*");	  
	}   
		
}
