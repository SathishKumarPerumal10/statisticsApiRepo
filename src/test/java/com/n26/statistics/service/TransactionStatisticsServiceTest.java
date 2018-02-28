package com.n26.statistics.service;

import static org.junit.Assert.*;

import com.n26.statistics.model.TransactionRequestDto;
import com.n26.statistics.model.TransactionStatisticsResponseDto;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class TransactionStatisticsServiceTest {

    // class under test
    private TransactionStatisticsService transactionStatisticsService;

    private TransactionsStatisticsLoadData transactionsStatisticsLoadData;

    @Before
    public void setUp() {
        transactionStatisticsService = new TransactionStatisticsService();
        transactionsStatisticsLoadData = new TransactionsStatisticsLoadData();
    }

    @Test
    public void givenValidList_WhenGetSum_ThenReturnTheTotalSum() {
        Double actualSum = transactionStatisticsService.getSum(transactionsStatisticsLoadData.getValidList());
        assertNotNull(actualSum);
        assertEquals(120L, actualSum.longValue());
    }

    @Test
    public void givenEmptyList_WhenGetSum_ThenReturnZero() {
        Double actualSum = transactionStatisticsService.getSum(transactionsStatisticsLoadData.getEmptyList());
        assertNotNull(actualSum);
        assertEquals(0L, actualSum.longValue());
    }

    @Test
    public void givenValidList_WhenGetMax_ThenReturnTheMaxValueOfTheList() {
        Double actualMaxValue = transactionStatisticsService.getMax(transactionsStatisticsLoadData.getValidList());
        assertNotNull(actualMaxValue);
        assertEquals(50L, actualMaxValue.longValue());
    }

    @Test
    public void givenEmptyList_WhenGetMax_ThenReturnZero() {
        Double actualMaxValue = transactionStatisticsService.getMax(transactionsStatisticsLoadData.getEmptyList());
        assertNotNull(actualMaxValue);
        assertEquals(0L, actualMaxValue.longValue());
    }

    @Test
    public void givenValidList_WhenGetMin_ThenReturnTheMaxValueOfTheList() {
        Double actualMinValue = transactionStatisticsService.getMin(transactionsStatisticsLoadData.getValidList());
        assertNotNull(actualMinValue);
        assertEquals(10L, actualMinValue.longValue());
    }

    @Test
    public void givenEmptyList_WhenGetMin_ThenReturnZero() {
        Double actualMinValue = transactionStatisticsService.getMin(transactionsStatisticsLoadData.getEmptyList());
        assertNotNull(actualMinValue);
        assertEquals(0L, actualMinValue.longValue());
    }

    @Test
    public void givenValidList_WhenGetAvg_ThenReturnTheAvgValueOfTheList() {
        Double actualAvgValue = transactionStatisticsService.getAvg(transactionsStatisticsLoadData.getValidList());
        assertNotNull(actualAvgValue);
        assertEquals(30L, actualAvgValue.longValue());
    }

    @Test
    public void givenEmptyList_WhenGetAvg_ThenReturnZero() {
        Double actualAvgValue = transactionStatisticsService.getAvg(transactionsStatisticsLoadData.getEmptyList());
        assertNotNull(actualAvgValue);
        assertEquals(0L, actualAvgValue.longValue());
    }

    @Test
    public void givenValidList_WhenGetCount_ThenReturnTheCountOfTheList() {
        Long actualCountValue = transactionStatisticsService.getCount(transactionsStatisticsLoadData.getValidList());
        assertNotNull(actualCountValue);
        assertEquals(4L, actualCountValue.longValue());
    }

    @Test
    public void givenEmptyList_WhenGetCount_ThenReturnZero() {
        Long actualCountValue = transactionStatisticsService.getCount(transactionsStatisticsLoadData.getEmptyList());
        assertNotNull(actualCountValue);
        assertEquals(0L, actualCountValue.longValue());
    }

    @Test
    public void givenTransactionsRequests_WhenValidateRequest_ThenReturnValidResults() {

        assertTrue(transactionStatisticsService.validateRequest(transactionsStatisticsLoadData.getValidTransactionRequestDto()));
        assertFalse(
                transactionStatisticsService.validateRequest(transactionsStatisticsLoadData.getInValidTransactionRequestDtoWithExpiredTimestamp()));
        assertFalse(transactionStatisticsService.validateRequest(transactionsStatisticsLoadData.getInValidTransactionRequestDtoWithNoAmount()));

    }

    @Test
    public void givenValidTransactionRequest_WhenCreateTransactionStatistics_ThenCreateTransaction() {

        ResponseEntity<String> response = transactionStatisticsService
                .createTransactionStatistics(transactionsStatisticsLoadData.getValidTransactionRequestDto());

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void givenInValidTransactionRequestWithNoAmount_WhenCreateTransactionStatistics_ThenDontCreateTransactionAndReturnNoContent() {

        ResponseEntity<String> response = transactionStatisticsService
                .createTransactionStatistics(transactionsStatisticsLoadData.getInValidTransactionRequestDtoWithNoAmount());

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void givenInValidTransactionRequestWithExpiredTimestamp_WhenCreateTransactionStatistics_ThenDontCreateTransactionAndReturnNoContent() {

        ResponseEntity<String> response = transactionStatisticsService
                .createTransactionStatistics(transactionsStatisticsLoadData.getInValidTransactionRequestDtoWithExpiredTimestamp());

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void givenValidTransactions_WhenGetStatistics_ThenReturnStatistics() {

        createTransactions();
        TransactionStatisticsResponseDto transactionStatisticsResponse = transactionStatisticsService.getTransactionStatisticsForLastMin();

        assertNotNull(transactionStatisticsResponse);
        assertEquals(4L, transactionStatisticsResponse.getCount().longValue());
        assertEquals(10L, transactionStatisticsResponse.getMin().longValue());
        assertEquals(100L, transactionStatisticsResponse.getMax().longValue());
        assertEquals(45L, transactionStatisticsResponse.getAvg().longValue());
        assertEquals(180L, transactionStatisticsResponse.getSum().longValue());

    }

    @Test
    public void givenValidTransactions_WhenGetStatisticsAfter1min_ThenReturnStatisticsWithZero() {

        createTransactions();
        try {
            Thread.sleep(70000);
        } catch (InterruptedException ex) {

        }
        TransactionStatisticsResponseDto transactionStatisticsResponse = transactionStatisticsService.getTransactionStatisticsForLastMin();

        assertNotNull(transactionStatisticsResponse);
        assertNull(transactionStatisticsResponse.getCount());
        assertNull(transactionStatisticsResponse.getMin());
        assertNull(transactionStatisticsResponse.getMax());
        assertNull(transactionStatisticsResponse.getAvg());
        assertNull(transactionStatisticsResponse.getSum());

    }

    private void createTransactions() {

        transactionStatisticsService.createTransactionStatistics(new TransactionRequestDto(10.0, System.currentTimeMillis()));
        transactionStatisticsService.createTransactionStatistics(new TransactionRequestDto(20.0, System.currentTimeMillis()));
        transactionStatisticsService.createTransactionStatistics(new TransactionRequestDto(100.0, System.currentTimeMillis()));
        transactionStatisticsService.createTransactionStatistics(new TransactionRequestDto(50.0, System.currentTimeMillis()));
    }

}
