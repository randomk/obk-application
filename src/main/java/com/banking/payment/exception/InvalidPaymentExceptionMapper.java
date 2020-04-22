package com.banking.payment.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class InvalidPaymentExceptionMapper implements ExceptionMapper<InvalidPaymentException> {
    @Override
    public Response toResponse(InvalidPaymentException e) {
        return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
    }
}