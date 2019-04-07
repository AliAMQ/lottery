package com.lottery.web.repository;

import com.lottery.web.domain.Prize;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Prize entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PrizeRepository extends JpaRepository<Prize, Long>, JpaSpecificationExecutor<Prize> {

}
