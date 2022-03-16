package com.smallcase.stocktrade.repositories;

import com.smallcase.stocktrade.entities.TradeHistory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Meenal created on 15/03/22 at 12:38 AM
 **/

@Repository
public interface TradeHistoryRepository extends JpaRepository<TradeHistory, Long> {

  Optional<TradeHistory> findByIdAndActiveIsTrue(Long tradeId);

  Optional<List<TradeHistory>> findByCustomerId(Long custId);
}
