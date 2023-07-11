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

        System.out.println("The API request is starting.");
        String apiURL = "https://api.propertydata.co.uk/valuation-rent?key=YZSEDKSVC4"
                + "&postcode=" + house.getPostcode()
                + "&internal_area=" + house.getInternalArea()
                + "&property_type=" + house.getPropertyType()
                + "&construction_date=" + house.getConstructionDate()
                + "&bedrooms=" + house.getBedrooms()
                + "&bathrooms=" + house.getBathrooms()
                + "&finish_quality=" + house.getFinishQuality()
                + "&outdoor_space=" + house.getOutdoorSpace()
                + "&off_street_parking=" + house.getOffStreetParking();

        System.out.println(apiURL);
        //perform API request and retrieve response.
        String apiResponse = houseCalculationService.performApiRequest(apiURL);
        house.setRentRecommend(apiResponse);
        System.out.println("This is the api response:" + apiResponse);
        //double cashFlow houseCalculationService.calcCashFlow();

        houseDetailRepo.save(house);
        //double debtToIncomeRatio = houseCalculationService.calcDebtToIncome();
        //double monthlyMortgagePayment = houseCalculationService.calcMonthlyMortgage();
        //double rentalIncome = houseCalculationService.calcRentalIncome();
        //double expenses = houseCalculationService.calcExpenses();
        //double cashFlow = houseCalculationService.calcCashFlow();
        //double rOI = houseCalculationService.calcRoi();


        return "stats";
    }
//    @PostMapping("/rent")
//    public String rent(@ModelAttribute House house){
//        System.out.println("The API request is starting.");
//        String apiURL = "https://propertydata.co.uk/valuation-rent"
//                + "postcode=" + house.getPostcode()
//                + "&bedroomNumber=" + house.getBedrooms()
//                + "&propertyType=" + house.getPropertyType()
//                + "&constructionDate=" + house.getConstructionDate()
//                + "&outdoorSpace=" + house.getOutdoorSpace()
//                + "&finishQuality=" + house.getFinishQuality()
//                + "&internalArea=" + house.getInternalArea()
//                + "&offStreetParking=" + house.getOffStreetParking()
//                + "&bathrooms=" + house.getBathrooms();
//
//        System.out.println(apiURL);
//        //perform API request and retrieve response.
//        String apiResponse = houseCalculationService.performApiRequest(apiURL);
//        house.setRentRecommend(apiResponse);
//        System.out.println("This is the api response:" + house.getRentRecommend());
//        return "stats";
}

