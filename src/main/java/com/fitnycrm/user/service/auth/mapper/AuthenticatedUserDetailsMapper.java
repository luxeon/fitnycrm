package com.fitnycrm.user.service.auth.mapper;

import com.fitnycrm.user.service.auth.model.AuthenticatedUserDetails;
import com.fitnycrm.user.repository.entity.User;
import com.fitnycrm.user.repository.entity.UserRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Mapper(componentModel = "spring")
public interface AuthenticatedUserDetailsMapper {

    @Mapping(target = "authorities", source = "roles")
    AuthenticatedUserDetails toUserDetails(User user);

    @Mapping(target = "role", source = ".", qualifiedByName = "toRole")
    SimpleGrantedAuthority toSimpleGrantedAuthority(UserRole role);

    @Named("toRole")
    default String toRole(UserRole role) {
        return "ROLE_" + role.getName();
    }
}
