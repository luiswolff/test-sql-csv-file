package com.github.luiswolff.tests;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class SqlToCsvConverter {

	private String jdbcUrl;
	private String jdbcUser;
	private String jdbcPassword;
	private String sqlQuery;
	private String targetFile;

	void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	void setJdbcUser(String jdbcUser) {
		this.jdbcUser = jdbcUser;
	}

	void setJdbcPassword(String jdbcPassword) {
		this.jdbcPassword = jdbcPassword;
	}

	void setSqlQuery(String sqlQuery) {
		this.sqlQuery = sqlQuery;
	}

	void setTargetFile(String targetFile) {
		this.targetFile = targetFile;
	}
	
	void convert() {
		try (Connection conn = createConnection()) {
			ResultSet rs = conn.createStatement().executeQuery(sqlQuery);
			try (CSVPrinter out = createPrinter(rs)) {
				out.printRecords(rs);
			}
		} catch (SQLException | IOException e) {
			throw new IllegalStateException("Unexpected exception", e);
		}
	}

	private Connection createConnection() throws SQLException {
		return DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
	}

	private CSVPrinter createPrinter(ResultSet rs) throws IOException, SQLException {
		return new CSVPrinter(new PrintWriter(new FileOutputStream(targetFile)), CSVFormat.EXCEL.withHeader(rs));
	}

}
