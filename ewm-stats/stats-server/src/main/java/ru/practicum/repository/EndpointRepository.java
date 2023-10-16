package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.entity.Endpoint;

public interface EndpointRepository extends JpaRepository<Endpoint, Long> {

}
