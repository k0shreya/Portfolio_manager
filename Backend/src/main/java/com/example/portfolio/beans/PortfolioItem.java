package com.example.portfolio.beans;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "portfolio_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PortfolioItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private AssetType assetType;

    private String symbol;
    private int quantity;


    @Column(nullable = false)
    private double avgBuyPrice;
}
