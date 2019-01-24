
package com.learn.logging.connections;

import java.sql.Connection;
import java.sql.SQLException;

public class CPInstance {
	
	public ConnectionPool connectionPool;

	public CPInstance() throws SQLException {
		connectionPool = new ConnectionPool("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/archonbig", "root",
				"secret", 5, 10, true);

//        connectionPool = new ConnectionPool(
//                "com.mysql.jdbc.Driver",
//                "jdbc:mysql://localhost:3306/ARCHON_DEMO_DB", "ubuntu", "p3solutions",
//                5, 10, true);
//

//        connectionPool = new ConnectionPool(
//                "com.microsoft.sqlserver.jdbc.SQLServerDriver",
//                "jdbc:sqlserver://34.213.4.182:57997;databaseName=PS_FINANCE", "sa", "secret@P3",
//                5, 10, true);
	}

	public Connection getConnectionFRomPool() throws SQLException {
		return connectionPool.getConnection();
		
	}
}
