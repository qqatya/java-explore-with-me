package ru.practicum.dto.event.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestStatusDto {

    private List<RequestDto> confirmedRequests;

    private List<RequestDto> rejectedRequests;

}
