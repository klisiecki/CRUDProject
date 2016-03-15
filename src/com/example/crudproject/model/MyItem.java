package com.example.crudproject.model;

import java.util.Date;

public class MyItem {
	private int id;
	private String name;
	private Date date;

	public MyItem() {
	}

	public MyItem(int id, String name, Date date) {
		this.id = id;
		this.name = name;
		this.date = date;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "MyItem [id=" + id + ", name=" + name + ", date=" + date + "]";
	}
}
