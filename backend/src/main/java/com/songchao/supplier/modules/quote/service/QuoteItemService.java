package com.songchao.supplier.modules.quote.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.songchao.supplier.modules.quote.dto.QuoteItemSaveRequest;
import com.songchao.supplier.modules.quote.entity.QuoteItem;
import com.songchao.supplier.modules.quote.mapper.QuoteItemMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class QuoteItemService {
    private final QuoteItemMapper mapper;

    public QuoteItemService(QuoteItemMapper mapper) {
        this.mapper = mapper;
    }

    public List<QuoteItem> list(Long inquiryId, Long quoteId) {
        LambdaQueryWrapper<QuoteItem> wrapper = new LambdaQueryWrapper<>();
        if (inquiryId != null) {
            wrapper.eq(QuoteItem::getInquiryId, inquiryId);
        }
        if (quoteId != null) {
            wrapper.eq(QuoteItem::getQuoteId, quoteId);
        }
        wrapper.orderByAsc(QuoteItem::getQuoteId).orderByAsc(QuoteItem::getId);
        return mapper.selectList(wrapper);
    }

    public void saveBatch(Long quoteId, Long inquiryId, List<QuoteItemSaveRequest> items) {
        if (items == null || items.isEmpty()) {
            return;
        }
        for (QuoteItemSaveRequest itemRequest : items) {
            QuoteItem entity = new QuoteItem();
            entity.setQuoteId(quoteId);
            entity.setInquiryId(inquiryId);
            entity.setProductName(itemRequest.getProductName());
            entity.setSpecification(itemRequest.getSpecification());
            entity.setBrand(itemRequest.getBrand());
            entity.setUnit(itemRequest.getUnit());
            entity.setQty(defaultDecimal(itemRequest.getQty()));
            entity.setUnitPrice(defaultDecimal(itemRequest.getUnitPrice()));
            entity.setLineAmount(resolveLineAmount(itemRequest));
            entity.setTaxRate(defaultDecimal(itemRequest.getTaxRate()));
            entity.setDeliveryCycleDays(itemRequest.getDeliveryCycleDays() == null ? 0 : itemRequest.getDeliveryCycleDays());
            entity.setWarrantyMonths(itemRequest.getWarrantyMonths() == null ? 0 : itemRequest.getWarrantyMonths());
            entity.setRemark(itemRequest.getRemark());
            mapper.insert(entity);
        }
    }

    public BigDecimal sumAmount(Long quoteId) {
        return list(null, quoteId).stream()
                .map(QuoteItem::getLineAmount)
                .filter(value -> value != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void deleteByQuoteId(Long quoteId) {
        mapper.delete(new LambdaQueryWrapper<QuoteItem>().eq(QuoteItem::getQuoteId, quoteId));
    }

    public void deleteByInquiryId(Long inquiryId) {
        mapper.delete(new LambdaQueryWrapper<QuoteItem>().eq(QuoteItem::getInquiryId, inquiryId));
    }

    private BigDecimal resolveLineAmount(QuoteItemSaveRequest itemRequest) {
        if (itemRequest.getLineAmount() != null) {
            return itemRequest.getLineAmount();
        }
        return defaultDecimal(itemRequest.getQty()).multiply(defaultDecimal(itemRequest.getUnitPrice()));
    }

    private BigDecimal defaultDecimal(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }
}
