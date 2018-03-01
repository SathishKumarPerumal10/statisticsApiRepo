package com.n26.statistics.validator;

import com.n26.statistics.exception.NoContentException;
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
    public void givenTransactionsRequests_WhenValidateRequest_ThenDontThrowException() throws NoContentException {
        transactionRequestValidator.validateRequest(transactionsStatisticsLoadData.getValidTransactionRequestDto());

    }

    @Test(expected = NoContentException.class)
    public void givenTransactionsRequestsTxnWithExpiredTimestamp_WhenValidateRequest_ThenThrowException() throws NoContentException {
        transactionRequestValidator.validateRequest(transactionsStatisticsLoadData.getInValidTransactionRequestDtoWithExpiredTimestamp());
    }

    @Test(expected = NoContentException.class)
    public void givenTransactionsRequestsTxnWithNoAmount_WhenValidateRequest_ThenThrowException() throws NoContentException {
        transactionRequestValidator.validateRequest(transactionsStatisticsLoadData.getInValidTransactionRequestDtoWithNoAmount());
    }

}
