package com.lottery.web.repository;

import com.lottery.web.domain.Lottery;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Lottery entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LotteryRepository extends JpaRepository<Lottery, Long>, JpaSpecificationExecutor<Lottery> {

}
