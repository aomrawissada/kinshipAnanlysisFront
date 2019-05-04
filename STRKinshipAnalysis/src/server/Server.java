package server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import tools.AlleleFreqCal;

public class Server {
	
	private static Server instance;
	
	private Server() {};
	
	public static Server getInstance() {
		if (instance == null) {
            instance = new Server();
           }
       return instance;
	}
	
	public void startService() throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/test", new RequestHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
        System.out.println("----- Start Service -----");
    }

    static class RequestHandler implements HttpHandler {
    	
//    	private final Object someObject;
//
//        public RequestHandler(Object someObject) {
//            super();
//            this.someObject = someObject;
//        }
    	
        public void handle(HttpExchange t) throws IOException {
            String response = "This is the response";
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

}
