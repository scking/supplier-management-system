package com.songchao.supplier.modules.supplierproduct.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.songchao.supplier.common.exception.BizException;
import com.songchao.supplier.modules.supplierproduct.dto.SupplierProductPriceHistorySaveRequest;
import com.songchao.supplier.modules.supplierproduct.entity.SupplierProduct;
import com.songchao.supplier.modules.supplierproduct.entity.SupplierProductPriceHistory;
import com.songchao.supplier.modules.supplierproduct.mapper.SupplierProductMapper;
import com.songchao.supplier.modules.supplierproduct.mapper.SupplierProductPriceHistoryMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class SupplierProductPriceHistoryService {
    private final SupplierProductPriceHistoryMapper mapper;
    private final SupplierProductMapper supplierProductMapper;

    public SupplierProductPriceHistoryService(
            SupplierProductPriceHistoryMapper mapper,
            SupplierProductMapper supplierProductMapper
    ) {
        this.mapper = mapper;
        this.supplierProductMapper = supplierProductMapper;
    }

    public List<SupplierProductPriceHistory> list(Long supplierProductId) {
        LambdaQueryWrapper<SupplierProductPriceHistory> wrapper = new LambdaQueryWrapper<>();
        if (supplierProductId != null) {
            wrapper.eq(SupplierProductPriceHistory::getSupplierProductId, supplierProductId);
        }
        wrapper.orderByDesc(SupplierProductPriceHistory::getId);
        return mapper.selectList(wrapper);
    }

    public SupplierProductPriceHistory create(SupplierProductPriceHistorySaveRequest request) {
        SupplierProductPriceHistory entity = new SupplierProductPriceHistory();
        fill(entity, request);
        mapper.insert(entity);
        return entity;
    }

    public SupplierProductPriceHistory update(Long id, SupplierProductPriceHistorySaveRequest request) {
        SupplierProductPriceHistory entity = getById(id);
        fill(entity, request);
        mapper.updateById(entity);
        return entity;
    }

    public void delete(Long id) {
        mapper.deleteById(getById(id).getId());
    }

    private SupplierProductPriceHistory getById(Long id) {
        SupplierProductPriceHistory entity = mapper.selectById(id);
        if (entity == null) {
            throw new BizException("历史价格不存在", 404);
        }
        return entity;
    }

    public void createBySupplierProduct(SupplierProduct supplierProduct) {
        SupplierProductPriceHistory entity = new SupplierProductPriceHistory();
        entity.setSupplierProductId(supplierProduct.getId());
        entity.setQuotePrice(defaultDecimal(supplierProduct.getQuotePrice()));
        entity.setTaxRate(defaultDecimal(supplierProduct.getTaxRate()));
        entity.setDeliveryCycleDays(defaultInt(supplierProduct.getDeliveryCycleDays()));
        entity.setWarrantyMonths(defaultInt(supplierProduct.getWarrantyMonths()));
        entity.setEffectiveDate(supplierProduct.getPriceEffectiveDate());
        entity.setSourceType("SUPPLIER_PRODUCT");
        entity.setSourceId(supplierProduct.getId());
        entity.setRemark(supplierProduct.getPriceRemark());
        mapper.insert(entity);
    }

    private void fill(SupplierProductPriceHistory entity, SupplierProductPriceHistorySaveRequest request) {
        entity.setSupplierProductId(request.getSupplierProductId());
        entity.setQuotePrice(defaultDecimal(request.getQuotePrice()));
        entity.setTaxRate(defaultDecimal(request.getTaxRate()));
        entity.setDeliveryCycleDays(defaultInt(request.getDeliveryCycleDays()));
        entity.setWarrantyMonths(defaultInt(request.getWarrantyMonths()));
        entity.setEffectiveDate(request.getEffectiveDate());
        entity.setExpireDate(request.getExpireDate());
        entity.setSourceType(request.getSourceType());
        entity.setSourceId(request.getSourceId());
        entity.setRemark(request.getRemark());

        SupplierProduct supplierProduct = supplierProductMapper.selectById(request.getSupplierProductId());
        if (supplierProduct != null) {
            supplierProduct.setQuotePrice(entity.getQuotePrice());
            supplierProduct.setTaxRate(entity.getTaxRate());
            supplierProduct.setDeliveryCycleDays(entity.getDeliveryCycleDays());
            supplierProduct.setWarrantyMonths(entity.getWarrantyMonths());
            supplierProduct.setPriceEffectiveDate(entity.getEffectiveDate());
            supplierProduct.setPriceRemark(entity.getRemark());
            supplierProductMapper.updateById(supplierProduct);
        }
    }

    private BigDecimal defaultDecimal(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    private Integer defaultInt(Integer value) {
        return value == null ? 0 : value;
    }
}
