package com.lottery.web.service.mapper;

import com.lottery.web.domain.*;
import com.lottery.web.service.dto.HistoryDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity History and its DTO HistoryDTO.
 */
@Mapper(componentModel = "spring", uses = {LotteryMapper.class})
public interface HistoryMapper extends EntityMapper<HistoryDTO, History> {

    @Mapping(source = "lottery.id", target = "lotteryId")
    HistoryDTO toDto(History history);

    @Mapping(source = "lotteryId", target = "lottery")
    @Mapping(target = "prizes", ignore = true)
    History toEntity(HistoryDTO historyDTO);

    default History fromId(Long id) {
        if (id == null) {
            return null;
        }
        History history = new History();
        history.setId(id);
        return history;
    }
}
