package com.example.demo;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;

@Controller
public class HouseCalculationController {
    // autowired relation tool injects the dependency to all
    // access to functionality of other classes.
    @Autowired
    private HouseCalculationRepository houseDetailRepo;

    @Autowired
    private HouseCalculationService houseCalculationService;

    @GetMapping("/analyse")
    public String analyse(){
        return "analyse";
    }

    @PostMapping("/process-form")
    public String houseRegistration(@ModelAttribute House house, HttpSession httpSession){
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


        try {
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

            //recommended rent and monthly conversion
            System.out.println(apiURL);
            String apiResponse = houseCalculationService.performApiRequest(apiURL); ///changed
            System.out.println("This is the api response:" + apiResponse);
            String weeklyRent = houseCalculationService.handleApiResponse(apiResponse);

            double monthlyRent = houseCalculationService.calcMonthlyRent(weeklyRent);
            house.setRentRecommend(String.valueOf(monthlyRent));
            String handledApiResponse = houseCalculationService.handleApiResponse(apiResponse);
            System.out.println("This is the handled API response: " + handledApiResponse);
            // monthly expenses
            double expenses = houseCalculationService.calcExpenses(monthlyRent, house);
            house.setExpenses(String.valueOf(expenses));
            // monthly mortgage payment
            double monthlyMortgagePayment = houseCalculationService.calcMonthlyMortgagePayment
                    (house.getMortgageAmount(), house.getInterestRate(), house.getLoanTerm());
            house.setMonthlyMortgagePayment(String.valueOf(monthlyMortgagePayment));
            // api for demand
            String apiURLDemand = "https://api.propertydata.co.uk/demand?key=YZSEDKSVC4"
                    + "&postcode=" + house.getPostcode();
            String apiResponseDemand = houseCalculationService.performApiRequest(apiURLDemand);
            System.out.println(apiURLDemand);
            System.out.println("this is the demand API response " + apiResponseDemand);
            String demand = houseCalculationService.handleDaysOnMarketApiRequest(apiResponseDemand);
            System.out.println("this is the no days on market: " + demand);
            house.setDaysOnMarket(demand);
            // demand rating
            String demandRating = houseCalculationService.handleDemandRatingApiRequest
                    (apiResponseDemand);
            System.out.println("This is the demand rating:" + demandRating);
            house.setDemandRating(demandRating);
            // gross yield
            String grossYield = houseCalculationService.calcGrossYield(monthlyRent,
                    Double.parseDouble(house.getPurchasePrice()));
            house.setGrossYield(grossYield);
            // net yield
            String netYield = houseCalculationService.calcNetYield(monthlyRent, monthlyMortgagePayment,
                    expenses, Double.parseDouble(house.getPurchasePrice()));
            house.setNetYield(netYield);
            // calculate vacancy rate based off demand rating
            String vacancyRate = houseCalculationService.calcVacancyRate(house);
            house.setVacancyRate(vacancyRate);
            // calculate cash flow
            String cashFlow = houseCalculationService.calcNetCashFlow(monthlyRent, monthlyMortgagePayment,
                    vacancyRate, expenses);
            house.setCashFlow(cashFlow);
            // calculate score for the show-results html page
            String investmentRating = houseCalculationService.calculateInvestmentRating(house.getPurchasePrice(), netYield, demandRating,
                    cashFlow);
            house.setInvestmentRating(investmentRating);
            System.out.println("This is the investment rating: " + investmentRating);
            // save new details
            //houseDetailRepo.save(house);
            House savedHouse = houseCalculationService.saveHouse(house);
            // carry forward these to the next HTTP page so an analysis of them can be
            // performed to better inform the user's understanding of the investment rating
            httpSession.setAttribute("investmentRating", investmentRating);
            httpSession.setAttribute("cashFlow", cashFlow);
            httpSession.setAttribute("purchasePrice", savedHouse.getPurchasePrice());
            httpSession.setAttribute("vacancyRate", vacancyRate);
            httpSession.setAttribute("netYield", netYield);
            return "stats";
        }
        catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
    }

    @GetMapping("/process-score")
    public String showResults(Model model, HttpSession httpSession) {
        String investmentRating = (String) httpSession.getAttribute("investmentRating");
        String cashFlow = (String) httpSession.getAttribute("cashFlow");
        String purchasePrice = (String) httpSession.getAttribute("purchasePrice");
        String vacancyRate = (String) httpSession.getAttribute("vacancyRate");
        //String netYield = (String) httpSession.getAttribute("netYield");
        model.addAttribute("investmentRating", investmentRating);
        model.addAttribute("cashFlow", cashFlow);
        model.addAttribute("vacancyRate", vacancyRate);
        model.addAttribute("purchasePrice", purchasePrice);

        String ratingAnalysis =houseCalculationService.produceRatingAnalysis(investmentRating, cashFlow, vacancyRate,
                purchasePrice);

        // then can display this in the HTML using this reference? (I think):
        model.addAttribute("ratingAnalysis", ratingAnalysis);
        return "investmentRating";
    }
}

