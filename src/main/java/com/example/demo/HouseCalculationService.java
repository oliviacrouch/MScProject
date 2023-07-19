package com.example.demo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.DecimalFormat;
import java.util.Objects;

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
        DecimalFormat df = new DecimalFormat("0.00");
        //int weeklyRentInt = Integer.parseInt(weeklyRent);
        double baseExpenses = monthlyRent * 0.4;
        if(house.getConstructionDate() == House.ConstructionDate.pre_1914 &&
        house.getFinishQuality() == House.FinishQuality.unmodernised) {
            double totalExpenses = baseExpenses * 1.3;
            return Double.parseDouble(df.format(totalExpenses));
        }
        if (house.getConstructionDate() == House.ConstructionDate.pre_1914 &&
        house.getFinishQuality() == House.FinishQuality.below_average) {
            double totalExpenses = baseExpenses * 1.25;
            return Double.parseDouble(df.format(totalExpenses));
        }
        if (house.getConstructionDate() == House.ConstructionDate.pre_1914 &&
                house.getFinishQuality() == House.FinishQuality.average) {
            double totalExpenses = baseExpenses * 1.2;
            return Double.parseDouble(df.format(totalExpenses));
        }
        if (house.getConstructionDate() == House.ConstructionDate.pre_1914 &&
                house.getFinishQuality() == House.FinishQuality.high) {
            double totalExpenses = baseExpenses * 1.175;
            return Double.parseDouble(df.format(totalExpenses));
        }
        if (house.getConstructionDate() == House.ConstructionDate.pre_1914 &&
                house.getFinishQuality() == House.FinishQuality.very_high) {
            double totalExpenses = baseExpenses * 1.15;
            return Double.parseDouble(df.format(totalExpenses));
        }
        if (house.getConstructionDate() == House.ConstructionDate.nineteen_fourteen_to_two_thousand
        && house.getFinishQuality() == House.FinishQuality.unmodernised) {
            double totalExpenses =  baseExpenses * 1.25;
            return Double.parseDouble(df.format(totalExpenses));
        }
        if (house.getConstructionDate() == House.ConstructionDate.nineteen_fourteen_to_two_thousand
        && house.getFinishQuality() == House.FinishQuality.below_average){
            double totalExpenses = baseExpenses * 1.2;
            return Double.parseDouble(df.format(totalExpenses));
        }
        if (house.getConstructionDate() == House.ConstructionDate.nineteen_fourteen_to_two_thousand
                && house.getFinishQuality() == House.FinishQuality.average){
            double totalExpenses = baseExpenses * 1.15;
            return Double.parseDouble(df.format(totalExpenses));
        }
        if (house.getConstructionDate() == House.ConstructionDate.nineteen_fourteen_to_two_thousand
                && house.getFinishQuality() == House.FinishQuality.high){
            double totalExpenses = baseExpenses * 1.15;
            return Double.parseDouble(df.format(totalExpenses));
        }
        if (house.getConstructionDate() == House.ConstructionDate.nineteen_fourteen_to_two_thousand
                && house.getFinishQuality() == House.FinishQuality.very_high){
            double totalExpenses = baseExpenses * 1.10;
            return Double.parseDouble(df.format(totalExpenses));
        }
        if (house.getConstructionDate() == House.ConstructionDate.two_thousand_onwards
                && house.getFinishQuality() == House.FinishQuality.unmodernised){
            double totalExpenses = baseExpenses * 1.2;
            return Double.parseDouble(df.format(totalExpenses));
        }
        if (house.getConstructionDate() == House.ConstructionDate.two_thousand_onwards
                && house.getFinishQuality() == House.FinishQuality.below_average){
            double totalExpenses = baseExpenses * 1.1;
            return Double.parseDouble(df.format(totalExpenses));
        }
        return Double.parseDouble(df.format(baseExpenses));
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
    public String handleDaysOnMarketApiRequest(String apiURL) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // parses apiResponse string from string to json node
            JsonNode jsonNode = objectMapper.readTree(apiURL);
            String daysOnMarket = String.valueOf(jsonNode.get("days_on_market"));
            return daysOnMarket;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // return before expenses
    public String calcGrossYield(double monthlyRentalIncome, double purchasePrice) {
        DecimalFormat df = new DecimalFormat("0.00");
        double annualGrossIncome = monthlyRentalIncome * 12;
        double grossYield = (annualGrossIncome / purchasePrice) * 100;
        return df.format(grossYield);
    }

    // net rental is return after expenses
    public String calcNetYield (double monthlyRentalIncome, double monthlyMortgagePayment,
                                double expenses, double purchasePrice)  {
        DecimalFormat df = new DecimalFormat("0.00");
        double annualGrossIncome = monthlyRentalIncome * 12;
        double allExpenses = (monthlyMortgagePayment*12)+(expenses*12);
        double rentalMinusExpenses = annualGrossIncome - allExpenses;
        double netYield = (rentalMinusExpenses / purchasePrice) * 100;
        return df.format(netYield);
    }

    public String handleDemandRatingApiRequest(String apiURL){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // parses apiResponse string from string to json node
            JsonNode jsonNode = objectMapper.readTree(apiURL);
            String demandRating = String.valueOf(jsonNode.get("demand_rating"));
            String demandRatingNoQuotes = demandRating.replaceAll("\"", "");
            return demandRatingNoQuotes;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String calcVacancyRate(House house) {
        if (Objects.equals(house.getDemandRating(), "Balanced market")){
            return "3%";
        }
        if (Objects.equals(house.getDemandRating(), "Seller's market")){
            return "2%";
        }
        if (Objects.equals(house.getDemandRating(), "Buyer's market")) {
            return "5%";
        }
        return null;
    }
    public String calcNetCashFlow(double monthlyRent, double monthlyMortgagePayment,
                               String vacancyRate, double expenses) {
        DecimalFormat df = new DecimalFormat("0.00");
        // calc gross rental income
        double grossRentalIncome = monthlyRent * 12;
        // handle vacancy rate string
        String vacancyRate1 = vacancyRate.replaceAll("%", "");
        double vacancyRateDouble = Double.parseDouble(vacancyRate1) / 100;
        // calc effective rental income
        double effectiveRentalIncome = grossRentalIncome * (1 - vacancyRateDouble);
        // yearly cashflow
        double cashFlow = effectiveRentalIncome - (expenses * 12) - (monthlyMortgagePayment * 12);
        // return monthly cashflow
        return String.valueOf(df.format(cashFlow / 12));
    }
}
