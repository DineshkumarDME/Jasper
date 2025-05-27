package com.jasper.project.dto;

public class ReportRequest {
	private String reportName;
	private String userId;
	private String reportType;

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public ReportRequest(String reportName, String userId, String reportType) {
		super();
		this.reportName = reportName;
		this.userId = userId;
		this.reportType = reportType;
	}

	public ReportRequest() {
		super();
	}

	@Override
	public String toString() {
		return "ReportRequest [reportName=" + reportName + ", userId=" + userId + ", reportType=" + reportType + "]";
	}

}
