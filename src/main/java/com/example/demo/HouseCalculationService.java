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

    public String calculateInvestmentRating(String purchasePrice, String netYield,
                                            String demandRating, String cashFlow) {
        // must justify these selections in write-up.
        // outline weightings for each statistic based on their importance (out of 1)
        // Define the minimum and maximum possible values for each parameter
        double minPurchasePrice = 50000; // Set the minimum value for purchase price
        double maxPurchasePrice = 5000000; // Set the maximum value for purchase price
        double minNetYield = -2; // Set the minimum value for net yield
        double maxNetYield = 5; // Set the maximum value for net yield
        double minDemandRating = 1; // Set the minimum value for demand rating
        double maxDemandRating = 10; // Set the maximum value for demand rating
        double minCashFlow = -500; // Set the minimum value for cash flow
        double maxCashFlow = 5000; // Set the maximum value for cash flow

        // Parse input values to doubles
        double purchasePriceDouble = Double.parseDouble(purchasePrice);
        double netYieldDouble = Double.parseDouble(netYield);
        double cashFlowDouble = Double.parseDouble(cashFlow);

        // Normalize the values using min-max normalization
        double normalizedPurchasePrice = (purchasePriceDouble - minPurchasePrice) /
                (maxPurchasePrice - minPurchasePrice);
        double normalizedNetYield = (netYieldDouble - minNetYield) / (maxNetYield - minNetYield);
        double normalizedCashFlow = (cashFlowDouble - minCashFlow) / (maxCashFlow - minCashFlow);
        double normalizedDemandRating = (maxDemandRating - minDemandRating) /
                (maxDemandRating - minDemandRating); // No normalization required for demand rating as it's already within the desired range

        // Define weightings for each parameter (out of 1)
        double purchasePriceWeight = 0.3; // long term value of the asset
        double demandRatingWeight = 0.2;
        double netCashFlowWeight = 0.3; // short term affordability of financing the asset
        double netYieldWeight = 0.2;

        // Calculate rating based on weighted averages
        double investmentRatingValue = (purchasePriceWeight * normalizedPurchasePrice) +
                (demandRatingWeight * normalizedDemandRating) +
                (netCashFlowWeight * normalizedCashFlow) +
                (netYieldWeight * normalizedNetYield);

        // scale  rating to the desired range (1 to 10)
        int investmentRating = (int) Math.round((investmentRatingValue * 9) + 1);

        // ensure rating is within the desired range
        investmentRating = Math.min(Math.max(investmentRating, 1), 10);

        return String.valueOf(investmentRating);
    }

    public String produceRatingAnalysis(String investmentRating, String cashFlow, String vacancyRate,
                                        String purchasePrice) {
        // figure out how to describe each level of possible rating for all variables in
        // one sentence each then place in strings, append all strings and return combined string.
        int investmentRatingInt = Integer.parseInt(investmentRating);
        double cashFlowDouble = Double.parseDouble(cashFlow);
        // not sure if this needs net yield and cash flow - kinda same thing?
        // double netYieldDouble = Double.parseDouble(netYield);
        String vacancyRate1 = vacancyRate.replaceAll("%", "");
        double vacancyRateDouble = Double.parseDouble(vacancyRate1);
        double purchasePriceDouble = Double.parseDouble(purchasePrice);

        String investmentDescription = null;
        // 1. the investment rating - classify into bands
        if (investmentRatingInt <= 3) {
            investmentDescription = "this is an unfavourable rating. This would imply that to invest in " +
                    "this property would be a significant financial burden, which is not advised unless your financial " +
                    "position affords it. ";
        } else if (investmentRatingInt > 3 && investmentRatingInt < 6) {
            investmentDescription = "this is an average rating. The property represents an average investment " +
                    "which is not certain to yield large profit, if any. It may still represent a worthwhile investment if " +
                    "it can be supported by an external income source. Only pursue advice from legal and financial professionals for investments in this" +
                    " property if it can be supported by your disposable income. ";
        } else if (investmentRatingInt >= 6 && investmentRatingInt < 8) {
            investmentDescription = "this is a good rating. The property represents a reasonably optimistic investment which is likely to produce" +
                    " some profit. Albeit this is unlikely to be enough to become a primary source of income, but it could represent " +
                    "beneficial supplementary income in the short term and a valuable asset in the long term. Investigations into this " +
                    "property should be taken further with legal and financial professionals. ";
        } else if (investmentRatingInt >= 8 & investmentRatingInt <= 9) {
            investmentDescription = "this is an excellent rating. The property represents an extremely positive investment which is likely to " +
                    "yield a significant income in the short term and represents an excellent asset for future benefit. It is definitely advisable " +
                    "that this property should be further investigated by legal and financial professionals as a great investment " +
                    "opportunity. ";
        } else if (investmentRatingInt == 10) {
            investmentDescription = "this is a perfect investment rating. It is rare that a rating like this is produced, since it demonstrates" +
                    " that the property will have extremely high turnover and represents a perfect asset. It is strongly encouraged that confirmation" +
                    " on the potential of the property is sought from legal and financial professionals to pursue this property as an investment. ";
        }

        String cashFlowDescription = null;
        if (cashFlowDouble < -150) {
            cashFlowDescription = "The cash flow is predicted to average a large cost for this property. This cost is unlikely to be manageable through " +
                    "disposable income for most individuals. ";
        } else if (cashFlowDouble < 0 && cashFlowDouble > -150) {
            cashFlowDescription = "The cash flow is predicted to average a negative figure. Although negative, this cost could be manageable " +
                    "for someone willing to invest their disposable income for the eventual ownership of this property as an asset. ";
        } else if (cashFlowDouble > 0 && cashFlowDouble <= 200) {
            cashFlowDescription = "The cash flow is predicted to be moderate for this property. This is generally deemed an acceptable" +
                    " cash flow, however a marginal level of cash flow such as this has little room to buffer unexpected expenses. ";
        } else if (cashFlowDouble > 200 && cashFlowDouble <= 400) {
            cashFlowDescription = "The cash flow is predicted to be healthy for this property. This provides the " +
                    " opportunity for reinvestment, saving or being used for other financial goals. ";
        } else if (cashFlowDouble > 400) {
            cashFlowDescription = "The cash flow is extremely favourable for this property. The accumulation of cash flow " +
                    " from this property represents a healthy cushion to protect against unforeseen expenses and economic fluctuations, as well as capital gain. ";
        }

        String purchasePriceDescription = null;
        if (purchasePriceDouble <= 200000 && cashFlowDouble > 0) {
            purchasePriceDescription = "The purchase price is on the lower end of the spectrum. Houses within this" +
                    " price range are generally deemed more affordable, but given the positive cash flow it presents, this could " +
                    "be a great first-time investment in the BTL market.";
        } else if (purchasePriceDouble <= 200000 && cashFlowDouble < 0) {
            purchasePriceDescription = "The purchase price of this property is on the lower end of the spectrum, which is no problem in and of itself." +
                    " However, accompanied by a negative cash flow demonstrates the property lacks in any opportunity for short term " +
                    " gain, as well as not being a hugely impactful asset in the long term. ";
        } else if (purchasePriceDouble > 200000 && purchasePriceDouble < 350000 && cashFlowDouble < 0) {
            purchasePriceDescription = "This property is mid-priced, offering a balance between affordability and desirable features." +
                    " Compared to lower priced properties, this may provide more pace, a better finish, and improved amenities. Since the cash flow is " +
                    " negative, some consideration should be taken as to whether a negative cash flow is affordable for the sake of a " +
                    " more valuable asset in the long term. ";
        } else if (purchasePriceDouble > 200000 && purchasePriceDouble < 350000 && cashFlowDouble > 0) {
            purchasePriceDescription = "This property is mid-priced, offering a balance between affordability and desirable features." +
                    " Compared to lower priced properties, this may provide more space, a better finish, and improved amenities. Accompanied by a positive cash"
                    + " flow, this property represents a great short term generator of income, and long term asset. ";
        } else if (purchasePriceDouble >= 350000 && cashFlowDouble < 0) {
            purchasePriceDescription = "This property would be considered to be at the higher end of the property market. Given the price point, " +
                    "properties in this price range are likely to offer premium features and amenities. This property therefore represents a great asset to possess." +
                    " Although accompanied by a negative cash flow, the property could demonstrate long term appreciation which may enable " +
                    "rent to rise over time eventually returning a positive cash flow. The higher end pricing of the property makes it a more " +
                    " attractive asset in spite of the negative cash flow. ";
        } else if (purchasePriceDouble >= 350000 && cashFlowDouble > 0) {
            purchasePriceDescription = "This property would be considered to be at the higher end of the property market. Given the price point, " +
                    "properties in this price range are likely to offer premium features and amenities. This property therefore represents a great asset to possess. Accompanied"
            + " by a positive cash flow, this property represents the right combination of short term positive cash flow and a long term attractive asset. ";
        }

        String vacancyRateDescription = null;
        if (vacancyRateDouble == 2 && cashFlowDouble < 0) {
            vacancyRateDescription = "The market rating for the property suggests that as a seller, you will experience high demand for " +
                    "your rental property. This suggest that you will have more negotiating power if you do decide to increase rent to produce " +
                    "a better cash flow. ";
        } else if (vacancyRateDouble == 2 && cashFlowDouble > 0) {
            vacancyRateDescription = "The market rating for the property suggests that as a seller, you will experience high demand for " +
                    "your rental property. Since you already have a positive cash flow projected for this property, this implies that you have no need " +
                    "to leverage your negotiating power over renters, but if expenses are to increase unexpectedly you have the option to leverage this to " +
                    "increase prices in line with expenses. ";
        } else if (vacancyRateDouble == 5 && cashFlowDouble < 0) {
            vacancyRateDescription = "The market rating for the property suggests that as a seller, the supply of properties of this specification " +
                    "in the area exceeds the demand. Properties tend to stay on the market for a longer period before finding new " +
                    "tenants, which costs money. Alongside a negative cash flow this could put more pressure on your budget that other properties. ";
        } else if (vacancyRateDouble == 5 && cashFlowDouble > 0) {
            vacancyRateDescription = "The market rating for the property suggests that as a seller, the supply of properties of this specification " +
            "in the area exceeds the demand. Properties tend to stay on the market for a longer period before finding new " +
                    "tenants, which costs money. Since you have a positive cash flow predicted for the property,  it's possible that " +
                    "this shouldn't pose too much of an issue, but is something to consider if you had greater stability in mind. ";
        } else if (vacancyRateDouble == 3 && cashFlowDouble < 0) {
            vacancyRateDescription = "The market rating for the property implies that the supply of homes is roughly equal to the demand from potential renters. " +
                    " In such a market, there is a relatively stable equilibrium between sellers and buyers, and neither side has a significant advantage in negotiations." +
                    " Since the property's cash flow is negative, this could imply that the  property's purchase price is too high relative to its income potential. " +
                    "It is worth looking to negotiate the purchase price, or find alternatives in another bracket. ";
        } else if (vacancyRateDouble == 3 && cashFlowDouble > 0) {
            vacancyRateDescription = "The market rating for the property implies that the supply of homes is roughly equal to the demand from potential renters. " +
                    " In such a market, there is a relatively stable equilibrium between sellers and buyers, and neither side has a significant advantage in negotiations." +
                    "The combination of a balanced market alongside a positive cash flow is an attractive scenario for BTL investment, a offering stable, " +
                    "income generated property. ";
        }


        return cashFlowDescription + purchasePriceDescription + vacancyRateDescription + "Overall, " + investmentDescription;
    }
}
