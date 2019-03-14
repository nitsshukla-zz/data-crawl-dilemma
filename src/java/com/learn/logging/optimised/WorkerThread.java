package com.learn.logging.optimised;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.learn.logging.connections.CPInstance;

import helper.FindMatches;
import latch.CustomLatch;

public class WorkerThread extends Thread {
	String table;
	Connection conn;
	Trie trie;
	int size;
	String tab1;
	String col1, col2;
	CPInstance obj1;
	private CustomLatch latch;

	public WorkerThread(CustomLatch latch, String col2, String table, Trie trie, int size, String tab1, String col1,
			CPInstance obj1) {
		this.table = table;
		this.trie = trie;
		this.size = size;
		this.col1 = col1;
		this.col2 = col2;
		this.tab1 = tab1;
		this.obj1 = obj1;
		this.latch = latch;
	}

	@Override
	public void run() {
		String sql2;
		ResultSet rss = null;
		sql2 = "select " + col2 + " from " + table;
		FindMatches finder = new FindMatches();
		PreparedStatement sql = null;
		try {
			conn = obj1.getConnectionFRomPool();
			sql = conn.prepareStatement(sql2, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			rss = sql.executeQuery();
			finder.findMatches(trie, rss, size, col2, table, tab1, col1);
			latch.down();
			sql.close();
			rss.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			obj1.connectionPool.free(conn);
		}
	}
}
