/*******************************************************************************
 * Copyright (C) 2011 Atlas of Living Australia
 * All Rights Reserved.
 * 
 * The contents of this file are subject to the Mozilla Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/MPL/
 * 
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 ******************************************************************************/
package au.org.ala.delta.slotfile;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.SyncFailedException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
//binfile.cpp

//Encapsulate a binary data file similar to TFile
//but incorporate exception handling.
//
//Currently uses low level c function.
//Could be rewritten to use ansi compatible FILE functions or c++ streams.
 * 
 */
public class BinFile {

	protected String _filename;
	protected BinFileMode _fileMode;
	protected RandomAccessFile _file;
	protected byte[] _buffer;
	protected int _filePointer;

	public BinFile() {
	}

	public BinFile(String filename, BinFileMode mode) {
		if (filename == null) {
			filename = makeTempFileName();
			mode = BinFileMode.FM_TEMPORARY;
		}
		_filename = filename;
		open(mode);
	}

	public void write(byte[] data) {
		try {
			_file.write(data);
		} catch (IOException ioex) {
			throw new RuntimeException(ioex);
		}
	}

	public byte[] read(int length) {
		return readBytes(length);
	}

	protected String makeTempFileName() {
		try {
			return File.createTempFile("delta_temp_file", ".dlt").getAbsolutePath();
		} catch (IOException ioex) {
			throw new RuntimeException(ioex);
		}
	}

	public void open(BinFileMode mode) {
		_fileMode = mode;
		File f = new File(_filename);
		_filename = f.getAbsolutePath();
		try {
			String ra_mode = "r";
			switch (mode) {
				case FM_APPEND:
				case FM_NEW:
				case FM_EXISTING:
				case FM_TEMPORARY:
					ra_mode = "rw";
					break;
			}
			_file = new RandomAccessFile(f, ra_mode);

			/*if (mode == BinFileMode.FM_READONLY) {
				CodeTimer t = new CodeTimer("loading file buffer");
				_file.seek(0);
				_buffer = new byte[(int) _file.length()];
				_file.read(_buffer);
				t.stop(true);
				_file.seek(0);
			}*/
		} catch (IOException ioex) {
			throw new RuntimeException(ioex);
		}
	}

	
	public void close() {
		if (_file != null) {
			try {
				_file.close();
				_file = null;
			} catch (IOException ioex) {
				throw new RuntimeException(ioex);
			}
		}
		if (_buffer != null) {
			_filePointer = 0;
			_buffer = null;
		}
		// Clean up the file after closing if it is a temporary file.
		if (_fileMode == BinFileMode.FM_TEMPORARY) {
			File temp = new File(_filename);
			temp.delete();
		}
	}

	public boolean isOpen() {
		return _file != null;
	}

	public int seek(int offset) {

		if (_buffer != null) {
			_filePointer = offset;
		} else {
			assert _file != null;
			try {
				_file.seek(offset);
			} catch (IOException ioex) {
				throw new RuntimeException(ioex);
			}
		}
		return offset;
	}
	
