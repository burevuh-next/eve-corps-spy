package com.example.backend.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="agents")
public class Agent {
  @Id
  @GeneratedValue (strategy=GenerationType.IDENTITY)
  private Long id;

  @OneToOne
  @JoinColumn (name="user_id", referencedColumnName="id", unique=true, nullable=false)
  private User user;

  @Column(nullable = false)
  private String name; //Name agent
  
  @Column(nullable = false)
  private String corporation; //Corps

  @Column(nullable = false)
  private String specialization; //hacker, social, saboteur и т.д.
  private String bio; //biography
  
  @Column(nullable = false)
  private int suspicionLevel = 0;
  
  @Column(nullable = false)
  private int credits = 1000;

  @Column(nullable = false)
  private boolean active = true;

  @Column(nullable = false, updatable = false)
  private Instant createdAt = Instant.now();

  private Instant retiredAt;

  public Agent() {}

  public Agent(User user, String name, String corporation, String specialization, String bio) {
        this.user = user;
        this.name = name;
        this.corporation = corporation;
        this.specialization = specialization;
        this.bio = bio;
    }

    // Геттеры и сеттеры (сгенерировать или написать)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCorporation() { return corporation; }
    public void setCorporation(String corporation) { this.corporation = corporation; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public int getSuspicionLevel() { return suspicionLevel; }
    public void setSuspicionLevel(int suspicionLevel) { this.suspicionLevel = suspicionLevel; }

    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getRetiredAt() { return retiredAt; }
    public void setRetiredAt(Instant retiredAt) { this.retiredAt = retiredAt; }
}