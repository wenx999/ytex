package my.mas.jdl.test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * Manage of sql for resources files.
 * 
 * @author mas
 */
public class SqlJdl {
	/**
	 * @param connection
	 *            the connection to manage
	 * @throws SQLException
	 *             exception
	 */
	public static void create(Connection connection) throws SQLException {
		String sql = "create table tab_test (id numeric, name varchar(32), thekey numeric, thevalue varchar(64), code numeric, descr varchar(256));";
		Statement statement = connection.createStatement();
		statement.executeUpdate(sql);
		statement.close();
	}

	/**
	 * @param connection
	 *            the connection to manage
	 * @return list of rows
	 * @throws SQLException
	 *             exception
	 */
	private static List<String[]> select(Connection connection) throws SQLException {
		String sql = "select * from tab_test;";
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery(sql);
		List<String[]> list = new ArrayList<String[]>();
		while (resultSet.next()) {
			list.add(new String[] { resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5), resultSet.getString(6) });
		}
		resultSet.close();
		statement.close();
		return list;
	}

	/**
	 * @param connection
	 *            the connetcion to manage
	 * @param print
	 *            check if print rows
	 * @return list of rows
	 * @throws SQLException
	 *             exception
	 */
	public static List<String[]> select(Connection connection, boolean print) throws SQLException {
		List<String[]> list = select(connection);
		if (print) {
			for (String[] c : list) {
				System.out.println(c[0] + "\t" + StringUtils.rightPad(c[1], 31) + c[2] + "\t" + StringUtils.rightPad(c[3], 23) + c[4] + "\t" + c[5]);
			}
		}
		return list;
	}

	/**
	 * @param connection
	 *            the connection to manage
	 * @throws SQLException
	 *             exception
	 */
	public static void delete(Connection connection) throws SQLException {
		String sql = "delete from tab_test";
		Statement statement = connection.createStatement();
		statement.executeUpdate(sql);
		statement.close();
	}

	/**
	 * @param connection
	 *            the connection to manage
	 * @throws SQLException
	 *             exception
	 */
	public static void drop(Connection connection) throws SQLException {
		String sql = "drop table tab_test;";
		Statement statement = connection.createStatement();
		statement.executeUpdate(sql);
		statement.close();
	}
}
