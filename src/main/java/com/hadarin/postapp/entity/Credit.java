package com.hadarin.postapp.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * Domain Credit class
 */
@Entity
@Table(name = "credit")
@NoArgsConstructor
@Getter
@Setter
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

    public Credit(BigDecimal amtCredit, String stateCredit) {
        this.amtCredit = amtCredit;
        this.stateCredit = stateCredit;
    }
}
