package com.banking.payment.service;

import com.banking.account.service.AccountService;
import com.banking.payment.repository.AbstractPaymentOrder;
import com.banking.payment.repository.DepositPaymentOrder;
import com.banking.payment.repository.FundTransferPaymentOrder;
import com.banking.payment.repository.PaymentRepository;
import com.banking.payment.repository.PaymentStatus;

import java.math.BigDecimal;
import java.util.UUID;

public class PaymentService {

  private final PaymentRepository paymentRepository;

  private final AccountService accountService;

  public PaymentService(PaymentRepository paymentRepository, AccountService accountService) {

    this.paymentRepository = paymentRepository;
    this.accountService = accountService;
  }

  public UUID acceptFundTransferPaymentOrder(FundTransferPaymentOrder paymentOrder) {
      return paymentRepository.acceptFundTransferPaymentOrder(paymentOrder);
  }

  public UUID acceptDepositPaymentOrder(DepositPaymentOrder depositPaymentOrder) {
    return paymentRepository.acceptDepositPaymentOrder(depositPaymentOrder);
  }

  public void processPaymentOrder(AbstractPaymentOrder paymentOrder) {

    if (paymentOrder instanceof FundTransferPaymentOrder) {

      FundTransferPaymentOrder fundTransferPaymentOrder = (FundTransferPaymentOrder) paymentOrder;

      if (payeeAccountIsValid(fundTransferPaymentOrder)
          && beneficiaryAccountIsValid(fundTransferPaymentOrder)
          && payeeAccountHasSufficientBalanceFor(fundTransferPaymentOrder)
          && paymentAmountIsValid(fundTransferPaymentOrder)
          && payeeAndBeneficiaryAccountsAreNotSame(fundTransferPaymentOrder)) {
        accountService.debitToAccountBalance(
            fundTransferPaymentOrder.getBeneficiaryAccountId(),
            fundTransferPaymentOrder.getAmount());
        accountService.creditToAccountBalance(
            fundTransferPaymentOrder.getPayeeAccountId(), fundTransferPaymentOrder.getAmount());
        paymentRepository.processPaymentOrder(
            fundTransferPaymentOrder, "Processed successfully", PaymentStatus.SUCCESS);
      }

    } else if (paymentOrder instanceof DepositPaymentOrder) {

      DepositPaymentOrder depositPaymentOrder = (DepositPaymentOrder) paymentOrder;

      if (beneficiaryAccountIsValid(depositPaymentOrder)
              && paymentAmountIsValid(depositPaymentOrder)) {
        accountService.creditToAccountBalance(
                depositPaymentOrder.getBeneficiaryAccountId(), depositPaymentOrder.getAmount());
        paymentRepository.processPaymentOrder(
                depositPaymentOrder, "Processed successfully", PaymentStatus.SUCCESS);
      }
    }
  }

  private boolean paymentAmountIsValid(AbstractPaymentOrder paymentOrder) {

    if (paymentOrder.getAmount().compareTo(BigDecimal.ZERO) < 0) {
      paymentRepository.processPaymentOrder(
              paymentOrder,
          "Payment Order Amount must be Positive and Greater than Zero",
          PaymentStatus.REJECTED);
      return false;
    }
    return true;
  }

  private boolean payeeAccountHasSufficientBalanceFor(FundTransferPaymentOrder fundTransferPaymentOrder) {
    if (accountService
            .getAccountBalance(fundTransferPaymentOrder.getPayeeAccountId())
            .compareTo(fundTransferPaymentOrder.getAmount())
        < 0) {
      paymentRepository.processPaymentOrder(
              fundTransferPaymentOrder, "Payment Order Declined, Insufficient Balance", PaymentStatus.REJECTED);
      return false;
    }
    return true;
  }

  private boolean payeeAccountIsValid(FundTransferPaymentOrder fundTransferPaymentOrder) {
    if (!accountService.isAccountPresent(fundTransferPaymentOrder.getPayeeAccountId())) {
      paymentRepository.processPaymentOrder(
              fundTransferPaymentOrder, "Payee account is Invalid", PaymentStatus.REJECTED);
      return false;
    }
    return true;
  }

  private boolean beneficiaryAccountIsValid(AbstractPaymentOrder paymentOrder) {

    if (!accountService.isAccountPresent(paymentOrder.getBeneficiaryAccountId())) {
      paymentRepository.processPaymentOrder(
              paymentOrder, "Beneficiary account is Invalid", PaymentStatus.REJECTED);
      return false;
    }
    return true;
  }

  private boolean payeeAndBeneficiaryAccountsAreNotSame(FundTransferPaymentOrder fundTransferPaymentOrder) {
    if (fundTransferPaymentOrder.getPayeeAccountId().equals(fundTransferPaymentOrder.getBeneficiaryAccountId())) {
      paymentRepository.processPaymentOrder(
              fundTransferPaymentOrder, "Beneficiary and Payee accounts cannot be same", PaymentStatus.REJECTED);
      return false;
    }
    return true;
  }

    public AbstractPaymentOrder getPayment(UUID paymentId) {
    return paymentRepository.getPayment(paymentId);
  }

}
