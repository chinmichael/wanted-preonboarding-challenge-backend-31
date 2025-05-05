package com.wanted.cqrs.apis.category.repository;

import com.wanted.cqrs.apis.category.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long>, CategoryRepositoryCustom {
}
