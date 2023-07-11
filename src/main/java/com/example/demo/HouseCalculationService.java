package com.example.demo;

import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class HouseCalculationService {

    public String performApiRequest(String apiURL) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiURL))
                .header("X-RapidAPI-Key", "YZSEDKSVC4")
                .build();
        System.out.println("API request sent.");

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode();
            if (statusCode == 200) {
                System.out.println("API response received.");
                return response.body();
            } else {
                System.out.println("API request failed. Status code: " + statusCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            System.out.println("Finally block executed.");
        }
        return null;
    }

}
