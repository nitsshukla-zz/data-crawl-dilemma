package com.learn.logging.optimised;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class GenerateMetadata {
	static Connection conn;

	 public static void setConnection() {
		try {
			String url = "jdbc:mysql://localhost:3306/archonbig";
			String user = "root";
			String password = "secret";
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url, user, password);

//			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//	        conn = DriverManager.getConnection("jdbc:sqlserver://34.213.4.182:57997;databaseName=PS_FINANCE", "sa", "secret@P3");
			
	}catch (Exception e) {
		e.printStackTrace();
	}
}		
	public static void main(String[] args) {
		Instant start = Instant.now();
		ArrayList<Thread> threads = new ArrayList<>();
		try {
			setConnection();
			ResultSet rst = conn.getMetaData().getTables(null, null, "%", null);
			System.out.println("Connection : "+conn);
			int tnum=0;
			while (rst.next()) {
				tnum++;
			}
			System.out.println("No of tables = "+tnum);
			int nThreads = 5; int listSize = 15;
//			int nThreads = 3; int listSize = 10;
//			int nThreads = 9; int listSize = 10000;


			rst = conn.getMetaData().getTables(null, null, "%", null);
			List<List<String>> lists = new ArrayList<List<String>>();
			
			RunClass th[] = new RunClass[nThreads];
			for(int i = 0; i <nThreads; i++) {
				List<String> innerList = new ArrayList<String>();

				for(int j = 0; j< listSize; j++) {
					if(rst.next()){
						String t = rst.getString(3);
						innerList.add(t);
					}
				}
				lists.add(i, innerList);
			}
			for(int i = 0 ;i<nThreads;i++) {
				th[i] = new RunClass(conn, lists.get(i), i);
				th[i].start();
				threads.add(th[i]);
			}

			for(Thread t : threads) {
				t.join();
			}
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}

		Duration timeElapsed = Duration.between(start, Instant.now());
		System.out.println("timeElapsed "+timeElapsed);

	}
}