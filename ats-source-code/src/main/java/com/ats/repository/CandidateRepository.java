package com.ats.repository;

import com.ats.entity.Candidate;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class CandidateRepository {

    @PersistenceContext(unitName = "atsDB")
    private EntityManager em;

    @Transactional
    public Candidate save(Candidate candidate) {
        if (candidate.getId() == null) {
            em.persist(candidate);
            return candidate;
        } else {
            return em.merge(candidate);
        }
    }

    public Optional<Candidate> findById(Long id) {
        return Optional.ofNullable(em.find(Candidate.class, id));
    }

    public List<Candidate> findAll() {
        return em.createQuery("SELECT c FROM Candidate c ORDER BY c.createdAt DESC", Candidate.class)
                .getResultList();
    }

    public Optional<Candidate> findByEmail(String email) {
        try {
            Candidate candidate = em.createQuery("SELECT c FROM Candidate c WHERE c.email = :email", Candidate.class)
                    .setParameter("email", email)
                    .getSingleResult();
            return Optional.of(candidate);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Transactional
    public void delete(Long id) {
        Candidate candidate = em.find(Candidate.class, id);
        if (candidate != null) {
            em.remove(candidate);
        }
    }
}
