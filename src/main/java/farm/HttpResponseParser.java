package farm;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class HttpResponseParser extends HttpMessageParser {

	private HttpResponseParser(InputStream is, boolean debug) {
		super(is, debug);
	}

	public static HttpResponse parse(InputStream is, boolean debug)
			throws UnexpectedCharException, IOException {
		return new HttpResponseParser(is, debug).parse();
	}

	/**
	 * generic-message = start-line *(message-header CRLF) CRLF [ message-body ]
	 * start-line = Request-Line | Status-Line
	 * 
	 * @param in
	 * @return
	 * @throws UnexpectedCharException
	 * @throws IOException
	 */
	private HttpResponse parse() throws UnexpectedCharException, IOException {
		HttpResponse response = new HttpResponse();

		statusLine(response);
		messageHeader(response);
		//if (response.hasMessageBody())
		messageBody(response);

		return response;
	}

	/**
	 * Status-Line = HTTP-Version SP Status-Code SP Reason-Phrase CRLF
	 * 
	 * @throws IOException
	 * @throws UnexpectedCharException
	 */
	private void statusLine(HttpResponse response)
			throws IOException, UnexpectedCharException {
		int c;
		StringBuffer sbuf;

		// HTTP-Versoin
		sbuf = new StringBuffer();
		while ((c = is.read()) != -1) {
			if (c == ' ')
				break;
			sbuf.append((char) c);
		}
		response.setHttpVersion(sbuf.toString());

		// Status-Code
		sbuf = new StringBuffer();
		while ((c = is.read()) != -1) {
			if (c == ' ')
				break;
			sbuf.append((char) c);
		}
		response.setStatusCode(sbuf.toString());

		// Reason-Phrase
		sbuf = new StringBuffer();
		while ((c = is.read()) != -1) {
			if (c == '\r') {
				consum('\n');
				break;
			}
			sbuf.append((char) c);
		}
		response.setResonPhrase(sbuf.toString());
	}

	/**
	 * @throws IOException
	 * @throws UnexpectedCharException
	 */
	private void messageHeader(HttpResponse response)
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

		response.setAllHeaders(map);
	}

	private void messageBody(HttpResponse response) throws IOException {
		int c;
		int maxLen = Integer.parseInt(response.getHeader("Content-Length"));
		ByteArrayOutputStream os = new ByteArrayOutputStream();

		for (int len = 0; len < maxLen; len++) {
			if ((c = is.read()) == -1)
				break;
			os.write(c);
		}
		response.setBody(os.toByteArray());
	}
}
