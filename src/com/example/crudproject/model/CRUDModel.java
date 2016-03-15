package com.example.crudproject.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class CRUDModel {
	private static final String TABLE_NAME = "mytable";
	private static final String DATA_SOURCE = "java:comp/env/jdbc/sqlite";
	private Connection conn;

	public void initDB() throws NamingException, SQLException {
		Context ctx = new InitialContext();
		DataSource ds = (DataSource) ctx.lookup(DATA_SOURCE);
		conn = ds.getConnection();
		Statement statement = conn.createStatement();
		statement.executeUpdate("create table if not exists " + TABLE_NAME + "(id integer primary key autoincrement,"
				+ "name varchar(255)," + "date Date)");
	}

	public void addItem(MyItem item) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("insert into " + TABLE_NAME + "(name, date) values(?, ?)");
		ps.setString(1, item.getName());
		long time = item.getDate().getTime();
		ps.setDate(2, new java.sql.Date(time));
		ps.execute();
	}

	public void deleteItem(int id) throws SQLException {
		Statement st = conn.createStatement();
		st.execute("delete from " + TABLE_NAME + " where id = " + id);
	}

	public MyItem getItem(int id) throws SQLException {
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery("select * from " + TABLE_NAME + " where id = " + id);
		if (rs.next()) {
			MyItem item = new MyItem();
			item.setId(id);
			item.setName(rs.getNString("name"));
			item.setDate(rs.getDate("date"));
			return item;
		}
		return null;
	}

	public void editItem(MyItem item) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("update " + TABLE_NAME + " set name = ?, date = ? where id = "
				+ item.getId());
		ps.setString(1, item.getName());
		long time = item.getDate().getTime();
		ps.setDate(2, new java.sql.Date(time));
		ps.execute();
	}

	public List<MyItem> getAllItems() throws SQLException {
		ArrayList<MyItem> list = new ArrayList<MyItem>();
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery("select * from " + TABLE_NAME);
		while (rs.next()) {
			MyItem item = new MyItem();
			item.setId(rs.getInt("id"));
			item.setName(rs.getString("name"));
			item.setDate(rs.getDate("date"));
			list.add(item);
		}
		return list;
	}
	
	public void addRandomItem() throws NamingException, SQLException {
		MyItem item = new MyItem();
		Random generator = new Random();
		String AB = "abcdefghijklmnopqrstuvwxyz";
		int len = generator.nextInt(30) + 10;
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++)
			sb.append(AB.charAt(generator.nextInt(AB.length())));
		item.setName(sb.toString());
		item.setDate(new java.util.Date(System.currentTimeMillis()));
		addItem(item);
	}

}
