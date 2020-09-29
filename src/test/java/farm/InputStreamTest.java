package farm;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;

import org.junit.jupiter.api.Test;

public class InputStreamTest {
	private String byteArrayToString(String str) {
		StringBuffer buf = new StringBuffer();
		ByteArrayInputStream i = new ByteArrayInputStream(str.getBytes());

		int b;
		while ((b = i.read()) != -1) {
			buf.append((char) b);
		}

		return buf.toString();
	}

	@Test
	public void foo() {
		assertEquals("abc", byteArrayToString("abc"));
		assertEquals("", byteArrayToString(""));

		// this test may fail.
		//assertEquals("日本語", byteArrayToString("日本語"));
	}
}
