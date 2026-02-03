package com.example.portfolio.dto;

import com.example.portfolio.beans.AssetType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SellRequest {
    private AssetType assetType;
    private String symbol;
    private int quantity;
}
