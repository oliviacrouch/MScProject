package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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
    public String houseRegistration(@ModelAttribute House house){
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
        
        String houseTypeApiFormat = "";
        if(house.getPropertyType() == House.PropertyType.semidetached_house) {
            houseTypeApiFormat = "semi-detached_house";
        }
        else if (house.getPropertyType() == House.PropertyType.detached_house) {
            houseTypeApiFormat = "detached_house";
        }
        else if (house.getPropertyType() == House.PropertyType.terraced_house){
            houseTypeApiFormat = "terraced_house";
        }
        else if (house.getPropertyType() == House.PropertyType.flat) {
            houseTypeApiFormat = "flat";
        }
        else {
            constructionDateApiFormat = "Format not recognised";
        }

        System.out.println("The API request is starting.");
        String apiURL = "https://api.propertydata.co.uk/valuation-rent?key=YZSEDKSVC4"
                + "&postcode=" + house.getPostcode()
                + "&internal_area=" + house.getInternalArea()
                + "&property_type=" + houseTypeApiFormat
                + "&construction_date=" + constructionDateApiFormat
                + "&bedrooms=" + house.getBedrooms()
                + "&bathrooms=" + house.getBathrooms()
                + "&finish_quality=" + house.getFinishQuality()
                + "&outdoor_space=" + house.getOutdoorSpace()
                + "&off_street_parking=" + house.getOffStreetParking();

        System.out.println(apiURL);
        String apiResponse = houseCalculationService.performRentApiRequest(apiURL);
        System.out.println("This is the api response:" + apiResponse);
        String weeklyRent = houseCalculationService.handleApiResponse(apiResponse);
        double monthlyRent = houseCalculationService.calcMonthlyRent(weeklyRent);
        house.setRentRecommend(String.valueOf(monthlyRent));
        String handledApiResponse = houseCalculationService.handleApiResponse(apiResponse);
        System.out.println("This is the handled API response: " + handledApiResponse);
        double expenses = houseCalculationService.calcExpenses(monthlyRent, house);
        house.setExpenses(String.valueOf(expenses));
        double monthlyMortgagePayment = houseCalculationService.calcMonthlyMortgagePayment
                (house.getMortgageAmount(), house.getInterestRate(), house.getLoanTerm());
        house.setMonthlyMortgagePayment(String.valueOf(monthlyMortgagePayment));
        houseDetailRepo.save(house);

        return "stats";
    }
}

