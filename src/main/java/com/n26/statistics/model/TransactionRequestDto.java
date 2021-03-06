package com.n26.statistics.model;

public class TransactionRequestDto {

    private Double amount;

    private Long timestamp;

    public TransactionRequestDto() {

    }

    public TransactionRequestDto(Double amount, Long timestamp) {
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

}
