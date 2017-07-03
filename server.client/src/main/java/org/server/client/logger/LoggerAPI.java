package org.server.client.logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Date;

public class LoggerAPI {

	private static final LoggerFile loggerFileDetail = new LoggerFile();

	private static PrintWriter writer;

	public static void setLoggerFilePath(String path) {
		loggerFileDetail.setFilePath(path);
	}

	private static PrintWriter getFileWriter() {
		if (writer == null) {
			if (loggerFileDetail.getFilePath() != null) {
				try {
					File file = new File(loggerFileDetail.getFilePath());
					if (!file.exists()) {
						try {
							file.createNewFile();
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
					writer = new PrintWriter(new FileOutputStream(file));
					return writer;
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} else
				new RuntimeException("Unable to get the logger.");
		}
		return writer;
	}

	public static void logInfo(String info) {
		getFileWriter().println("[" + new Date() + " INFO" + "] " + info);
		LoggerAPI.flush(writer);
	}

	public static void logError(String error) {
		getFileWriter().println("[" + new Date() + " ERROR" + "] " + error);
		LoggerAPI.flush(writer);
	}

	public static void logWarning(String warning) {
		getFileWriter().println("[" + new Date() + " WARN" + "] " + warning);
		LoggerAPI.flush(writer);
	}

	private static void flush(PrintWriter writer) {
		writer.flush();
	}

	private static void closeWriter(PrintWriter writer) {
		try {
			writer.close();
			LoggerAPI.writer = null;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static void closeWriter() {
		try {
			writer.close();
			writer = null;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static class LoggerFile {
		private String filePath;

		public String getFilePath() {
			return filePath;
		}

		public void setFilePath(String filePath) {
			this.filePath = filePath;
		}

	}

	public static void logError(Exception ex) {
		getFileWriter().println("[" + new Date() + " ERROR" + "] " + ex.getMessage());
		for (StackTraceElement stackTraceElement : ex.getStackTrace()) {
			getFileWriter()
					.println("[" + new Date() + " ERROR" + "] " + "Class Name:" + stackTraceElement.getClassName()
							+ " Method Name:" + stackTraceElement.getMethodName() + " Field Name:"
							+ stackTraceElement.getFileName() + " Line Number:" + stackTraceElement.getLineNumber());
		}
		flush(getFileWriter());
	}

}
