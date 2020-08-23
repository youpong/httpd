package farm;

public class HttpRequest {
    private String method;
    private String requestURI;
    private String httpVersion;

    public String genRequestLine() {
        return method + " " + requestURI + " " + httpVersion;
    }

    public String getMethod() {
        return method;
    }

    public String getRequestURI() {
        return requestURI;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setRequestURI(String requestURI) {
        this.requestURI = requestURI;
    }

    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }

    public boolean hasMessageBody() {
        // TODO
        return false;
    }

}
