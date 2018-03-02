package com.n26.statistics.endpoint.rest;

import com.n26.statistics.exception.NoContentException;
import com.n26.statistics.model.TransactionRequestDto;
import com.n26.statistics.model.TransactionStatisticsResponseDto;
import com.n26.statistics.service.TransactionStatisticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionStatisticsRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionStatisticsRestController.class);

    @Autowired
    private TransactionStatisticsService transactionStatisticsService;

    @RequestMapping(value = "/transactions", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> createTransactionStatistics(@RequestBody TransactionRequestDto transactionRequestDto) {
        LOGGER.trace("TransactionStatisticsRestController : createTransactionStatistics endpoint is invoked");
        try {
            transactionStatisticsService.createTransactionStatistics(transactionRequestDto);
        } catch (NoContentException exception) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/statistics", method = RequestMethod.GET)
    public TransactionStatisticsResponseDto getTransactionStatistics() {
        LOGGER.trace("TransactionStatisticsRestController : getTransactionStatistics endpoint is invoked");
        return transactionStatisticsService.getTransactionStatisticsForLastMin();
    }

}
