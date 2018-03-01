package com.n26.statistics.validator;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import com.n26.statistics.exception.NoContentException;
import com.n26.statistics.model.TransactionRequestDto;
import org.springframework.stereotype.Component;

@Component
public class TransactionRequestValidator {

    public void validateRequest(TransactionRequestDto transactionRequestDto) throws NoContentException {
        if (!(transactionRequestDto != null && transactionRequestDto.getAmount() != null && transactionRequestDto
                .getTimestamp() != null && transactionRequestDto.getTimestamp() > getValidTime())) {
            throw new NoContentException();
        }
    }

    private Long getValidTime() {
        return LocalDateTime.now().minusSeconds(60).toInstant(ZoneOffset.UTC).toEpochMilli();
    }

}
