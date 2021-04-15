package com.github.luiswolff.tests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class SqlToCsvConverterTest {
	
	static final String JDBC_URL = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";
	static final String JDBC_USER = "sa";
	static final String JDBC_PASSWORD = "sa";
	
	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();
	
	@Before
	public void setup() throws Exception {
		executeSql(resource("setup.ddl"), resource("initialData.sql"));
	}

	@Test
	public void test() throws IOException {
		File targetFile = new File(tempFolder.getRoot(), "actual.csv");
		
		SqlToCsvConverter converter = new SqlToCsvConverter();
		converter.setJdbcUrl(JDBC_URL);
		converter.setJdbcUser(JDBC_USER);
		converter.setJdbcPassword(JDBC_PASSWORD);
		converter.setSqlQuery(resource("testQuery.sql"));
		converter.setTargetFile(targetFile.getAbsolutePath());
		
		converter.convert();
		
		assertTrue("Target file wasn't created", targetFile.exists());
		assertEquals("File has not the expected content", resource("expected.csv"), new String(Files.readAllBytes(targetFile.toPath())));
	}
	
	@After
	public void tearDown() throws Exception {
		executeSql(resource("tearDown.ddl"));
	}

	private void executeSql(String... statements) throws SQLException {
		try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
			Statement statment = conn.createStatement();
			for (String sql : statements) {
				statment.execute(sql);
			}
		}
	}

	private String resource(String fileName) throws IOException {
		StringWriter out = new StringWriter();
		try (Reader in = new InputStreamReader(getClass().getResourceAsStream(fileName))) {
			int c;
			while ((c = in.read()) != -1) {
				out.write(c);
			}
		}
		return out.toString();
	}

}
