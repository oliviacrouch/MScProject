package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RatingController {

    @Autowired
    HouseDetailRepository houseDetailRepository;

    @Autowired
    RatingService ratingService;

    @PostMapping("/process-score")
    public String showResults(@ModelAttribute House house) {
        String investmentRating = ratingService.calculateInvestmentRating(house.getMonthlyMortgagePayment(),
                house.getNetYield(), house.getDemandRating(), house.getCashFlow());
        house.setInvestmentRating(investmentRating);
        return "investmentRating";
    }

}
