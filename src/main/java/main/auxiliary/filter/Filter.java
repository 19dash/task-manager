package main.auxiliary.filter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import main.model.Task;

public class Filter implements Specification<Task> {
	private static final long serialVersionUID = 42L;
	private final List<Condition> conditions;

	public Filter() {
		conditions = new ArrayList<>();
	}

	public void addCondition(Condition condition) {
		conditions.add(condition);
	}

	public Predicate toPredicate(Root<Task> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
		List<Predicate> predicates = buildPredicates(root, criteriaQuery, criteriaBuilder);
		return predicates.size() > 1 ? criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]))
				: predicates.get(0);
	}
	public boolean isEmpty() {
		return conditions.isEmpty();
	}

	private List<Predicate> buildPredicates(Root<Task> root, CriteriaQuery<?> criteriaQuery,
			CriteriaBuilder criteriaBuilder) {
		List<Predicate> predicates = new ArrayList<>();
		conditions.forEach(
				condition -> predicates.add(criteriaBuilder.equal(root.get(condition.field), condition.value)));
		return predicates;
	}
}
