package com.n26.statistics.domain.repository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.junit.Assert.*;

import com.n26.statistics.model.TransactionRequestDto;
import com.n26.statistics.model.TransactionStatisticsResponseDto;
import org.junit.Before;
import org.junit.Test;

public class TransactionRepositoryTest {

    // class under test
    private TransactionRepository transactionRepository;

    @Before
    public void setUp() {

        transactionRepository = new TransactionRepository();
    }

    private void createTransactions() {
        transactionRepository.createTransaction(new TransactionRequestDto(20.0, System.currentTimeMillis()));
        transactionRepository
                .createTransaction(new TransactionRequestDto(10.0, LocalDateTime.now().minusSeconds(60).toInstant(ZoneOffset.UTC).toEpochMilli()));
        transactionRepository
                .createTransaction(new TransactionRequestDto(100.0, LocalDateTime.now().minusSeconds(50).toInstant(ZoneOffset.UTC).toEpochMilli()));
        transactionRepository.createTransaction(new TransactionRequestDto(30.0, System.currentTimeMillis()));

    }

    @Test
    public void whenScheduleStatisticIsCalled_thenDeleteExpiredAndUpdateTransactionsStats() {

        createTransactions();
        transactionRepository.scheduleStatisticUpdate();
        TransactionStatisticsResponseDto response = transactionRepository.getTransactionStatistics();

        assertNotNull(response);
        assertEquals(3L, response.getCount().longValue());
        assertEquals(20L, response.getMin().longValue());
        assertEquals(100L, response.getMax().longValue());
        assertEquals(50L, response.getAvg().longValue());
        assertEquals(150L, response.getSum().longValue());

        // Making the thread to sleep for 11 seconds to test if one record is cleared from the previous statistics
        try {
            Thread.sleep(11000);
        } catch (InterruptedException ex) {

        }
        transactionRepository.scheduleStatisticUpdate();
        TransactionStatisticsResponseDto responseAfter = transactionRepository.getTransactionStatistics();
        assertNotNull(response);
        assertEquals(2L, response.getCount().longValue());
        assertEquals(20L, response.getMin().longValue());
        assertEquals(30L, response.getMax().longValue());
        assertEquals(25L, response.getAvg().longValue());
        assertEquals(50L, response.getSum().longValue());

    }

}
