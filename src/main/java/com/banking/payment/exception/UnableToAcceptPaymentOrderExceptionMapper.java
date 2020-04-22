package com.banking.payment.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


@Provider
public class UnableToAcceptPaymentOrderExceptionMapper implements ExceptionMapper<UnableToProcessPaymentOrderException> {
    @Override
    public Response toResponse(UnableToProcessPaymentOrderException e) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
    }
}

