package com.hadarin.postapp.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * This class is necessary to the mapping of the currencies from the api
 */
@NoArgsConstructor
@Getter
@Setter
public class Currency {

    private String ccy;
    private String base_ccy;
    private BigDecimal buy;
    private BigDecimal sale;

}


