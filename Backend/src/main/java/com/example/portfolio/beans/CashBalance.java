package com.example.portfolio.beans;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cash_balance")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CashBalance {

    @Id
    private Integer id;

    @Column(nullable = false)
    private double balance;
}
