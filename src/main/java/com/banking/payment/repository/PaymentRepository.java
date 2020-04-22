package com.banking.payment.repository;

import com.banking.payment.exception.InvalidPaymentException;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class PaymentRepository {

  Logger logger = Logger.getLogger(PaymentRepository.class.getName());

    private final Map<UUID, AbstractPaymentOrder> paymentsContainer = new ConcurrentHashMap<UUID, AbstractPaymentOrder>();

  private final PaymentOrderQueue paymentOrderQueue;

  public PaymentRepository(PaymentOrderQueue paymentOrderQueue) {
    this.paymentOrderQueue = paymentOrderQueue;
  }

  public UUID acceptFundTransferPaymentOrder(FundTransferPaymentOrder paymentOrder) {
        final Payment payment = new Payment("Initiated", PaymentStatus.PENDING);
        paymentOrder.setPayment(payment);

        paymentsContainer.put(paymentOrder.getId(), paymentOrder);
        return paymentOrderQueue.putPaymentOrder(paymentOrder);
  }

  public UUID acceptDepositPaymentOrder(DepositPaymentOrder paymentOrder) {

        final Payment payment = new Payment("Initiated", PaymentStatus.PENDING);
        paymentOrder.setPayment(payment);
        paymentsContainer.put(paymentOrder.getId(), paymentOrder);
        return paymentOrderQueue.putPaymentOrder(paymentOrder);
  }

    public void processPaymentOrder(AbstractPaymentOrder paymentOrder, String message, PaymentStatus status) {
    logger.info(String.format("Processing Payment Order %s", paymentOrder));
        getPayment(paymentOrder.getId()).getPayment().updateState(message, status);
  }

    public AbstractPaymentOrder getPayment(UUID paymentId) {

    if (!paymentsContainer.containsKey(paymentId)) {
      throw new InvalidPaymentException(
          "No Payment found with this Payment Id " + paymentId.toString());
    }
    return paymentsContainer.get(paymentId);
  }
}
