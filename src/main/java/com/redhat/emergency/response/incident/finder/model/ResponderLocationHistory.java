package com.redhat.emergency.response.incident.finder.model;

import java.math.BigDecimal;

public class ResponderLocationHistory {

    private BigDecimal lat;

    private BigDecimal lon;

    private long timestamp;

    public BigDecimal getLat() {
        return lat;
    }

    public BigDecimal getLon() {
        return lon;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
