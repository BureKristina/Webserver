package webserver;

import java.net.*;
import java.io.*;
import java.util.StringTokenizer;

class WebServer extends Thread {

    //all html files are in one folder with the path in PATH_TO_HTML_FILES
    static final String PATH_TO_HTML_FILES = "D:\\IntelliJ\\Projects\\VVS Webserver\\HTML Files\\";
    static final String INDEX = "index.html";
    static final String LINK = "link.html";
    static final String MAINTENANCE = "maintenance.html";
    static final String FILE_NOT_FOUND = "404.html";
    static final String CONNECTION_TIMEOUT = "stopped.html";

    protected Socket clientSocket;

    public static void main(String[] args) {
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(10008);
            System.out.println("Connection Socket Created");
            try {
                while (true) {
                    System.out.println("Waiting for Connection");
                    new WebServer(serverSocket.accept());
                }
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port: 10008.");
            System.exit(1);
        } finally {
            try {

                serverSocket.close();
            } catch (IOException e) {
                System.err.println("Could not close port: 10008.");
                System.exit(1);
            }
        }
    }

    public WebServer(Socket clientSoc) {
        clientSocket = clientSoc;
        start();
    }

    public void run() {

        System.out.println("New Communication Thread Started");
        //0 = stopped, 1 = running, 2 = maintenance
        int serverState = 1;
        String fileRequested;

        //int checkStates;
        try {
            PrintWriter outputToBrowser = new PrintWriter(clientSocket.getOutputStream(),
                    true);
            BufferedReader inputFromBrowser = new BufferedReader(new InputStreamReader(
                    clientSocket.getInputStream()));

            String inputLine;

            inputLine = inputFromBrowser.readLine();
            StringTokenizer parse = new StringTokenizer(inputLine);
            fileRequested = parse.nextToken(); //gets rid of first token
            fileRequested = parse.nextToken().toLowerCase(); //secound token is the requested file from browser

            switch (serverState){
                case 0: //server stopped
                    stopped(outputToBrowser, CONNECTION_TIMEOUT);
                    break;

                case 1: //server running
                    running(outputToBrowser, fileRequested);
                    break;

                case 2: //server maintenance
                    maintenance(outputToBrowser, MAINTENANCE);
                    break;
            }

            outputToBrowser.close();
            inputFromBrowser.close();
            clientSocket.close();

        } catch (IOException e) {
            System.err.println("Problem with Communication Server");
            System.exit(1);
        }
    }

    protected int stopped(PrintWriter out, String file) {

        int stopOk = 0; //for testing
        if(file.equals("stopped.html")) {
            String fileName = PATH_TO_HTML_FILES + file;
            String fileContent = readFromFile(fileName);

            printToBrowser(out, "HTTP/1.1 522 Connection timed out");

            out.println(fileContent);
            out.flush();
            stopOk = 1;
        }
        return stopOk;
    }

    protected int running(PrintWriter out, String fileRequested) {

        int state = 0; //1 for first if, 2 for else if, 3 for else, for testing
        String fileName = PATH_TO_HTML_FILES, fileContent;
        if(fileRequested.endsWith("/")  || fileRequested.endsWith("index.html")) {

            fileName += INDEX;

            fileContent = readFromFile(fileName);

            printToBrowser(out, "HTTP/1.1 200 OK");

            out.println(fileContent);
            out.flush();

            state = 1;
        } else if(fileRequested.endsWith("link.html")){

            fileName += LINK;

            fileContent = readFromFile(fileName);

            printToBrowser(out, "HTTP/1.1 200 OK");

            out.println(fileContent);
            out.flush();

            state = 2;
        } else {
            fileName += FILE_NOT_FOUND;

            fileContent = readFromFile(fileName);

            printToBrowser(out, "HTTP/1.1 404 File Not Found");

            out.println(fileContent);
            out.flush();

            state = 3;
        }
        return state;
    }

    private int maintenance(PrintWriter out, String file) {

        int maintenanceOk = 0; //for testing
        if(file.equals("maintenance.html")) {
            String fileName = PATH_TO_HTML_FILES + file;
            String fileContent = readFromFile(fileName);

            printToBrowser(out, "HTTP/1.1 200 OK");

            out.println(fileContent);
            out.flush();

            maintenanceOk = 1;
        }
        return maintenanceOk;
    }

    protected String readFromFile(String fileName) {

        String fileContent = null;
        try {
            File file = new File(fileName);

            int fileLength = (int) file.length();

            FileInputStream inFile = null;

            inFile = new FileInputStream(file);

            byte[] fileData = new byte[fileLength];
            inFile.read(fileData);
            inFile.close();

            fileContent = new String(fileData);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileContent;
    }

    private void printToBrowser(PrintWriter out, String httpForm) {
        out.println(httpForm); //HTTP/1.1 200 OK
        out.println("Content-Type: text/html");
        out.println("\r\n");
        out.flush();
    }
}
