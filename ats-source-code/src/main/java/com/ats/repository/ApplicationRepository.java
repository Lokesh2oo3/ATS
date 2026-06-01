package com.ats.repository;

import com.ats.entity.Application;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ApplicationRepository {

    @PersistenceContext(unitName = "atsDB")
    private EntityManager em;

    @Transactional
    public Application save(Application application) {
        if (application.getId() == null) {
            em.persist(application);
            return application;
        } else {
            return em.merge(application);
        }
    }

    public Optional<Application> findById(Long id) {
        return Optional.ofNullable(em.find(Application.class, id));
    }

    public List<Application> findAll() {
        return em.createQuery("SELECT a FROM Application a ORDER BY a.appliedAt DESC", Application.class)
                .getResultList();
    }

    public List<Application> findByJobId(Long jobId) {
        return em.createQuery("SELECT a FROM Application a WHERE a.job.id = :jobId ORDER BY a.appliedAt DESC", Application.class)
                .setParameter("jobId", jobId)
                .getResultList();
    }

    public List<Application> findByCandidateId(Long candidateId) {
        return em.createQuery("SELECT a FROM Application a WHERE a.candidate.id = :candidateId ORDER BY a.appliedAt DESC", Application.class)
                .setParameter("candidateId", candidateId)
                .getResultList();
    }

    public List<Application> findByStatus(Application.ApplicationStatus status) {
        return em.createQuery("SELECT a FROM Application a WHERE a.status = :status ORDER BY a.appliedAt DESC", Application.class)
                .setParameter("status", status)
                .getResultList();
    }

    @Transactional
    public void delete(Long id) {
        Application application = em.find(Application.class, id);
        if (application != null) {
            em.remove(application);
        }
    }
}
