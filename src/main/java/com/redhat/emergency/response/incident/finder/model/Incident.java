package com.redhat.emergency.response.incident.finder.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Incident {

    private String id;

    private String lat;

    private String lon;

    private Integer numberOfPeople;

    private Boolean medicalNeeded;

    private String victimName;

    private String victimPhoneNumber;

    private Long timestamp;

    private String status;

    private BigDecimal destinationLat;

    private BigDecimal destinationLon;

    private String destinationName;

    private BigDecimal currentPositionLat;

    private BigDecimal currentPositionLon;

    public String getId() {
        return id;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }

    public Integer getNumberOfPeople() {
        return numberOfPeople;
    }

    public Boolean isMedicalNeeded() {
        return medicalNeeded;
    }

    public String getVictimName() {
        return victimName;
    }

    public String getVictimPhoneNumber() {
        return victimPhoneNumber;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public String getStatus() {
        return status;
    }

    public BigDecimal getDestinationLat() {
        return destinationLat;
    }

    public void setDestinationLat(BigDecimal destinationLat) {
        this.destinationLat = destinationLat;
    }

    public BigDecimal getDestinationLon() {
        return destinationLon;
    }

    public void setDestinationLon(BigDecimal destinationLon) {
        this.destinationLon = destinationLon;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public BigDecimal getCurrentPositionLat() {
        return currentPositionLat;
    }

    public void setCurrentPositionLat(BigDecimal currentPositionLat) {
        this.currentPositionLat = currentPositionLat;
    }

    public BigDecimal getCurrentPositionLon() {
        return currentPositionLon;
    }

    public void setCurrentPositionLon(BigDecimal currentPositionLon) {
        this.currentPositionLon = currentPositionLon;
    }
}
