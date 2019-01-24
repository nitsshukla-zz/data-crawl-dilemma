package com.learn.logging.optimised;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.learn.logging.logger.JobLogger;

class Helper {
	Trie trie;
	GetResults o;

	public GetResults constructTrie(ResultSet rs1, String col1) throws SQLException {
		int size = 0;
		trie = new Trie();
		while (rs1.next()) {
			size++;
			if (rs1.getString(col1) == null)
				continue;
			trie.insert(rs1.getString(col1));
		}
		rs1.close();
		rs1 = null;
		o = new GetResults();
		o.trie = trie;
		o.size = size;
		trie = null;
		return (o);
	}

	@SuppressWarnings("rawtypes")
	public void findMatches(Trie trie, ResultSet rs, int size, String col2, String table2, String tab1, String col1)
			throws SQLException {
		HashMap<String, Boolean> results = new HashMap<>();
		double count = 0;
		double pct;
		while (rs.next()) {
			String param2 = rs.getString(col2);
			if (param2 != null)
				results.put(param2, trie.search(param2));
		}
		rs.close();
		rs = null;
		for (Map.Entry entry : results.entrySet()) {
			if ((Boolean) entry.getValue() == true) {
				count++;
			}
		}
		results = null;
		if (size == 0)
			JobLogger.getLogger().info(Helper.class.getName(), "OUTSIDE",
					tab1 + "." + col1 + " " + table2 + "." + col2 + " NA");
		else {
			pct = count * 100 / size;
			if (pct != 0)
				JobLogger.getLogger().info(Helper.class.getName(), "findMatches",
						tab1 + "." + col1 + " " + table2 + "." + col2 + "  " + pct + "%");
			else
				System.out.print(".");
		}
	}

	public ResultSet getRecords(Connection conn, String sqlQuery) throws SQLException {
		PreparedStatement sql = conn.prepareStatement(sqlQuery, ResultSet.TYPE_FORWARD_ONLY,
				ResultSet.CONCUR_READ_ONLY);
//		sql.setFetchSize(Integer.MIN_VALUE); //For mysql
		sql.setFetchSize(10);
//		rs.setFetchSize(10);
		return sql.executeQuery();
//		return conn.prepareStatement(sqlQuery,ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY ).executeQuery();
	}

	public Map getColumnNames(ResultSet rs) throws SQLException {
		ArrayList<String> colNames = new ArrayList<String>();
		Map tuple = new HashMap<String, String>();
		ResultSetMetaData rsmd = rs.getMetaData();
		String type;
		for (int j = 1; j <= rsmd.getColumnCount(); j++) {
			type = rsmd.getColumnTypeName(j);
			if (isallowedtype(type) == true) {
				colNames.add(rsmd.getColumnName(j));
				tuple.put(rsmd.getColumnName(j), type);
			}
		}
		return tuple;
	}

	public int getRowCount(Connection conn, String t) throws SQLException {
		ResultSet r = conn.createStatement().executeQuery("SELECT COUNT(*) FROM " + t);
		int count = 0;
		while (r.next()) {
			count = r.getInt(1);
		}
		r.close();
		r = null;
		return count;
	}

	public boolean isallowedtype(String DataType) {
		switch (DataType.toUpperCase()) {
		case "BLOB":
		case "TINYBLOB":
		case "MEDIUMBLOB":
		case "LONGBLOB":
		case "VARBINARY":
		case "NVARBINARY":
		case "IMAGE":
		case "PHOTO":
		case "BIT":
		case "BOOLEAN":
		case "MONEY":
		case "CURRENCY":
		case "SMALLMONEY":
		case "BINARY_BOUBLE":
		case "BINARY_FLOAT":
		case "DOUBLE_PRECISION":
		case "DOUBLE PRECISION":
		case "BINARY VARYING":
		case "TIME":
		case "TIMESTAMP":
		case "INTERVAL":
		case "TIMESTAMP WITH LOCAL TIME ZONE":
		case "TIMESTAMP WITH TIME ZONE":
		case "DATETIME":
		case "DATETIME2":
		case "SMALLDATETIME":
		case "DATETIMEOFFSET":
		case "DATE":
		case "RAW":
		case "LONG RAW":
			return false;
		default:
			return true;
		}
	}
}