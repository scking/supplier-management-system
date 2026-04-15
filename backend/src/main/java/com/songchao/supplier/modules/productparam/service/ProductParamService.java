package com.songchao.supplier.modules.productparam.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.songchao.supplier.common.exception.BizException;
import com.songchao.supplier.modules.productparam.dto.ProductParamSaveRequest;
import com.songchao.supplier.modules.productparam.entity.ProductParam;
import com.songchao.supplier.modules.productparam.mapper.ProductParamMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductParamService {
    private final ProductParamMapper mapper;

    public ProductParamService(ProductParamMapper mapper) {
        this.mapper = mapper;
    }

    public List<ProductParam> list(Long productId) {
        LambdaQueryWrapper<ProductParam> wrapper = new LambdaQueryWrapper<>();
        if (productId != null) {
            wrapper.eq(ProductParam::getProductId, productId);
        }
        wrapper.orderByAsc(ProductParam::getSortNo).orderByDesc(ProductParam::getId);
        return mapper.selectList(wrapper);
    }

    public ProductParam create(ProductParamSaveRequest request) {
        ProductParam entity = new ProductParam();
        fill(entity, request);
        mapper.insert(entity);
        return entity;
    }

    public ProductParam update(Long id, ProductParamSaveRequest request) {
        ProductParam entity = getById(id);
        fill(entity, request);
        mapper.updateById(entity);
        return entity;
    }

    public void delete(Long id) {
        mapper.deleteById(getById(id).getId());
    }

    private ProductParam getById(Long id) {
        ProductParam entity = mapper.selectById(id);
        if (entity == null) {
            throw new BizException("产品参数不存在", 404);
        }
        return entity;
    }

    private void fill(ProductParam entity, ProductParamSaveRequest request) {
        entity.setProductId(request.getProductId());
        entity.setParamName(request.getParamName());
        entity.setParamValue(request.getParamValue());
        entity.setParamUnit(request.getParamUnit());
        entity.setSortNo(request.getSortNo() == null ? 0 : request.getSortNo());
    }
}
