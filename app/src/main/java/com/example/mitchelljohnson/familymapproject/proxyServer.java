package com.example.mitchelljohnson.familymapproject;

import com.example.mitchelljohnson.familymapproject.RequestsAndResponses.event.eventSet;
import com.example.mitchelljohnson.familymapproject.RequestsAndResponses.Login.loginRequest;
import com.example.mitchelljohnson.familymapproject.dataObjects.model;
import com.example.mitchelljohnson.familymapproject.RequestsAndResponses.person.personRequest;
import com.example.mitchelljohnson.familymapproject.RequestsAndResponses.person.personResponse;
import com.example.mitchelljohnson.familymapproject.RequestsAndResponses.person.personsResponse;
import com.example.mitchelljohnson.familymapproject.RequestsAndResponses.register.registerRequest;
import com.example.mitchelljohnson.familymapproject.RequestsAndResponses.register.registerResponse;
import com.google.gson.Gson;
import org.json.*;
import java.io.*;
import java.net.*;

/*
	The Client class shows how to call a web API operation from
	a Java program.  This is typical of how your Android client
	app will call the web API operations of your server.
*/
public class proxyServer {

model m = model.getInstance();
    public static registerResponse Login(String serverHost, String serverPort, loginRequest log) {

        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/login");
            JSONObject requestBodyJson = new JSONObject();
            requestBodyJson.put("userName", log.getUserName());
            requestBodyJson.put("password", log.getPassword());
            String requestBodyString = requestBodyJson.toString();
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(false);
            http.addRequestProperty("Accept", "application/json");
            String reqData = log.convertToJson();
            http.setDoOutput(true);
            OutputStream reqBody = http.getOutputStream();
            // Write the JSON data to the request body
            reqBody.write(requestBodyString.getBytes());
            reqBody.close();
            http.connect();
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);
                Gson a = new Gson();
                return a.fromJson(respData, registerResponse.class);
            }
            else {
                System.out.println("ERROR: " + http.getResponseMessage());
                return null;
            }
        }
        catch (Exception e) {
            // An exception was thrown, so display the exception's stack trace
            e.printStackTrace();
        }
        return null;
    }

    public static personsResponse getPersons(String serverHost, String serverPort, personRequest person){
        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/person");
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("GET");
            http.setDoOutput(false);
            http.addRequestProperty("Authorization", person.authToken);
            http.addRequestProperty("Accept", "application/json");
            http.connect();
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);
                Gson a = new Gson();
                return a.fromJson(respData, personsResponse.class);

            }
            else {
                System.out.println("ERROR: " + http.getResponseMessage());
                return null;
            }
        }catch (Exception e) {
            // An exception was thrown, so display the exception's stack trace
            e.printStackTrace();
        }
        return null;
    }

    public static personResponse getPerson(String serverHost, String serverPort, personRequest person) {
        try {


            URL url = new URL("http://" + serverHost + ":" + serverPort + "/person/" +person.personID);
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("GET");
            http.setDoOutput(false);
            http.addRequestProperty("Authorization", person.authToken);
            http.addRequestProperty("Accept", "application/json");
            http.connect();
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);
                Gson a = new Gson();
                return a.fromJson(respData, personResponse.class);

            }
            else {
                System.out.println("ERROR: " + http.getResponseMessage());
                return null;
            }
        }catch (Exception e) {
            // An exception was thrown, so display the exception's stack trace
            e.printStackTrace();
        }
        return null;
    }

    public static eventSet getEvents(String serverHost, String serverPort, personRequest person) {
        try {

            URL url = new URL("http://" + serverHost + ":" + serverPort + "/event" );
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("GET");
            http.setDoOutput(false);
            http.addRequestProperty("Authorization", person.authToken);
            http.addRequestProperty("Accept", "application/json");
            http.connect();
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);
                Gson a = new Gson();
                return a.fromJson(respData, eventSet.class);

            }
            else {
                System.out.println("ERROR: " + http.getResponseMessage());
                return null;
            }
        }catch (Exception e) {
            // An exception was thrown, so display the exception's stack trace
            e.printStackTrace();
        }
        return null;
    }

    public static personResponse getEvent(String serverHost, String serverPort, personRequest person) {
        try {


            URL url = new URL("http://" + serverHost + ":" + serverPort + "/event/" +person.personID);
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("GET");
            http.setDoOutput(false);
            http.addRequestProperty("Authorization", person.authToken);
            http.addRequestProperty("Accept", "application/json");
            http.connect();
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);
                Gson a = new Gson();
                return a.fromJson(respData, personResponse.class);

            }
            else {
                System.out.println("ERROR: " + http.getResponseMessage());
                return null;
            }
        }catch (Exception e) {
            // An exception was thrown, so display the exception's stack trace
            e.printStackTrace();
        }
        return null;
    }


    public static registerResponse Register(String serverHost, String serverPort, registerRequest reg) {

        // This method shows how to send a POST request to a server

        try {
            JSONObject requestBodyJson = new JSONObject();
            requestBodyJson.put("userName", reg.getUserName());
            requestBodyJson.put("password", reg.getPassword());
            requestBodyJson.put("email", reg.getEmail());
            requestBodyJson.put("firstName", reg.getFirstName());
            requestBodyJson.put("lastName", reg.getLastName());
            requestBodyJson.put("gender", reg.getGender());
            String requestBodyString = requestBodyJson.toString();
            // Create a URL indicating where the server is running, and which
            // web API operation we want to call
            URL url = new URL( "http://" + serverHost + ":" + serverPort + "/user/register");
            // Start constructing our HTTP request
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            // Specify that we are sending an HTTP POST request
            http.setRequestMethod("POST");
            // Indicate that this request will contain an HTTP request body
            http.setDoOutput(true);	// There is a request body
            http.addRequestProperty("Accept", "application/json");
            http.connect();
            // This is the JSON string we will send in the HTTP request body    THIS INPUT MAY NEED SCRUBBING
            // Get the output stream containing the HTTP request body
            OutputStream reqBody = http.getOutputStream();
            // Write the JSON data to the request body
            reqBody.write(requestBodyString.getBytes());
            reqBody.close();

            int r = http.getResponseCode();
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);
                Gson a = new Gson();
               return a.fromJson(respData, registerResponse.class);
            }
            else {
                System.out.println("ERROR: " + http.getResponseMessage());
            }
        }
        catch (Exception e) {
            // An exception was thrown, so display the exception's stack trace
            e.printStackTrace();
        }
        return null;
    }

    /*
        The readString method shows how to read a String from an InputStream.
    */
    private static String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }
}
