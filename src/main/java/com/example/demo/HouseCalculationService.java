package com.example.demo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.DecimalFormat;

@Service
public class HouseCalculationService {

    public String performRentApiRequest(String apiURL) {
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

    public String handleApiResponse(String apiResponse) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            // parses apiResponse string from string to json node
            JsonNode jsonNode = objectMapper.readTree(apiResponse);

            JsonNode resultNode = jsonNode.get("result");
            String gbpPerWeek = String.valueOf(resultNode.get("estimate"));
            return gbpPerWeek;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public double calcMonthlyRent(String weeklyRent) {
        DecimalFormat df = new DecimalFormat("0.00");
        int weeklyRentInt = Integer.parseInt(weeklyRent);
        double monthlyRent = weeklyRentInt * 4.34524;
        return Double.parseDouble(df.format(monthlyRent));
    }

    // expenses will vary between construction date and finish quality because
    // this will affect insurance - so as a safe estimate this must be accounted
    public double calcExpenses(double monthlyRent, House house) {
        //int weeklyRentInt = Integer.parseInt(weeklyRent);
        double baseExpenses = monthlyRent * 0.5;
        if(house.getConstructionDate() == House.ConstructionDate.pre_1914 &&
        house.getFinishQuality() == House.FinishQuality.unmodernised) {
            double totalExpenses = baseExpenses * 1.3;
            return totalExpenses;
        }
        if (house.getConstructionDate() == House.ConstructionDate.pre_1914 &&
        house.getFinishQuality() == House.FinishQuality.below_average) {
            double totalExpenses = baseExpenses * 1.25;
            return totalExpenses;
        }
        if (house.getConstructionDate() == House.ConstructionDate.pre_1914 &&
                house.getFinishQuality() == House.FinishQuality.average) {
            double totalExpenses = baseExpenses * 1.2;
            return totalExpenses;
        }
        if (house.getConstructionDate() == House.ConstructionDate.pre_1914 &&
                house.getFinishQuality() == House.FinishQuality.high) {
            double totalExpenses = baseExpenses * 1.175;
            return totalExpenses;
        }
        if (house.getConstructionDate() == House.ConstructionDate.pre_1914 &&
                house.getFinishQuality() == House.FinishQuality.very_high) {
            double totalExpenses = baseExpenses * 1.15;
            return totalExpenses;
        }
        if (house.getConstructionDate() == House.ConstructionDate.nineteen_fourteen_to_two_thousand
        && house.getFinishQuality() == House.FinishQuality.unmodernised) {
            double totalExpenses =  baseExpenses * 1.25;
            return totalExpenses;
        }
        if (house.getConstructionDate() == House.ConstructionDate.nineteen_fourteen_to_two_thousand
        && house.getFinishQuality() == House.FinishQuality.below_average){
            double totalExpenses = baseExpenses * 1.2;
            return totalExpenses;
        }
        if (house.getConstructionDate() == House.ConstructionDate.nineteen_fourteen_to_two_thousand
                && house.getFinishQuality() == House.FinishQuality.average){
            double totalExpenses = baseExpenses * 1.15;
            return totalExpenses;
        }
        if (house.getConstructionDate() == House.ConstructionDate.nineteen_fourteen_to_two_thousand
                && house.getFinishQuality() == House.FinishQuality.high){
            double totalExpenses = baseExpenses * 1.15;
            return totalExpenses;
        }
        if (house.getConstructionDate() == House.ConstructionDate.nineteen_fourteen_to_two_thousand
                && house.getFinishQuality() == House.FinishQuality.very_high){
            double totalExpenses = baseExpenses * 1.10;
            return totalExpenses;
        }
        if (house.getConstructionDate() == House.ConstructionDate.two_thousand_onwards
                && house.getFinishQuality() == House.FinishQuality.unmodernised){
            double totalExpenses = baseExpenses * 1.2;
            return totalExpenses;
        }
        if (house.getConstructionDate() == House.ConstructionDate.two_thousand_onwards
                && house.getFinishQuality() == House.FinishQuality.below_average){
            double totalExpenses = baseExpenses * 1.1;
            return totalExpenses;
        }
        return baseExpenses;
    }

    public double calcMonthlyMortgagePayment(String totalMortgage, String interestRate, String loanTerm) {
        DecimalFormat df = new DecimalFormat("0.00");
        int totalMortgageInt = Integer.parseInt(totalMortgage);
        double interestRateDouble = Double.parseDouble(interestRate);
        double interestRateDecimal = interestRateDouble/100;
        double monthlyInterestRate = interestRateDecimal/12;
        int loanTermInt = Integer.parseInt(loanTerm);
        int loanTermMonths = loanTermInt * 12;
        double monthlyMortgagePayment = (totalMortgageInt*monthlyInterestRate)/(1 -
                Math.pow(1 + monthlyInterestRate, -loanTermMonths));

        return Double.parseDouble(df.format(monthlyMortgagePayment));
    }

    // api for demand in area - Days on market imply how hot the market
    // is in this area. and how long the house is likely to stay on the market for.
//    public String performDemandApiRequest(String ApiURL) {
//
//    }
}
