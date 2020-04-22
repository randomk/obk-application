package com.banking;

import com.banking.account.exception.AccountNotFoundExceptionMapper;
import com.banking.account.repository.AccountRepository;
import com.banking.account.resource.AccountResource;
import com.banking.account.service.AccountService;
import com.banking.payment.exception.UnableToAcceptPaymentOrderExceptionMapper;
import com.banking.payment.repository.AbstractPaymentOrder;
import com.banking.payment.repository.PaymentOrderProcessor;
import com.banking.payment.repository.PaymentOrderQueue;
import com.banking.payment.repository.PaymentRepository;
import com.banking.payment.resource.PaymentResource;
import com.banking.payment.service.PaymentService;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJsonProvider;
import org.glassfish.jersey.server.ResourceConfig;

import java.net.URI;
import java.util.concurrent.ArrayBlockingQueue;


public class Application {

    public static final String BASE_URI = "http://localhost:8090/";

    public static void main(String[] args) {
        startServer();
    }

    public static HttpServer startServer() {

        PaymentOrderQueue paymentOrderQueue=new PaymentOrderQueue(new ArrayBlockingQueue<AbstractPaymentOrder>(10000,true));
        AccountRepository accountRepository = new AccountRepository();
        PaymentRepository paymentRepository = new PaymentRepository(paymentOrderQueue);
        AccountService accountService = new AccountService(accountRepository);
        PaymentService paymentService = new PaymentService(paymentRepository, accountService);
        AccountResource accountResource = new AccountResource(accountService);
        PaymentResource paymentResource = new PaymentResource(paymentService);
        PaymentOrderProcessor paymentOrderProcessor = new PaymentOrderProcessor(paymentService,paymentOrderQueue);
        AccountNotFoundExceptionMapper accountNotFoundExceptionMapper = new AccountNotFoundExceptionMapper();
        UnableToAcceptPaymentOrderExceptionMapper unableToAcceptPaymentOrderExceptionMapper = new UnableToAcceptPaymentOrderExceptionMapper();


        ResourceConfig config = new ResourceConfig()
                .register(accountResource)
                .register(paymentResource)
                .register(accountRepository)
                .register(paymentRepository)
                .register(accountService)
                .register(paymentService)
                .register(accountNotFoundExceptionMapper)
                .register(JacksonJsonProvider.class)
                .register(unableToAcceptPaymentOrderExceptionMapper)
                .register(paymentOrderProcessor)
                .register(paymentOrderQueue);

        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), config);
    }
}
