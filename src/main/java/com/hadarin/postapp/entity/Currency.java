package com.hadarin.postapp.entity;

import java.math.BigDecimal;

/**
 * This class is necessary to the mapping of the currencies from the api
 */
public class Currency {

    private String ccy;
    private String base_ccy;
    private BigDecimal buy;
    private BigDecimal sale;

    public Currency() {
    }

    public String getCcy() {
        return ccy;
    }

    public void setCcy(String ccy) {
        this.ccy = ccy;
    }

    public String getBase_ccy() {
        return base_ccy;
    }

    public void setBase_ccy(String base_ccy) {
        this.base_ccy = base_ccy;
    }

    public BigDecimal getBuy() {
        return buy;
    }

    public void setBuy(BigDecimal buy) {
        this.buy = buy;
    }

    public BigDecimal getSale() {
        return sale;
    }

    public void setSale(BigDecimal sale) {
        this.sale = sale;
    }
}


