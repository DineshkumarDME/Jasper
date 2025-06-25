package com.jasper.project.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import com.jasper.project.dto.ReportRequest;
import com.jasper.project.service.serviceImpl.JasperServiceImpl;

@SpringBootTest
public class JasperServiceImplTest {

    @Autowired
    private JasperServiceImpl jasperService;

    @Test
    void testGenerateJasperReport() {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", "admin");

        byte[] pdf = jasperService.generateJasperReport("audit_report", params);

        assertNotNull(pdf, "Report byte array should not be null");
        assertTrue(pdf.length > 0, "Generated report should not be empty");
    }

    @Test
    void testGenerateJasperReportWithoutParam() {
        ReportRequest request = new ReportRequest();
        request.setReportName("audit_report");

        byte[] pdf = jasperService.generateJasperReportWithoutParam(request);

        assertNotNull(pdf, "Report byte array should not be null");
        assertTrue(pdf.length > 0, "Generated report should not be empty");
    }

    @Test
    void testGenerateJasperPDF() {
        ReportRequest request = new ReportRequest();
        request.setReportName("audit_report");
        request.setReportType("pdf");
        request.setUserId("admin");

        ResponseEntity<byte[]> response = jasperService.generateJasper(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 0);
        assertEquals("application/pdf", response.getHeaders().getContentType().toString());
    }

//    @Test
//    void testGenerateJasperCSV() {
//        ReportRequest request = new ReportRequest();
//        request.setReportName("audit_report");
//        request.setReportType("csv");
//        request.setUserId("admin");
//
//        ResponseEntity<byte[]> response = jasperService.generateJasper(request);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertNotNull(response.getBody());
//        assertTrue(response.getBody().length > 0);
//        assertEquals("text/csv", response.getHeaders().getContentType().toString());
//    }

    @Test
    void testGenerateJasperXLS() {
        ReportRequest request = new ReportRequest();
        request.setReportName("audit_report");
        request.setReportType("xls");
        request.setUserId("admin");

        ResponseEntity<byte[]> response = jasperService.generateJasper(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 0);
        assertEquals("application/vnd.ms-excel", response.getHeaders().getContentType().toString());
    }
}

