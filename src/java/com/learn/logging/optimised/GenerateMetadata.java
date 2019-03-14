package com.learn.logging.optimised;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
public class GenerateMetadata extends Thread {
	 Connection conn;
	 boolean allMetadataThreadsFinished =false;
//	 public static int threadCount = 0;
//
//		public synchronized static void setThreadCount(int value) {
//			threadCount += value;
//		}
	public GenerateMetadata(Connection conn) throws ClassNotFoundException, SQLException {
		this.conn = conn;
	}
	public void run(){
		System.out.println("job started");
		Instant start = Instant.now();
		ArrayList<Thread> threads = new ArrayList<>();
		String schema = "";
		try {
			ResultSet rst = conn.getMetaData().getTables(null, schema, "%", new String[] { "TABLE" });
			System.out.println("no of tables rs "+rst);
			int tnum=0;
			while (rst.next()) {
				tnum++;
			}
			System.out.println("No of tables = "+tnum);
			int f = tnum/10;
			int listSize = (int) Math.pow(10, Integer.toString(f).length());
			if(listSize==tnum)
				listSize /= 10;
			int nThreads = tnum/listSize;
			if(tnum%listSize>0)
				nThreads++;
			System.out.println("listSize = "+listSize+" nThreads = "+nThreads);
			
			rst = conn.getMetaData().getTables(null, schema, "%", null);
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
			rst.close();
			for(int i = 0 ;i<nThreads;i++) {
				th[i] = new RunClass(conn, lists.get(i), i);
				th[i].start();
				threads.add(th[i]); 
			}
			for(Thread t : threads) { 
				t.join(); 
			}
			allMetadataThreadsFinished = true;
			System.out.println("All threads finished");
		}catch (Throwable e) {
			e.printStackTrace();
		}
		Duration timeElapsed = Duration.between(start, Instant.now());
		System.out.println("timeElapsed in metadata creation "+timeElapsed);
	}
}