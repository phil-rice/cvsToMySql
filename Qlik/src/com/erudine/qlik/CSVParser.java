package com.erudine.qlik;

import java.util.ArrayList;
import java.util.List;

public class CSVParser {

	private final String line;
	private int index;
	 List<String> result;

	public CSVParser(String line) {
		this.line = line;
		this.index = 0;
		this.result = new ArrayList<String>();
		line();
	}

	private void line() {
		value();
		while (nextChEqualsAndEat(','))
			value();
	}

	private boolean nextChEqualsAndEat(char ch) {
		if (index >= line.length())
			return false;
		char thisCh = line.charAt(index);
		if (thisCh == ch) {
			index++;
			return true;
		}
		return false;
	}

	private boolean nextChIsNotAndNotEoln(char ch) {
		if (index >= line.length())
			return false; //
		char thisCh = line.charAt(index);
		return thisCh != ch;

	}

	private void value() {
		if (nextChEqualsAndEat('"'))
			quotedString();
		else
			string();

	}

	private void string() {
		StringBuilder stringBuilder = new StringBuilder();
		while (nextChIsNotAndNotEoln(','))
			addChar(stringBuilder);
		result.add(stringBuilder.toString());
	}

	private void addChar(StringBuilder stringBuilder) {
		stringBuilder.append(line.charAt(index++));
	}

	private void quotedString() {
		StringBuilder stringBuilder = new StringBuilder();
		while (nextChIsNotAndNotEoln('"'))
			addChar(stringBuilder);
		result.add(stringBuilder.toString());
		index++;//eat the "
	}
}
