
package com.learn.logging.connections;

import java.sql.Connection;
import java.sql.SQLException;

public class CPInstance {
	
	public ConnectionPool connectionPool;

	public CPInstance() throws SQLException {
		connectionPool = new ConnectionPool("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/archonbig", "root",
				"secret", 5, 10, true);
		
//		connectionPool = new ConnectionPool("com.ibm.db2.jcc.DB2Driver", "jdbc:db2://34.213.222.85:50000/SAMPLE", "db2admin",
//				"secret@P3", 5, 10, true);
		
//		String url = "jdbc:oracle:thin:@34.213.222.85:1521:ORCL", user = "SCOTT",password = "secret@P3";
//		connectionPool = new ConnectionPool("oracle.jdbc.driver.OracleDriver", url, user, password, 5, 10, true);
		
//		String url = "jdbc:oracle:thin://locahost:1521/XE", user = "test",password = "test";
//		connectionPool = new ConnectionPool("oracle.jdbc.driver.OracleDriver", url, user, password, 5, 10, true);

//        connectionPool = new ConnectionPool(
//                "com.microsoft.sqlserver.jdbc.SQLServerDriver",
//                "jdbc:sqlserver://34.213.4.182:57997;databaseName=PS_FINANCE", "sa", "secret@P3",
//                5, 10, true);

        //        connectionPool = new ConnectionPool(
//                "com.microsoft.sqlserver.jdbc.SQLServerDriver",
//                "jdbc:sqlserver://localhost:1433;databaseName=PS_FINANCE", "Platform3", "Platform3",
//                5, 10, true);

	}

	public Connection getConnectionFRomPool() throws SQLException {
		return connectionPool.getConnection();
		
	}
}


//Class.forName("oracle.jdbc.driver.OracleDriver");
//String urlResult = "jdbc:oracle:thin:@" + model.getHost() + ":" + model.getPort() + ":" + model.getDatabaseName();
//connectionDataBases = DriverManager.getConnection(urlResult,
//		model.getUserName(), decryptPassword);