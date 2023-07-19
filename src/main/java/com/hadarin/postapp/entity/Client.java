package com.hadarin.postapp.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * Domain Client class
 */
@Entity
@Table(name ="client")
@NoArgsConstructor
@Getter
@Setter
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

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "client")
    private List<Credit> credits;

}
