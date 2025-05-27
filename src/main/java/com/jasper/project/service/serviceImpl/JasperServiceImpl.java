package com.jasper.project.service.serviceImpl;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.jasper.project.util.ReportConstants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.jasper.project.dto.ReportRequest;
import com.jasper.project.service.JasperService;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;

@Service
public class JasperServiceImpl implements JasperService {

	@Autowired
	private DataSource dataSource;

	@Value("${jasper.report.path}")
	private String reportPath;

	@Override
	public byte[] generateJasperReport(String reportName, Map<String, Object> parameters) {
		System.out.println(reportName);
		try (InputStream reportStream = getClass().getResourceAsStream(reportPath + reportName + ".jrxml");
				Connection connection = dataSource.getConnection();
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

			Map<String, Object> params = new HashMap<>();
			if (parameters != null) {
				params.putAll(parameters);
			}

			JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, connection);
			JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
			return outputStream.toByteArray();

		} catch (Exception e) {
			throw new RuntimeException("Failed to generate Jasper report: " + reportName, e);
		}
	}

	@Override
	public ResponseEntity<byte[]> generateJasper(ReportRequest reportRequest) {

		byte[] reportBytes = generateRepottStream(reportRequest);

		String reportType = reportRequest.getReportType().toLowerCase();
		String reportName = reportRequest.getReportName() != null ? reportRequest.getReportName() : "report";

		MediaType mediaType = switch (reportType) {
		case ReportConstants.FORMAT_PDF -> ReportConstants.MEDIA_TYPE_PDF;
		case ReportConstants.FORMAT_XLS -> ReportConstants.MEDIA_TYPE_XLS;
		case ReportConstants.FORMAT_CSV -> ReportConstants.MEDIA_TYPE_CSV;
		default -> ReportConstants.MEDIA_TYPE_DEFAULT;
		};

		return ResponseEntity.ok().contentType(mediaType)
				.header(HttpHeaders.CONTENT_DISPOSITION,
						String.format(ReportConstants.ATTACHMENT_FILENAME_TEMPLATE, reportName, reportType))
				.body(reportBytes);
	}

	private byte[] generateRepottStream(ReportRequest reportRequest) {

		try (InputStream reportStream = getClass()
				.getResourceAsStream(reportPath + reportRequest.getReportName() + ".jrxml");
				Connection connection = dataSource.getConnection();
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

			Map<String, Object> params = new HashMap<>();

			if (reportRequest.getUserId() != null) {
				params.put(ReportConstants.USER_ID, reportRequest.getUserId());
			}
			JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, connection);

			String format = reportRequest.getReportType();
			switch (format.toLowerCase()) {
			case ReportConstants.FORMAT_PDF -> {
				JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
			}
			case ReportConstants.FORMAT_XLS -> {
				JRXlsExporter exporter = new JRXlsExporter();
				exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
				exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
				SimpleXlsReportConfiguration config = new SimpleXlsReportConfiguration();
				config.setOnePagePerSheet(false);
				config.setDetectCellType(true);
				config.setCollapseRowSpan(true);
				exporter.setConfiguration(config);
				exporter.exportReport();
			}
			case ReportConstants.FORMAT_CSV -> {
				JRCsvExporter exporter = new JRCsvExporter();
				exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
				exporter.setExporterOutput(new SimpleWriterExporterOutput(outputStream));
				exporter.exportReport();
			}
			default -> throw new IllegalArgumentException("Unsupported report format: " + format);
			}
			return outputStream.toByteArray();

		} catch (Exception e) {
			throw new RuntimeException("Failed to generate Jasper report: " + reportRequest.getReportName(), e);
		}

	}

	@Override
	public byte[] generateJasperReportWithoutParam(ReportRequest reportRequest) {
		try (InputStream reportStream = getClass()
				.getResourceAsStream(reportPath + reportRequest.getReportName() + ".jrxml");
				Connection connection = dataSource.getConnection();
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
			Map<String, Object> params = new HashMap<>();
			JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, connection);
			JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
			return outputStream.toByteArray();

		} catch (Exception e) {
			throw new RuntimeException("Failed to generate Jasper report: " + reportRequest.getReportName(), e);
		}
	}
}
