package com.lottery.web.service.mapper;

import com.lottery.web.domain.*;
import com.lottery.web.service.dto.PrizeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Prize and its DTO PrizeDTO.
 */
@Mapper(componentModel = "spring", uses = {CategoryMapper.class, LotteryMapper.class, HistoryMapper.class})
public interface PrizeMapper extends EntityMapper<PrizeDTO, Prize> {

    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "lottery.id", target = "lotteryId")
    @Mapping(source = "history.id", target = "historyId")
    PrizeDTO toDto(Prize prize);

    @Mapping(source = "categoryId", target = "category")
    @Mapping(source = "lotteryId", target = "lottery")
    @Mapping(source = "historyId", target = "history")
    Prize toEntity(PrizeDTO prizeDTO);

    default Prize fromId(Long id) {
        if (id == null) {
            return null;
        }
        Prize prize = new Prize();
        prize.setId(id);
        return prize;
    }
}
