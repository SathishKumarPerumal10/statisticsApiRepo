package com.n26.statistics.model;

public class TransactionStatisticsResponseDto {

    private Double sum;

    private Double avg;

    private Double max;

    private Double min;

    private Long count;

    public TransactionStatisticsResponseDto() {

    }

    public TransactionStatisticsResponseDto(Double sum, Double avg, Double max, Double min, Long count) {

        this.sum = sum;
        this.avg = avg;
        this.max = max;
        this.min = min;
        this.count = count;
    }

    public Double getSum() {
        return sum;
    }

    public Double getAvg() {
        return avg;
    }

    public Double getMax() {
        return max;
    }

    public Double getMin() {
        return min;
    }

    public Long getCount() {
        return count;
    }

    public void setSum(Double sum) {
        this.sum = sum;
    }

    public void setAvg(Double avg) {
        this.avg = avg;
    }

    public void setMax(Double max) {
        this.max = max;
    }

    public void setMin(Double min) {
        this.min = min;
    }

    public void setCount(Long count) {
        this.count = count;
    }

}
