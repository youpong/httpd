package farm;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

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
