package com.songchao.supplier.modules.qualification.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.songchao.supplier.common.exception.BizException;
import com.songchao.supplier.modules.qualification.dto.QualificationSaveRequest;
import com.songchao.supplier.modules.qualification.entity.SupplierQualification;
import com.songchao.supplier.modules.qualification.mapper.SupplierQualificationMapper;
import com.songchao.supplier.modules.risk.entity.RiskWarning;
import com.songchao.supplier.modules.risk.mapper.RiskWarningMapper;
import com.songchao.supplier.modules.supplier.entity.Supplier;
import com.songchao.supplier.modules.supplier.mapper.SupplierMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SupplierQualificationService {
    private final SupplierQualificationMapper mapper;
    private final RiskWarningMapper riskWarningMapper;
    private final SupplierMapper supplierMapper;

    public SupplierQualificationService(
            SupplierQualificationMapper mapper,
            RiskWarningMapper riskWarningMapper,
            SupplierMapper supplierMapper
    ) {
        this.mapper = mapper;
        this.riskWarningMapper = riskWarningMapper;
        this.supplierMapper = supplierMapper;
    }

    public List<SupplierQualification> list(Long supplierId) {
      LambdaQueryWrapper<SupplierQualification> wrapper = new LambdaQueryWrapper<>();
      if (supplierId != null) {
          wrapper.eq(SupplierQualification::getSupplierId, supplierId);
      }
      wrapper.orderByDesc(SupplierQualification::getId);
      return mapper.selectList(wrapper);
    }

    public SupplierQualification create(QualificationSaveRequest request) {
        SupplierQualification entity = new SupplierQualification();
        fill(entity, request);
        if (entity.getStatus() == null || entity.getStatus().isBlank()) {
            entity.setStatus("VALID");
        }
        mapper.insert(entity);
        syncExpiryRisk(entity);
        return entity;
    }

    public SupplierQualification update(Long id, QualificationSaveRequest request) {
        SupplierQualification entity = getById(id);
        fill(entity, request);
        if (entity.getStatus() == null || entity.getStatus().isBlank()) {
            entity.setStatus("VALID");
        }
        mapper.updateById(entity);
        syncExpiryRisk(entity);
        return entity;
    }

    public void delete(Long id) {
        mapper.deleteById(getById(id).getId());
    }

    private SupplierQualification getById(Long id) {
        SupplierQualification entity = mapper.selectById(id);
        if (entity == null) {
            throw new BizException("供应商资质不存在", 404);
        }
        return entity;
    }

    private void fill(SupplierQualification entity, QualificationSaveRequest request) {
        entity.setSupplierId(request.getSupplierId());
        entity.setQualificationType(request.getQualificationType());
        entity.setQualificationName(request.getQualificationName());
        entity.setQualificationNo(request.getQualificationNo());
        entity.setIssuedBy(request.getIssuedBy());
        entity.setIssueDate(request.getIssueDate());
        entity.setExpireDate(request.getExpireDate());
        entity.setStatus(request.getStatus());
        entity.setRemark(request.getRemark());
    }

    private void syncExpiryRisk(SupplierQualification qualification) {
        if (qualification.getExpireDate() == null || qualification.getExpireDate().isBlank()) {
            return;
        }
        LocalDate expireDate = LocalDate.parse(qualification.getExpireDate());
        LocalDate today = LocalDate.now();
        if (expireDate.isBefore(today) || expireDate.isAfter(today.plusDays(30))) {
            return;
        }

        Supplier supplier = supplierMapper.selectById(qualification.getSupplierId());
        String supplierName = supplier == null ? "未知供应商" : supplier.getSupplierName();

        LambdaQueryWrapper<RiskWarning> wrapper = new LambdaQueryWrapper<RiskWarning>()
                .eq(RiskWarning::getWarningType, "QUALIFICATION_EXPIRE")
                .eq(RiskWarning::getBizId, qualification.getId())
                .eq(RiskWarning::getStatus, "OPEN")
                .last("LIMIT 1");
        RiskWarning riskWarning = riskWarningMapper.selectOne(wrapper);
        if (riskWarning == null) {
            riskWarning = new RiskWarning();
            riskWarning.setWarningType("QUALIFICATION_EXPIRE");
            riskWarning.setSupplierId(qualification.getSupplierId());
            riskWarning.setBizId(qualification.getId());
            riskWarning.setStatus("OPEN");
            riskWarning.setTriggerTime(LocalDateTime.now().toString());
        }
        riskWarning.setWarningLevel(expireDate.isEqual(today) ? "HIGH" : "MEDIUM");
        riskWarning.setWarningTitle("供应商资质即将到期");
        riskWarning.setWarningContent(
                supplierName + " 的【" + qualification.getQualificationName() + "】将在 "
                        + qualification.getExpireDate() + " 到期"
        );
        riskWarning.setRemark("由资质新增/更新自动生成");
        if (riskWarning.getId() == null) {
            riskWarningMapper.insert(riskWarning);
        } else {
            riskWarningMapper.updateById(riskWarning);
        }
    }
}
