package tk.icudi;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;


@Component
public class DBProvider {

	@Value("${spring.datasource.url}")
	String dbUrl;

	@Bean
	public DataSource dataSource() throws SQLException, URISyntaxException {
		if (dbUrl == null || dbUrl.isEmpty()) {
			throw new IllegalArgumentException("no db url configured");
		} else {
			URI dbUri = new URI(dbUrl);
		    
			HikariConfig config = new HikariConfig();

			if(dbUri.getUserInfo() != null && dbUri.getUserInfo().contains(":")) {
				String jdbcUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() + "?sslmode=require";
				String username = dbUri.getUserInfo().split(":")[0];
				String password = dbUri.getUserInfo().split(":")[1];
				config.setJdbcUrl(jdbcUrl);
				config.setUsername(username);
				config.setPassword(password);
			} else {
				config.setJdbcUrl(dbUrl);
			}
			return new HikariDataSource(config);
		}
	}
	
}
