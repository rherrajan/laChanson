//package tk.icudi;
//
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.Statement;
//import java.sql.Timestamp;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//import javax.servlet.http.HttpServletResponse;
//import javax.sql.DataSource;
//
//import org.apache.commons.io.IOUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.io.Resource;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//
//@Controller
//public class DBExample {
//
//	@Value("${spring.datasource.url}")
//	private String dbUrl;
//
//	@Autowired
//	private DataSource dataSource;
//	
//	@RequestMapping(value="/dbexample", method = RequestMethod.GET, produces = "application/json")
//	@ResponseBody
//	Object getDbexample() throws IOException {
//
//		try (Connection connection = dataSource.getConnection()) {
//			Statement stmt = connection.createStatement();
//			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)");
//			stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
//			ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks");
//
//			List<Timestamp> calls = new ArrayList<Timestamp>();
//			while (rs.next()) {
//				calls.add(rs.getTimestamp("tick"));
//			}
//
//			return calls;
//		} catch (Exception e) {
//			return "error: " + e;
//		}
//	}
//
//	@ModelAttribute
//	public void setVaryResponseHeader(HttpServletResponse response) {
//	    response.setHeader("Access-Control-Allow-Origin", "*");	    
//	}   
//	
//}
