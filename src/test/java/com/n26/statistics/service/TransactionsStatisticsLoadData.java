package com.n26.statistics.service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;

import static java.util.Collections.emptyList;

import com.n26.statistics.model.TransactionRequestDto;

public class TransactionsStatisticsLoadData {

    List<Double> getValidList() {
        return Arrays.asList(10.0, 20.0, 50.0, 40.0);
    }

    List<Double> getEmptyList() {
        return emptyList();
    }

    public TransactionRequestDto getValidTransactionRequestDto() {

        TransactionRequestDto transactionRequestDto = new TransactionRequestDto();
        transactionRequestDto.setAmount(100.0);
        transactionRequestDto.setTimestamp(LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli());

        return transactionRequestDto;
    }

    public TransactionRequestDto getInValidTransactionRequestDtoWithExpiredTimestamp() {

        TransactionRequestDto transactionRequestDto = new TransactionRequestDto();
        transactionRequestDto.setAmount(100.0);
        transactionRequestDto.setTimestamp(LocalDateTime.now().minusMinutes(5).toInstant(ZoneOffset.UTC).toEpochMilli());

        return transactionRequestDto;
    }

    public TransactionRequestDto getInValidTransactionRequestDtoWithNoAmount() {

        TransactionRequestDto transactionRequestDto = new TransactionRequestDto();
        transactionRequestDto.setTimestamp(LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli());

        return transactionRequestDto;
    }

}
