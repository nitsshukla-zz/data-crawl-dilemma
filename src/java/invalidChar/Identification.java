/*package invalidChar;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import com.learn.logging.connections.CPInstance;
import com.learn.logging.iterator.Column;
import com.learn.logging.iterator.MetadataIterator;
import com.learn.logging.iterator.TableMetadata;
import com.learn.logging.logger.JobLogger;
import com.learn.logging.optimised.Helper;
import com.learn.logging.optimised.StartAnalysis;

public class Identification {
	static boolean status = false;
	static boolean buffer = false;
	

	public static void main(String[] args)
			throws InterruptedException, SQLException, ClassNotFoundException, FileNotFoundException {
		final int SAMPLINGPCT = 5;
		Helper obj = new Helper();
		JobLogger.getLogger().info(StartAnalysis.class.getName(), "main method", "Start");
		CPInstance connObj = new CPInstance();
		Connection conn = null;
		System.out.println("Started");
		Instant start = Instant.now();
		try {
			conn = connObj.getConnectionFRomPool();
			if (conn != null)
				JobLogger.getLogger().info(StartAnalysis.class.getName(), "main method", "Connection Established");
			System.out.println("Connection object "+conn);
			//Trigger only if metadata present
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
					int rowCount1 = pri.rowCount;
					c1 = null;
					pri = null;
					String colDataFetchQry = "select " + col1 + " from " + table1;
					PreparedStatement sql = conn.prepareStatement(colDataFetchQry,ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY );
					ResultSet rs = sql.executeQuery();
					int size = Math.round(SAMPLINGPCT *rowCount1/100);
					if(size==0&&rowCount1!=0)
						size = rowCount1;
					obj.checkFunc(rs, col1, size);
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
*/