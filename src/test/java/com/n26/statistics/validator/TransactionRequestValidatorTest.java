package com.n26.statistics.validator;

import com.n26.statistics.exception.InValidTransactionException;
import com.n26.statistics.service.TransactionsStatisticsLoadData;
import org.junit.Before;
import org.junit.Test;

public class TransactionRequestValidatorTest {

    private TransactionRequestValidator transactionRequestValidator;

    private TransactionsStatisticsLoadData transactionsStatisticsLoadData;

    @Before
    public void setUp() {
        transactionRequestValidator = new TransactionRequestValidator();
        transactionsStatisticsLoadData = new TransactionsStatisticsLoadData();
    }

    @Test
    public void givenTransactionsRequests_WhenValidateRequest_ThenDontThrowException() throws InValidTransactionException {
        transactionRequestValidator.validateRequest(transactionsStatisticsLoadData.getValidTransactionRequestDto());

    }

    @Test(expected = InValidTransactionException.class)
    public void givenTransactionsRequestsTxnWithExpiredTimestamp_WhenValidateRequest_ThenThrowException() throws InValidTransactionException {
        transactionRequestValidator.validateRequest(transactionsStatisticsLoadData.getInValidTransactionRequestDtoWithExpiredTimestamp());
    }

    @Test(expected = InValidTransactionException.class)
    public void givenTransactionsRequestsTxnWithNoAmount_WhenValidateRequest_ThenThrowException() throws InValidTransactionException {
        transactionRequestValidator.validateRequest(transactionsStatisticsLoadData.getInValidTransactionRequestDtoWithNoAmount());
    }

}
