package com.apps.repository;

import com.apps.model.App;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AppRepository extends JpaRepository<App, Long> {
    Optional<App> findByName(String name);
    boolean existsByName(String name);
}
