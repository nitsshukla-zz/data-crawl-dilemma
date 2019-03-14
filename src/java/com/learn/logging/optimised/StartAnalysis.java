package com.learn.logging.optimised;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import com.learn.logging.connections.CPInstance;
import com.learn.logging.iterator.Column;
import com.learn.logging.iterator.MetadataIterator;
import com.learn.logging.iterator.TableMetadata;
import com.learn.logging.logger.JobLogger;

import helper.ConstructTrie;
import latch.CustomLatch;

public class StartAnalysis {
	public static void main(String[] args)
			throws InterruptedException, SQLException, ClassNotFoundException, FileNotFoundException {
		final int SAMPLINGPCT = 5;
		JobLogger.getLogger().info(StartAnalysis.class.getName(), "main method", "Start");
		CPInstance connObj = new CPInstance();
		System.out.println("Started");
		ExecutorService executor = Executors.newFixedThreadPool(5);
		Instant start = Instant.now();
		CustomLatch latch = new CustomLatch();

		try (Connection conn = connObj.getConnectionFRomPool()) {
			if (conn != null)
				JobLogger.getLogger().info(StartAnalysis.class.getName(), "main method", "Connection Established");
			System.out.println("Connection object " + conn);
			System.out.println("Starting metadata generation");
			GenerateMetadata gen = new GenerateMetadata(conn);
			gen.start();
			while (gen.allMetadataThreadsFinished != true) {
				Thread.sleep(100);
			}
			String path = "C:/metadata/";
			File file = new File(path);
			File[] files = file.listFiles();
			MetadataIterator iterator1 = new MetadataIterator(files);
			file = null;
			files = null;
			MetadataIterator iterator2 = null;
			ConstructTrie makeTrie = new ConstructTrie();
			while (iterator1.hasNext()) {
				TableMetadata pri = iterator1.next();
				String table1 = pri.tableName;
				Column c1 = pri.column;
				String col1 = c1.colName;
				String type1 = c1.colType;
				int rowCount1 = pri.rowCount;
				c1 = null;
				pri = null;
				String colDataFetchQry = "select " + col1 + " from " + table1;
				try (PreparedStatement sql = conn.prepareStatement(colDataFetchQry, ResultSet.TYPE_FORWARD_ONLY,
						ResultSet.CONCUR_READ_ONLY); ResultSet rs = sql.executeQuery();) {
					int size = SAMPLINGPCT * rowCount1 / 100;
					if (size == 0 && rowCount1 != 0)
						size = rowCount1;
					Trie trie = makeTrie.constructCustomizedTrie(rs, col1, size);
					iterator2 = iterator1.clone();
					latch.init(trie);
					while (iterator2.hasNext()) {
						TableMetadata sec = iterator2.next();
						String table2 = sec.tableName;
						int rowCount2 = sec.rowCount;
						if (rowCount2 == 0)
							continue;
						Column c2 = sec.column;
						String col2 = c2.colName;
						String type2 = c2.colType;
						if (type1.equals(type2)) {
							latch.up();
							Runnable worker = new WorkerThread(latch, col2, table2, trie, size, table1, col1, connObj);
							executor.execute(worker);
						}
					}
					latch.readyToFinish();					
					System.out.println("Finished");
					iterator2 = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			executor.shutdown();
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

		} catch (Exception e) {
			e.printStackTrace();
		}
		Duration timeElapsed = Duration.between(start, Instant.now());
		JobLogger.getLogger().info(StartAnalysis.class.getName(), "main method", "Time elapsed " + timeElapsed);
		System.out.println("timeElapsed " + timeElapsed);
		System.out.println("Done");
	}
}