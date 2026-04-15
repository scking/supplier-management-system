package com.songchao.supplier.modules.productattachment.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.songchao.supplier.common.exception.BizException;
import com.songchao.supplier.modules.productattachment.dto.ProductAttachmentSaveRequest;
import com.songchao.supplier.modules.productattachment.entity.ProductAttachment;
import com.songchao.supplier.modules.productattachment.mapper.ProductAttachmentMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductAttachmentService {
    private final ProductAttachmentMapper mapper;

    public ProductAttachmentService(ProductAttachmentMapper mapper) {
        this.mapper = mapper;
    }

    public List<ProductAttachment> list(Long productId) {
        LambdaQueryWrapper<ProductAttachment> wrapper = new LambdaQueryWrapper<>();
        if (productId != null) {
            wrapper.eq(ProductAttachment::getProductId, productId);
        }
        wrapper.orderByDesc(ProductAttachment::getId);
        return mapper.selectList(wrapper);
    }

    public ProductAttachment create(ProductAttachmentSaveRequest request) {
        ProductAttachment entity = new ProductAttachment();
        fill(entity, request);
        entity.setUploadedAt(LocalDateTime.now().toString());
        mapper.insert(entity);
        return entity;
    }

    public ProductAttachment update(Long id, ProductAttachmentSaveRequest request) {
        ProductAttachment entity = getById(id);
        fill(entity, request);
        mapper.updateById(entity);
        return entity;
    }

    public void delete(Long id) {
        mapper.deleteById(getById(id).getId());
    }

    private ProductAttachment getById(Long id) {
        ProductAttachment entity = mapper.selectById(id);
        if (entity == null) {
            throw new BizException("产品附件不存在", 404);
        }
        return entity;
    }

    private void fill(ProductAttachment entity, ProductAttachmentSaveRequest request) {
        entity.setProductId(request.getProductId());
        entity.setFileName(request.getFileName());
        entity.setFilePath(request.getFilePath());
        entity.setFileType(request.getFileType());
        entity.setFileSize(request.getFileSize());
        entity.setAttachmentType(request.getAttachmentType());
        entity.setRemark(request.getRemark());
        entity.setUploadedBy(request.getUploadedBy());
    }
}
