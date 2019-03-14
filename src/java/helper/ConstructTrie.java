package helper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.xerces.util.XMLChar;

import com.learn.logging.logger.JobLogger;
import com.learn.logging.optimised.Trie;

public class ConstructTrie {

	public Trie constructCustomizedTrie(ResultSet rs1, String col1, int size) throws SQLException {
		Trie trie = new Trie();
		while (rs1.next() && size != 0) {
			size--;
			if (rs1.getString(col1) == null)
				continue;
			trie.insert(rs1.getString(col1));
		}
		rs1.close();
		rs1 = null;
		return trie;
	}
	private void validationSetup(String codePoint, String col1) {
		for (int i = 0; i < codePoint.length(); i++) {

			try {
				if (XMLChar.isValid(codePoint.charAt(i))) {
				} else {
					System.out.println("Invalid character " + codePoint.charAt(i) + " at " + col1);
					JobLogger.getLogger().info(ConstructTrie.class.getName(), "checkFunc",
							"Invalid character " + codePoint.charAt(i) + " at " + col1);

				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
	}



	public void garbageCollect() {
		System.out.println(" >> GC triggered");
		System.out.println(" Before GC :\t MAX : " + (Runtime.getRuntime().maxMemory() / 1048576) + " MB \t TOTAL : "
				+ (Runtime.getRuntime().totalMemory() / 1048576) + " MB \t FREE : "
				+ (Runtime.getRuntime().freeMemory() / 1048576) + " MB");
		System.gc();
		System.out.println(" After GC :\t MAX : " + (Runtime.getRuntime().maxMemory() / 1048576) + " MB \t TOTAL : "
				+ (Runtime.getRuntime().totalMemory() / 1048576) + " MB \t FREE : "
				+ (Runtime.getRuntime().freeMemory() / 1048576) + " MB");
		System.out.println("\n\n");
	}


}