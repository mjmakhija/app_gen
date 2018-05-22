package com.hm.app_gen;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MysqlHelper
{

	public Connection connect(String url, String username, String password, String database) throws ClassNotFoundException, SQLException
	{
		String driver = "com.mysql.jdbc.Driver";
		Class.forName(driver);
		return DriverManager.getConnection(url + "/" + database, username, password);
	}

	public Connection connect(String url, String username, String password) throws ClassNotFoundException, SQLException
	{
		String driver = "com.mysql.jdbc.Driver";
		Class.forName(driver);
		return DriverManager.getConnection(url, username, password);
	}

	static List<String> getAllTables(String database, Statement stmt) throws SQLException
	{
		List<String> table = new ArrayList<>();
		ResultSet rs;
		rs = stmt.executeQuery("SHOW TABLE STATUS FROM `" + database + "`;");
		while (rs.next())
		{
			table.add(rs.getString("Name"));
		}
		return table;
	}

}
