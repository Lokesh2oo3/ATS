package com.ats.api;

import com.ats.entity.Application;
import com.ats.entity.Candidate;
import com.ats.entity.Job;
import com.ats.repository.ApplicationRepository;
import com.ats.repository.CandidateRepository;
import com.ats.repository.JobRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
@Path("/applications")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ApplicationResource {

    @Inject
    private ApplicationRepository applicationRepository;

    @Inject
    private CandidateRepository candidateRepository;

    @Inject
    private JobRepository jobRepository;

    @GET
    public Response getAllApplications() {
        List<Application> applications = applicationRepository.findAll();
        return Response.ok(applications).build();
    }

    @GET
    @Path("/{id}")
    public Response getApplicationById(@PathParam("id") Long id) {
        Optional<Application> application = applicationRepository.findById(id);
        if (application.isPresent()) {
            return Response.ok(application.get()).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("/job/{jobId}")
    public Response getApplicationsByJob(@PathParam("jobId") Long jobId) {
        List<Application> applications = applicationRepository.findByJobId(jobId);
        return Response.ok(applications).build();
    }

    @GET
    @Path("/candidate/{candidateId}")
    public Response getApplicationsByCandidate(@PathParam("candidateId") Long candidateId) {
        List<Application> applications = applicationRepository.findByCandidateId(candidateId);
        return Response.ok(applications).build();
    }

    @GET
    @Path("/status/{status}")
    public Response getApplicationsByStatus(@PathParam("status") String status) {
        try {
            Application.ApplicationStatus appStatus = Application.ApplicationStatus.valueOf(status.toUpperCase());
            List<Application> applications = applicationRepository.findByStatus(appStatus);
            return Response.ok(applications).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid status: " + status)
                    .build();
        }
    }

    @POST
    public Response createApplication(ApplicationDTO dto) {
        Optional<Candidate> candidate = candidateRepository.findById(dto.getCandidateId());
        Optional<Job> job = jobRepository.findById(dto.getJobId());

        if (!candidate.isPresent() || !job.isPresent()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid candidate or job ID")
                    .build();
        }

        Application application = new Application();
        application.setCandidate(candidate.get());
        application.setJob(job.get());
        application.setStatus(Application.ApplicationStatus.APPLIED);

        Application savedApplication = applicationRepository.save(application);
        return Response.status(Response.Status.CREATED).entity(savedApplication).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateApplication(@PathParam("id") Long id, Application applicationDetails) {
        Optional<Application> existingApplication = applicationRepository.findById(id);
        if (existingApplication.isPresent()) {
            Application application = existingApplication.get();
            if (applicationDetails.getStatus() != null) application.setStatus(applicationDetails.getStatus());
            if (applicationDetails.getComments() != null) application.setComments(applicationDetails.getComments());
            Application updatedApplication = applicationRepository.save(application);
            return Response.ok(updatedApplication).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteApplication(@PathParam("id") Long id) {
        Optional<Application> application = applicationRepository.findById(id);
        if (application.isPresent()) {
            applicationRepository.delete(id);
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    // DTO for creating applications
    public static class ApplicationDTO {
        private Long candidateId;
        private Long jobId;

        public Long getCandidateId() {
            return candidateId;
        }

        public void setCandidateId(Long candidateId) {
            this.candidateId = candidateId;
        }

        public Long getJobId() {
            return jobId;
        }

        public void setJobId(Long jobId) {
            this.jobId = jobId;
        }
    }
}
