package com.ats.api;

import com.ats.entity.Candidate;
import com.ats.repository.CandidateRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
@Path("/candidates")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CandidateResource {

    @Inject
    private CandidateRepository candidateRepository;

    @GET
    public Response getAllCandidates() {
        List<Candidate> candidates = candidateRepository.findAll();
        return Response.ok(candidates).build();
    }

    @GET
    @Path("/{id}")
    public Response getCandidateById(@PathParam("id") Long id) {
        Optional<Candidate> candidate = candidateRepository.findById(id);
        if (candidate.isPresent()) {
            return Response.ok(candidate.get()).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("/email/{email}")
    public Response getCandidateByEmail(@PathParam("email") String email) {
        Optional<Candidate> candidate = candidateRepository.findByEmail(email);
        if (candidate.isPresent()) {
            return Response.ok(candidate.get()).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    public Response createCandidate(Candidate candidate) {
        if (candidate.getFirstName() == null || candidate.getFirstName().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("First name is required")
                    .build();
        }
        if (candidate.getEmail() == null || candidate.getEmail().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Email is required")
                    .build();
        }
        Candidate savedCandidate = candidateRepository.save(candidate);
        return Response.status(Response.Status.CREATED).entity(savedCandidate).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateCandidate(@PathParam("id") Long id, Candidate candidateDetails) {
        Optional<Candidate> existingCandidate = candidateRepository.findById(id);
        if (existingCandidate.isPresent()) {
            Candidate candidate = existingCandidate.get();
            if (candidateDetails.getFirstName() != null) candidate.setFirstName(candidateDetails.getFirstName());
            if (candidateDetails.getLastName() != null) candidate.setLastName(candidateDetails.getLastName());
            if (candidateDetails.getPhone() != null) candidate.setPhone(candidateDetails.getPhone());
            if (candidateDetails.getResume() != null) candidate.setResume(candidateDetails.getResume());
            if (candidateDetails.getNotes() != null) candidate.setNotes(candidateDetails.getNotes());
            Candidate updatedCandidate = candidateRepository.save(candidate);
            return Response.ok(updatedCandidate).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCandidate(@PathParam("id") Long id) {
        Optional<Candidate> candidate = candidateRepository.findById(id);
        if (candidate.isPresent()) {
            candidateRepository.delete(id);
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
