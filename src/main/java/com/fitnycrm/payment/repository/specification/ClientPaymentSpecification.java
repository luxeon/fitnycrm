package com.fitnycrm.payment.repository.specification;

import com.fitnycrm.payment.repository.entity.ClientPayment;
import com.fitnycrm.payment.rest.model.ClientPaymentFilterRequest;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ClientPaymentSpecification {

    public static Specification<ClientPayment> withFilters(UUID tenantId, UUID clientId, ClientPaymentFilterRequest filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(root.get("tenant").get("id"), tenantId));
            predicates.add(cb.equal(root.get("client").get("id"), clientId));

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