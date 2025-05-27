package com.jasper.project.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.jasper.project.dto.ReportRequest;

public interface JasperService {

	public byte[] generateJasperReport(String reportName, Map<String, Object> parameters);

	public ResponseEntity<byte[]> generateJasper(ReportRequest reportRequest);
	
	public byte[] generateJasperReportWithoutParam(ReportRequest reportRequest);
}
