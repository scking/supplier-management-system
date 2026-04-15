package com.songchao.supplier.modules.supplierproduct.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.songchao.supplier.common.exception.BizException;
import com.songchao.supplier.modules.supplierproduct.dto.SupplierProductSaveRequest;
import com.songchao.supplier.modules.supplierproduct.entity.SupplierProduct;
import com.songchao.supplier.modules.supplierproduct.mapper.SupplierProductMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierProductService {
    private final SupplierProductMapper mapper;
    private final SupplierProductPriceHistoryService priceHistoryService;

    public SupplierProductService(
            SupplierProductMapper mapper,
            SupplierProductPriceHistoryService priceHistoryService
    ) {
        this.mapper = mapper;
        this.priceHistoryService = priceHistoryService;
    }

    public List<SupplierProduct> list(Long supplierId, Long productId) {
        LambdaQueryWrapper<SupplierProduct> wrapper = new LambdaQueryWrapper<>();
        if (supplierId != null) {
            wrapper.eq(SupplierProduct::getSupplierId, supplierId);
        }
        if (productId != null) {
            wrapper.eq(SupplierProduct::getProductId, productId);
        }
        wrapper.orderByDesc(SupplierProduct::getId);
        return mapper.selectList(wrapper);
    }

    public SupplierProduct create(SupplierProductSaveRequest request) {
        LambdaQueryWrapper<SupplierProduct> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SupplierProduct::getSupplierId, request.getSupplierId())
                .eq(SupplierProduct::getProductId, request.getProductId());
        if (mapper.selectCount(wrapper) > 0) {
            throw new BizException("该供应商与产品的关系已存在");
        }
        SupplierProduct entity = new SupplierProduct();
        entity.setSupplierId(request.getSupplierId());
        entity.setProductId(request.getProductId());
        entity.setSupplyStatus(request.getSupplyStatus() == null || request.getSupplyStatus().isBlank() ? "ACTIVE" : request.getSupplyStatus());
        entity.setQuotePrice(request.getQuotePrice());
        entity.setCurrency(request.getCurrency() == null || request.getCurrency().isBlank() ? "CNY" : request.getCurrency());
        entity.setPriceEffectiveDate(request.getPriceEffectiveDate());
        entity.setDeliveryCycleDays(request.getDeliveryCycleDays());
        entity.setWarrantyMonths(request.getWarrantyMonths());
        entity.setMinOrderQty(request.getMinOrderQty());
        entity.setTaxRate(request.getTaxRate());
        entity.setPriceRemark(request.getPriceRemark());
        mapper.insert(entity);
        priceHistoryService.createBySupplierProduct(entity);
        return entity;
    }

    public SupplierProduct update(Long id, SupplierProductSaveRequest request) {
        SupplierProduct entity = getById(id);
        LambdaQueryWrapper<SupplierProduct> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SupplierProduct::getSupplierId, request.getSupplierId())
                .eq(SupplierProduct::getProductId, request.getProductId())
                .ne(SupplierProduct::getId, id);
        if (mapper.selectCount(wrapper) > 0) {
            throw new BizException("该供应商与产品的关系已存在");
        }
        entity.setSupplierId(request.getSupplierId());
        entity.setProductId(request.getProductId());
        entity.setSupplyStatus(request.getSupplyStatus() == null || request.getSupplyStatus().isBlank() ? "ACTIVE" : request.getSupplyStatus());
        entity.setQuotePrice(request.getQuotePrice());
        entity.setCurrency(request.getCurrency() == null || request.getCurrency().isBlank() ? "CNY" : request.getCurrency());
        entity.setPriceEffectiveDate(request.getPriceEffectiveDate());
        entity.setDeliveryCycleDays(request.getDeliveryCycleDays());
        entity.setWarrantyMonths(request.getWarrantyMonths());
        entity.setMinOrderQty(request.getMinOrderQty());
        entity.setTaxRate(request.getTaxRate());
        entity.setPriceRemark(request.getPriceRemark());
        mapper.updateById(entity);
        return entity;
    }

    public void delete(Long id) {
        mapper.deleteById(getById(id).getId());
    }

    private SupplierProduct getById(Long id) {
        SupplierProduct entity = mapper.selectById(id);
        if (entity == null) {
            throw new BizException("供应关系不存在", 404);
        }
        return entity;
    }
}
