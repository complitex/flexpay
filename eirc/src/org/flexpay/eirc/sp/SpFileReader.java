package org.flexpay.eirc.sp;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

public class SpFileReader implements Serializable {

	public static final String DEFAULT_CHARSET = "Cp1251";
	public static final int MAX_RECORD_LENGTH = 10000;

	private BufferedInputStream is;
	private String charset;
	private int b;
	private byte[] record = new byte[MAX_RECORD_LENGTH];
	private Message message;
	private long position;

	public SpFileReader(InputStream is) {
		this.is = new BufferedInputStream(is);
		this.charset = DEFAULT_CHARSET;
		this.position = 0;
	}

	public SpFileReader(InputStream is, int bufferSize) {
		this.is = new BufferedInputStream(is, bufferSize);
		this.charset = DEFAULT_CHARSET;
		this.position = 0;
	}

	public Message readMessage() throws IOException, RegistryFormatException {

		if (b == -1) {
			return null;
		}

		int ind = 0;
		while ((b = is.read()) != -1) {
			position++;
			if (b == Message.MESSAGE_TYPE_HEADER
					|| b == Message.MESSAGE_TYPE_RECORD
					|| b == Message.MESSAGE_TYPE_FOOTER) {
				if (message == null) {
					message = new Message();
					message.setType(b);
					message.setPosition(position);
				} else {
					break;
				}
			} else if (message != null) {
				if (ind >= MAX_RECORD_LENGTH) {
					throw new RegistryFormatException("Message is too long", position);
				}
				record[ind] = (byte) b;
				ind++;
			}
		}

		if (message != null) {
			message.setBody(new String(record, 0, ind, charset).trim());
		}

		Message result = message;
		message = new Message();
		message.setType(b);
		message.setPosition(position);

		return result;
	}

    public void setInputStream(InputStream is) throws IOException {
		if (is == null) {
			this.is = null;
		} else {
			this.is = new BufferedInputStream(is);
			this.is.skip(position);
		}
    }

	public long getPosition() {
		return position;
	}

	public void close() throws IOException {
        is.close();
    }

	public void setCharset(String charset) {
		this.charset = charset;
	}

	@Override
	public String toString() {
		return "SpFileReader{" +
				"position=" + position +
				", message=" + message +
				'}';
	}

	public static class Message implements Serializable {

		public static final int MESSAGE_TYPE_HEADER = 0xC;
		public static final int MESSAGE_TYPE_RECORD = 0x3;
		public static final int MESSAGE_TYPE_FOOTER = 0xB;

		private int type;
		private String body;
		private long position;

		public Message() {
		}

		public int getType() {
			return type;
		}

		public void setType(int type) {
			this.type = type;
		}

		public String getBody() {
			return body;
		}

		public void setBody(String body) {
			this.body = body;
		}

		public long getPosition() {
			return position;
		}

		public void setPosition(long position) {
			this.position = position;
		}


		@Override
		public String toString() {
			return "Message{" +
					"type=" + type +
					", body='" + body + '\'' +
					", position=" + position +
					'}';
		}
	}

}
