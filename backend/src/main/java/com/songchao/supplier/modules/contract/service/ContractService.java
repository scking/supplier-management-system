package com.songchao.supplier.modules.contract.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.songchao.supplier.common.exception.BizException;
import com.songchao.supplier.modules.contract.dto.ContractDetailResponse;
import com.songchao.supplier.modules.contract.dto.ContractReminderResponse;
import com.songchao.supplier.modules.contract.dto.ContractSaveRequest;
import com.songchao.supplier.modules.contract.entity.Contract;
import com.songchao.supplier.modules.contract.mapper.ContractMapper;
import com.songchao.supplier.modules.contractattachment.entity.ContractAttachment;
import com.songchao.supplier.modules.contractattachment.service.ContractAttachmentService;
import com.songchao.supplier.security.auth.AuthContext;
import com.songchao.supplier.security.permission.DataScopeHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class ContractService {
    private final ContractMapper mapper;
    private final DataScopeHelper dataScopeHelper;
    private final ContractAttachmentService contractAttachmentService;

    public ContractService(
            ContractMapper mapper,
            DataScopeHelper dataScopeHelper,
            ContractAttachmentService contractAttachmentService
    ) {
        this.mapper = mapper;
        this.dataScopeHelper = dataScopeHelper;
        this.contractAttachmentService = contractAttachmentService;
    }

    public List<Contract> list(String keyword, String contractStatus) {
        LambdaQueryWrapper<Contract> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isBlank()) {
            wrapper.and(w -> w.like(Contract::getContractNo, keyword)
                    .or().like(Contract::getContractTitle, keyword)
                    .or().like(Contract::getSupplierName, keyword)
                    .or().like(Contract::getProjectName, keyword));
        }
        if (contractStatus != null && !contractStatus.isBlank()) {
            wrapper.eq(Contract::getContractStatus, contractStatus);
        }
        dataScopeHelper.applyDeptOrSelfScope(
                wrapper,
                scope -> {
                    if (AuthContext.get() != null && AuthContext.get().deptId() != null) {
                        scope.eq(Contract::getCreatedDeptId, AuthContext.get().deptId());
                    }
                },
                scope -> {
                    if (AuthContext.get() != null && AuthContext.get().userId() != null) {
                        scope.eq(Contract::getCreatedBy, AuthContext.get().userId());
                    }
                }
        );
        wrapper.orderByDesc(Contract::getId);
        return mapper.selectList(wrapper);
    }

    public Contract create(ContractSaveRequest request) {
        LambdaQueryWrapper<Contract> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Contract::getContractNo, request.getContractNo());
        if (mapper.selectCount(wrapper) > 0) {
            throw new BizException("合同编号已存在");
        }
        Contract entity = new Contract();
        entity.setContractNo(request.getContractNo());
        entity.setProjectId(request.getProjectId());
        entity.setProjectName(request.getProjectName());
        entity.setSupplierId(request.getSupplierId());
        entity.setSupplierName(request.getSupplierName());
        entity.setInquiryId(request.getInquiryId());
        entity.setContractTitle(request.getContractTitle());
        entity.setContractAmount(request.getContractAmount());
        entity.setTaxRate(request.getTaxRate());
        entity.setSignDate(normalizeDate(request.getSignDate()));
        entity.setEffectiveDate(normalizeDate(request.getEffectiveDate()));
        entity.setExpireDate(normalizeDate(request.getExpireDate()));
        entity.setContractStatus(request.getContractStatus() == null || request.getContractStatus().isBlank() ? "DRAFT" : request.getContractStatus());
        entity.setPaymentStatus(request.getPaymentStatus());
        if (AuthContext.get() != null) {
            entity.setCreatedBy(AuthContext.get().userId());
            entity.setCreatedDeptId(AuthContext.get().deptId());
        }
        entity.setRemark(request.getRemark());
        mapper.insert(entity);
        return entity;
    }

    public Contract update(Long id, ContractSaveRequest request) {
        Contract entity = getById(id);
        LambdaQueryWrapper<Contract> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Contract::getContractNo, request.getContractNo()).ne(Contract::getId, id);
        if (mapper.selectCount(wrapper) > 0) {
            throw new BizException("合同编号已存在");
        }
        entity.setContractNo(request.getContractNo());
        entity.setProjectId(request.getProjectId());
        entity.setProjectName(request.getProjectName());
        entity.setSupplierId(request.getSupplierId());
        entity.setSupplierName(request.getSupplierName());
        entity.setInquiryId(request.getInquiryId());
        entity.setContractTitle(request.getContractTitle());
        entity.setContractAmount(request.getContractAmount());
        entity.setTaxRate(request.getTaxRate());
        entity.setSignDate(normalizeDate(request.getSignDate()));
        entity.setEffectiveDate(normalizeDate(request.getEffectiveDate()));
        entity.setExpireDate(normalizeDate(request.getExpireDate()));
        entity.setContractStatus(request.getContractStatus() == null || request.getContractStatus().isBlank() ? "DRAFT" : request.getContractStatus());
        entity.setPaymentStatus(request.getPaymentStatus());
        entity.setRemark(request.getRemark());
        mapper.updateById(entity);
        return entity;
    }

    public void delete(Long id) {
        Contract entity = getById(id);
        mapper.deleteById(entity.getId());
    }

    public ContractDetailResponse detail(Long id) {
        Contract contract = mapper.selectById(id);
        if (contract == null) {
            throw new BizException("合同不存在");
        }

        ContractDetailResponse response = new ContractDetailResponse();
        response.setContract(contract);
        response.setAttachments(contractAttachmentService.list(id));
        response.setReminderStatus(resolveReminderStatus(contract));
        response.setDaysToExpire(resolveDaysToExpire(contract));
        return response;
    }

    public List<ContractReminderResponse> reminders() {
        List<ContractReminderResponse> responses = new ArrayList<>();
        for (Contract contract : list(null, null)) {
            String reminderStatus = resolveReminderStatus(contract);
            if ("正常".equals(reminderStatus)) {
                continue;
            }
            ContractReminderResponse item = new ContractReminderResponse();
            item.setContractId(contract.getId());
            item.setContractNo(contract.getContractNo());
            item.setContractTitle(contract.getContractTitle());
            item.setSupplierName(contract.getSupplierName());
            item.setExpireDate(contract.getExpireDate());
            item.setPaymentStatus(contract.getPaymentStatus());
            item.setReminderStatus(reminderStatus);
            item.setDaysToExpire(resolveDaysToExpire(contract));
            responses.add(item);
        }
        return responses;
    }

    private String resolveReminderStatus(Contract contract) {
        Long daysToExpire = resolveDaysToExpire(contract);
        if (daysToExpire == null) {
            return "正常";
        }
        if (daysToExpire < 0) {
            return "已逾期";
        }
        if (daysToExpire <= 7) {
            return "近7天";
        }
        if (daysToExpire <= 30) {
            return "后续";
        }
        return "正常";
    }

    private Long resolveDaysToExpire(Contract contract) {
        if (contract.getExpireDate() == null || contract.getExpireDate().isBlank()) {
            return null;
        }
        try {
            LocalDate expireDate = LocalDate.parse(contract.getExpireDate());
            return ChronoUnit.DAYS.between(LocalDate.now(), expireDate);
        } catch (DateTimeParseException ex) {
            return null;
        }
    }

    private Contract getById(Long id) {
        Contract entity = mapper.selectById(id);
        if (entity == null) {
            throw new BizException("合同不存在", 404);
        }
        return entity;
    }

    private String normalizeDate(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
