package com.example.backend.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "corporations")
public class Corporation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "eve_corporation_id", unique = true, nullable = false)
    private Long eveCorporationId;

    @Column(nullable = false)
    private String name;

    private String ticker;

    @Column(name = "member_count")
    private Integer memberCount;

    @Column(name = "ceo_id")
    private Long ceoId;

    @Column(name = "creator_id")
    private Long creatorId;

    @Column(name = "tax_rate")
    private Float taxRate;

    @Column(name = "last_updated")
    private Instant lastUpdated;

    @Column(name = "is_npc")
    private Boolean isNpc = true;

    // Конструкторы
    public Corporation() {}

    public Corporation(Long eveCorporationId, String name, String ticker, Integer memberCount) {
        this.eveCorporationId = eveCorporationId;
        this.name = name;
        this.ticker = ticker;
        this.memberCount = memberCount;
        this.lastUpdated = Instant.now();
    }

    // Геттеры и сеттеры (сгенерировать)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getEveCorporationId() { return eveCorporationId; }
    public void setEveCorporationId(Long eveCorporationId) { this.eveCorporationId = eveCorporationId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getTicker() { return ticker; }
    public void setTicker(String ticker) { this.ticker = ticker; }
    public Integer getMemberCount() { return memberCount; }
    public void setMemberCount(Integer memberCount) { this.memberCount = memberCount; }
    public Long getCeoId() { return ceoId; }
    public void setCeoId(Long ceoId) { this.ceoId = ceoId; }
    public Long getCreatorId() { return creatorId; }
    public void setCreatorId(Long creatorId) { this.creatorId = creatorId; }
    public Float getTaxRate() { return taxRate; }
    public void setTaxRate(Float taxRate) { this.taxRate = taxRate; }
    public Instant getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(Instant lastUpdated) { this.lastUpdated = lastUpdated; }
    public Boolean getIsNpc() { return isNpc; }
    public void setIsNpc(Boolean isNpc) { this.isNpc = isNpc; }
}