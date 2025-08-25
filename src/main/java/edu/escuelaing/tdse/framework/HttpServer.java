package edu.escuelaing.tdse.framework;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.function.BiFunction;
import java.util.logging.Logger;

public class HttpServer {

    // Loger from Lombok
    private static final Logger logger = Logger.getLogger(HttpServer.class.getName());

    // Server variables
    private static final int PORT = 35000;
    private ServerSocket serverSocket;
    private boolean running = true;

    // REST services
    private static String RESOURCE_PATH = "src/main/java/edu/escuelaing/tdse/framework/";
    public static HashMap<String, BiFunction<HttpRequest, HttpResponse, String>> servicesGet = new HashMap<>();
    public static HashMap<String, BiFunction<HttpRequest, HttpResponse, String>> servicesPost = new HashMap<>();

    public static void main(String[] args) throws IOException, URISyntaxException {
        HttpServer server = new HttpServer();
        server.startServer();
    }

    public void startServer() throws URISyntaxException {
        try {
            this.serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            logger.severe("Failed to start server on port: " + PORT);
            System.exit(1);
        }

        while (running) {
            try {
                logger.info("Server started on port: " + PORT);
                Socket clientSocket = serverSocket.accept();
                RequestHandler requestHandler = new RequestHandler(clientSocket, RESOURCE_PATH);
                requestHandler.handlerRequest();
            } catch (IOException e) {
                if (!running) {
                    logger.info("Server stopped.");
                    break;
                }
                logger.severe("Error accepting connection: " + e.getMessage());
            }
        }
    }

    public static void get(String path, BiFunction<HttpRequest, HttpResponse, String> restService) {
        servicesGet.put(path, restService);
    }

    public static void staticFiles(String path) {
        RESOURCE_PATH = RESOURCE_PATH + path;
    }

    public static void post(String path, BiFunction<HttpRequest, HttpResponse, String> restService) {
        servicesPost.put(path, restService);
    }

    public HashMap<String, BiFunction<HttpRequest, HttpResponse, String>> getServicesGet() {
        return servicesGet;
    }

    public HashMap<String, BiFunction<HttpRequest, HttpResponse, String>> getServicesPost() {
        return servicesPost;
    }

    public void stopServer() {
        this.running = false;
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
                logger.info("Server stopped successfully.");
            } catch (IOException e) {
                logger.severe("Error closing server: " + e.getMessage());
            }
        }
    }

}