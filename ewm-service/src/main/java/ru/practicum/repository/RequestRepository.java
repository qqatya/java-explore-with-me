package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.dto.type.RequestStatus;
import ru.practicum.entity.Request;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    @Query("SELECT Request from Request r WHERE r.event.initiator.id = ?1 AND r.event.id = ?2")
    List<Request> findByEventInitiatorIdAndEventId(Long requesterId, Long eventId);

    @Query("UPDATE Request r set r.status = 'REJECTED' WHERE r.event.id = ?1 AND r.status != 'CONFIRMED'")
    void setRejectedStatusByEventId(Long eventId);

    List<Request> findByEventIdAndStatusEquals(Long eventId, RequestStatus status);

    List<Request> findByRequesterId(Long requesterId);

    Optional<Request> findByRequesterIdAndEventId(Long requesterId, Long eventId);
}
