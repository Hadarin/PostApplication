package com.hadarin.postapp.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * Domain Credit class
 */
@Entity
@Table(name = "credit")
public class Credit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_credit")
    private Long idCredit;

    @Column(name = "amt_credit")
    private BigDecimal amtCredit;

    @Column(name = "date_start")
    private Date dateStart;

    @Column(name = "state_credit")
    private String stateCredit;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="id_client", nullable = false)
    private Client client;

    public Credit() {
    }

    public Credit(BigDecimal amtCredit, String stateCredit) {
        this.amtCredit = amtCredit;
        this.stateCredit = stateCredit;
    }

    public Credit(String stateCredit, BigDecimal amtCredit) {
        this.stateCredit = stateCredit;
        this.amtCredit = amtCredit;
    }

    public Credit(BigDecimal amtCredit) {
        this.amtCredit = amtCredit;
    }

    public BigDecimal getAmtCredit() {
        return amtCredit;
    }

    public void setAmtCredit(BigDecimal amtCredit) {
        this.amtCredit = amtCredit;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public String getStateCredit() {
        return stateCredit;
    }

    public void setStateCredit(String stateCredit) {
        this.stateCredit = stateCredit;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Long getIdCredit() {
        return idCredit;
    }

    public void setIdCredit(Long idCredit) {
        this.idCredit = idCredit;
    }
}
