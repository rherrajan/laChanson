//package tk.icudi;
//
//import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.ArrayList;
//import java.util.Map;
//
//import javax.servlet.http.HttpServletResponse;
//import javax.sql.DataSource;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import com.zaxxer.hikari.HikariConfig;
//import com.zaxxer.hikari.HikariDataSource;
//
//@Controller
//public class CallCounter {
//
//	@Value("${spring.datasource.url}")
//	private String dbUrl;
//
//	@Autowired
//	private DataSource dataSource;
//
//	@ModelAttribute
//	public void setVaryResponseHeader(HttpServletResponse response) {
//		response.setHeader("Access-Control-Allow-Origin", "*");
//	}
//	
//	@RequestMapping("/call")
//	String db(Map<String, Object> model) {
//		try (Connection connection = dataSource.getConnection()) {
//			Statement stmt = connection.createStatement();
//			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)");
//			stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
//			ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks");
//
//			ArrayList<String> output = new ArrayList<String>();
//			while (rs.next()) {
//				output.add("Read from DB: " + rs.getTimestamp("tick"));
//			}
//
//			model.put("records", output);
//			return "db";
//		} catch (Exception e) {
//			model.put("message", e.getMessage());
//			return "error";
//		}
//	}
//
//	@Bean
//	public DataSource dataSource() throws SQLException {
//		if (dbUrl == null || dbUrl.isEmpty()) {
//			return new HikariDataSource();
//		} else {
//			HikariConfig config = new HikariConfig();
//			config.setJdbcUrl(dbUrl);
//			return new HikariDataSource(config);
//		}
//	}
//
//}
