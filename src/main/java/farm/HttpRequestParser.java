package farm;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestParser extends HttpMessageParser {

	private HttpRequestParser(InputStream is, boolean debug) {
		super(is, debug);
	}

	public static HttpRequest parse(InputStream is, boolean debug)
			throws UnexpectedCharException, IOException {
		return new HttpRequestParser(is, debug).parse();
	}

	/**
	 * generic-message = start-line *(message-header CRLF) CRLF [ message-body ]
	 * start-line = Request-Line | Status-Line
	 * 
	 * @param in
	 * @return
	 * @throws UnexpectedCharException
	 */
	private HttpRequest parse() throws IOException, UnexpectedCharException {
		HttpRequest request = new HttpRequest();

		requestLine(request);
		messageHeader(request);
		if (request.hasMessageBody())
			messageBody(request);

		return request;
	}

	/**
	 * Request-Line = Method SP Request-URI SP HTTP-Version CRLF
	 * 
	 * @param request
	 * @throws IOException
	 * @throws UnexpectedCharException
	 */
	private void requestLine(HttpRequest request)
			throws IOException, UnexpectedCharException {
		int c;
		StringBuffer sbuf;

		// Method
		sbuf = new StringBuffer();
		while ((c = is.read()) != -1) {
			if (c == ' ')
				break;
			sbuf.append((char) c);
		}
		request.setMethod(sbuf.toString());

		// Request-URI
		sbuf = new StringBuffer();
		while ((c = is.read()) != -1) {
			if (c == ' ')
				break;
			sbuf.append((char) c);
		}
		request.setRequestURI(sbuf.toString());

		// HTTP-Versoin
		sbuf = new StringBuffer();
		while ((c = is.read()) != -1) {
			if (c == '\r') {
				consum('\n');
				break;
			}
			sbuf.append((char) c);
		}
		request.setHttpVersion(sbuf.toString());
	}

	/**
	 * @param request
	 * @throws IOException
	 * @throws UnexpectedCharException
	 */
	private void messageHeader(HttpRequest request)
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

		request.setAllHeaders(map);
	}

	private void messageBody(HttpRequest request) {
		// TODO Auto-generated method stub
	}
}
