package com.learn.logging.iterator;

public class TableMetadata {
	public String tableName;
	public Column column;
	public int rowcount;
	@Override
	public String toString() {
		return "Table Name - "+tableName+" Column - "+column+" Rowcount - "+rowcount ;
	}
}
