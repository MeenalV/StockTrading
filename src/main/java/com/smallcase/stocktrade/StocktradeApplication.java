package com.smallcase.stocktrade;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class StocktradeApplication {

  public static void main(String[] args) {
    SpringApplication.run(StocktradeApplication.class, args);
  }

}
