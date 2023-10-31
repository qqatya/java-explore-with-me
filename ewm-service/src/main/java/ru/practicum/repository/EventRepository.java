package ru.practicum.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import ru.practicum.dto.type.PublicationState;
import ru.practicum.entity.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {

    List<Event> findByInitiatorId(Long initiatorId, Pageable pageable);

    Optional<Event> findByIdAndInitiatorId(Long id, Long initiatorId);

    List<Event> findByCategoryId(Long id);

    List<Event> findByCompilationIdIn(List<Long> ids);

    List<Event> findByCompilationId(Long id);

    Optional<Event> findByIdAndPublicationStateEquals(Long id, PublicationState state);

    List<Event> findByInitiatorIdAndEventDateGreaterThanAndPublicationStateEquals(Long initiatorId,
                                                                                  LocalDateTime eventDate,
                                                                                  PublicationState state,
                                                                                  Pageable pageable);

}
