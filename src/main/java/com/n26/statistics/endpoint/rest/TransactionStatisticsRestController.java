package com.n26.statistics.endpoint.rest;

import com.n26.statistics.model.TransactionRequestDto;
import com.n26.statistics.model.TransactionStatisticsResponseDto;
import com.n26.statistics.service.TransactionStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionStatisticsRestController {

    @Autowired
    private TransactionStatisticsService transactionStatisticsService;

    @RequestMapping(value = "/transactions", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> createTransactionStatistics(@RequestBody TransactionRequestDto transactionRequestDto) {
        return transactionStatisticsService.createTransactionStatistics(transactionRequestDto);
    }

    @RequestMapping("/statistics")
    @GetMapping
    public TransactionStatisticsResponseDto getTransactionStatistics() {
        return transactionStatisticsService.getTransactionStatisticsForLastMin();
    }

}
