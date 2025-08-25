package edu.escuelaing.tdse.framework;

import static edu.escuelaing.tdse.framework.HttpServer.get;
import static edu.escuelaing.tdse.framework.HttpServer.post;
import static edu.escuelaing.tdse.framework.HttpServer.staticFiles;
import java.io.IOException;
import java.net.URISyntaxException;

public class FrameworkApplication {

    public static void main(String[] args) throws IOException, URISyntaxException {
        staticFiles("resources/static");
        get("/app/hello", (req, resp) -> "Hello, world");
        get("/app/helloget", (req, resp) -> "Get received: " + req.getQueryParam("name"));
        get("/app/pi", (req, resp) -> String.valueOf(Math.PI));
        post("/app/hellopost", (req, resp) -> "Post received: " + req.getQueryParam("name"));
        HttpServer.main(args);
    }

}