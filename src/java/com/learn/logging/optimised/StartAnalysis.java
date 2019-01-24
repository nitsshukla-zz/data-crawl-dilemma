package com.learn.logging.optimised;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.learn.logging.logger.JobLogger;
import com.learn.logging.connections.*;
import com.learn.logging.iterator.*;

public class StartAnalysis {

	public static void main(String[] args)
			throws InterruptedException, SQLException, ClassNotFoundException, FileNotFoundException {
		Helper obj = new Helper();
		JobLogger.getLogger().info(StartAnalysis.class.getName(), "main method", "Start");
		CPInstance connObj = new CPInstance();
		Connection conn = null;
		System.out.println("Started");
		ExecutorService executor = Executors.newFixedThreadPool(5);
		try {
			conn = connObj.getConnectionFRomPool();
			if (conn != null)
				JobLogger.getLogger().info(StartAnalysis.class.getName(), "main method", "Connection Established");
			
			String path = "C:/archonbig/";
			File file = new File(path);
			File[] files = file.listFiles();
			MetadataIterator iterator1 = new MetadataIterator(files);
//			String table1, table2, col1, col2, type1, type2;Column c1, c2;
//			MetadataIterator iterator2;
			Thread worker;
			while (iterator1.hasNext()) {
				TableMetadata pri = iterator1.next();
				String table1 = pri.tableName;
				Column c1 = pri.column;
				String col1 = c1.colName;
				String type1 = c1.colType;
				c1 = null;
				pri = null;
				System.out.println("Table1 : " + table1 + " Column1 : " + col1);
				String colDataFetchQry = "select " + col1 + " from " + table1;
				ResultSet rs = obj.getRecords(conn, colDataFetchQry);
				GetResults complex = obj.constructTrie(rs, col1);
				Trie trie = complex.trie;
				int size = complex.size;
				complex = null;
				rs.close();
				rs = null;

				MetadataIterator iterator2 = iterator1.clone();
				while (iterator2.hasNext()) {
					TableMetadata sec = iterator2.next();
					String table2 = sec.tableName;
					Column c2 = sec.column;
					String col2 = c2.colName;
					String type2 = c2.colType;
					c2 = null;
					sec = null;
/*
					if (type1.equals(type2)) {
						System.out.println("Table2 : " + table2 + " Column2 : " + col2);
						worker = new WorkerThread(col2, table2, trie, size, table1, col1, connObj);
						executor.execute(worker);
						worker = null;
					}
*/					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}
//		executor.shutdown();
//		Duration timeElapsed = Duration.between(start, Instant.now());
//		JobLogger.getLogger().info(StartAnalysis.class.getName(), "main method", "Time elapsed " + timeElapsed);
//		System.out.println("timeElapsed "+timeElapsed);
//		System.out.println("Done");
	}
}