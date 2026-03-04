package com.example.backend.service;

import com.example.backend.client.EveApiClient;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class EveMarketService {

    @Autowired
    private EveApiClient eveApiClient;

    // Кешируем цены в памяти на 1 час
	@Cacheable(value = "marketPrices", unless = "#result == null")
	@RateLimiter(name = "esiRateLimiter")
	public Map<Long, Double> getAdjustedPrices() {
		EveApiClient.MarketPrice[] prices = eveApiClient.getMarketPrices().block();
		
		Map<Long, Double> priceMap = new ConcurrentHashMap<>();
		if (prices != null) {
			for (EveApiClient.MarketPrice price : prices) {
				Long typeId = price.type_id();
				// Проверка на null ключа
				if (typeId == null) {
					System.err.println("Skipping market price with null type_id");
					continue;
				}
				
				Double adjPrice = price.adjusted_price();
				Double avgPrice = price.average_price();
				
				double finalPrice = 0.0;
				if (adjPrice != null && adjPrice > 0) {
					finalPrice = adjPrice;
				} else if (avgPrice != null && avgPrice > 0) {
					finalPrice = avgPrice;
				} else {
					// Нет валидной цены – пропускаем
					continue;
				}
				
				priceMap.put(typeId, finalPrice);
			}
		}
		return priceMap;
	}

    // Получить цену конкретного предмета
    public Double getItemPrice(Long typeId) {
        return getAdjustedPrices().get(typeId);
    }

    // Рассчитать награду для миссии на основе рыночных цен
    public int calculateMissionReward(String missionType, int baseReward) {
        // Например, берём цену тританиума (type_id = 34) как базовый множитель
        Double tritaniumPrice = getItemPrice(34L);
        if (tritaniumPrice == null) tritaniumPrice = 5.0; // запасное значение
        
        // Множитель: текущая цена / историческая средняя (условно 6 ISK)
		Double multiplier = Math.max(2.0, tritaniumPrice / 6.0);
        
        return (int) (baseReward * multiplier);
    }
}