package farm;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class MimeTest {
	@Test
	public void foo() {
		assertEquals("text/html", Mime.getMime("index.html"));
		assertEquals("text/plain", Mime.getMime("endwithdot."));
		assertEquals("text/plain", Mime.getMime(".dotfile"));
		assertEquals("text/html", Mime.getMime(".html"));
	}
}
