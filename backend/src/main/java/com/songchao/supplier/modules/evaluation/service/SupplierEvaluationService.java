package com.songchao.supplier.modules.evaluation.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.songchao.supplier.common.exception.BizException;
import com.songchao.supplier.modules.evaluation.dto.EvaluationSaveRequest;
import com.songchao.supplier.modules.evaluation.entity.SupplierEvaluation;
import com.songchao.supplier.modules.evaluation.mapper.SupplierEvaluationMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SupplierEvaluationService {
    private final SupplierEvaluationMapper mapper;

    public SupplierEvaluationService(SupplierEvaluationMapper mapper) {
        this.mapper = mapper;
    }

    public List<SupplierEvaluation> list(Long supplierId) {
        LambdaQueryWrapper<SupplierEvaluation> wrapper = new LambdaQueryWrapper<>();
        if (supplierId != null) {
            wrapper.eq(SupplierEvaluation::getSupplierId, supplierId);
        }
        wrapper.orderByDesc(SupplierEvaluation::getId);
        return mapper.selectList(wrapper);
    }

    public SupplierEvaluation create(EvaluationSaveRequest request) {
        SupplierEvaluation entity = new SupplierEvaluation();
        fill(entity, request);
        mapper.insert(entity);
        return entity;
    }

    public SupplierEvaluation update(Long id, EvaluationSaveRequest request) {
        SupplierEvaluation entity = getById(id);
        fill(entity, request);
        mapper.updateById(entity);
        return entity;
    }

    public void delete(Long id) {
        mapper.deleteById(getById(id).getId());
    }

    private SupplierEvaluation getById(Long id) {
        SupplierEvaluation entity = mapper.selectById(id);
        if (entity == null) {
            throw new BizException("供应商评价不存在", 404);
        }
        return entity;
    }

    private void fill(SupplierEvaluation entity, EvaluationSaveRequest request) {
        entity.setSupplierId(request.getSupplierId());
        entity.setProjectId(request.getProjectId());
        entity.setContractId(request.getContractId());
        entity.setQualityScore(defaultScore(request.getQualityScore()));
        entity.setDeliveryScore(defaultScore(request.getDeliveryScore()));
        entity.setServiceScore(defaultScore(request.getServiceScore()));
        entity.setPriceScore(defaultScore(request.getPriceScore()));
        entity.setComplianceScore(defaultScore(request.getComplianceScore()));
        BigDecimal total = entity.getQualityScore()
                .add(entity.getDeliveryScore())
                .add(entity.getServiceScore())
                .add(entity.getPriceScore())
                .add(entity.getComplianceScore())
                .divide(BigDecimal.valueOf(5), 2, RoundingMode.HALF_UP);
        entity.setTotalScore(total);
        entity.setRating(resolveRating(total));
        entity.setEvaluationUserId(request.getEvaluationUserId());
        entity.setEvaluationTime(LocalDateTime.now().toString());
        entity.setRemark(request.getRemark());
    }

    private BigDecimal defaultScore(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    private String resolveRating(BigDecimal score) {
        if (score.compareTo(BigDecimal.valueOf(90)) >= 0) return "A";
        if (score.compareTo(BigDecimal.valueOf(75)) >= 0) return "B";
        if (score.compareTo(BigDecimal.valueOf(60)) >= 0) return "C";
        return "D";
    }
}
