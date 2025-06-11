package com.fitavera.payment.repository.specification;

import com.fitavera.payment.repository.entity.ClientPayment;
import com.fitavera.payment.rest.model.ClientPaymentFilterRequest;
import com.fitavera.payment.rest.model.ExtendedClientPaymentFilterRequest;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ClientPaymentSpecification {

    public static Specification<ClientPayment> withFilters(UUID tenantId, UUID clientId, ClientPaymentFilterRequest filter) {
        return withTenantFilters(tenantId,
                new ExtendedClientPaymentFilterRequest(filter.status(),
                        filter.trainingId(),
                        clientId,
                        filter.createdAtFrom(),
                        filter.createdAtTo()));
    }

    public static Specification<ClientPayment> withTenantFilters(UUID tenantId, ExtendedClientPaymentFilterRequest filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (query != null && Long.class != query.getResultType()) {
                root.fetch("training", JoinType.LEFT);
            }

            predicates.add(cb.equal(root.get("tenant").get("id"), tenantId));

            if (filter.clientId() != null) {
                predicates.add(cb.equal(root.get("client").get("id"), filter.clientId()));
            }

            if (filter.status() != null) {
                predicates.add(cb.equal(root.get("status"), filter.status()));
            }

            if (filter.trainingId() != null) {
                predicates.add(cb.equal(root.get("training").get("id"), filter.trainingId()));
            }

            if (filter.createdAtFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), filter.createdAtFrom()));
            }

            if (filter.createdAtTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), filter.createdAtTo()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
} 