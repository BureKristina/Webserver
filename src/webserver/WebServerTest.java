package webserver;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;

public class WebServerTest {

    private static WebServer ws;
    private static final String PATH = "D:\\IntelliJ\\Projects\\VVS Webserver\\HTML Files\\";
    private static final String startHTML = "<!DOCTYPE html>\r\n";
    private static final String styleHTML = "<style>\r\n" +
            "  body { text-align: center; padding: 150px; }\r\n" +
            "  h1 { font-size: 50px; }\r\n" +
            "  body { font: 20px Helvetica, sans-serif; color: #333; }\r\n" +
            "  article { display: block; text-align: left; width: 650px; margin: 0 auto; }\r\n" +
            "  a { color: #dc8100; text-decoration: none; }\r\n" +
            "  a:hover { color: #333; text-decoration: none; }\r\n" +
            "</style>\r\n";
    private static final String startBody = "<body>\r\n" + "\r\n";
    private static final String endBody = "\r\n" + "</body>\r\n" + "</html>";

    @BeforeClass
    public static void setUp() {
        try {
            ServerSocket serverSocket = new ServerSocket(10008);
            ws = new WebServer(serverSocket.accept());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void createInstanceOfWebserver() {
        Assert.assertNotNull(ws);
    }

    @Test
    public void checkReadFromFileForIndexHtml() {
        String result = ws.readFromFile(PATH + "index.html");
        String expected = startHTML + "<title>Homepage</title>\r\n" + styleHTML +
                startBody + "<h1>Welcome!</h1>\r\n" +
                "<p>Try out this <a href=\"link.html\">link</a>!</p>\r\n" +
                "<p>Or go to <a href=\"https://www.google.com/\">Google</a></p>\r\n" + endBody;
        Assert.assertEquals(result, expected);
    }

    @Test
    public void checkReadFromFileFor404Html() {
        String result = ws.readFromFile(PATH + "404.html");
        String expected = startHTML + "<title>Error 404</title>\r\n" + startBody +
                "<h1>404 File Not Found :(</h1>\r\n" + endBody;
        Assert.assertEquals(result, expected);
    }

    @Test
    public void checkReadFromFileForLinkHtml() {
        String result = ws.readFromFile(PATH + "link.html");
        String expected = startHTML + "<title>Homepage</title>\r\n" + styleHTML +
                startBody + "<h1>Yaaaaaayy!</h1>\r\n" + "<p>This link works</p>\r\n" +
                "<p>Go <a href=\"index.html\">back</a> to homepage</p>\r\n" + endBody;
        Assert.assertEquals(result, expected);
    }

    @Test
    public void checkReadFromFileForMaintenanceHtml() {
        String result = ws.readFromFile(PATH + "maintenance.html");
        String expected = startHTML + "<title>Site Maintenance</title>\r\n" + styleHTML + startBody +
                "    <h1>We&rsquo;ll be back soon!</h1>\r\n" + "    <div>\r\n" +
                "        <p>Sorry for the inconvenience but we&rsquo;re performing some maintenance at the moment. We&rsquo;ll be back online shortly!</p>\r\n" +
                "    </div>\r\n" + endBody;
        Assert.assertEquals(result, expected);
    }

    @Test
    public void checkReadFromFileForStoppedHtml() {
        String result = ws.readFromFile(PATH + "stopped.html");
        String expected = startHTML + "<title>Server Stopped</title>\r\n" + startBody +
                "<h1>Connection Timeout</h1>\r\n" + endBody;
        Assert.assertEquals(result, expected);
    }

    @Test
    public void checkIfStoppedOk() {
        try {
            int result = ws.stopped(new PrintWriter(ws.clientSocket.getOutputStream(),
                    true), "stopped.html");
            Assert.assertEquals(result, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void checkIfStoppedNotOk() {
        try {
            int result = ws.stopped(new PrintWriter(ws.clientSocket.getOutputStream(),
                    true), "aaaa");
            Assert.assertEquals(result, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void checkIfMeintenanceOk() {
        try {
            int result = ws.stopped(new PrintWriter(ws.clientSocket.getOutputStream(),
                    true), "meintenance.html");
            Assert.assertEquals(result, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void checkIfMeintenanceNotOk() {
        try {
            int result = ws.stopped(new PrintWriter(ws.clientSocket.getOutputStream(),
                    true), "aaaa");
            Assert.assertEquals(result, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void checkIfRunningEntersIfOnce() {
        try {
            int result = ws.running(new PrintWriter(ws.clientSocket.getOutputStream(),
                    true), "index.html");
            Assert.assertEquals(result, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void checkIfRunningEntersIfTwice() {
        try {
            int result = ws.running(new PrintWriter(ws.clientSocket.getOutputStream(),
                    true), "/");
            Assert.assertEquals(result, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void checkIfRunningEntersElseIf() {
        try {
            int result = ws.running(new PrintWriter(ws.clientSocket.getOutputStream(),
                    true), "link.html");
            Assert.assertEquals(result, 2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void checkIfRunningEntersElse() {
        try {
            int result = ws.running(new PrintWriter(ws.clientSocket.getOutputStream(),
                    true), "aaa");
            Assert.assertEquals(result, 2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}