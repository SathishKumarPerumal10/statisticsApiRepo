package com.n26.statistics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TransactionStatisticsApp {

    public static void main(String[] args) {
        SpringApplication.run(TransactionStatisticsApp.class, args);
    }

}
