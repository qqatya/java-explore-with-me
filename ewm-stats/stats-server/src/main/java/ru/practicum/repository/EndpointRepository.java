package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.entity.Endpoint;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EndpointRepository extends JpaRepository<Endpoint, Long> {

    Endpoint findByUriEquals(String uri);

    @Query(value = "SELECT e.endpoint_id, e.app_name, e.uri FROM endpoints e INNER JOIN hits h using(endpoint_id) "
            + "WHERE h.sent_dttm >= ?1 AND h.sent_dttm <= ?2 "
            + "GROUP BY e.endpoint_id", nativeQuery = true)
    List<Endpoint> findBySentDttmRange(LocalDateTime start, LocalDateTime end);

}
