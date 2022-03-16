package com.smallcase.stocktrade.repositories;

import com.smallcase.stocktrade.entities.Portfolio;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Meenal created on 15/03/22 at 12:38 AM
 **/

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {

  Optional<Portfolio> findByCustomerIdAndActiveIsTrue(Long custId);
}
