package com.n26.statistics.validator;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import com.n26.statistics.exception.NoContentException;
import com.n26.statistics.model.TransactionRequestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TransactionRequestValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionRequestValidator.class);

    public void validateRequest(TransactionRequestDto transactionRequestDto) throws NoContentException {
        if (!(transactionRequestDto != null && transactionRequestDto.getAmount() != null && transactionRequestDto
                .getTimestamp() != null && transactionRequestDto.getTimestamp() > getValidTime())) {
            LOGGER.error("Submitted Request is not valid");
            throw new NoContentException();
        }
    }

    /**
     * (SP) This method will return the valid time, Valid time in our app is basically 60 seconds from current time.
     */
    private Long getValidTime() {
        return LocalDateTime.now().minusSeconds(60).toInstant(ZoneOffset.UTC).toEpochMilli();
    }

}
