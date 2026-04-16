package com.songchao.supplier.modules.purchasereq.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.songchao.supplier.common.exception.BizException;
import com.songchao.supplier.modules.purchasereq.dto.PurchaseReqSaveRequest;
import com.songchao.supplier.modules.purchasereq.entity.PurchaseReq;
import com.songchao.supplier.modules.purchasereq.entity.PurchaseReqItem;
import com.songchao.supplier.modules.purchasereq.mapper.PurchaseReqItemMapper;
import com.songchao.supplier.modules.purchasereq.mapper.PurchaseReqMapper;
import com.songchao.supplier.security.auth.AuthContext;
import com.songchao.supplier.security.permission.DataScopeHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaseReqService {
    private final PurchaseReqMapper mapper;
    private final PurchaseReqItemMapper itemMapper;
    private final DataScopeHelper dataScopeHelper;

    public PurchaseReqService(PurchaseReqMapper mapper, PurchaseReqItemMapper itemMapper, DataScopeHelper dataScopeHelper) {
        this.mapper = mapper;
        this.itemMapper = itemMapper;
        this.dataScopeHelper = dataScopeHelper;
    }

    public List<PurchaseReq> list(String keyword, String reqStatus) {
        LambdaQueryWrapper<PurchaseReq> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isBlank()) {
            wrapper.and(w -> w.like(PurchaseReq::getReqNo, keyword)
                    .or().like(PurchaseReq::getReqTitle, keyword)
                    .or().like(PurchaseReq::getProjectName, keyword));
        }
        if (reqStatus != null && !reqStatus.isBlank()) {
            wrapper.eq(PurchaseReq::getReqStatus, reqStatus);
        }
        dataScopeHelper.applyDeptOrSelfScope(
                wrapper,
                scope -> {
                    if (AuthContext.get() != null && AuthContext.get().deptId() != null) {
                        scope.eq(PurchaseReq::getDeptId, AuthContext.get().deptId());
                    }
                },
                scope -> {
                    if (AuthContext.get() != null && AuthContext.get().userId() != null) {
                        scope.eq(PurchaseReq::getApplicantId, AuthContext.get().userId());
                    }
                }
        );
        wrapper.orderByDesc(PurchaseReq::getId);
        return mapper.selectList(wrapper);
    }

    public PurchaseReq create(PurchaseReqSaveRequest request) {
        LambdaQueryWrapper<PurchaseReq> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PurchaseReq::getReqNo, request.getReqNo());
        if (mapper.selectCount(wrapper) > 0) {
            throw new BizException("采购需求编号已存在");
        }
        PurchaseReq entity = new PurchaseReq();
        fill(entity, request);
        entity.setReqStatus("DRAFT");
        if (AuthContext.get() != null) {
            if (entity.getApplicantId() == null) {
                entity.setApplicantId(AuthContext.get().userId());
            }
            if (entity.getApplicantName() == null || entity.getApplicantName().isBlank()) {
                entity.setApplicantName(AuthContext.get().realName());
            }
            if (entity.getDeptId() == null) {
                entity.setDeptId(AuthContext.get().deptId());
            }
            if (entity.getDeptName() == null || entity.getDeptName().isBlank()) {
                entity.setDeptName(AuthContext.get().deptName());
            }
        }
        mapper.insert(entity);
        return entity;
    }

    public PurchaseReq update(Long id, PurchaseReqSaveRequest request) {
        PurchaseReq entity = getById(id);
        LambdaQueryWrapper<PurchaseReq> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PurchaseReq::getReqNo, request.getReqNo()).ne(PurchaseReq::getId, id);
        if (mapper.selectCount(wrapper) > 0) {
            throw new BizException("采购需求编号已存在");
        }
        Long applicantId = entity.getApplicantId();
        String applicantName = entity.getApplicantName();
        Long deptId = entity.getDeptId();
        String deptName = entity.getDeptName();
        fill(entity, request);
        if (entity.getApplicantId() == null) {
            entity.setApplicantId(applicantId);
        }
        if (entity.getApplicantName() == null || entity.getApplicantName().isBlank()) {
            entity.setApplicantName(applicantName);
        }
        if (entity.getDeptId() == null) {
            entity.setDeptId(deptId);
        }
        if (entity.getDeptName() == null || entity.getDeptName().isBlank()) {
            entity.setDeptName(deptName);
        }
        mapper.updateById(entity);
        return entity;
    }

    public PurchaseReq submit(Long id) {
        PurchaseReq entity = getById(id);
        if ("SUBMITTED".equals(entity.getReqStatus())) {
            throw new BizException("采购需求已提交");
        }
        if (countItems(id) <= 0) {
            throw new BizException("需求明细不能为空");
        }
        entity.setReqStatus("SUBMITTED");
        mapper.updateById(entity);
        return entity;
    }

    public void delete(Long id) {
        PurchaseReq entity = getById(id);
        itemMapper.delete(new LambdaQueryWrapper<PurchaseReqItem>().eq(PurchaseReqItem::getReqId, entity.getId()));
        mapper.deleteById(entity.getId());
    }

    private long countItems(Long reqId) {
        LambdaQueryWrapper<PurchaseReqItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PurchaseReqItem::getReqId, reqId);
        Long count = itemMapper.selectCount(wrapper);
        return count == null ? 0L : count;
    }

    private void fill(PurchaseReq entity, PurchaseReqSaveRequest request) {
        entity.setReqNo(request.getReqNo());
        entity.setProjectId(request.getProjectId());
        entity.setProjectName(request.getProjectName());
        entity.setReqTitle(request.getReqTitle());
        entity.setApplicantId(request.getApplicantId());
        entity.setApplicantName(request.getApplicantName());
        entity.setDeptId(request.getDeptId());
        entity.setDeptName(request.getDeptName());
        entity.setRequiredDate(request.getRequiredDate());
        entity.setTotalAmount(request.getTotalAmount());
        entity.setRemark(request.getRemark());
    }

    private PurchaseReq getById(Long id) {
        PurchaseReq entity = mapper.selectById(id);
        if (entity == null) {
            throw new BizException("采购需求不存在", 404);
        }
        return entity;
    }
}
