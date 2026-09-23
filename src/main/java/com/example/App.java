package com.example;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class App {

    public static void main(String[] args) throws IOException {

        HttpServer server =
                HttpServer.create(new InetSocketAddress(8081), 0);

        server.createContext("/", (HttpExchange exchange) -> {

            String response =
                    "<!DOCTYPE html>" +
                    "<html>" +
                    "<head>" +
                    "<title>Hostel Management System</title>" +
                    "<style>" +
                    "body {" +
                    "font-family: Arial, sans-serif;" +
                    "background-color: #f2f2f2;" +
                    "text-align: center;" +
                    "padding: 50px;" +
                    "}" +

                    ".container {" +
                    "background-color: white;" +
                    "width: 450px;" +
                    "margin: auto;" +
                    "padding: 30px;" +
                    "border-radius: 10px;" +
                    "box-shadow: 0 0 10px #999;" +
                    "}" +

                    "input {" +
                    "width: 90%;" +
                    "padding: 10px;" +
                    "margin: 8px 0;" +
                    "border: 1px solid #ccc;" +
                    "border-radius: 5px;" +
                    "}" +

                    "button {" +
                    "padding: 10px 20px;" +
                    "margin-top: 10px;" +
                    "background-color: #333;" +
                    "color: white;" +
                    "border: none;" +
                    "border-radius: 5px;" +
                    "cursor: pointer;" +
                    "}" +

                    "#message {" +
                    "margin-top: 20px;" +
                    "font-weight: bold;" +
                    "}" +
                    "</style>" +
                    "</head>" +

                    "<body>" +

                    "<div class='container'>" +

                    "<h1>Hostel Management System</h1>" +

                    "<p>Student Hostel Room Allocation</p>" +

                    "<input id='studentName' type='text' " +
                    "placeholder='Enter Student Name'>" +

                    "<input id='roomNumber' type='text' " +
                    "placeholder='Enter Room Number'>" +

                    "<input id='course' type='text' " +
                    "placeholder='Enter Course'>" +

                    "<br>" +

                    "<button onclick='allocateRoom()'>" +
                    "Allocate Room" +
                    "</button>" +

                    "<div id='message'></div>" +

                    "</div>" +

                    "<script>" +

                    "function allocateRoom() {" +

                    "var name = document.getElementById('studentName').value;" +
                    "var room = document.getElementById('roomNumber').value;" +
                    "var course = document.getElementById('course').value;" +

                    "if (name === '' || room === '' || course === '') {" +

                    "document.getElementById('message').innerHTML =" +
                    "'Please fill all details.';" +

                    "} else {" +

                    "document.getElementById('message').innerHTML =" +
                    "'Room ' + room + ' allocated to ' + name + " +
                    "' (' + course + ').';" +

                    "}" +

                    "}" +

                    "</script>" +

                    "</body>" +
                    "</html>";

            byte[] responseBytes =
                    response.getBytes(StandardCharsets.UTF_8);

            exchange.getResponseHeaders()
                    .set("Content-Type", "text/html; charset=UTF-8");

            exchange.sendResponseHeaders(
                    200,
                    responseBytes.length);

            OutputStream output =
                    exchange.getResponseBody();

            output.write(responseBytes);
            output.close();
        });

        server.start();

        System.out.println(
                "Hostel Management System started at http://localhost:8081");
    }
}