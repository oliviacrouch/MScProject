package com.example.demo;

import java.io.IOException;

public interface HouseCalculationService {
    House saveHouse(House house);
    String performApiRequest(String apiURL) throws IOException;
    String handleApiResponse(String apiResponse);
    double calcMonthlyRent(String weeklyRent);
    double calcExpenses(double monthlyRent, House house);
    double calcMonthlyMortgagePayment(String totalMortgage, String interestRate, String loanTerm);
    String handleDaysOnMarketApiRequest(String apiURL);
    String calcGrossYield(double monthlyRentalIncome, double purchasePrice);
    String calcNetYield (double monthlyRentalIncome, double monthlyMortgagePayment,
                         double expenses, double purchasePrice);
    String handleDemandRatingApiRequest(String apiURL);
    String calcVacancyRate(House house);
    String calcNetCashFlow(double monthlyRent, double monthlyMortgagePayment,
                           String vacancyRate, double expenses);
    String calculateInvestmentRating(String purchasePrice, String netYield,
                                     String demandRating, String cashFlow);
    String produceRatingAnalysis(String investmentRating, String cashFlow, String vacancyRate,
                          String purchasePrice);
}
