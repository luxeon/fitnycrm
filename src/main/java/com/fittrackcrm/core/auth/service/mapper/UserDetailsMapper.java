package com.fittrackcrm.core.auth.service.mapper;

import com.fittrackcrm.core.auth.service.model.UserDetailsImpl;
import com.fittrackcrm.core.user.repository.entity.User;
import com.fittrackcrm.core.user.repository.entity.UserRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Mapper(componentModel = "spring")
public interface UserDetailsMapper {

    UserDetailsImpl toUserDetails(User user);

    @Mapping(target = "role", source = ".", qualifiedByName = "toRole")
    SimpleGrantedAuthority toSimpleGrantedAuthority(UserRole role);

    @Named("toRole")
    default String toRole(UserRole role) {
        return "ROLE_" + role.getName();
    }
}
