package com.erudine.qlik;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

public class FileRipper {

	public final FileFilter isDirectory = new FileFilter() {
		@Override
		public boolean accept(File pathname) {
			return pathname.isDirectory();
		}
	};

	public final String noExtension(File file) {
		int index = file.getName().lastIndexOf(".");
		if (index == -1)
			return file.getName();
		else
			return file.getName().substring(0, index);
	}

	public String firstLine(File file) {
		return nthLine(file, 1);
	}

	public String nthLine(File file, int n) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			try {
				for (int i = 0; i < n - 1; i++)
					reader.readLine();
				return reader.readLine();
			} finally {
				reader.close();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private Pattern isIntPattern = Pattern.compile("\\d+");
	private Map<String, String> map = new HashMap<String, String>();

	public boolean isInt(String s) {
		return isIntPattern.matcher(s).matches();
	}

	List<String> split(String string) {
		List<String> result = new ArrayList<String>();
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < string.length(); i++) {
			char ch = string.charAt(i);
			if (ch == ',') {
				result.add(builder.toString());
				builder = new StringBuilder();
			}else
				builder.append(ch);
		}
		result.add(builder.toString());
		return result;

	}

	public boolean columnIsInt(File file, int col) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			try {
				String firstLine = reader.readLine(); // good bye header
				while (true) {
					String line = reader.readLine();
					if (line == null)
						return false;
					List<String> cols = split(line);
					try {
						String value = cols.get(col);
						if (value.length() > 0)
							return isInt(value);
					} catch (Exception e) {
						throw new RuntimeException("File: " + file + "\nFirstLine: " + firstLine + "\nLine: " + line + "\nCols: " + cols + "\nCol: " + col, e);
					}
				}
			} finally {
				reader.close();
			}
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public FileFilter extension(final String extension) {
		return new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return pathname.getAbsolutePath().endsWith(extension);
			}
		};
	}

	public void walkFiles(File root, FileFilter filter, ICallback<File> callback) {
		if (root.isDirectory()) {
			for (File file : root.listFiles(filter))
				walkFiles(file, filter, callback);
			for (File file : root.listFiles(isDirectory))
				walkFiles(file, filter, callback);
		} else if (filter.accept(root))
			callback.call(root);
	}

	public String createStatement(File file) {
		StringBuffer buffer = new StringBuffer();
		String[] columnNames = getColumnNames(file);
		for (int i = 0; i < columnNames.length; i++) {
			if (buffer.length() > 0)
				buffer.append(",");
			buffer.append(columnNames[i]);
			buffer.append(" ");
			buffer.append(columnIsInt(file, i) ? "integer" : "varchar(30)");
		}
		return "create table " + noExtension(file) + "( id integer auto_increment, PRIMARY KEY (id)," + buffer + ");";
	}

	public String[] getColumnNames(File file) {
		String firstLine = firstLine(file);
		for (Entry<String, String> entry : map.entrySet())
			firstLine = firstLine.replace(entry.getKey(), entry.getValue());
		String[] split = firstLine.split(",");
		return split;
	}

	public void map(String from, String to) {
		map.put(from, to);
	}

}
