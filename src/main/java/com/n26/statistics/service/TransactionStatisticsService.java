package com.n26.statistics.service;

import com.n26.statistics.domain.repository.TransactionRepository;
import com.n26.statistics.exception.NoContentException;
import com.n26.statistics.model.TransactionRequestDto;
import com.n26.statistics.model.TransactionStatisticsResponseDto;
import com.n26.statistics.validator.TransactionRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionStatisticsService {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private TransactionRequestValidator transactionRequestValidator;

    /**
     * This method will validate the request and if it is valid, it creates the transaction and then updates the statistics
     */
    public void createTransactionStatistics(TransactionRequestDto transactionRequestDto) throws NoContentException {
        transactionRequestValidator.validateRequest(transactionRequestDto);
        transactionRepository.createTransaction(transactionRequestDto);
    }

    /**
     * This method will return the transaction statistics for the last minute. It does not do any iteration,
     * so that it executes in constant time and memory o(1)
     */
    public TransactionStatisticsResponseDto getTransactionStatisticsForLastMin() {
        return transactionRepository.getTransactionStatistics();
    }

}
