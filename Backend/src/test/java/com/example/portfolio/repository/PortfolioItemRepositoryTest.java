package com.example.portfolio.repository;

import com.example.portfolio.beans.AssetType;
import com.example.portfolio.beans.PortfolioItem;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PortfolioItemRepositoryTest {

    @Test
    void returnsPortfolioItemWhenFound() {
        PortfolioItemRepository repository = Mockito.mock(PortfolioItemRepository.class);
        PortfolioItem item = Mockito.mock(PortfolioItem.class);
        AssetType assetType = Mockito.mock(AssetType.class);

        Mockito.when(repository.findBySymbolAndAssetType("SYM", assetType))
                .thenReturn(Optional.of(item));

        Optional<PortfolioItem> result = repository.findBySymbolAndAssetType("SYM", assetType);

        assertTrue(result.isPresent());
        assertSame(item, result.get());
        Mockito.verify(repository).findBySymbolAndAssetType("SYM", assetType);
    }

    @Test
    void returnsEmptyWhenSymbolNotFound() {
        PortfolioItemRepository repository = Mockito.mock(PortfolioItemRepository.class);
        AssetType assetType = Mockito.mock(AssetType.class);

        Mockito.when(repository.findBySymbolAndAssetType("UNKNOWN", assetType))
                .thenReturn(Optional.empty());

        Optional<PortfolioItem> result = repository.findBySymbolAndAssetType("UNKNOWN", assetType);

        assertFalse(result.isPresent());
        Mockito.verify(repository).findBySymbolAndAssetType("UNKNOWN", assetType);
    }

    @Test
    void handlesNullSymbolGracefully() {
        PortfolioItemRepository repository = Mockito.mock(PortfolioItemRepository.class);
        AssetType assetType = Mockito.mock(AssetType.class);

        Mockito.when(repository.findBySymbolAndAssetType(null, assetType))
                .thenReturn(Optional.empty());

        Optional<PortfolioItem> result = repository.findBySymbolAndAssetType(null, assetType);

        assertFalse(result.isPresent());
        Mockito.verify(repository).findBySymbolAndAssetType(null, assetType);
    }

    @Test
    void distinguishesDifferentAssetTypesForSameSymbol() {
        PortfolioItemRepository repository = Mockito.mock(PortfolioItemRepository.class);
        PortfolioItem itemForTypeA = Mockito.mock(PortfolioItem.class);
        AssetType typeA = Mockito.mock(AssetType.class);
        AssetType typeB = Mockito.mock(AssetType.class);

        Mockito.when(repository.findBySymbolAndAssetType("DUP", typeA))
                .thenReturn(Optional.of(itemForTypeA));
        Mockito.when(repository.findBySymbolAndAssetType("DUP", typeB))
                .thenReturn(Optional.empty());

        Optional<PortfolioItem> resultA = repository.findBySymbolAndAssetType("DUP", typeA);
        Optional<PortfolioItem> resultB = repository.findBySymbolAndAssetType("DUP", typeB);

        assertTrue(resultA.isPresent());
        assertSame(itemForTypeA, resultA.get());
        assertFalse(resultB.isPresent());
        Mockito.verify(repository).findBySymbolAndAssetType("DUP", typeA);
        Mockito.verify(repository).findBySymbolAndAssetType("DUP", typeB);
    }

    @Test
    void searchIsCaseSensitiveForSymbol() {
        PortfolioItemRepository repository = Mockito.mock(PortfolioItemRepository.class);
        PortfolioItem upperItem = Mockito.mock(PortfolioItem.class);
        AssetType assetType = Mockito.mock(AssetType.class);

        Mockito.when(repository.findBySymbolAndAssetType("AAPL", assetType))
                .thenReturn(Optional.of(upperItem));
        Mockito.when(repository.findBySymbolAndAssetType("aapl", assetType))
                .thenReturn(Optional.empty());

        Optional<PortfolioItem> resultUpper = repository.findBySymbolAndAssetType("AAPL", assetType);
        Optional<PortfolioItem> resultLower = repository.findBySymbolAndAssetType("aapl", assetType);

        assertTrue(resultUpper.isPresent());
        assertSame(upperItem, resultUpper.get());
        assertFalse(resultLower.isPresent());
        Mockito.verify(repository).findBySymbolAndAssetType("AAPL", assetType);
        Mockito.verify(repository).findBySymbolAndAssetType("aapl", assetType);
    }

    @Test
    void invocationCountIsExactlyOnePerCall() {
        PortfolioItemRepository repository = Mockito.mock(PortfolioItemRepository.class);
        AssetType assetType = Mockito.mock(AssetType.class);

        Mockito.when(repository.findBySymbolAndAssetType("ONE", assetType))
                .thenReturn(Optional.empty());

        repository.findBySymbolAndAssetType("ONE", assetType);
        Mockito.verify(repository, Mockito.times(1)).findBySymbolAndAssetType("ONE", assetType);
    }
}
