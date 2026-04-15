package com.songchao.supplier.modules.supplier.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.songchao.supplier.common.exception.BizException;
import com.songchao.supplier.modules.supplier.dto.SupplierSaveRequest;
import com.songchao.supplier.modules.supplier.entity.Supplier;
import com.songchao.supplier.modules.supplier.mapper.SupplierMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierService {
    private final SupplierMapper supplierMapper;

    public SupplierService(SupplierMapper supplierMapper) {
        this.supplierMapper = supplierMapper;
    }

    public List<Supplier> list(String keyword, String status) {
        LambdaQueryWrapper<Supplier> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Supplier::getDeleted, 0);
        if (keyword != null && !keyword.isBlank()) {
            wrapper.and(w -> w.like(Supplier::getSupplierName, keyword)
                    .or().like(Supplier::getSupplierCode, keyword)
                    .or().like(Supplier::getContactPerson, keyword));
        }
        if (status != null && !status.isBlank()) {
            wrapper.eq(Supplier::getStatus, status);
        }
        wrapper.orderByDesc(Supplier::getId);
        return supplierMapper.selectList(wrapper);
    }

    public Supplier detail(Long id) {
        Supplier supplier = supplierMapper.selectById(id);
        if (supplier == null || Integer.valueOf(1).equals(supplier.getDeleted())) {
            throw new BizException("供应商不存在");
        }
        return supplier;
    }

    public Supplier create(SupplierSaveRequest request) {
        checkCodeDuplicate(request.getSupplierCode(), null);
        checkCreditCodeDuplicate(request.getCreditCode(), null);
        Supplier supplier = new Supplier();
        fillSupplier(supplier, request);
        supplier.setStatus("PENDING_APPROVAL");
        supplier.setDeleted(0);
        supplierMapper.insert(supplier);
        return supplier;
    }

    public Supplier update(Long id, SupplierSaveRequest request) {
        Supplier supplier = detail(id);
        checkCodeDuplicate(request.getSupplierCode(), id);
        checkCreditCodeDuplicate(request.getCreditCode(), id);
        fillSupplier(supplier, request);
        supplierMapper.updateById(supplier);
        return supplier;
    }

    public Supplier changeStatus(Long id, String status) {
        Supplier supplier = detail(id);
        supplier.setStatus(status);
        supplierMapper.updateById(supplier);
        return supplier;
    }

    public void delete(Long id) {
        Supplier supplier = detail(id);
        supplier.setDeleted(1);
        supplierMapper.updateById(supplier);
    }

    private void checkCodeDuplicate(String supplierCode, Long excludeId) {
        LambdaQueryWrapper<Supplier> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Supplier::getSupplierCode, supplierCode).eq(Supplier::getDeleted, 0);
        if (excludeId != null) {
            wrapper.ne(Supplier::getId, excludeId);
        }
        Long count = supplierMapper.selectCount(wrapper);
        if (count != null && count > 0) {
            throw new BizException("供应商编号已存在");
        }
    }

    private void checkCreditCodeDuplicate(String creditCode, Long excludeId) {
        if (creditCode == null || creditCode.isBlank()) {
            return;
        }
        LambdaQueryWrapper<Supplier> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Supplier::getCreditCode, creditCode).eq(Supplier::getDeleted, 0);
        if (excludeId != null) {
            wrapper.ne(Supplier::getId, excludeId);
        }
        Long count = supplierMapper.selectCount(wrapper);
        if (count != null && count > 0) {
            throw new BizException("统一社会信用代码已存在", 409);
        }
    }

    private void fillSupplier(Supplier supplier, SupplierSaveRequest request) {
        supplier.setSupplierCode(request.getSupplierCode());
        supplier.setSupplierName(request.getSupplierName());
        supplier.setSupplierType(request.getSupplierType());
        supplier.setCreditCode(request.getCreditCode());
        supplier.setLegalPerson(request.getLegalPerson());
        supplier.setContactPerson(request.getContactPerson());
        supplier.setContactPhone(request.getContactPhone());
        supplier.setContactEmail(request.getContactEmail());
        supplier.setProvince(request.getProvince());
        supplier.setCity(request.getCity());
        supplier.setAddress(request.getAddress());
        supplier.setBankName(request.getBankName());
        supplier.setBankAccount(request.getBankAccount());
        supplier.setTaxpayerType(request.getTaxpayerType());
        supplier.setCooperationLevel(request.getCooperationLevel());
        supplier.setRemark(request.getRemark());
    }
}