	public int seekToEnd() {
		assert _file != null;
		try {
			return seek((int)_file.length());
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public int seekToBegin() {
		assert _file != null;
		
		return seek(0);
	}
	

	public int readBytes(byte[] buffer) {
		if (_buffer != null) {
			for (int i = 0; i < buffer.length; ++i) {
				buffer[i] = _buffer[_filePointer];
				_filePointer++;
			}
			return buffer.length;
		} else {
			try {
				return _file.read(buffer);
			} catch (IOException ioex) {
				throw new RuntimeException(ioex);
			}
		}
	}

	public int tell() {
		if (_buffer != null) {
			return _filePointer;
		} else {
			try {
				return (int) _file.getFilePointer();
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		}
	}

	protected byte[] readBytes(int count) {
		byte[] buffer = new byte[count];
		int read = readBytes(buffer);
		if (read < count) {
			throw new RuntimeException("Incorrect number of bytes read reading " + this.getClass().getSimpleName() + " Expected " + count + " got " + read);
		}
		return buffer;
	}

	public void writeBytes(byte[] buffer) {
		try {
			_file.write(buffer);
		} catch (IOException ioex) {
			throw new RuntimeException(ioex);
		}
	}

	public void writeByte(byte b) {
		try {
			_file.write(b);
		} catch (IOException ioex) {
			throw new RuntimeException(ioex);
		}
	}

	public void writeShort(short value) {
		ByteBuffer buffer = ByteBuffer.allocate(2);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putShort(value);
		writeBytes(buffer.array());
	}

	public void write(int i) {
		writeInt(i);
	}

	public void write(short i) {
		writeShort(i);
	}

	public void write(byte b) {
		writeByte(b);
	}

	public void write(String str, int length) {
		swrite(str, length);
	}

	public void write(long lng) {
		writeLong(lng);
	}

	public void writeInt(int value) {
		ByteBuffer b = ByteBuffer.allocate(4);
		b.order(ByteOrder.LITTLE_ENDIAN);
		b.putInt(value);
		writeBytes(b.array());
	}

	public void writeLong(long value) {
		ByteBuffer b = ByteBuffer.allocate(8);
		b.order(ByteOrder.LITTLE_ENDIAN);
		b.putLong(value);
		writeBytes(b.array());
	}
	
	protected int read() {
		if (_buffer != null) {
			if (_filePointer >= _buffer.length) {
				return -1;
			}
			int result = _buffer[_filePointer];
			_filePointer++;
			return result & 0x000000ff;
		} else {
			try {
				return _file.read();
			} catch (IOException ioex) {
				throw new RuntimeException(ioex);
			}
		}
	}

	public byte readByte() {
		int b = read();
		if (b < 0) {
			throw new RuntimeException("EOF encountered reading object of type " + this.getClass().getSimpleName());
		}
		return (byte) b;
	}

	public short readShort() {
		// Files are little endian
		byte lo = readByte();
		byte hi = readByte();

		return (short) (((hi << 8) & 0xFF00) + (lo & 0x00FF));
	}

	public long readLong() {
		byte[] bytes = readBytes(8);
		ByteBuffer b = ByteBuffer.wrap(bytes);
		b.order(ByteOrder.LITTLE_ENDIAN);
		return b.getLong();
	}

	public int readInt() {
		byte[] bytes = readBytes(4);
		ByteBuffer b = ByteBuffer.wrap(bytes);
		b.order(ByteOrder.LITTLE_ENDIAN);

		return b.getInt();
	}

	// Not really sure what swrite is all about yet...
	public void swrite(byte[] data) {
		write(data);
	}

	public void swrite(String data, int length) {
		byte[] buffer = new byte[length];
		byte[] stringBytes = data.getBytes();
		for (int i = 0; i < length; ++i) {
			if (i < stringBytes.length) {
				buffer[i] = stringBytes[i];
			} else {
				buffer[i] = 0;
			}
		}
		write(buffer);
	}

	public String sread(int size) {
		byte[] buffer = readBytes(size);
		return new String(buffer);
	}
	
	/**
	 * Forces the contents of this file to be written to disk.
	 */
	public void commit() {
		
		try {
			_file.getFD().sync();
		}
		catch (SyncFailedException e) {
			// TODO uhoh, what do we do if the sync fails... (i am not sure why it would)
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO mmm file descriptor was null - that is bad.
			e.printStackTrace();
		}
		if (_fileMode == BinFileMode.FM_TEMPORARY) {
			_fileMode = BinFileMode.FM_EXISTING;
		}
	}
	
	public String getFileName() {
		return _filename;
	}
	
	public long getFileTime() { 
		File f = new File(_filename);
		return f.lastModified();
	}
	
	public void setFileTime (long time) { 
		File f = new File(_filename);
		f.setLastModified(time);
	}

	public BinFileMode getFileMode() {
		return _fileMode;
	}
	
	public void copyFile(BinFile other, int dataSize) {
		// Make sure other is not this.
		  if(other == this) {
			  // TODO create a BinFileException
			  throw new RuntimeException("FE_BAD_COPY : " + _filename);
		  }
		  // Copy in blocks.
		  int blkSize = 1024 * 8; // 8K
		  int numBlk = dataSize / blkSize;
		  int rest = dataSize % blkSize;
		  byte[] buf = new byte[blkSize];
		  for (int i=0; i<numBlk; i++) {
		      other.readBytes(buf);
		      swrite(buf);
		  }
		  buf = new byte[rest];
		  other.readBytes(buf);
		  swrite(buf);
	}
	
	public void setLength(int newLength) {
		try {
			_file.setLength(newLength);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public int getLength() {
		try {
			return (int)_file.length();
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
