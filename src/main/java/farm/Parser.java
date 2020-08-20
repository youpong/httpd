package farm;

import java.io.IOException;
import java.io.Reader;

public class Parser {

    /**
     * generic-message = start-line
     * *(message-header CRLF)
     * CRLF
     * [ message-body ]
     * start-line = Request-Line | Status-Line
     * 
     * @param in
     * @return
     */
    public static HttpRequest parseHttpRequest(Reader in) {
        HttpRequest request = new HttpRequest();
        // TODO:
        try {
            requestLine(in, request); // = start-line
            messageHeader(in, request);
            if (request.hasMessageBody())
                messageBody(in, request);
        } catch (IOException e) {
            // TODO:
        }
        return request;
    }

    private static void messageBody(Reader in, HttpRequest request) {
        // TODO Auto-generated method stub

    }

    /**
     * 
     * @param in
     * @param request
     * @throws IOException
     */
    private static void messageHeader(Reader in, HttpRequest request) throws IOException {
        int c;
        StringBuffer sbuf = new StringBuffer();

        while ((c = in.read()) != -1) {
            if (c != '\r') {
                in.read(); // leave '\n'
                return;
            }

            sbuf.append((char) c);
            while ((c = in.read()) != -1) {
                if (c == '\r') {
                    in.read(); // leave '\n'	
                    break;
                }
                sbuf.append((char) c);
            }
            //
        }
    }

    /**
     * Request-Line = Method SP Request-URI SP HTTP-Version CRLF
     * 
     * @param in
     * @param request
     * @throws IOException
     */
    private static void requestLine(Reader in, HttpRequest request) throws IOException {
        int c;
        StringBuffer sbuf;

        // Method
        sbuf = new StringBuffer();
        while ((c = in.read()) != -1) {
            if (c == ' ')
                break;
            sbuf.append((char) c);
        }
        request.setMethod(sbuf.toString());

        // Request-URI
        sbuf = new StringBuffer();
        while ((c = in.read()) != -1) {
            if (c == ' ')
                break;
            sbuf.append((char) c);
        }
        request.setRequestURI(sbuf.toString());

        // HTTP-Versoin
        sbuf = new StringBuffer();
        while ((c = in.read()) != -1) {
            if (c == '\r')
                break;
            sbuf.append((char) c);
        }
        request.setHttpVersion(sbuf.toString());
        in.read(); // leave '\n'
    }
}
