package com.project.ensitech.training_service.model.dto.specialityDto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SpecialityStatsDto {
    private List<String> labels;
    private List<Long> count;
}
