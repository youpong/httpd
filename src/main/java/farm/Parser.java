package farm;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

public class Parser {
	/**
	 * generic-message = start-line *(message-header CRLF) CRLF [ message-body ]
	 * start-line = Request-Line | Status-Line
	 * 
	 * @param in
	 * @return
	 */
	public static HttpRequest parseHttpRequest(Reader reader) {
		Unreadable in = new Unreadable(reader);
		HttpRequest request = new HttpRequest();
		// TODO:
		try {
			requestLine(in, request); // = start-line
			messageHeader(in, request);
			if (request.hasMessageBody())
				messageBody(in, request);
		} catch (IOException e) {
			// TODO:
		}

		if (HttpServer.DEBUG_MODE)
			System.out.println("Debug: " + in.getCopy());

		return request;
	}

	private static void messageBody(Unreadable in, HttpRequest request) {
		// TODO Auto-generated method stub

	}

	/**
	 * 
	 * @param in
	 * @param request
	 * @throws IOException
	 */
	private static void messageHeader(Unreadable in, HttpRequest request)
			throws IOException {
		Map<String, String> map = new HashMap<String, String>();
		StringBuffer sbuf = new StringBuffer();

		int c;
		while ((c = in.read()) != -1) {
			// CRLF - end of header
			if (c == '\r') {
				in.read(); // leave '\n'
				break;
			}
			in.unread(c);

			// key
			while ((c = in.read()) != -1) {
				if (c == ':')
					break;
				sbuf.append((char) c);
			}
			String key = sbuf.toString(); // not include ':'
			sbuf = new StringBuffer();

			// SP
			while ((c = in.read()) != -1)
				if (c != ' ')
					break;
			in.unread(c);

			// value
			while ((c = in.read()) != -1) {
				if (c == '\r') {
					in.read(); // read '\n'
					break;
				}
				sbuf.append((char) c);
			}
			String value = sbuf.toString();
			sbuf = new StringBuffer();

			map.put(key, value);
		}
		request.setHeader(map);
	}

	/**
	 * Request-Line = Method SP Request-URI SP HTTP-Version CRLF
	 * 
	 * @param in
	 * @param request
	 * @throws IOException
	 */
	private static void requestLine(Unreadable in, HttpRequest request)
			throws IOException {
		int c;
		StringBuffer sbuf;

		// Method
		sbuf = new StringBuffer();
		while ((c = in.read()) != -1) {
			if (c == ' ')
				break;
			sbuf.append((char) c);
		}
		request.setMethod(sbuf.toString());

		// Request-URI
		sbuf = new StringBuffer();
		while ((c = in.read()) != -1) {
			if (c == ' ')
				break;
			sbuf.append((char) c);
		}
		request.setRequestURI(sbuf.toString());

		// HTTP-Versoin
		sbuf = new StringBuffer();
		while ((c = in.read()) != -1) {
			if (c == '\r')
				break;
			sbuf.append((char) c);
		}
		request.setHttpVersion(sbuf.toString());
		in.read(); // leave '\n'
	}
}

class Unreadable extends Reader {
	Reader reader;
	StringBuffer sbuf = new StringBuffer();
	int buf;
	boolean loaded = false;

	Unreadable(Reader reader) {
		this.reader = reader;
	}

	public int read() throws IOException {
		if (loaded) {
			loaded = false;
			return buf;
		}
		int c = reader.read();
		sbuf.append((char) c);
		return c;
	}

	void unread(int c) {
		loaded = true;
		buf = c;
	}

	@Override
	public int read(char[] cbuf, int off, int len) throws IOException {
		return reader.read(cbuf, off, len);
	}

	@Override
	public void close() throws IOException {
		reader.close();
	}

	public String getCopy() {
		return sbuf.toString();
	}
}
