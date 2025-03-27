package com.fitnycrm.user.service.auth.mapper;

import com.fitnycrm.tenant.repository.entity.Tenant;
import com.fitnycrm.user.service.auth.model.AuthenticatedUserDetails;
import com.fitnycrm.user.repository.entity.User;
import com.fitnycrm.user.repository.entity.UserRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Mapper(componentModel = "spring")
public interface AuthenticatedUserDetailsMapper {

    @Mapping(target = "authorities", source = "roles")
    @Mapping(target = "tenantIds", source = "tenants", qualifiedByName = "toTenantIds")
    AuthenticatedUserDetails toUserDetails(User user);

    @Named("toTenantIds")
    default Set<UUID> toTenantId(Set<Tenant> tenants) {
        return tenants.stream().map(Tenant::getId).collect(toSet());
    }

    @Mapping(target = "role", source = ".", qualifiedByName = "toRole")
    SimpleGrantedAuthority toSimpleGrantedAuthority(UserRole role);

    @Named("toRole")
    default String toRole(UserRole role) {
        return "ROLE_" + role.getName();
    }
}
