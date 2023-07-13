package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AnalyseController {
    // autowired relation tool injects the dependency to all
    // access to functionality of other classes.
    @Autowired
    private HouseDetailRepository houseDetailRepo;

    @Autowired
    private HouseCalculationService houseCalculationService;

    @GetMapping("/analyse")
    public String analyse(){
        return "analyse";
    }

    @PostMapping("/process-form")
    public String houseRegistration(@ModelAttribute House house, Model model){
        System.out.println(house.toString());
        // validate entries into the database
        System.out.println(house.getPurchasePrice());
        System.out.println(house.getDownPayment());
        System.out.println(house.getMortgageAmount());
        System.out.println(house.getInterestRate());
        System.out.println(house.getLoanTerm());
        System.out.println(house.getIncome());
        System.out.println(house.getPostcode());
        System.out.println(house.getBedrooms());
        System.out.println(house.getPropertyType());
        System.out.println(house.getOutdoorSpace());
        System.out.println(house.getConstructionDate());
        System.out.println(house.getFinishQuality());
        System.out.println(house.getInternalArea());

        String constructionDateApiFormat;
        if (house.getConstructionDate() == House.ConstructionDate.pre_1914){
            constructionDateApiFormat = "pre_1914";
        } else if (house.getConstructionDate() == House.ConstructionDate.
                nineteen_fourteen_to_two_thousand) {
            constructionDateApiFormat = "1914_2000";
        }
        else if (house.getConstructionDate() == House.ConstructionDate.two_thousand_onwards) {
            constructionDateApiFormat = "2000_onwards";
        }
        else {
            constructionDateApiFormat = "Format not recognised";
        }

        System.out.println("The API request is starting.");
        String apiURL = "https://api.propertydata.co.uk/valuation-rent?key=YZSEDKSVC4"
                + "&postcode=" + house.getPostcode()
                + "&internal_area=" + house.getInternalArea()
                + "&property_type=" + house.getPropertyType()
                + "&construction_date=" + constructionDateApiFormat
                + "&bedrooms=" + house.getBedrooms()
                + "&bathrooms=" + house.getBathrooms()
                + "&finish_quality=" + house.getFinishQuality()
                + "&outdoor_space=" + house.getOutdoorSpace()
                + "&off_street_parking=" + house.getOffStreetParking();

        System.out.println(apiURL);
        //perform API request and retrieve response.
        String apiResponse = houseCalculationService.performApiRequest(apiURL);
        System.out.println("This is the api response:" + apiResponse);
//        handle API response to extract relevant info in service class
        String weeklyRent = houseCalculationService.handleApiResponse(apiResponse);
        double monthlyRent = houseCalculationService.calcMonthlyRent(weeklyRent);
        house.setRentRecommend(String.valueOf(monthlyRent));
//        house.setRentRecommend(houseCalculationService.handleApiResponse(apiResponse));
        String handledApiResponse = houseCalculationService.handleApiResponse(apiResponse);
        System.out.println("This is the handled API response: " + handledApiResponse);

        // weight the expenses calculated in the service class according to the age of the house and
        // the finish quality within
        double expenses = houseCalculationService.calcExpenses(monthlyRent, house);
        house.setExpenses(String.valueOf(expenses));
        houseDetailRepo.save(house);

        return "stats";
    }
}

