package com.revolut.test.api;

import com.google.inject.Inject;
import com.revolut.test.domain.Transaction;
import com.revolut.test.domain.TransferRequest;
import com.revolut.test.domain.TransferService;
import com.revolut.test.domain.exception.CustomException;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;

@Path("/transactions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TransferResource {

    @Inject
    private TransferService service;


    @GET
    public Collection<Transaction> getTransactions()
    {
        return service.getAllTransactions();
    }

    @POST
    public Response transfer(@Valid TransferRequest transferRequest) {
        try {
            return  Response.ok(service.performTransfer(transferRequest)).build();
        } catch (CustomException e) {
            throw new WebApplicationException(e.getMessage(), e, Response.Status.BAD_REQUEST);
        }
    }
}
