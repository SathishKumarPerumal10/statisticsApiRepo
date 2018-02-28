package com.n26.statistics.service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.n26.statistics.model.TransactionRequestDto;
import com.n26.statistics.model.TransactionStatisticsResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TransactionStatisticsService {

    private static TransactionStatisticsResponseDto transactionStatisticsResponseDto = new TransactionStatisticsResponseDto();

    private Map<Integer, TransactionRequestDto> transactionsMap = new ConcurrentHashMap<>();

    private AtomicInteger counterKey = new AtomicInteger(0);

    /**
     * This method will validate the request and if it is valid, it creates the transaction and then updates the statistics
     */
    public ResponseEntity<String> createTransactionStatistics(TransactionRequestDto transactionRequestDto) {

        if (validateRequest(transactionRequestDto)) {
            transactionsMap.put(counterKey.getAndIncrement(), transactionRequestDto);
            updateTransactionStatistics();
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * This method will return the transaction statistics for the last minute
     */
    public TransactionStatisticsResponseDto getTransactionStatisticsForLastMin() {

        Long lastUpdatedTimestamp = transactionStatisticsResponseDto.getUpdatedTimestamp();
        return lastUpdatedTimestamp != null && getValidTime() < lastUpdatedTimestamp
               ? transactionStatisticsResponseDto
               : new TransactionStatisticsResponseDto();
    }

    Boolean validateRequest(TransactionRequestDto transactionRequestDto) {
        return (transactionRequestDto != null && transactionRequestDto.getAmount() != null && transactionRequestDto
                .getTimestamp() != null && transactionRequestDto.getTimestamp() > getValidTime());
    }

    private Long getValidTime() {
        return LocalDateTime.now().minusSeconds(60).toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    private void updateTransactionStatistics() {

        List<Double> transactionStatistics = transactionsMap.values().stream()
                .filter(transactionRequestDto -> transactionRequestDto.getTimestamp() > getValidTime()).map(TransactionRequestDto::getAmount)
                .collect(Collectors.toList());

        transactionStatisticsResponseDto.setSum(getSum(transactionStatistics));
        transactionStatisticsResponseDto.setCount(getCount(transactionStatistics));
        transactionStatisticsResponseDto.setAvg(getAvg(transactionStatistics));
        transactionStatisticsResponseDto.setMax(getMax(transactionStatistics));
        transactionStatisticsResponseDto.setMin(getMin(transactionStatistics));
        transactionStatisticsResponseDto.setUpdatedTimestamp(LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli());
    }

    Double getSum(List<Double> transactionStatistics) {
        return transactionStatistics.stream().mapToDouble(Number::doubleValue).sum();
    }

    Double getAvg(List<Double> transactionStatistics) {
        return transactionStatistics.stream().mapToDouble(Number::doubleValue).average().orElse(0L);
    }

    Double getMax(List<Double> transactionStatistics) {
        return transactionStatistics.stream().mapToDouble(Number::doubleValue).max().orElse(0L);
    }

    Double getMin(List<Double> transactionStatistics) {
        return transactionStatistics.stream().mapToDouble(Number::doubleValue).min().orElse(0L);
    }

    Long getCount(List<Double> transactionStatistics) {
        return Long.valueOf(transactionStatistics.size());
    }

}
