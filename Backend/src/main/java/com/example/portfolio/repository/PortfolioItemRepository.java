package com.example.portfolio.repository;

import com.example.portfolio.beans.PortfolioItem;
import com.example.portfolio.beans.AssetType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PortfolioItemRepository
        extends JpaRepository<PortfolioItem, Integer> {

    Optional<PortfolioItem> findBySymbolAndAssetType(
            String symbol,
            AssetType assetType
    );
}
