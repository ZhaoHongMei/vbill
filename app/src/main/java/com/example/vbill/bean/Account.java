package com.example.vbill.bean;

import java.sql.Date;
import java.sql.Time;

public class Account {
	private int id;
	private String createDay;
	private String createTime;
	private String amount;
	private String customerId;
	private Category category;

	public Account() {
	}

	public Account(int id, String createDay, String createTime, String amount, String customerId, Category category) {
		this.id = id;
		this.createDay = createDay;
		this.createTime = createTime;
		this.amount = amount;
		this.customerId = customerId;
		this.category = category;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCreateDay() {
		return createDay;
	}

	public void setCreateDay(String createDay) {
		this.createDay = createDay;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
}
