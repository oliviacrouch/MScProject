package com.example.demo;
import org.springframework.stereotype.Service;
import java.util.Objects;
@Service
public class RatingService {
    public String calculateInvestmentRating(String purchasePrice, String netYield,
                                            String demandRating, String cashFlow) {
        // must justify these selections in write-up.
        // outline weightings for each statistic based on their importance (out of 1)
        double purchasePriceWeight = 0.3;
        double demandRatingWeight = 0.2;
        double netCashFlowWeight = 0.3;
        double netYieldWeight = 0.2;
        double cashFlowDouble = Double.parseDouble(cashFlow);
        double purchasePriceDouble = Double.parseDouble(purchasePrice);
        String netYield1 = netYield.replaceAll("%", "");
        double demandRatingDouble = 0.0;
        double netYieldDouble = Double.parseDouble(netYield1)/100;
        if (Objects.equals(demandRating, "Balanced market")) {
            demandRatingDouble = 5;
        } else if (Objects.equals(demandRating, "Seller's market")){
            demandRatingDouble = 10;
        } else if (Objects.equals(demandRating, "Buyer's market")) {
            demandRatingDouble = 1;
        }
        System.out.println("The net yield: " + netYieldDouble);
        System.out.println("The cash flow: " + cashFlowDouble);
        System.out.println("The purchase price: " + purchasePriceDouble);
        System.out.println("The demand rating in numerical form: " + demandRatingDouble);
        // calculate rating based on weighted averages:
        double investmentRatingValue = (purchasePriceWeight * purchasePriceDouble) +
                (netYieldWeight * netYieldDouble) + (netCashFlowWeight * cashFlowDouble)
                + (demandRatingWeight * demandRatingDouble);
        int investmentRating = (int) Math.round((investmentRatingValue*9) + 1);
        return String.valueOf(investmentRating);
    }
}
