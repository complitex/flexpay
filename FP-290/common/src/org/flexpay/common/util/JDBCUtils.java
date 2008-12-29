package org.flexpay.common.util;

import java.sql.Connection;
import java.sql.SQLException;

public class JDBCUtils {

	public static void closeQuitly(Connection c) {
		if (c != null) {
			try {
				c.close();
			} catch (SQLException e) {
				// do nothing
			}
		}
	}
}
