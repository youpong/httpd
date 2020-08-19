package farm;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.Reader;
import java.io.StringReader;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParserTest {
	
	@Test
	public void requestLine() {
		Reader reader = new StringReader("GET / HTTP/1.1\r\n");
		Parser parser = new Parser();
		HttpRequest request = parser.parseHttpRequest(reader);
		
		assertEquals("GET", request.getMethod());
		assertEquals("/", request.getRequestURI());
		assertEquals("HTTP/1.1", request.getHttpVersion());
	}
	
	@Test
	public void bar() {
		assertTrue(false);
	}
}
