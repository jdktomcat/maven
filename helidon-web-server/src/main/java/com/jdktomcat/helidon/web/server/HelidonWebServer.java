package com.jdktomcat.helidon.web.server;

import io.helidon.webserver.Routing;
import io.helidon.webserver.WebServer;

import java.util.concurrent.TimeUnit;

/**
 * web服务器
 *
 * @author: 汤旗
 * @date: 2020-05-20 14:31
 **/
public class HelidonWebServer {

    public static void main(String[] args) throws Exception {
        WebServer webServer = WebServer.create(Routing.builder()
                        .any((req, res) -> res.send("It works!"))
                        .build()).start().toCompletableFuture().get(10, TimeUnit.SECONDS);
        System.out.println("Server started at: http://localhost:" + webServer.port());
    }
}
