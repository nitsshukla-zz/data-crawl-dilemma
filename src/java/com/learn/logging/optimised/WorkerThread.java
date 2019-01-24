package com.learn.logging.optimised;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.learn.logging.connections.*;

class WorkerThread extends Thread {
	String table;
	Connection conn;
	Trie trie;
	int size;
	String tab1;
	String col1, col2;
	CPInstance obj1;
	String[] c1;

    public WorkerThread(String col2, String table, Trie trie, int size, String tab1, String col1, CPInstance obj1) throws ClassNotFoundException, SQLException{
    	this.table = table;
    	this.trie = trie;
    	this.size = size;
    	this.col1 = col1;
    	this.col2 = col2;
    	this.tab1 = tab1;
    	this.obj1 = obj1;    
    	}
 
    public void run() {
    	String sql2;
    	ResultSet rss = null;
		sql2 = "select " + col2 + " from " + table;
		Helper obj = new Helper();
		try {
			conn =obj1.getConnectionFRomPool();
			rss = obj.getRecords(conn, sql2);
	    	obj1.connectionPool.free(conn);
			obj.findMatches(trie, rss, size, col2, table, tab1, col1);
			rss.close();
			rss = null;
		} catch (SQLException e) {
			System.out.println("Error came in WorkerThread ");
			e.printStackTrace();
		}
    }
}

