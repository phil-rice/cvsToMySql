package com.erudine.qlik;

import java.io.File;

public class FilesToMySql {
	public static void main(String[] args) {
		File root = new File("L:/Eur");
		final FileRipper ripper = new FileRipper();
		ripper.map("\"Autonumber(1,1)\"", "autonumber");
		ripper.walkFiles(root, ripper.extension("csv"), new ICallback<File>() {
			@Override
			public void call(File file) {
				String tableName = ripper.noExtension(file);
				System.out.println("drop table if exists " + tableName+";");
			}
		});
		ripper.	walkFiles(root, ripper.extension("csv"), new ICallback<File>() {
			@Override
			public void call(File file) {
				System.out.println(ripper.createStatement(file));
			}
		});
		ripper.	walkFiles(root,ripper. extension("csv"), new ICallback<File>() {
			@Override
			public void call(File file) {
				String tableName = ripper.noExtension(file);
				String firstLine = ripper.firstLine(file);
				String withoutAutoNumber = firstLine.replace("\"Autonumber(1,1)\"", "autoNumber");
				StringBuffer buffer = new StringBuffer();
				buffer.append("load data local infile '" + file.getAbsolutePath().replace('\\','/') + "' into table " + tableName + " fields terminated by ',' ");
				buffer.append(" lines terminated by '\\n'");
				buffer.append(" ignore 1 lines");
				buffer.append(" (" + withoutAutoNumber + ");");
				System.out.println(buffer);
			}
		});
	}
}
