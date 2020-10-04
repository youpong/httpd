package farm;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpRequestParserTest {

	@Test
	public void requestLine() throws Exception {
		InputStream is = new ByteArrayInputStream(
				"GET / HTTP/1.1\r\n\r\n".getBytes());
		HttpRequest request = HttpRequestParser.parse(is, false);

		assertEquals("GET", request.getMethod());
		assertEquals("/", request.getRequestURI());
		assertEquals("HTTP/1.1", request.getHttpVersion());
	}

	@Test
	public void requestLine2() throws Exception {
		String requestString =
		// @formatter:off
				"GET / HTTP/1.1\r\n" + 
				"Connection: keep-alive\r\n\r\n";
		// @formatter:on
		HttpRequest request = HttpRequestParser
				.parse(new ByteArrayInputStream(requestString.getBytes()), false);

		assertEquals("GET", request.getMethod());
		assertEquals("/", request.getRequestURI());
		assertEquals("HTTP/1.1", request.getHttpVersion());
	}

	@Test
	public void getHeader() throws Exception {
		String requestString =
		// @formatter:off
				"GET / HTTP/1.1\r\n" + 
				"Referer: http://192.168.1.9/\r\n\r\n";
		// @formatter:on
		HttpRequest request = HttpRequestParser
				.parse(new ByteArrayInputStream(requestString.getBytes()), false);

		assertEquals("GET", request.getMethod());
		assertEquals("/", request.getRequestURI());
		assertEquals("HTTP/1.1", request.getHttpVersion());
		assertEquals("http://192.168.1.9/", request.getHeader("Referer"));
	}
}
