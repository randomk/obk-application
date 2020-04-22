package integration.com.banking;

import com.banking.Application;
import com.banking.payment.repository.PaymentStatus;
import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.UUID;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.OK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class Operations {

  private HttpServer server;
  private WebTarget target;

  @Before
  public void setUp() {
    server = Application.startServer();
    Client c = ClientBuilder.newClient();
    target = c.target(Application.BASE_URI);
  }

  @After
  public void tearDown() {
    server.shutdownNow();
  }

  @Test
  public void shouldReturnCreatedStatusOnAccountCreation() {
    UUID accountId = createAccount();
    assertNotNull(accountId);
  }

  @Test
  public void shouldReturnErrorMessageWhenAccountDoesNotExist() {

    String accountId = UUID.randomUUID().toString();
    Response response =
        target.path("accounts/").path(accountId).path("/balance").request().method("GET");

    assertEquals(BAD_REQUEST.getStatusCode(), response.getStatus());
    String accountNotFoundExceptionMessage = response.readEntity(String.class);
    assertEquals("Account " + accountId + " not found", accountNotFoundExceptionMessage);
  }

  @Test
  public void shouldReturnAccountBalanceWhenAccountExists() {

    UUID accountId = createAccount();

    Response response = getAccountBalance(accountId);

    assertEquals(OK.getStatusCode(), response.getStatus());
    assertEquals("Initial balance is Zero", "0", response.readEntity(String.class));
  }


  @Test
  public void shouldReturnErrorMessageWhenPaymentIsMadeAgainstInvalidPayeeAccount() {

    UUID beneficiaryAccountId = createAccount();

    Response response = createFundTransferPayment(UUID.randomUUID(), beneficiaryAccountId, 20);

    assertEquals(CREATED.getStatusCode(), response.getStatus());

    final UUID paymentOrderID = response.readEntity(UUID.class);

        final PaymentTransferOrderClientResponse payment = getPaymentInformation(paymentOrderID);

        assertEquals(PaymentStatus.REJECTED, payment.getPayment().getStatus());

        assertEquals("Initiated - Payee account is Invalid", payment.getPayment().getMessage());

  }

  @Test
  public void shouldReturnErrorMessageWhenPaymentIsMadeAgainstInvalidBeneficiaryAccount() {

    UUID payeeAccountId = createAccount();

    Response response = createFundTransferPayment(payeeAccountId, UUID.randomUUID(), 20);

    assertEquals(CREATED.getStatusCode(), response.getStatus());

    final UUID paymentId = response.readEntity(UUID.class);

        final PaymentTransferOrderClientResponse payment = getPaymentInformation(paymentId);

        assertEquals(PaymentStatus.REJECTED, payment.getPayment().getStatus());

        assertEquals("Initiated - Beneficiary account is Invalid", payment.getPayment().getMessage());
  }

  @Test
  public void
      shouldReturnErrorMessageWhenPaymentIsMadeAgainstPayeeAccountWithInsufficientBalance() {

    UUID payeeAccountId = createAccount();

    UUID beneficiaryAccountId = createAccount();

    Response response = createFundTransferPayment(payeeAccountId, beneficiaryAccountId, 20);

    assertEquals(CREATED.getStatusCode(), response.getStatus());

    final UUID paymentId = response.readEntity(UUID.class);

        final PaymentTransferOrderClientResponse payment = getPaymentInformation(paymentId);

        assertEquals(PaymentStatus.REJECTED, payment.getPayment().getStatus());

        assertEquals("Initiated - Payment Order Declined, Insufficient Balance", payment.getPayment().getMessage());
  }

  @Test
  public void shouldReturnErrorMessageWhenPaymentIsWithInvalidAmount() {

    UUID payeeAccountId = createAccount();

    UUID beneficiaryAccountId = createAccount();

    Response response = createFundTransferPayment(payeeAccountId, beneficiaryAccountId, -20);

    assertEquals(CREATED.getStatusCode(), response.getStatus());

    final UUID paymentId = response.readEntity(UUID.class);

        final PaymentTransferOrderClientResponse payment = getPaymentInformation(paymentId);

        assertEquals(PaymentStatus.REJECTED, payment.getPayment().getStatus());

        assertEquals(
            "Initiated - Payment Order Amount must be Positive and Greater than Zero",
            payment.getPayment().getMessage());
  }

  @Test
  public void shouldRespondWithProperBalanceWhenPaymentIsValid() {

    UUID payeeAccountId = createAccount();

    UUID beneficiaryAccountId = createAccount();

    final Response creditPaymentResponse = createCreditPayment(payeeAccountId, 20);

    final UUID creditPaymentId = creditPaymentResponse.readEntity(UUID.class);

    assertEquals(CREATED.getStatusCode(), creditPaymentResponse.getStatus());

        final PaymentTransferOrderClientResponse creditPayment = getPaymentInformation(creditPaymentId);

        assertEquals(PaymentStatus.SUCCESS, creditPayment.getPayment().getStatus());

    final Response accountBalanceResponse = getAccountBalance(payeeAccountId);

    final Integer integer = accountBalanceResponse.readEntity(Integer.class);

    assertTrue(integer > 0);

        Response fundTransferPaymentResponse = createFundTransferPayment(payeeAccountId, beneficiaryAccountId, 20);

    assertEquals(CREATED.getStatusCode(), fundTransferPaymentResponse.getStatus());

    final UUID fundTransferPaymentId = fundTransferPaymentResponse.readEntity(UUID.class);

        final PaymentTransferOrderClientResponse payment = getPaymentInformation(fundTransferPaymentId);

        assertEquals(PaymentStatus.SUCCESS, payment.getPayment().getStatus());

        assertEquals("Initiated - Processed successfully", payment.getPayment().getMessage());
  }

    @Test
    public void shouldRespondWithProperBalanceWhenPreviousPaymentsWereRejected() {

        Response creditPaymentResponse = createCreditPayment(UUID.randomUUID(), 20);

        UUID creditPaymentId = creditPaymentResponse.readEntity(UUID.class);

        assertEquals(CREATED.getStatusCode(), creditPaymentResponse.getStatus());

        PaymentTransferOrderClientResponse creditPayment = getPaymentInformation(creditPaymentId);

        assertEquals(PaymentStatus.REJECTED, creditPayment.getPayment().getStatus());

        UUID payeeAccountId = createAccount();

        creditPaymentResponse = createCreditPayment(payeeAccountId, 20);

        creditPaymentId = creditPaymentResponse.readEntity(UUID.class);

        creditPayment = getPaymentInformation(creditPaymentId);

        assertEquals(PaymentStatus.SUCCESS, creditPayment.getPayment().getStatus());

    }
  private UUID createAccount() {
    Response response = target.path("accounts/").request().method("POST");

    assertEquals(CREATED.getStatusCode(), response.getStatus());
    return response.readEntity(UUID.class);
  }

    private PaymentTransferOrderClientResponse getPaymentInformation(UUID paymentOrderID) {
    Response response;
    response = target.path("payments/").path(paymentOrderID.toString()).request().get();
    assertEquals(OK.getStatusCode(), response.getStatus());

        return response.readEntity(PaymentTransferOrderClientResponse.class);
  }

  private Response createFundTransferPayment(UUID payeeAccountId, UUID beneficiaryAccountId, int amount) {
    return target
        .path("payments/")
        .path("/transfer")
        .request()
        .post(
            Entity.json(
                "{\"payeeAccountId\":\""
                    + payeeAccountId.toString()
                    + "\",\"beneficiaryAccountId\":\""
                    + beneficiaryAccountId.toString()
                    + "\",\"amount\":"
                    + amount
                    + "}"));
  }

  private Response createCreditPayment(UUID beneficiaryAccountId, int amount) {
    return target
        .path("payments/")
        .path("/credit")
        .request()
        .post(
            Entity.json(
                "{\"beneficiaryAccountId\":\""
                    + beneficiaryAccountId.toString()
                    + "\",\"amount\":"
                    + amount
                    + "}"));
  }


  private Response getAccountBalance(UUID accountId) {
    return target
            .path("accounts/")
            .path(accountId.toString())
            .path("/balance")
            .request()
            .method("GET");
  }
}
