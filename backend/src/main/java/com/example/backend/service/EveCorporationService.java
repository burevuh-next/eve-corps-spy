package com.example.backend.service;

import com.example.backend.client.EveApiClient;
import com.example.backend.entity.Corporation;
import com.example.backend.repository.CorporationRepository;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;



@Service
public class EveCorporationService {

    @Autowired
    private EveApiClient eveApiClient;

    @Autowired
    private CorporationRepository corporationRepository;

    // Получаем список всех NPC корпораций (с кешированием)
    @Cacheable(value = "npcCorporations", unless = "#result.isEmpty()")
    public List<Corporation> getNpcCorporations() {
        // Пробуем сначала из базы
        List<Corporation> fromDb = corporationRepository.findAll();
        if (!fromDb.isEmpty()) {
            return fromDb;
        }
        
        // Если база пуста, грузим из ESI
        return fetchAndSaveCorporations();
    }

    // Периодическое обновление корпораций (раз в день)
    @Scheduled(cron = "0 0 3 * * *") // каждый день в 3 часа ночи
    @Transactional
    @RateLimiter(name = "esiRateLimiter")
    public void updateCorporations() {
        fetchAndSaveCorporations();
    }

	private List<Corporation> fetchAndSaveCorporations() {
		System.out.println("Fetching NPC corporations from ESI...");
		try {
			long[] corpIds = eveApiClient.getNpcCorporations().block();
			System.out.println("Received " + (corpIds == null ? "null" : corpIds.length) + " corporation IDs");
			if (corpIds == null) return List.of();

			List<Corporation> saved = new ArrayList<>();
			for (long corpId : corpIds) {
				if (corporationRepository.findByEveCorporationId(corpId).isEmpty()) {
					System.out.println("Fetching details for corp ID: " + corpId);
					EveApiClient.CorporationInfo info = eveApiClient.getCorporationInfo(corpId).block();
					if (info != null) {
						Corporation corp = new Corporation(
							corpId,
							info.name(),
							info.ticker(),
							info.member_count()
						);
						corp.setCeoId(info.ceo_id());
						corp.setCreatorId(info.creator_id());
						corp.setTaxRate(info.tax_rate());
						saved.add(corporationRepository.save(corp));
						System.out.println("Saved corporation: " + info.name());
					}
				}
			}
			return saved;
		} catch (Exception e) {
			System.err.println("Failed to fetch corporations: " + e.getMessage());
			e.printStackTrace();
			return List.of();
		}
	}

    // Получить корпорацию по ID
    public Corporation getCorporation(Long eveCorporationId) {
        return corporationRepository.findByEveCorporationId(eveCorporationId)
                .orElseThrow(() -> new RuntimeException("Corporation not found"));
    }
}