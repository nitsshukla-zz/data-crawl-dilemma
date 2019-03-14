package com.learn.logging.optimised;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.learn.logging.logger.JobLogger;

import helper.MetadataDetails;

import com.learn.logging.connections.*;
import com.learn.logging.iterator.*;

public class SamplingTest {
	static boolean status = false;
	static boolean buffer = true;

	public static void main(String[] args)
			throws InterruptedException, SQLException, ClassNotFoundException, FileNotFoundException {
		MetadataDetails obj = new MetadataDetails();
		JobLogger.getLogger().info(StartAnalysis.class.getName(), "main method", "Start");
		CPInstance connObj = new CPInstance();
		Connection conn = null;
		System.out.println("Started");
		Instant start = Instant.now();
		int samplingPct=5;
		try {
			conn = connObj.getConnectionFRomPool();
			if (conn != null)
				JobLogger.getLogger().info(StartAnalysis.class.getName(), "main method", "Connection Established");
			System.out.println("Connection object "+conn);
			System.out.println("Starting metadata generation");
			GenerateMetadata gen = new GenerateMetadata(conn);
			gen.start();
			while(gen.allMetadataThreadsFinished!=true) {
				Thread.sleep(100);
			}
			String path = "C:/metadata/";
			File file = new File(path);
			File[] files = file.listFiles();
			MetadataIterator iterator1 = new MetadataIterator(files);
			while (iterator1.hasNext()) {
				try {
					TableMetadata pri = iterator1.next();
					String table1 = pri.tableName;
					Column c1 = pri.column;
					String col1 = c1.colName;
					String type1 = c1.colType;
					int limit = Math.round((samplingPct *obj.getRowCount(conn, table1))/100);
					System.out.println("count of "+table1+" = "+obj.getRowCount(conn, table1));
					System.out.println("limited to = "+limit);

					//obj.counstructCustomizedTrie(rs,conn);
				}catch(Exception e) {
					System.out.print("Came in iterator1");
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}
		Duration timeElapsed = Duration.between(start, Instant.now());
		JobLogger.getLogger().info(StartAnalysis.class.getName(), "main method", "Time elapsed " + timeElapsed);
		System.out.println("timeElapsed "+timeElapsed);
		System.out.println("Done");	
		
	}
}
