package com.jasper.project.util;

import org.springframework.http.MediaType;

public class ReportConstants {

	public static final String REPORT_PATH = "/reports/";

	public static final String FORMAT_PDF = "pdf";
	public static final String FORMAT_XLS = "xls";
	public static final String FORMAT_CSV = "csv";

	public static final String CONTENT_DISPOSITION_HEADER = "Content-Disposition";
	public static final String ATTACHMENT_FILENAME_TEMPLATE = "attachment; filename=%s.%s";

	public static final String USER_ID = "userId";

	public static final MediaType MEDIA_TYPE_PDF = MediaType.APPLICATION_PDF;
	public static final MediaType MEDIA_TYPE_XLS = MediaType.parseMediaType("application/vnd.ms-excel");
	public static final MediaType MEDIA_TYPE_CSV = MediaType.TEXT_PLAIN;
	public static final MediaType MEDIA_TYPE_DEFAULT = MediaType.APPLICATION_OCTET_STREAM;

}
