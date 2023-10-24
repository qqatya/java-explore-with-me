package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.dto.type.RequestStatus;
import ru.practicum.entity.Request;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findByEventInitiatorIdAndEventId(Long requesterId, Long eventId);

    @Modifying
    @Query("UPDATE Request r set r.status = 'REJECTED' WHERE r.event.id = ?1 AND r.status != 'CONFIRMED'")
    void setRejectedStatusByEventId(Long eventId);

    List<Request> findByEventIdAndStatusEquals(Long eventId, RequestStatus status);

    List<Request> findByRequesterId(Long requesterId);

    Optional<Request> findByRequesterIdAndEventId(Long requesterId, Long eventId);
}
