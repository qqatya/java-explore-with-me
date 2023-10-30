package ru.practicum.dto.compilation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompilationUpdateDto {

    private List<Long> events;

    private Boolean pinned = false;

    @Size(min = 1)
    @Size(max = 50)
    private String title;

}
