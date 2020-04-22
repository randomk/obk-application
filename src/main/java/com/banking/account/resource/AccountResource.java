package com.banking.account.resource;


import com.banking.account.service.AccountService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;


@Path("accounts")
@Produces(MediaType.APPLICATION_JSON)
public class AccountResource {


    private final AccountService accountService;

    public AccountResource(AccountService accountService) {
        this.accountService = accountService;
    }

    @POST
    @Path("/")
    public Response create() {
        return Response.status(Response.Status.CREATED).entity(accountService.createAccount()).build();
    }

    @GET
    @Path("/{id}/balance")
    public Response accountBalance(@PathParam("id") UUID accountId) {
        return Response.status(Response.Status.OK).entity(accountService.getAccountBalance(accountId)).build();

    }
}
