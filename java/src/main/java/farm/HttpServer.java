package farm;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HttpServer {
    private Service service;
    private ServerSocket svSock;

    public static void main(String args[]) {
        String service = Http.HTTP_SERVICE;

        if (args.length >= 2) {
            printUsage();
            System.exit(Http.EXIT_FAILURE);
        }

        if (args.length == 1) {
            service = args[0];
        }

        try {
            HttpServer server = new HttpServer(service);
            server.run();
        } catch (UnknownServiceException e) {
            printUsage();
            System.exit(Http.EXIT_FAILURE);
        }
    }

    public HttpServer(String service) throws UnknownServiceException {
        this.service = new Service(service);
    }

    public HttpServer(Service service) {
        this.service = service;
    }

    private static void printUsage() {
        System.err.println("Usage: Daytime [service]");
    }

    public void run() {
        try {
            svSock = new ServerSocket(service.getPort());
            printHostPort();
            while (true) {
                Socket sock = svSock.accept();
                tcpPeerAddrPrint(sock);
                reply(sock);
                sock.close();
            }
        } catch (Exception e) {
            System.err.println(e);
            System.exit(Http.EXIT_FAILURE);
        }
    }

    //Wed Jun  6 03:03:08 2012
    private void reply(Socket sock) throws IOException {
        PrintWriter os = new PrintWriter(sock.getOutputStream());
        //	os.println("HTTP/1.1 200
        os.println("Content-type: text/html");
        os.println("");
        os.println("<HTML><BODY>Hello, World</BODY></HTML>");
        os.flush();
    }

    private void printHostPort() {
        System.out.println(
                svSock.getInetAddress().getHostName() + ":" + svSock.getLocalPort());
    }

    /*
     * 
     * int sock, struct sockaddr *addr, socklen_t len);
     */
    private void tcpPeerAddrPrint(Socket sock) {
        System.out.println("." + sock.getInetAddress() + ":" + sock.getPort());
    }
}
