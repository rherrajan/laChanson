package tk.icudi;

import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

public class AbstractDBTest {

	private DataSource dataSource;

	protected DataSource getDataSource() {
		if(dataSource == null) {
			this.dataSource = initDatabase();
		}
		return dataSource;
	}

	private DataSource initDatabase() {
		try {
			DBProvider dbProvider = new DBProvider();
			dbProvider.dbUrl = System.getenv("JDBC_DATABASE_URL");
			return dbProvider.dataSource();
		} catch (SQLException | URISyntaxException e) {
			throw new RuntimeException("could not init database", e);
		}
	}
	
	protected void dropTable(String tablename){
		try (Connection connection = getDataSource().getConnection()) {
			Statement stmt = connection.createStatement();
			stmt.executeUpdate("DROP TABLE players");
		}catch(SQLException ex) {
			throw new RuntimeException("could not drop " + tablename, ex);
		}
	}
}
