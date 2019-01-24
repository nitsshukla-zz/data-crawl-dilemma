package com.learn.logging.iterator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.ListIterator;

import org.apache.commons.io.input.CountingInputStream;

import com.google.gson.Gson;

public class MetadataIterator implements Iterator<TableMetadata>, Cloneable {
	ListIterator<File> filesIterator;
	BufferedReader stream;
	CountingInputStream in;
	private File[] files;

	public MetadataIterator(File[] files) {
		init(files, 0);
	}
	private void init(File[] files, int index) {
		this.filesIterator = Arrays.asList(files).listIterator(index);
		initStream();
		this.files = files;
	}
	public MetadataIterator(File[] files, int nextIndex, long l) {
		this.filesIterator = Arrays.asList(files).listIterator(nextIndex);
		this.files = files;
		try {
			initStream();
			in.skip(l);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initStream() {
		if (in!=null)
			try {
				in.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		if (filesIterator.hasNext()) {
			File file = filesIterator.next();
			try {
				in = new CountingInputStream(new FileInputStream(file));
				file = null;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	public MetadataIterator clone() {
		MetadataIterator iterator = new MetadataIterator(files, filesIterator.previousIndex(), in.getByteCount());
		return iterator;
	}

  	public boolean hasNext() {
		try {
			boolean hasNext = in.available()!=0;
			if (hasNext) {
				return hasNext;
			}
			if (filesIterator.hasNext()) {
				initStream();
				return hasNext();
			}
			return false;
		} catch (IOException e) {
			throw new RuntimeException("exception while reading available " + e.getMessage());
		}
	}
	public TableMetadata next() {
		StringBuilder builder = new StringBuilder();
		String json;
		int c1; char c;
		try {
			while((c1=in.read())!=-1) {
				c = (char)c1;
				if (c!='\n')
					builder.append(c);
				else {
					json = builder.toString();
					Gson gson = new Gson();
					return gson.fromJson(json, TableMetadata.class);	
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			if(in.available()!=0)
				throw new RuntimeException("Wrong format of file");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}