package com.songchao.supplier.modules.risk.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.songchao.supplier.common.exception.BizException;
import com.songchao.supplier.modules.risk.dto.RiskHandleRequest;
import com.songchao.supplier.modules.risk.entity.RiskWarning;
import com.songchao.supplier.modules.risk.mapper.RiskWarningMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RiskWarningService {
    private final RiskWarningMapper mapper;

    public RiskWarningService(RiskWarningMapper mapper) {
        this.mapper = mapper;
    }

    public List<RiskWarning> list(String status) {
        LambdaQueryWrapper<RiskWarning> wrapper = new LambdaQueryWrapper<>();
        if (status != null && !status.isBlank()) {
            wrapper.eq(RiskWarning::getStatus, status);
        }
        wrapper.orderByDesc(RiskWarning::getId);
        return mapper.selectList(wrapper);
    }

    public RiskWarning handle(Long id, RiskHandleRequest request, String status) {
        RiskWarning entity = mapper.selectById(id);
        if (entity == null) {
            throw new BizException("风险预警不存在");
        }
        entity.setStatus(status);
        entity.setHandlerId(request.getHandlerId());
        entity.setRemark(request.getRemark());
        entity.setResolvedTime(LocalDateTime.now().toString());
        mapper.updateById(entity);
        return entity;
    }
}

