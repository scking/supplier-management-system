package com.songchao.supplier.modules.performance.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.songchao.supplier.common.exception.BizException;
import com.songchao.supplier.modules.performance.dto.PerformanceSaveRequest;
import com.songchao.supplier.modules.performance.entity.Performance;
import com.songchao.supplier.modules.performance.mapper.PerformanceMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PerformanceService {
    private final PerformanceMapper mapper;

    public PerformanceService(PerformanceMapper mapper) {
        this.mapper = mapper;
    }

    public List<Performance> list(Long contractId, String status) {
        LambdaQueryWrapper<Performance> wrapper = new LambdaQueryWrapper<>();
        if (contractId != null) {
            wrapper.eq(Performance::getContractId, contractId);
        }
        if (status != null && !status.isBlank()) {
            wrapper.eq(Performance::getStatus, status);
        }
        wrapper.orderByDesc(Performance::getId);
        return mapper.selectList(wrapper);
    }

    public Performance create(PerformanceSaveRequest request) {
        Performance entity = new Performance();
        fill(entity, request);
        entity.setStatus("PENDING");
        mapper.insert(entity);
        return entity;
    }

    public Performance update(Long id, PerformanceSaveRequest request) {
        Performance entity = getById(id);
        String status = entity.getStatus();
        fill(entity, request);
        entity.setStatus(status == null || status.isBlank() ? "PENDING" : status);
        mapper.updateById(entity);
        return entity;
    }

    public void delete(Long id) {
        mapper.deleteById(getById(id).getId());
    }

    private Performance getById(Long id) {
        Performance entity = mapper.selectById(id);
        if (entity == null) {
            throw new BizException("履约节点不存在", 404);
        }
        return entity;
    }

    private void fill(Performance entity, PerformanceSaveRequest request) {
        entity.setContractId(request.getContractId());
        entity.setProjectId(request.getProjectId());
        entity.setSupplierId(request.getSupplierId());
        entity.setPerformanceType(request.getPerformanceType());
        entity.setNodeName(request.getNodeName());
        entity.setPlannedDate(request.getPlannedDate());
        entity.setActualDate(request.getActualDate());
        entity.setResponsibleUserId(request.getResponsibleUserId());
        entity.setDescription(request.getDescription());
        entity.setRemark(request.getRemark());
    }
}
