package webserver;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;

import static org.junit.jupiter.api.Assertions.*;

public class WebServerTest {

    private static WebServer ws;

    @BeforeClass
    public static void setUp() {
        try {
            ServerSocket serverSocket = new ServerSocket(10008);
            ws = new WebServer(serverSocket.accept());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void tearDown() {
        ws = null;
    }

    @Test
    public void createInstanceOfWebserver() {
        Assert.assertNotNull(ws);
    }

    @Test
    public void checkReadFromFileForIndexHtml() {
        String result = ws.ReadFromFile("C:\\Users\\tinab\\Desktop\\VVS HTML Files\\index.html");
        String expected = "<!DOCTYPE html>\r\n" +
                "<title>Homepage</title>\r\n" +
                "<style>\r\n" +
                "  body { text-align: center; padding: 150px; }\r\n" +
                "  h1 { font-size: 50px; }\r\n" +
                "  body { font: 20px Helvetica, sans-serif; color: #333; }\r\n" +
                "  article { display: block; text-align: left; width: 650px; margin: 0 auto; }\r\n" +
                "  a { color: #dc8100; text-decoration: none; }\r\n" +
                "  a:hover { color: #333; text-decoration: none; }\r\n" +
                "</style>\r\n" +
                "<body>\r\n" +
                "\r\n" +
                "<h1>Welcome!</h1>\r\n" +
                "<p>Try out this <a href=\"link.html\">link</a>!</p>\r\n" +
                "<p>Or go to <a href=\"https://www.google.com/\">Google</a></p>\r\n" +
                "\r\n" +
                "</body>\r\n" +
                "</html>";
        Assert.assertEquals(result, expected);
    }

    @Test
    public void checkReadFromFileFor404Html() {
        String result = ws.ReadFromFile("C:\\Users\\tinab\\Desktop\\VVS HTML Files\\404.html");
        String expected = "<!DOCTYPE html>\r\n" +
                "<title>Error 404</title>\r\n" +
                "<body>\r\n" +
                "\r\n" +
                "<h1>404 File Not Found :(</h1>\r\n" +
                "\r\n" +
                "</body>\r\n" +
                "</html>";
        Assert.assertEquals(result, expected);
    }

    @Test
    public void checkReadFromFileForLinkHtml() {
        String result = ws.ReadFromFile("C:\\Users\\tinab\\Desktop\\VVS HTML Files\\link.html");
        String expected = "<!DOCTYPE html>\r\n" +
                "<title>Homepage</title>\r\n" +
                "<style>\r\n" +
                "  body { text-align: center; padding: 150px; }\r\n" +
                "  h1 { font-size: 50px; }\r\n" +
                "  body { font: 20px Helvetica, sans-serif; color: #333; }\r\n" +
                "  article { display: block; text-align: left; width: 650px; margin: 0 auto; }\r\n" +
                "  a { color: #dc8100; text-decoration: none; }\r\n" +
                "  a:hover { color: #333; text-decoration: none; }\r\n" +
                "</style>\r\n" +
                "<body>\r\n" +
                "\r\n" +
                "<h1>Yaaaaaayy!</h1>\r\n" +
                "<p>This link works</p>\r\n" +
                "<p>Go <a href=\"index.html\">back</a> to homepage</p>\r\n" +
                "\r\n" +
                "</body>\r\n" +
                "</html>";
        Assert.assertEquals(result, expected);
    }

    @Test
    public void checkReadFromFileForMaintenanceHtml() {
        String result = ws.ReadFromFile("C:\\Users\\tinab\\Desktop\\VVS HTML Files\\maintenance.html");
        String expected = "<!doctype html>\r\n" +
                "<title>Site Maintenance</title>\r\n" +
                "<style>\r\n" +
                "  body { text-align: center; padding: 150px; }\r\n" +
                "  h1 { font-size: 50px; }\r\n" +
                "  body { font: 20px Helvetica, sans-serif; color: #333; }\r\n" +
                "  article { display: block; text-align: left; width: 650px; margin: 0 auto; }\r\n" +
                "  a { color: #dc8100; text-decoration: none; }\r\n" +
                "  a:hover { color: #333; text-decoration: none; }\r\n" +
                "</style>\r\n" +
                "\r\n" +
                "<body>\r\n" +
                "    <h1>We&rsquo;ll be back soon!</h1>\r\n" +
                "    <div>\r\n" +
                "        <p>Sorry for the inconvenience but we&rsquo;re performing some maintenance at the moment. We&rsquo;ll be back online shortly!</p>\r\n" +
                "    </div>\r\n" +
                "</body>\r\n" +
                "</html>";
        Assert.assertEquals(result, expected);
    }

    @Test
    public void checkReadFromFileForStoppedHtml() {
        String result = ws.ReadFromFile("C:\\Users\\tinab\\Desktop\\VVS HTML Files\\stopped.html");
        String expected = "<!DOCTYPE html>\r\n" +
                "<title>Server Stopped</title>\r\n" +
                "<body>\r\n" +
                "\r\n" +
                "<h1>Connection Timeout</h1>\r\n" +
                "\r\n" +
                "</body>\r\n" +
                "</html>";
        Assert.assertEquals(result, expected);
    }

    @Test
    public void checkIfStoppedOk() {
        try {
            int result = ws.Stopped(new PrintWriter(ws.clientSocket.getOutputStream(),
                    true), "stopped.html");
            Assert.assertEquals(result, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void checkIfStoppedNotOk() {
        try {
            int result = ws.Stopped(new PrintWriter(ws.clientSocket.getOutputStream(),
                    true), "aaaa");
            Assert.assertEquals(result, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void checkIfMeintenanceOk() {
        try {
            int result = ws.Stopped(new PrintWriter(ws.clientSocket.getOutputStream(),
                    true), "meintenance.html");
            Assert.assertEquals(result, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void checkIfMeintenanceNotOk() {
        try {
            int result = ws.Stopped(new PrintWriter(ws.clientSocket.getOutputStream(),
                    true), "aaaa");
            Assert.assertEquals(result, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void checkIfRunningEntersIfOnce() {
        try {
            int result = ws.Running(new PrintWriter(ws.clientSocket.getOutputStream(),
                    true), "index.html");
            Assert.assertEquals(result, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void checkIfRunningEntersIfTwice() {
        try {
            int result = ws.Running(new PrintWriter(ws.clientSocket.getOutputStream(),
                    true), "/");
            Assert.assertEquals(result, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void checkIfRunningEntersElseIf() {
        try {
            int result = ws.Running(new PrintWriter(ws.clientSocket.getOutputStream(),
                    true), "link.html");
            Assert.assertEquals(result, 2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void checkIfRunningEntersElse() {
        try {
            int result = ws.Running(new PrintWriter(ws.clientSocket.getOutputStream(),
                    true), "aaa");
            Assert.assertEquals(result, 2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}