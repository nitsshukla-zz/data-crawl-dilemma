package com.learn.logging.optimised;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.learn.logging.iterator.Column;
import com.learn.logging.iterator.Metadata;
import com.learn.logging.logger.JobLogger;

import helper.MetadataDetails;

public class RunClass extends Thread {
	List<String> tableList = new ArrayList<>();
	Connection conn;
	int threadNum;
	public RunClass(Connection conn, List<String> list1, int threadNum) {
		tableList = list1;
		this.threadNum = threadNum;
		this.conn = conn;
	}
	@SuppressWarnings("unchecked")
	public void run() {
		Map<String, String> hm = new HashMap<String, String>();
		MetadataDetails selfObj = new MetadataDetails();
		String path = "C:/metadata/";
		Metadata md;
		int rowCount=0;
		try {
			File file = new File(path+"_md"+threadNum+".txt");
			file.getParentFile().mkdirs();
			FileWriter writer = new FileWriter(file);
			PrintWriter pw = new PrintWriter(writer);
			Gson gson = new Gson();
			for(String table : tableList) {
				String stmt = "SELECT * FROM " + table +" where 1=2";
				try(PreparedStatement sql = conn.prepareStatement(stmt,ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY );
						ResultSet rs = sql.executeQuery();) {
					rowCount = selfObj.getRowCount(conn, table);
					hm = selfObj.getColumnNames(rs);
				}catch (Throwable throwable) {
					System.out.println("Throwable error "+throwable);
				}
				for(Map.Entry entry : hm.entrySet()) {
					md = new Metadata();
					md.tableName = table;
					md.column = new Column((String) entry.getKey(), (String) entry.getValue());
					md.rowCount = rowCount;
					pw.write(gson.toJson(md)+"\n");
				}
			}
			pw.flush();
			pw.close();
			pw = null;
			System.out.println("1 file written");
//			GenerateMetadata.threadCount--;
			JobLogger.getLogger().info(StartAnalysis.class.getName(), "run", "1 file written");
		}catch(Throwable e) {
			e.printStackTrace();
			System.out.println("Can't fetch the table details");
		}
	}
}
