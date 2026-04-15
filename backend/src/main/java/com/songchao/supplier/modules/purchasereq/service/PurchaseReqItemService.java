package com.songchao.supplier.modules.purchasereq.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.songchao.supplier.common.exception.BizException;
import com.songchao.supplier.modules.purchasereq.dto.PurchaseReqItemSaveRequest;
import com.songchao.supplier.modules.purchasereq.entity.PurchaseReqItem;
import com.songchao.supplier.modules.purchasereq.mapper.PurchaseReqItemMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PurchaseReqItemService {
    private final PurchaseReqItemMapper mapper;

    public PurchaseReqItemService(PurchaseReqItemMapper mapper) {
        this.mapper = mapper;
    }

    public List<PurchaseReqItem> list(Long reqId) {
        LambdaQueryWrapper<PurchaseReqItem> wrapper = new LambdaQueryWrapper<>();
        if (reqId != null) {
            wrapper.eq(PurchaseReqItem::getReqId, reqId);
        }
        wrapper.orderByDesc(PurchaseReqItem::getId);
        return mapper.selectList(wrapper);
    }

    public PurchaseReqItem create(PurchaseReqItemSaveRequest request) {
        PurchaseReqItem entity = new PurchaseReqItem();
        fill(entity, request);
        mapper.insert(entity);
        return entity;
    }

    public PurchaseReqItem update(Long id, PurchaseReqItemSaveRequest request) {
        PurchaseReqItem entity = getById(id);
        fill(entity, request);
        mapper.updateById(entity);
        return entity;
    }

    public void delete(Long id) {
        mapper.deleteById(getById(id).getId());
    }

    public void deleteByReqId(Long reqId) {
        LambdaQueryWrapper<PurchaseReqItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PurchaseReqItem::getReqId, reqId);
        mapper.delete(wrapper);
    }

    private PurchaseReqItem getById(Long id) {
        PurchaseReqItem entity = mapper.selectById(id);
        if (entity == null) {
            throw new BizException("采购需求明细不存在", 404);
        }
        return entity;
    }

    private void fill(PurchaseReqItem entity, PurchaseReqItemSaveRequest request) {
        entity.setReqId(request.getReqId());
        entity.setProductId(request.getProductId());
        entity.setProductName(request.getProductName());
        entity.setSpecification(request.getSpecification());
        entity.setUnit(request.getUnit());
        entity.setQty(defaultDecimal(request.getQty()));
        entity.setBudgetPrice(defaultDecimal(request.getBudgetPrice()));
        entity.setBudgetAmount(resolveBudgetAmount(request));
        entity.setTechnicalRequirements(request.getTechnicalRequirements());
        entity.setRemark(request.getRemark());
    }

    private BigDecimal resolveBudgetAmount(PurchaseReqItemSaveRequest request) {
        if (request.getBudgetAmount() != null) {
            return request.getBudgetAmount();
        }
        return defaultDecimal(request.getQty()).multiply(defaultDecimal(request.getBudgetPrice()));
    }

    private BigDecimal defaultDecimal(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }
}
