package com.lottery.web.service.mapper;

import com.lottery.web.domain.*;
import com.lottery.web.service.dto.LotteryDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Lottery and its DTO LotteryDTO.
 */
@Mapper(componentModel = "spring", uses = {UserProfileMapper.class})
public interface LotteryMapper extends EntityMapper<LotteryDTO, Lottery> {

    @Mapping(source = "userProfile.id", target = "userProfileId")
    LotteryDTO toDto(Lottery lottery);

    @Mapping(source = "userProfileId", target = "userProfile")
    @Mapping(target = "prizes", ignore = true)
    @Mapping(target = "histories", ignore = true)
    Lottery toEntity(LotteryDTO lotteryDTO);

    default Lottery fromId(Long id) {
        if (id == null) {
            return null;
        }
        Lottery lottery = new Lottery();
        lottery.setId(id);
        return lottery;
    }
}
