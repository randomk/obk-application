package com.banking.payment.resource;

import com.banking.payment.repository.DepositPaymentOrder;
import com.banking.payment.repository.FundTransferPaymentOrder;
import com.banking.payment.service.PaymentService;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Path("payments")
@Produces(MediaType.APPLICATION_JSON)
public class PaymentResource {
    private final PaymentService paymentService;

    public PaymentResource(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @POST
    @Path("/transfer")
    public Response create(FundTransferPaymentOrder fundTransferPaymentOrder) {
        return Response.status(Response.Status.CREATED).entity(paymentService.acceptFundTransferPaymentOrder(fundTransferPaymentOrder)).build();
    }

    @POST
    @Path("/credit")
    public Response credit(DepositPaymentOrder depositPaymentOrder) {
        return Response.status(Response.Status.CREATED).entity(paymentService.acceptDepositPaymentOrder(depositPaymentOrder)).build();
    }

    @GET
    @Path("/{id}")
    public Response accountBalance(@PathParam("id") UUID paymentId) {
        return Response.status(Response.Status.OK).entity(paymentService.getPayment(paymentId)).build();
    }
}
