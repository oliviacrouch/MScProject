package com.example.demo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name = "house")
public class House {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int houseId;
    @NotEmpty
    @Column(name = "purchasePrice")
    private String purchasePrice;
    @NotEmpty
    @Column(name = "downPayment")
    private String downPayment;
    @NotEmpty
    @Column(name = "mortgageAmount")
    private String mortgageAmount;
    @NotEmpty
    @Column(name = "interestRate")
    private String interestRate;
    @NotEmpty
    @Column(name = "loanTerm")
    private String loanTerm;
//    @NotEmpty
//    @Column(name = "income")
//    private String income;
    @NotEmpty
    @Column(name="internalArea")
    private String internalArea;
    @NotEmpty
    @Column(name = "postcode")
    private String postcode;
    @NotEmpty
    @Column(name="offStreetParking")
    private String offStreetParking;
    @Column(name= "outdoorSpace")
    @Enumerated(EnumType.STRING)
    private OutdoorSpace outdoorSpace;
    @NotEmpty
    @Column(name = "bedrooms")
    private String bedrooms;
    @Column(name= "bathrooms")
    private String bathrooms;
    @Column(name = "propertyType")
    @Enumerated(EnumType.STRING)
    private PropertyType propertyType;
    @Column(name= "finishQuality")
    @Enumerated(EnumType.STRING)
    private FinishQuality finishQuality;
    @Column(name= "constructionDate")
    @Enumerated(EnumType.STRING)
    private ConstructionDate constructionDate;
    @Column(name="rentRecommend")
    private String rentRecommend;
    @Column(name="expenses")
    private String expenses;
    @Column(name="monthlyMortgagePayment")
    private String monthlyMortgagePayment;
    @Column(name = "daysOnMarket")
    private String daysOnMarket;
    @Column(name = "grossYield")
    private String grossYield;
    @Column(name = "netYield")
    private String netYield;
    @Column(name= "demandRating")
    private String demandRating;
    @Column(name = "vacancyRate")
    private String vacancyRate;
    @Column(name = "cashFlow")
    private String cashFlow;
    @Column(name = "investmentRating")
    private String investmentRating;

    public int getHouseId() {
        return houseId;
    }

    public void setHouseId(int houseId) {
        this.houseId = houseId;
    }

    public String getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(String purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public String getDownPayment() {
        return downPayment;
    }

    public void setDownPayment(String downPayment) {
        this.downPayment = downPayment;
    }

    public String getMortgageAmount() {
        return mortgageAmount;
    }

    public void setMortgageAmount(String mortgageAmount) {
        this.mortgageAmount = mortgageAmount;
    }

    public String getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(String interestRate) {
        this.interestRate = interestRate;
    }

    public String getLoanTerm() {
        return loanTerm;
    }

    public void setLoanTerm(String loanTerm) {
        this.loanTerm = loanTerm;
    }

//    public String getIncome() {
//        return income;
//    }
//
//    public void setIncome(String income) {
//        this.income = income;
//    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getBedrooms() {
        return bedrooms;
    }

    public void setBedrooms(String bedrooms) {
        this.bedrooms = bedrooms;
    }

    public PropertyType getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(PropertyType propertyType) {
        this.propertyType = propertyType;
    }

    public enum PropertyType {
        flat,
        terraced_house,
        detached_house,
        semidetached_house
    }

//    correct one
    public enum ConstructionDate {
        pre_1914,
        nineteen_fourteen_to_two_thousand,
        two_thousand_onwards;
    }

    public enum FinishQuality {
        very_high,
        high,
        average,
        below_average,
        unmodernised
    }

    public enum OutdoorSpace {
        none,
        balcony_terrace,
        garden,
        garden_very_large
    }

    public String getRentRecommend() {
        return rentRecommend;
    }

    public void setRentRecommend(String rentRecommend) {
        this.rentRecommend = rentRecommend;
    }

    public OutdoorSpace getOutdoorSpace() {
        return outdoorSpace;
    }

    public void setOutdoorSpace(OutdoorSpace outdoorSpace) {
        this.outdoorSpace = outdoorSpace;
    }

    public FinishQuality getFinishQuality() {
        return finishQuality;
    }

    public void setFinishQuality(FinishQuality finishQuality) {
        this.finishQuality = finishQuality;
    }

    public ConstructionDate getConstructionDate() {
        return constructionDate;
    }

    public void setConstructionDate(ConstructionDate constructionDate) {
        this.constructionDate = constructionDate;
    }

    public String getOffStreetParking() {
        return offStreetParking;
    }

    public void setOffStreetParking(String offStreetParking) {
        this.offStreetParking = offStreetParking;
    }

    public String getInternalArea() {
        return internalArea;
    }

    public void setInternalArea(String internalArea) {
        this.internalArea = internalArea;
    }

    public String getBathrooms() {
        return bathrooms;
    }

    public void setBathrooms(String bathrooms) {
        this.bathrooms = bathrooms;
    }

    public String getExpenses() {
        return expenses;
    }

    public void setExpenses(String expenses) {
        this.expenses = expenses;
    }

    public String getMonthlyMortgagePayment() {
        return monthlyMortgagePayment;
    }

    public void setMonthlyMortgagePayment(String monthlyMortgagePayment) {
        this.monthlyMortgagePayment = monthlyMortgagePayment;
    }

    public String getDaysOnMarket() {
        return daysOnMarket;
    }

    public void setDaysOnMarket(String daysOnMarket) {
        this.daysOnMarket = daysOnMarket;
    }

    public String getGrossYield() {
        return grossYield;
    }

    public void setGrossYield(String grossYield) {
        this.grossYield = grossYield;
    }

    public String getNetYield() {
        return netYield;
    }

    public void setNetYield(String netYield) {
        this.netYield = netYield;
    }

    public String getDemandRating() {
        return demandRating;
    }

    public void setDemandRating(String demandRating) {
        this.demandRating = demandRating;
    }

    public String getVacancyRate() {
        return vacancyRate;
    }

    public void setVacancyRate(String vacancyRate) {
        this.vacancyRate = vacancyRate;
    }

    public String getCashFlow() {
        return cashFlow;
    }

    public void setCashFlow(String cashFlow) {
        this.cashFlow = cashFlow;
    }

    public String getInvestmentRating() {
        return investmentRating;
    }

    public void setInvestmentRating(String investmentRating) {
        this.investmentRating = investmentRating;
    }
}
