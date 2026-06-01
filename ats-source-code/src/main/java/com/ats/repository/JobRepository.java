package com.ats.repository;

import com.ats.entity.Job;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class JobRepository {

    @PersistenceContext(unitName = "atsDB")
    private EntityManager em;

    @Transactional
    public Job save(Job job) {
        if (job.getId() == null) {
            em.persist(job);
            return job;
        } else {
            return em.merge(job);
        }
    }

    public Optional<Job> findById(Long id) {
        return Optional.ofNullable(em.find(Job.class, id));
    }

    public List<Job> findAll() {
        return em.createQuery("SELECT j FROM Job j ORDER BY j.createdAt DESC", Job.class)
                .getResultList();
    }

    public List<Job> findByStatus(Job.JobStatus status) {
        return em.createQuery("SELECT j FROM Job j WHERE j.status = :status ORDER BY j.createdAt DESC", Job.class)
                .setParameter("status", status)
                .getResultList();
    }

    @Transactional
    public void delete(Long id) {
        Job job = em.find(Job.class, id);
        if (job != null) {
            em.remove(job);
        }
    }
}
