package farm;

//import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.Reader;
import java.io.StringReader;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParserTest {

	@Test
	public void requestLine() {
		Reader reader = new StringReader("GET / HTTP/1.1\r\n\r\n");
		HttpRequest request = Parser.parseHttpRequest(reader, false);

		assertEquals("GET", request.getMethod());
		assertEquals("/", request.getRequestURI());
		assertEquals("HTTP/1.1", request.getHttpVersion());
	}

	@Test
	public void requestLine2() {
		Reader reader = new StringReader("GET / HTTP/1.1\r\n"
				+ "Connection: keep-alive\r\n\r\n");
		HttpRequest request = Parser.parseHttpRequest(reader, false);

		assertEquals("GET", request.getMethod());
		assertEquals("/", request.getRequestURI());
		assertEquals("HTTP/1.1", request.getHttpVersion());
	}

	@Test
	public void getHeader() {
		Reader reader = new StringReader("GET / HTTP/1.1\r\n"
				+ "Referer: http://192.168.1.9/\r\n\r\n");
		HttpRequest request = Parser.parseHttpRequest(reader, false);

		assertEquals("GET", request.getMethod());
		assertEquals("/", request.getRequestURI());
		assertEquals("HTTP/1.1", request.getHttpVersion());
		assertEquals("http://192.168.1.9/", request.getHeader("Referer"));
	}
}
