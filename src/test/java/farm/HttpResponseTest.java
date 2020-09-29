package farm;

//import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class HttpResponseTest {
	@Test
	public void generateAndParse() throws IOException, UnexpectedCharException {
		HttpResponse res = new HttpResponse();
		res.setStatusCode("200");

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		res.generate(os);
		res = null;
		HttpResponse res2 = HttpResponseParser
				.parse(new ByteArrayInputStream(os.toByteArray()), false);

		assertEquals("HTTP/1.1", res2.getHttpVersion());
		assertEquals("200", res2.getStatusCode());
		assertEquals("OK", res2.getReasonPhrase());
	}

	@Test
	public void generateAndParse2() throws IOException, UnexpectedCharException {
		HttpResponse res = new HttpResponse();
		res.setStatusCode("404");
		res.setHeader("Content-Type", "text/html");

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		res.generate(os);
		res = null;
		HttpResponse res2 = HttpResponseParser
				.parse(new ByteArrayInputStream(os.toByteArray()), false);

		assertEquals("HTTP/1.1", res2.getHttpVersion());
		assertEquals("404", res2.getStatusCode());
		assertEquals("Not Found", res2.getReasonPhrase());

		assertEquals("text/html", res2.getHeader("Content-Type"));
	}

	@Test
	public void testGen() throws IOException {
		HttpResponse response = new HttpResponse();
		response.setStatusCode("404");
		response.setHeader("Server", "bait/0.1");
		response.setHeader("Content-Type", "text/html");

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		response.generate(os);

		//@formatter:off
		assertEquals("HTTP/1.1 404 Not Found\r\n" 
				+ "Server: bait/0.1\r\n"
				+ "Content-Type: text/html\r\n" 
				+ "Content-Length: 0\r\n" + "\r\n",
				os.toString());
		//@formatter:on
	}
}
