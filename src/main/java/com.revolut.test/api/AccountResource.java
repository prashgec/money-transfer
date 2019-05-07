package com.revolut.test.api;

import com.google.inject.Inject;
import com.revolut.test.domain.Account;
import com.revolut.test.domain.AccountService;
import com.revolut.test.domain.exception.CustomException;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;

@Path("/accounts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AccountResource {

    @Inject
    private AccountService service;



    @GET
    public Collection<Account> getAccounts()
    {
        return service.findAll();
    }

    @GET
    @Path("/{accountName}")
    public Account getAccount(@PathParam("accountName") String accountName)
    {
        return service.findById(accountName).orElse(null);
    }

    @POST
    public Response create(@Valid Account account)
    {
        try {
            return Response.accepted(service.createAccount(account)).build();
        } catch (CustomException e) {
            throw new WebApplicationException(e.getMessage(),e,Response.Status.CONFLICT);
        }
    }

    @PUT
    public Response update(@Valid Account account)
    {
        try {
            return Response.accepted(service.updateAccount(account)).build();
        } catch (CustomException e) {
            throw new WebApplicationException(e.getMessage(),e,Response.Status.NOT_FOUND);
        }
    }

    @DELETE
    @Path("/{accountName}")
    public Response deleteAccount(@PathParam("accountName") String accountName)
    {
        if(service.delete(accountName))
            return Response.ok().build();
        return Response.noContent().build();
    }

}
