package com.example.backend.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.time.Duration;

@Component
public class EveApiClient {
    private final WebClient webClient;
    
    @Value("${eve.esi.user-agent}")
    private String userAgent;

    public EveApiClient(@Value("${eve.esi.base-url}") String baseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("User-Agent", userAgent)
                .defaultHeader("Accept", "application/json")
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(12*1024*1024))
                .build();
    }

    // Получить список всех NPC корпораций
    public Mono<long[]> getNpcCorporations() {
        return webClient.get()
                .uri("/v1/corporations/npccorps/")
                .retrieve()
                .bodyToMono(long[].class)
                .timeout(Duration.ofSeconds(10));
    }

    // Получить информацию о корпорации по ID
    public Mono<CorporationInfo> getCorporationInfo(long corporationId) {
        return webClient.get()
                .uri("/v4/corporations/{corporation_id}/", corporationId)
                .retrieve()
                .bodyToMono(CorporationInfo.class)
                .timeout(Duration.ofSeconds(10));
    }

    // Получить цены на все предметы
    public Mono<MarketPrice[]> getMarketPrices() {
        return webClient.get()
                .uri("/v1/markets/prices/")
                .retrieve()
                .bodyToMono(MarketPrice[].class)
                .timeout(Duration.ofSeconds(15));
    }

    // DTO для информации о корпорации
    public record CorporationInfo(
        String name,
        String ticker,
        Long ceo_id,
        Long creator_id,
        int member_count,
        Float tax_rate   // вместо float
    ) {}

    // DTO для рыночных цен
    public record MarketPrice(
        Long type_id,
        Double adjusted_price,
        Double average_price
    ) {}
}