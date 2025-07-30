package com.project.academic_service.service;

import com.project.academic_service.dao.repository.PeriodRepository;
import com.project.academic_service.domain.Period;
import com.project.academic_service.dto.PeriodDTO;
import com.project.academic_service.exception.ObjectAlreadyExistsException;
import com.project.academic_service.mapper.PeriodDTOMapper;
import com.project.academic_service.mapper.PeriodEntityMapper;
import com.project.academic_service.mapper.PeriodUpdateMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class PeriodServiceImpl implements IPeriodService{
    private final PeriodRepository periodRepository;
    private final PeriodDTOMapper periodDTOMapper;
    private final PeriodEntityMapper periodEntityMapper;
    private final PeriodUpdateMapper periodUpdateMapper;


    public PeriodServiceImpl(PeriodRepository periodRepository, PeriodDTOMapper periodDTOMapper, PeriodEntityMapper periodEntityMapper, PeriodUpdateMapper periodUpdateMapper) {
        this.periodRepository = periodRepository;
        this.periodDTOMapper = periodDTOMapper;
        this.periodEntityMapper = periodEntityMapper;
        this.periodUpdateMapper = periodUpdateMapper;
    }


    @Override
    public PeriodDTO get(int id) {
        return this.periodRepository.findById(id)
                    .map(this.periodDTOMapper)
                .orElseThrow(()-> new NoSuchElementException("There's not field with id"));
    }

    @Override
    public List<PeriodDTO> getAll() {
        return this.periodRepository.findAll()
                .stream()
                .map(this.periodDTOMapper)
                .collect(Collectors.toList());
    }


    @Override
    public Period create(PeriodDTO periodDTO) {
        if (this.periodRepository.existsByEntitled(periodDTO.entitled()))
            throw new ObjectAlreadyExistsException("This period already exist!");

        Period entity = this.periodEntityMapper.apply(periodDTO);
        return this.periodRepository.save(entity);

    }

    @Override
    public PeriodDTO update(int id, PeriodDTO periodDTO) {
        Period existingPeriod = this.periodRepository.findById(id)
                .orElseThrow(()-> new NoSuchElementException("There's no period with that id="+id));

        Period period = this.periodUpdateMapper.apply(periodDTO,existingPeriod);
        Period saved = this.periodRepository.save(period);
        return this.periodDTOMapper.apply(saved);
    }

    @Override
    public void delete(int id) {
        this.periodRepository.deleteById(id);
    }
}
