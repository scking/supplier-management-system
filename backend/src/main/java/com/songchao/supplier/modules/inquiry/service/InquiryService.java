package com.songchao.supplier.modules.inquiry.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.songchao.supplier.common.exception.BizException;
import com.songchao.supplier.modules.inquiry.dto.InquirySaveRequest;
import com.songchao.supplier.modules.inquiry.entity.Inquiry;
import com.songchao.supplier.modules.inquiry.mapper.InquiryMapper;
import com.songchao.supplier.modules.quote.service.QuoteService;
import com.songchao.supplier.security.auth.AuthContext;
import com.songchao.supplier.security.permission.DataScopeHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InquiryService {
    private final InquiryMapper mapper;
    private final QuoteService quoteService;
    private final DataScopeHelper dataScopeHelper;

    public InquiryService(InquiryMapper mapper, QuoteService quoteService, DataScopeHelper dataScopeHelper) {
        this.mapper = mapper;
        this.quoteService = quoteService;
        this.dataScopeHelper = dataScopeHelper;
    }

    public List<Inquiry> list(String keyword, String status) {
        LambdaQueryWrapper<Inquiry> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isBlank()) {
            wrapper.and(w -> w.like(Inquiry::getInquiryNo, keyword)
                    .or().like(Inquiry::getInquiryTitle, keyword));
        }
        if (status != null && !status.isBlank()) {
            wrapper.eq(Inquiry::getStatus, status);
        }
        dataScopeHelper.applyDeptOrSelfScope(
                wrapper,
                scope -> {
                    if (AuthContext.get() != null && AuthContext.get().deptId() != null) {
                        scope.eq(Inquiry::getCreatedDeptId, AuthContext.get().deptId());
                    }
                },
                scope -> {
                    if (AuthContext.get() != null && AuthContext.get().userId() != null) {
                        scope.eq(Inquiry::getCreatedBy, AuthContext.get().userId());
                    }
                }
        );
        wrapper.orderByDesc(Inquiry::getId);
        return mapper.selectList(wrapper);
    }

    public Inquiry create(InquirySaveRequest request) {
        LambdaQueryWrapper<Inquiry> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Inquiry::getInquiryNo, request.getInquiryNo());
        if (mapper.selectCount(wrapper) > 0) {
            throw new BizException("询价单编号已存在");
        }
        Inquiry entity = new Inquiry();
        entity.setInquiryNo(request.getInquiryNo());
        entity.setProjectId(request.getProjectId());
        entity.setReqId(request.getReqId());
        entity.setInquiryTitle(request.getInquiryTitle());
        entity.setDeadlineTime(request.getDeadlineTime());
        if (AuthContext.get() != null) {
            entity.setCreatedBy(AuthContext.get().userId());
            entity.setCreatedDeptId(AuthContext.get().deptId());
        }
        entity.setRemark(request.getRemark());
        entity.setStatus("DRAFT");
        mapper.insert(entity);
        return entity;
    }

    public Inquiry update(Long id, InquirySaveRequest request) {
        Inquiry entity = getById(id);
        LambdaQueryWrapper<Inquiry> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Inquiry::getInquiryNo, request.getInquiryNo()).ne(Inquiry::getId, id);
        if (mapper.selectCount(wrapper) > 0) {
            throw new BizException("询价单编号已存在");
        }
        entity.setInquiryNo(request.getInquiryNo());
        entity.setProjectId(request.getProjectId());
        entity.setReqId(request.getReqId());
        entity.setInquiryTitle(request.getInquiryTitle());
        entity.setDeadlineTime(request.getDeadlineTime());
        entity.setRemark(request.getRemark());
        mapper.updateById(entity);
        return entity;
    }

    public Inquiry compare(Long id) {
        return quoteService.compare(id);
    }

    public void delete(Long id) {
        Inquiry entity = getById(id);
        quoteService.deleteByInquiryId(entity.getId());
        mapper.deleteById(entity.getId());
    }

    private Inquiry getById(Long id) {
        Inquiry entity = mapper.selectById(id);
        if (entity == null) {
            throw new BizException("询价单不存在", 404);
        }
        return entity;
    }
}
