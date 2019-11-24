package com.hadarin.postapp.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * Domain Client class
 */
@Entity
@Table(name ="client")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_client")
    private Long idClient;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Transient
    private Date dateBirthday;

    @Column(name = "phone")
    private String phone;

    @Column(name = "mail")
    private String mail;

    @Column(name = "address")
    private String address;

    @Column(name = "month_salary")
    private BigDecimal monthSalary;

    @Column(name = "curr_salary")
    private String currSalary;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Transient
    private BigDecimal requestLimit;

    @Column(name = "date_curr")
    private Date dateCurr;

    @Column(name = "decision")
    private String decision;

    @Column(name = "limititog")
    private BigDecimal limitITog;

//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "client")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "client")
    private List<Credit> credits;

    public Client() {
    }

    public Client(Long idClient) {
        this.idClient = idClient;
    }

    public Long getIdClient() {
        return idClient;
    }

    public void setIdClient(Long idClient) {
        this.idClient = idClient;
    }

    public Date getDateBirthday() {
        return dateBirthday;
    }

    public void setDateBirthday(Date dateBirthday) {
        this.dateBirthday = dateBirthday;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getMonthSalary() {
        return monthSalary;
    }

    public void setMonthSalary(BigDecimal monthSalary) {
        this.monthSalary = monthSalary;
    }

    public String getCurrSalary() {
        return currSalary;
    }

    public void setCurrSalary(String currSalary) {
        this.currSalary = currSalary;
    }

    public BigDecimal getRequestLimit() {
        return requestLimit;
    }

    public void setRequestLimit(BigDecimal requestLimit) {
        this.requestLimit = requestLimit;
    }

    public Date getDateCurr() {
        return dateCurr;
    }

    public void setDateCurr(Date dateCurr) {
        this.dateCurr = dateCurr;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public BigDecimal getLimitITog() {
        return limitITog;
    }

    public void setLimitITog(BigDecimal limitITog) {
        this.limitITog = limitITog;
    }

    public List<Credit> getCredits() {
        return credits;
    }

    public void setCredits(List<Credit> credits) {
        this.credits = credits;
    }
}
