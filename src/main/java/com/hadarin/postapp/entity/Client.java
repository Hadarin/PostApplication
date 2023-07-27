package com.hadarin.postapp.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Domain Client class
 */
@Entity
@Table(name ="client")
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
@Setter
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_client")
    private Long idClient;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime birthdayDate;

    @Column(name = "phone")
    private String phone;

    @Column(name = "mail")
    private String mail;

    @Column(name = "address")
    private String address;

    @Column(name = "month_salary")
    private BigDecimal monthSalary;

    @Column(name = "curr_salary")
    private String salaryCurrency;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Transient
    private BigDecimal requestedLimit;

    @Column(name = "date_curr")
    private LocalDateTime dateCurr;

    @Column(name = "decision")
    private String decision;

    @Column(name = "limititog")
    private BigDecimal calculatedLimit;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "client")
    private List<Credit> credits;

}
