package com.learn.logging.optimised;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.Gson;
import com.learn.logging.iterator.*;

public class RunClass extends Thread {
	List<String> tableList = new ArrayList<>();
	Connection conn;
	int threadNum;
	public RunClass(Connection conn, List<String> list1, int threadNum) {
		tableList = list1;
		this.threadNum = threadNum;
		this.conn = conn;
	}

	public void run() {
		Map<String, String> hm = new HashMap<String, String>();
		Helper selfObj = new Helper();
		String path = "C:/archonbig/";
		Metadata md;
		try {
			File file = new File(path+"_md"+threadNum+".txt");
			file.getParentFile().mkdirs();
			FileWriter writer = new FileWriter(file);
			PrintWriter pw = new PrintWriter(writer);
			Gson gson = new Gson();
			for(String table : tableList) {
				String stmt = "SELECT * FROM " + table +" where 1=2";
				ResultSet rs = selfObj.getRecords(conn, stmt);
				int rowcount = selfObj.getRowCount(conn, table);
				hm = selfObj.getColumnNames(rs);
				if(rowcount!=0) {
					for(Map.Entry entry : hm.entrySet()) {
						md = new Metadata();
						md.tableName = table;
						md.column = new Column((String) entry.getKey(), (String) entry.getValue());
						md.rowcount = rowcount;
						System.out.println("md "+md);
						pw.write(gson.toJson(md)+"\n");
					}
				}
				else
					System.out.println("Empty table "+table);
			}
			pw.flush();
			pw.close();
			pw = null;
			System.out.println("1 file written");
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("Exception occured due to wrong table name");
		}
	}
}
