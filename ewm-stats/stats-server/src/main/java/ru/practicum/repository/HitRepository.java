package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.entity.Hit;

@Repository
public interface HitRepository extends JpaRepository<Hit, Long> {

    @Query(value = "SELECT COUNT(DISTINCT ip_address) FROM hits WHERE endpoint_id = ?1", nativeQuery = true)
    Long countHitsWithDistinctIpAddresses(Long endpointId);

    @Query(value = "SELECT COUNT(ip_address) FROM hits WHERE endpoint_id = ?1", nativeQuery = true)
    Long countHits(Long endpointId);

}
