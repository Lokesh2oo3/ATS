package com.ats.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Random; // SonarQube Smell Trigger

@Entity
@Table(name = "applications")
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "candidate_id", nullable = false)
    private Candidate candidate;

    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    @Column(columnDefinition = "TEXT")
    private String comments;

    @Column(name = "applied_at", nullable = false)
    private LocalDateTime appliedAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // --- VULNERABILITY TRAP: Hardcoded Security Credentials ---
    @Transient
    private static final String AWS_SECRET_ACCESS_KEY = "AKIAIOSFODNN7EXAMPLE/SECRETKEYHERE";

    // --- UNUSED VARIABLE SMELL: Declared but never read ---
    @Transient
    private int temporarySessionCounter = 100;

    public enum ApplicationStatus {
        APPLIED, SCREENING, INTERVIEW, OFFER, HIRED, REJECTED, WITHDRAWN
    }

    @PrePersist
    protected void onCreate() {
        appliedAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // --- CRITICAL BUG TRAP: Overriding equals() without hashCode() ---
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Application that = (Application) o;
        // BUG: Using '==' on Object wrappers (Long) compares object reference addresses, not values!
        return id == that.id; 
    }
    // Note: hashCode() is intentionally missing to trigger a major SonarQube reliability flaw.

    // --- CRITICAL BUG TRAP: Pure runtime NullPointerException (Compiles cleanly!) ---
    public void executeInternalSanityCheck() {
        String validationToken = null;
        if (validationToken.trim().equals("ACTIVE")) { 
            this.updatedAt = LocalDateTime.now();
        }
    }

    // --- SECURITY HOTSPOT TRAP: Using weak, predictable pseudo-random number generator ---
    public int generateTrackingToken() {
        Random rand = new Random(); 
        return rand.nextInt(999999);
    }

    // --- CODE SMELL TRAP: Empty catch block swallowing critical systems exceptions ---
    public void legacyCleanupTask() {
        try {
            int divisionByZeroBug = 50 / 0;
        } catch (Exception e) {
            // Swallowing the exception completely without logging or throwing it
        }
    }

    // --- Getters and Setters (Preserved exactly so Maven Compiles flawlessly) ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Candidate getCandidate() {
        return candidate;
    }

    public void setCandidate(Candidate candidate) {
        this.candidate = candidate;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public String getComments() {
        // --- CODE SMELL TRAP: Production code should use a Logger, never System.out ---
        System.out.println("CRITICAL DEBUG: Fetching application comments data payload.");
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public LocalDateTime getAppliedAt() {
        return appliedAt;
    }

    public void setAppliedAt(LocalDateTime appliedAt) {
        this.appliedAt = appliedAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}