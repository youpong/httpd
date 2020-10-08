package farm;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.HashMap;
import java.util.Map;

public abstract class HttpMessageParser {
	protected PushbackInputStream is;
	protected boolean debug;

	protected HttpMessageParser(InputStream is, boolean debug) {
		this.is = new PushbackInputStream(is);
		this.debug = debug;
	}
	
	/**
	 * @param request
	 * @throws IOException
	 * @throws UnexpectedCharException
	 */
	protected void messageHeader(HttpMessage msg)
			throws IOException, UnexpectedCharException {
		Map<String, String> map = new HashMap<String, String>();

		int c;
		while ((c = is.read()) != -1) {
			// CRLF - end of header
			if (c == '\r') {
				consum('\n');
				break;
			}
			is.unread(c);

			// key
			StringBuffer key = new StringBuffer();
			while ((c = is.read()) != -1) {
				if (c == ':')
					break;
				key.append((char) c);
			}

			// SP
			consum(' ');

			// value
			StringBuffer value = new StringBuffer();
			while ((c = is.read()) != -1) {
				if (c == '\r') {
					consum('\n');
					break;
				}
				value.append((char) c);
			}

			map.put(key.toString(), value.toString());
		}
		msg.setAllHeaders(map);
	}

	protected void consum(int expected) throws IOException, UnexpectedCharException {
		int c = is.read();
		if (c != expected)
			throw new UnexpectedCharException(
					"expected (" + expected + ") actually (" + c + ")");
	}
}
