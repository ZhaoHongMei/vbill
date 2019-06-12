package com.example.vbill.bean;

import java.util.List;

public class ChartVO {
	private String totalAmount;
	private List<Point> linePoints;
	private List<Point> piePoints;

	public ChartVO() {
	}

	public ChartVO(String totalAmount, List<Point> linePoints, List<Point> piePoints) {
		this.totalAmount = totalAmount;
		this.linePoints = linePoints;
		this.piePoints = piePoints;
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
}
