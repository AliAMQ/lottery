package com.lottery.web.service.mapper;

import com.lottery.web.domain.*;
import com.lottery.web.service.dto.UserProfileDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity UserProfile and its DTO UserProfileDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface UserProfileMapper extends EntityMapper<UserProfileDTO, UserProfile> {

    @Mapping(source = "user.id", target = "userId")
    UserProfileDTO toDto(UserProfile userProfile);

    @Mapping(source = "userId", target = "user")
    @Mapping(target = "lotteries", ignore = true)
    UserProfile toEntity(UserProfileDTO userProfileDTO);

    default UserProfile fromId(Long id) {
        if (id == null) {
            return null;
        }
        UserProfile userProfile = new UserProfile();
        userProfile.setId(id);
        return userProfile;
    }
}
