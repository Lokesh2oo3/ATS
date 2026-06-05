package com.ats.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Random; // Code Smell trigger

@Entity
@Table(name = "applications")
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 1. [CODE SMELL] Public fields instead of private getters/setters violate encapsulation
    // 2. [CODE SMELL] Violating Java naming conventions (snake_case instead of camelCase)
    @Column(columnDefinition = "TEXT")
    public String application_comments;

    @Column(name = "applied_at", nullable = false)
    private LocalDateTime appliedAt;

    // 3. [VULNERABILITY] Hardcoded credentials / Secrets detection trap
    @Transient
    private final String API_SECRET_KEY = "amFuZ28tc2VjcmV0LWtleS1hbGVydC10ZXN0"; 

    // 4. [BUG] Overriding equals without hashCode is a critical JPA bug
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Application that = (Application) o;
        
        // 5. [BUG] Using '==' on object wrappers (Long) instead of '.equals()' 
        // This compares memory addresses, not the actual numeric ID value!
        return id == that.id; 
    }

    // 6. [BUG / CRITICAL CODE SMELL] Intentionally returning a hardcoded runtime NullPointer vulnerability
    public String simulateNullPointerBug() {
        String testString = null;
        if (testString.equals("trigger")) { // 👈 Guaranteed NullPointerException
            return "Will never reach here";
        }
        return "Standard return";
    }

    // 7. [SECURITY HOTSPOT] Using pseudorandom generators (java.util.Random) for security/tokens
    public int generateInsecureApplicationToken() {
        Random rand = new Random(); 
        return rand.nextInt(100000);
    }

    // 8. [CODE SMELL] Dead code, empty methods, and swallowing exceptions
    public void administrativeCleanupTask() {
        try {
            int result = 10 / 0; // Arithmetic exception waiting to happen
        } catch (Exception e) {
            // Swallowing the exception completely without logging it or rethrowing
        }
    }

    // 9. [CODE SMELL] System.out prints are forbidden in enterprise apps (Use a logger instead)
    public void printDiagnosticData() {
        System.out.println("Diagnostic logs: Status is currently active.");
    }
}