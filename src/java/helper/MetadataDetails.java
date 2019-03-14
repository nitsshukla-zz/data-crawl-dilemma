package helper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MetadataDetails {
	public Map getColumnNames(ResultSet rs) throws SQLException {
		ArrayList<String> colNames = new ArrayList<String>();
		Map tuple = new HashMap<String, String>();
		ResultSetMetaData rsmd = rs.getMetaData();
		rs.close();
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
		ResultSet r = conn.createStatement().executeQuery("SELECT COUNT(1) FROM " + t);
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
