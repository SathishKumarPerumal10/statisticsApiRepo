package com.n26.statistics.service;

import static org.mockito.Mockito.*;

import com.n26.statistics.domain.repository.TransactionRepository;
import com.n26.statistics.exception.InValidTransactionException;
import com.n26.statistics.model.TransactionRequestDto;
import com.n26.statistics.validator.TransactionRequestValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.*;

@RunWith(MockitoJUnitRunner.class)
public class TransactionStatisticsServiceTest {

    // class under test
    @InjectMocks
    private TransactionStatisticsService transactionStatisticsService;

    private TransactionsStatisticsLoadData transactionsStatisticsLoadData;

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private TransactionRequestValidator transactionRequestValidator;

    @Before
    public void setUp() {
        transactionsStatisticsLoadData = new TransactionsStatisticsLoadData();
    }

    @Test
    public void givenTransactionRequest_whenCreateTransaction_ThenCallValidatorAndRepositorySuccessfully() throws InValidTransactionException {

        TransactionRequestDto transactionRequestDto = transactionsStatisticsLoadData.getValidTransactionRequestDto();
        transactionStatisticsService.createTransactionStatistics(transactionRequestDto);

        // Verify Method calls
        verify(transactionRepository).createTransaction(transactionRequestDto);
        verify(transactionRequestValidator).validateRequest(transactionRequestDto);

    }

    @Test
    public void whenGetTransactionsforLastMin_ThenCallRepositorySuccessfully() {

        transactionStatisticsService.getTransactionStatisticsForLastMin();
        // verify calls
        verify(transactionRepository).getTransactionStatistics();

    }

}
