package com.n26.statistics.domain.repository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import com.n26.statistics.model.TransactionRequestDto;
import com.n26.statistics.model.TransactionStatisticsResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

@Repository
public class TransactionRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionRepository.class);
    static TransactionStatisticsResponseDto transactionStatisticsResponseDto = new TransactionStatisticsResponseDto(0.0, 0.0, 0.0, 0.0, 0L);

    private List<TransactionRequestDto> transactions = new CopyOnWriteArrayList<>();

    /**
     * (SP) This scheduler will kick of every one second and this will be responsible to clear the expired transactions from the list
     * and update the response object with the latest statistics
     */
    @Scheduled(fixedRate = 1000)
    public void scheduleStatisticUpdate() {
        LOGGER.trace("Starting Scheduler Job");
        deleteExpiredTransactions();
        updateTransactionStatistics();
        LOGGER.trace("Scheduler Job Completed");
    }

    /**
     * (SP) This method will create the transaction.
     */
    public void createTransaction(TransactionRequestDto transactionRequestDto) {
        LOGGER.trace("Creating Transaction for request : {}", transactionRequestDto);
        transactions.add(transactionRequestDto);
        LOGGER.trace("Transaction Created : {}", transactionRequestDto);
    }

    /**
     * (SP) This method will return the statistics.
     */
    public TransactionStatisticsResponseDto getTransactionStatistics() {
        return transactionStatisticsResponseDto;
    }

    /**
     * (SP) This method will be responsible for updating the transaction statistics.
     */
    private void updateTransactionStatistics() {
        LOGGER.trace("Updating transaction statistics");
        List<Double> transactionStatistics = transactions.stream()
                .filter(transactionRequestDto -> transactionRequestDto.getTimestamp() > getValidTime()).map(TransactionRequestDto::getAmount)
                .collect(Collectors.toList());

        LOGGER.debug("Number of Valid Transaction statistics : {}", transactionStatistics.size());
        DoubleSummaryStatistics statistics = transactionStatistics.stream().collect(Collectors.summarizingDouble(Double::doubleValue));
        transactionStatisticsResponseDto.setSum(statistics.getSum());
        transactionStatisticsResponseDto.setCount(statistics.getCount());
        transactionStatisticsResponseDto.setAvg(statistics.getAverage());

        //(SP) The extra check is required because DoubleSummaryStatistics will return default POSITIVE_INFINITY and NEGATIVE_INFINITY
        //   in case no value is present.

        transactionStatisticsResponseDto.setMax(statistics.getMax() != Double.NEGATIVE_INFINITY
                                                ? statistics.getMax()
                                                : 0.0);
        transactionStatisticsResponseDto.setMin(statistics.getMin() != Double.POSITIVE_INFINITY
                                                ? statistics.getMin()
                                                : 0.0);

        LOGGER.trace("Transaction Statistics Updated");

    }

    /**
     * (SP) This method will delete all the expired transactions.
     */
    private void deleteExpiredTransactions() {
        LOGGER.trace("Deleting Expired transactions : Number of Transactions Before Deletion : {}", transactions.size());
        transactions.removeIf(transaction -> transaction.getTimestamp() < getValidTime());
        LOGGER.trace("Expired transactions deleted : Number of Transactions After Deletion : {}", transactions.size());
    }

    /**
     * (SP) This method will return the Valid time, In our case the valid time is the last 60 seconds.
     */
    private Long getValidTime() {
        return LocalDateTime.now().minusSeconds(60).toInstant(ZoneOffset.UTC).toEpochMilli();
    }

}
