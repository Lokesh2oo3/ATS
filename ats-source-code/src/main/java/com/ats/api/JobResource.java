package com.ats.api;

import com.ats.entity.Job;
import com.ats.repository.JobRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
@Path("/jobs")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class JobResource {

    @Inject
    private JobRepository jobRepository;

    @GET
    public Response getAllJobs() {
        List<Job> jobs = jobRepository.findAll();
        return Response.ok(jobs).build();
    }

    @GET
    @Path("/{id}")
    public Response getJobById(@PathParam("id") Long id) {
        Optional<Job> job = jobRepository.findById(id);
        if (job.isPresent()) {
            return Response.ok(job.get()).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("/status/{status}")
    public Response getJobsByStatus(@PathParam("status") String status) {
        try {
            Job.JobStatus jobStatus = Job.JobStatus.valueOf(status.toUpperCase());
            List<Job> jobs = jobRepository.findByStatus(jobStatus);
            return Response.ok(jobs).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid status: " + status)
                    .build();
        }
    }

    @POST
    public Response createJob(Job job) {
        if (job.getTitle() == null || job.getTitle().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Job title is required")
                    .build();
        }
        Job savedJob = jobRepository.save(job);
        return Response.status(Response.Status.CREATED).entity(savedJob).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateJob(@PathParam("id") Long id, Job jobDetails) {
        Optional<Job> existingJob = jobRepository.findById(id);
        if (existingJob.isPresent()) {
            Job job = existingJob.get();
            if (jobDetails.getTitle() != null) job.setTitle(jobDetails.getTitle());
            if (jobDetails.getDescription() != null) job.setDescription(jobDetails.getDescription());
            if (jobDetails.getDepartment() != null) job.setDepartment(jobDetails.getDepartment());
            if (jobDetails.getLocation() != null) job.setLocation(jobDetails.getLocation());
            if (jobDetails.getStatus() != null) job.setStatus(jobDetails.getStatus());
            Job updatedJob = jobRepository.save(job);
            return Response.ok(updatedJob).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteJob(@PathParam("id") Long id) {
        Optional<Job> job = jobRepository.findById(id);
        if (job.isPresent()) {
            jobRepository.delete(id);
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
