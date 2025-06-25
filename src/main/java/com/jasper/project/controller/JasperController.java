package com.jasper.project.controller;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jasper.project.dto.ReportRequest;
import com.jasper.project.service.JasperService;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;

@RestController
@RequestMapping("/api")
public class JasperController {

	@Autowired
	private JasperService jasperService;

	@Autowired
	private Tracer tracer;

	@GetMapping("/get-jasper-with-param")
	public byte[] createJasperWithParam(@RequestParam String userId) {
		return jasperService.generateJasperReport("audit_report", Map.of("userId", userId));
	}

	@GetMapping("/get-jasper-without-param")
	public byte[] createJasperWithoutParam() {
		return jasperService.generateJasperReport("audit_report", Collections.emptyMap());
	}

	@PostMapping("/get-jasper-without-param")
	public ResponseEntity<byte[]> createJasperReportWithoutParam(@RequestBody ReportRequest reportRequest) {
		return jasperService.generateJasper(reportRequest);
	}

	@PostMapping("/get-jasper-with-param")
	public ResponseEntity<byte[]> createJasperWithParam(@RequestBody ReportRequest reportRequest) {
		return jasperService.generateJasper(reportRequest);
	}

	@GetMapping("/error500")
	public String triggerError() {
		throw new RuntimeException("Intentional 500 error for testing");
	}

	@GetMapping("/trace")
	public String sendTrace() {
		Span span = tracer.spanBuilder("manual-test-span").startSpan();
		span.addEvent("doing manual trace work");
		span.end();
		return "Manual trace sent!";
	}
}
