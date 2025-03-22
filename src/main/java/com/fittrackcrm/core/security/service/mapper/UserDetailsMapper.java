package com.fittrackcrm.core.security.service.mapper;

import com.fittrackcrm.core.security.service.model.AuthenticatedUserDetails;
import com.fittrackcrm.core.user.repository.entity.User;
import com.fittrackcrm.core.user.repository.entity.UserRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Mapper(componentModel = "spring")
public interface UserDetailsMapper {

    @Mapping(target = "authorities", source = "roles")
    AuthenticatedUserDetails toUserDetails(User user);

    @Mapping(target = "role", source = ".", qualifiedByName = "toRole")
    SimpleGrantedAuthority toSimpleGrantedAuthority(UserRole role);

    @Named("toRole")
    default String toRole(UserRole role) {
        return "ROLE_" + role.getName();
    }
}
