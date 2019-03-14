package helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.learn.logging.logger.JobLogger;
import com.learn.logging.optimised.Trie;

public class FindMatches {
	@SuppressWarnings("rawtypes")
	public void findMatches(Trie trie, ResultSet rs, int size, String col2, String table2, String tab1, String col1)
			throws SQLException {
//    	System.out.println("Trie in findMatches "+trie+" T1 "+tab1+" T2 "+table2+" C1 "+col1+" C2 "+col2);

		HashMap<String, Boolean> results = new HashMap<>();
		double count = 0;
		double pct;
		try {
			while (rs.next()) {
				String param2 = rs.getString(col2);
				if (param2 != null)
					results.put(param2, trie.search(param2));
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Came in findMatches");
		} finally {
			rs.close();
		}
		for (Map.Entry entry : results.entrySet()) {
			if ((Boolean) entry.getValue() == true) {
				count++;
			}
		}
		results = null;
		if (size == 0) {
			JobLogger.getLogger().info(FindMatches.class.getName(), "OUTSIDE",
					tab1 + "." + col1 + " " + table2 + "." + col2 + " NA");
		} else {
			pct = count * 100 / size;
			if (pct != 0)
				JobLogger.getLogger().info(FindMatches.class.getName(), "findMatches",
						tab1 + "." + col1 + " " + table2 + "." + col2 + "  " + pct + "%");
		}
	}

}
