package com.example.vbill.bean;

import java.util.List;

public class ChartVO {
	private String totalAmount;
	private List<Point> linePoints;
	private List<Point> piePoints;
	private List<Point> columnPoints;
	private Account max;
	private Account min;

	public ChartVO() {
	}

	public ChartVO(String totalAmount, List<Point> linePoints, List<Point> piePoints, List<Point> columnPoints) {
		this.totalAmount = totalAmount;
		this.linePoints = linePoints;
		this.piePoints = piePoints;
		this.columnPoints = columnPoints;
	}

	public ChartVO(String totalAmount, List<Point> linePoints, List<Point> piePoints, List<Point> columnPoints, Account max, Account min) {
		this.totalAmount = totalAmount;
		this.linePoints = linePoints;
		this.piePoints = piePoints;
		this.columnPoints = columnPoints;
		this.max = max;
		this.min = min;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public List<Point> getLinePoints() {
		return linePoints;
	}

	public void setLinePoints(List<Point> linePoints) {
		this.linePoints = linePoints;
	}

	public List<Point> getPiePoints() {
		return piePoints;
	}

	public void setPiePoints(List<Point> piePoints) {
		this.piePoints = piePoints;
	}

	public List<Point> getColumnPoints() {
		return columnPoints;
	}

	public void setColumnPoints(List<Point> columnPoints) {
		this.columnPoints = columnPoints;
	}

	public Account getMax() {
		return max;
	}

	public void setMax(Account max) {
		this.max = max;
	}

	public Account getMin() {
		return min;
	}

	public void setMin(Account min) {
		this.min = min;
	}
}
