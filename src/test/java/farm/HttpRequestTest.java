package farm;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class HttpRequestTest {

	@Test
	public void generateAndParse() throws Exception {
		HttpRequest req = new HttpRequest();
		req.setMethod("GET");
		req.setRequestURI("/");
		req.setHttpVersion("HTTP/1.1");

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		req.generate(os);
		req = null;
		HttpRequest req2 = HttpRequestParser
				.parse(new ByteArrayInputStream(os.toByteArray()), false);

		assertEquals("GET", req2.getMethod());
		assertEquals("/", req2.getRequestURI());
		assertEquals("HTTP/1.1", req2.getHttpVersion());
	}

	@Test
	public void generateAndParse2() throws Exception {
		HttpRequest req = new HttpRequest();
		req.setMethod("POST");
		req.setRequestURI("/index.html");
		req.setHttpVersion("HTTP/1.0");
		req.setHeader("Host", "localhost");

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		req.generate(os);
		req = null;
		HttpRequest req2 = HttpRequestParser
				.parse(new ByteArrayInputStream(os.toByteArray()), false);

		assertEquals("POST", req2.getMethod());
		assertEquals("/index.html", req2.getRequestURI());
		assertEquals("HTTP/1.0", req2.getHttpVersion());
		assertEquals("localhost", req2.getHeader("Host"));
	}

}
