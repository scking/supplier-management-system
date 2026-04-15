package com.songchao.supplier.modules.product.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.songchao.supplier.common.exception.BizException;
import com.songchao.supplier.modules.product.dto.ProductSaveRequest;
import com.songchao.supplier.modules.product.entity.Product;
import com.songchao.supplier.modules.product.mapper.ProductMapper;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductMapper mapper;

    public ProductService(ProductMapper mapper) {
        this.mapper = mapper;
    }

    public List<Product> list(String keyword) {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isBlank()) {
            wrapper.and(w -> w.like(Product::getProductName, keyword)
                    .or().like(Product::getProductCode, keyword)
                    .or().like(Product::getBrand, keyword)
                    .or().like(Product::getModel, keyword));
        }
        wrapper.orderByDesc(Product::getId);
        return mapper.selectList(wrapper);
    }

    public Product create(ProductSaveRequest request) {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getProductCode, request.getProductCode());
        if (mapper.selectCount(wrapper) > 0) {
            throw new BizException("产品编号已存在");
        }
        Product entity = new Product();
        fill(entity, request);
        if (entity.getStatus() == null || entity.getStatus().isBlank()) {
            entity.setStatus("ENABLED");
        }
        try {
            mapper.insert(entity);
        } catch (DuplicateKeyException exception) {
            throw new BizException("产品编号已存在", 409);
        }
        return entity;
    }

    public Product update(Long id, ProductSaveRequest request) {
        Product entity = getById(id);
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getProductCode, request.getProductCode()).ne(Product::getId, id);
        if (mapper.selectCount(wrapper) > 0) {
            throw new BizException("产品编号已存在");
        }
        fill(entity, request);
        if (entity.getStatus() == null || entity.getStatus().isBlank()) {
            entity.setStatus("ENABLED");
        }
        mapper.updateById(entity);
        return entity;
    }

    public void delete(Long id) {
        mapper.deleteById(getById(id).getId());
    }

    private Product getById(Long id) {
        Product entity = mapper.selectById(id);
        if (entity == null) {
            throw new BizException("产品不存在", 404);
        }
        return entity;
    }

    private void fill(Product entity, ProductSaveRequest request) {
        entity.setProductCode(request.getProductCode());
        entity.setProductName(request.getProductName());
        entity.setProductCategory(request.getProductCategory());
        entity.setBrand(request.getBrand());
        entity.setModel(request.getModel());
        entity.setUnit(request.getUnit());
        entity.setStatus(request.getStatus());
        entity.setRemark(request.getRemark());
    }
}
