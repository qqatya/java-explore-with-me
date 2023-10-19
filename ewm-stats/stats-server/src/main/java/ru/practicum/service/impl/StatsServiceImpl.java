package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.dto.HitRequestDto;
import ru.practicum.dto.StatsDto;
import ru.practicum.entity.Endpoint;
import ru.practicum.entity.Hit;
import ru.practicum.mapper.EndpointMapper;
import ru.practicum.mapper.HitMapper;
import ru.practicum.repository.EndpointRepository;
import ru.practicum.repository.HitRepository;
import ru.practicum.service.StatsService;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final HitMapper hitMapper;

    private final EndpointMapper endpointMapper;

    private final EndpointRepository endpointRepository;

    private final HitRepository hitRepository;

    @Override
    public void create(HitRequestDto dto) {
        Endpoint endpoint = endpointRepository.findByUriEquals(dto.getUri());

        if (endpoint == null) {
            endpoint = endpointRepository.save(endpointMapper.mapToEntity(dto));
        }
        Hit hit = hitMapper.mapToEntity(dto, endpoint);

        hitRepository.save(hit);
        log.info("new hit for URI = {} has been created. app = {}, timestamp = {}",
                endpoint.getUri(), endpoint.getAppName(), hit.getSentDttm());
    }

    @Override
    public List<StatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        log.info("getting stats by start = {}, end = {}, URIs = {}, unique = {}", start, end, uris, unique);
        List<Endpoint> endpoints = endpointRepository.findBySentDttmRange(start, end);

        if (uris != null && !uris.isEmpty()) {
            endpoints = endpoints.stream().filter(e -> uris.contains(e.getUri())).collect(Collectors.toList());
            log.info("found {} endpoints by URIs {}", endpoints.size(), uris);
        }
        return endpoints.stream().map(e -> {
            Long hitsCount;

            if (unique) {
                hitsCount = hitRepository.countHitsWithDistinctIpAddresses(e.getId());
            } else {
                hitsCount = hitRepository.countHits(e.getId());
            }
            log.info("found {} hits by URI = {}", hitsCount, e.getUri());
            return endpointMapper.mapToDto(e, hitsCount);
        }).sorted(Comparator.comparing(StatsDto::getHits).reversed()).collect(Collectors.toList());
    }
}
