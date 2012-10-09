package com.erudine.qlik;

public class ColumnInfo {

	public static ColumnInfo makeAsInt(String name) {
		return new ColumnInfo(name);
	}

	public static ColumnInfo makeAsVarChar(String name, int length) {
		ColumnInfo result = new ColumnInfo(name);
		result.justInts = false;
		result.length = length;
		return result;
	}

	public final String name;

	public boolean justInts = true;
	public int length;

	public ColumnInfo(String name) {
		super();
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (justInts ? 1231 : 1237);
		result = prime * result + length;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ColumnInfo other = (ColumnInfo) obj;
		if (justInts != other.justInts)
			return false;
		if (length != other.length)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ColumnInfo [name=" + name + ", justInts=" + justInts + ", length=" + length + "]";
	}

}
