package com.redhat.emergency.response.incident.finder.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Shelter {

    private String id;

    private String name;

    private BigDecimal lon;
    
    private BigDecimal lat;
    
    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public BigDecimal getLon() {
        return this.lon;
    }

    public BigDecimal getLat() {
        return this.lat;
    }

}