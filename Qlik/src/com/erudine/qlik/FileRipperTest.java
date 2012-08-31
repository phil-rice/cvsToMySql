package com.erudine.qlik;

import java.io.File;
import java.net.URL;
import java.util.Arrays;

import junit.framework.TestCase;

import org.junit.Test;

public class FileRipperTest extends TestCase {

	private FileRipper fileRipper;

	@Test
	public void testNoExtension() {
		assertEquals("abc", fileRipper.noExtension(new File("abc.csv")));
		assertEquals("abc", fileRipper.noExtension(new File("K:/asd/abc.csv")));
	}

	public void testNthLine() {
		checkNthLine(1, "one,two,three,four,\"fi,ve\"");
		checkNthLine(2, "1,a,,,2");
	}

	public void testIsInt() {
		assertEquals(true, fileRipper.isInt("123"));
		assertEquals(true, fileRipper.isInt("1"));
		assertEquals(false, fileRipper.isInt("a"));
		assertEquals(false, fileRipper.isInt("a1"));
		assertEquals(false, fileRipper.isInt("1a"));

	}

	public void testColumnIsInt() {
		checkColumnIsInt(0, true);
		checkColumnIsInt(1, false);
		checkColumnIsInt(2, true);
		checkColumnIsInt(3, false);
		checkColumnIsInt(4, true);
	}

	public void testGetColumnNames() {
		String[] actual = fileRipper.getColumnNames(getTextDotTxt());
		assertEquals(Arrays.asList("one", "two", "three", "four", "five"), Arrays.asList(actual));
	}

	public void testCreateStatement() {
		assertEquals("create table test( id integer auto_increment, PRIMARY KEY (id),one integer,two varchar(30),three integer,four varchar(30),five integer);", fileRipper.createStatement(getTextDotTxt()));
	}

	public void testSplit() {
		checkSplit("a,b,c", "a", "b", "c");
		checkSplit("a,b,", "a", "b", "");
		checkSplit("a,,", "a", "", "");
		checkSplit("a,,,c", "a", "", "", "c");
	}

	private void checkSplit(String raw, String ...expected) {
		assertEquals(Arrays.asList(expected), fileRipper.split(raw));
	}

	private void checkColumnIsInt(int col, boolean expected) {
		File file = getTextDotTxt();
		assertEquals(expected, fileRipper.columnIsInt(file, col));
	}

	private void checkNthLine(int n, String expected) {
		File file = getTextDotTxt();
		assertEquals(expected, fileRipper.nthLine(file, n));
		assertEquals(expected, fileRipper.nthLine(file, n));

	}

	private File getTextDotTxt() {
		URL url = getClass().getClassLoader().getResource("com/erudine/qlik/test.txt");
		File file = new File(url.getFile());
		return file;
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		fileRipper = new FileRipper();
		fileRipper.map("\"fi,ve\"", "five");
	}

}
