package com.songchao.supplier.modules.quote.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.songchao.supplier.common.exception.BizException;
import com.songchao.supplier.modules.evaluation.entity.SupplierEvaluation;
import com.songchao.supplier.modules.evaluation.mapper.SupplierEvaluationMapper;
import com.songchao.supplier.modules.inquiry.entity.Inquiry;
import com.songchao.supplier.modules.inquiry.mapper.InquiryMapper;
import com.songchao.supplier.modules.quote.dto.QuoteSaveRequest;
import com.songchao.supplier.modules.quote.entity.Quote;
import com.songchao.supplier.modules.quote.mapper.QuoteMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class QuoteService {
    private final QuoteMapper mapper;
    private final InquiryMapper inquiryMapper;
    private final QuoteItemService quoteItemService;
    private final SupplierEvaluationMapper evaluationMapper;

    public QuoteService(
            QuoteMapper mapper,
            InquiryMapper inquiryMapper,
            QuoteItemService quoteItemService,
            SupplierEvaluationMapper evaluationMapper
    ) {
        this.mapper = mapper;
        this.inquiryMapper = inquiryMapper;
        this.quoteItemService = quoteItemService;
        this.evaluationMapper = evaluationMapper;
    }

    public List<Quote> list(Long inquiryId) {
        LambdaQueryWrapper<Quote> wrapper = new LambdaQueryWrapper<>();
        if (inquiryId != null) {
            wrapper.eq(Quote::getInquiryId, inquiryId);
        }
        wrapper.orderByAsc(Quote::getCompareRank).orderByDesc(Quote::getTotalScore).orderByAsc(Quote::getTotalAmount).orderByDesc(Quote::getId);
        return mapper.selectList(wrapper);
    }

    public Quote create(QuoteSaveRequest request) {
        Quote entity = new Quote();
        fill(entity, request);
        entity.setQuoteStatus("SUBMITTED");
        entity.setQuoteTime(LocalDateTime.now().toString());
        mapper.insert(entity);
        quoteItemService.saveBatch(entity.getId(), entity.getInquiryId(), request.getItems());
        refreshQuoteSummary(entity, request);
        return entity;
    }

    public Quote update(Long id, QuoteSaveRequest request) {
        Quote entity = getById(id);
        fill(entity, request);
        mapper.updateById(entity);
        quoteItemService.deleteByQuoteId(id);
        quoteItemService.saveBatch(entity.getId(), entity.getInquiryId(), request.getItems());
        refreshQuoteSummary(entity, request);
        return entity;
    }

    public void delete(Long id) {
        Quote entity = getById(id);
        quoteItemService.deleteByQuoteId(entity.getId());
        mapper.deleteById(entity.getId());
    }

    public void deleteByInquiryId(Long inquiryId) {
        for (Quote quote : list(inquiryId)) {
            quoteItemService.deleteByQuoteId(quote.getId());
        }
        mapper.delete(new LambdaQueryWrapper<Quote>().eq(Quote::getInquiryId, inquiryId));
        quoteItemService.deleteByInquiryId(inquiryId);
    }

    public Inquiry recommend(Long inquiryId) {
        List<Quote> quotes = calculateScores(inquiryId);
        Quote best = ensureQuotes(inquiryId, quotes).get(0);
        Inquiry inquiry = inquiryMapper.selectById(inquiryId);
        if (inquiry == null) {
            throw new BizException("询价单不存在");
        }
        inquiry.setRecommendedSupplierId(best.getSupplierId());
        inquiry.setRecommendedReason("综合金额、供货周期、质保周期、历史评价进行推荐；当前第一名综合分为 " + best.getTotalScore());
        inquiry.setStatus("RECOMMENDED");
        inquiryMapper.updateById(inquiry);
        return inquiry;
    }

    public Inquiry compare(Long inquiryId) {
        List<Quote> quotes = calculateScores(inquiryId);
        Inquiry entity = inquiryMapper.selectById(inquiryId);
        if (entity == null) {
            throw new BizException("询价单不存在");
        }
        if (quotes.isEmpty()) {
            throw new BizException("当前询价单暂无报价，无法执行比价");
        }
        Quote best = quotes.get(0);
        entity.setStatus("COMPARED");
        entity.setRecommendedSupplierId(best.getSupplierId());
        entity.setRecommendedReason("多因子比价已完成：价格40%、交期25%、质保15%、历史评价20%；当前领先供应商为 " + best.getSupplierName());
        inquiryMapper.updateById(entity);
        return entity;
    }

    private List<Quote> calculateScores(Long inquiryId) {
        List<Quote> quotes = ensureQuotes(inquiryId, list(inquiryId));
        BigDecimal minAmount = quotes.stream()
                .map(Quote::getTotalAmount)
                .filter(value -> value != null)
                .min(Comparator.naturalOrder())
                .orElse(BigDecimal.ONE);
        int minDelivery = quotes.stream()
                .map(Quote::getDeliveryCycleDays)
                .filter(value -> value != null && value > 0)
                .min(Integer::compareTo)
                .orElse(1);
        int maxWarranty = quotes.stream()
                .map(Quote::getWarrantyMonths)
                .filter(value -> value != null)
                .max(Integer::compareTo)
                .orElse(1);

        for (Quote quote : quotes) {
            BigDecimal priceScore = scoreByMin(quote.getTotalAmount(), minAmount);
            BigDecimal deliveryScore = scoreByMin(BigDecimal.valueOf(defaultPositive(quote.getDeliveryCycleDays(), minDelivery)), BigDecimal.valueOf(minDelivery));
            BigDecimal warrantyScore = scoreByMax(BigDecimal.valueOf(defaultPositive(quote.getWarrantyMonths(), maxWarranty)), BigDecimal.valueOf(maxWarranty));
            BigDecimal serviceScore = resolveServiceScore(quote.getSupplierId());
            BigDecimal totalScore = priceScore.multiply(new BigDecimal("0.40"))
                    .add(deliveryScore.multiply(new BigDecimal("0.25")))
                    .add(warrantyScore.multiply(new BigDecimal("0.15")))
                    .add(serviceScore.multiply(new BigDecimal("0.20")))
                    .setScale(2, RoundingMode.HALF_UP);
            quote.setPriceScore(priceScore);
            quote.setDeliveryScore(deliveryScore);
            quote.setWarrantyScore(warrantyScore);
            quote.setServiceScore(serviceScore);
            quote.setTotalScore(totalScore);
        }

        quotes.sort(Comparator.comparing(Quote::getTotalScore, Comparator.nullsLast(Comparator.reverseOrder()))
                .thenComparing(Quote::getTotalAmount, Comparator.nullsLast(Comparator.naturalOrder())));

        for (int i = 0; i < quotes.size(); i++) {
            Quote quote = quotes.get(i);
            quote.setCompareRank(i + 1);
            mapper.updateById(quote);
        }
        return quotes;
    }

    private List<Quote> ensureQuotes(Long inquiryId, List<Quote> quotes) {
        if (quotes.isEmpty()) {
            throw new BizException("当前询价单暂无报价");
        }
        Inquiry inquiry = inquiryMapper.selectById(inquiryId);
        if (inquiry == null) {
            throw new BizException("询价单不存在");
        }
        return quotes;
    }

    private BigDecimal resolveServiceScore(Long supplierId) {
        LambdaQueryWrapper<SupplierEvaluation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SupplierEvaluation::getSupplierId, supplierId)
                .orderByDesc(SupplierEvaluation::getId);
        List<SupplierEvaluation> evaluations = evaluationMapper.selectList(wrapper);
        if (evaluations.isEmpty()) {
            return new BigDecimal("80.00");
        }
        BigDecimal total = evaluations.stream()
                .map(SupplierEvaluation::getTotalScore)
                .filter(value -> value != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        long count = evaluations.stream().map(SupplierEvaluation::getTotalScore).filter(value -> value != null).count();
        if (count == 0) {
            return new BigDecimal("80.00");
        }
        return total.divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP);
    }

    private BigDecimal scoreByMin(BigDecimal value, BigDecimal minValue) {
        if (value == null || value.compareTo(BigDecimal.ZERO) <= 0 || minValue.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal score = minValue.divide(value, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
        return score.min(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal scoreByMax(BigDecimal value, BigDecimal maxValue) {
        if (value == null || maxValue == null || maxValue.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal score = value.divide(maxValue, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
        return score.min(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP);
    }

    private int resolveDeliveryFromItems(Long quoteId, Integer fallback) {
        return quoteItemService.list(null, quoteId).stream()
                .map(item -> item.getDeliveryCycleDays() == null ? 0 : item.getDeliveryCycleDays())
                .filter(value -> value > 0)
                .max(Integer::compareTo)
                .orElse(defaultInt(fallback));
    }

    private int resolveWarrantyFromItems(Long quoteId, Integer fallback) {
        return quoteItemService.list(null, quoteId).stream()
                .map(item -> item.getWarrantyMonths() == null ? 0 : item.getWarrantyMonths())
                .filter(value -> value > 0)
                .min(Integer::compareTo)
                .orElse(defaultInt(fallback));
    }

    private int defaultInt(Integer value) {
        return value == null ? 0 : value;
    }

    private int defaultPositive(Integer value, int fallback) {
        return value == null || value <= 0 ? fallback : value;
    }

    private Quote getById(Long id) {
        Quote entity = mapper.selectById(id);
        if (entity == null) {
            throw new BizException("报价单不存在", 404);
        }
        return entity;
    }

    private void fill(Quote entity, QuoteSaveRequest request) {
        entity.setInquiryId(request.getInquiryId());
        entity.setSupplierId(request.getSupplierId());
        entity.setSupplierName(request.getSupplierName());
        entity.setTotalAmount(request.getTotalAmount());
        entity.setDeliveryCycleDays(defaultInt(request.getDeliveryCycleDays()));
        entity.setWarrantyMonths(defaultInt(request.getWarrantyMonths()));
        entity.setRemark(request.getRemark());
    }

    private void refreshQuoteSummary(Quote entity, QuoteSaveRequest request) {
        if (request.getItems() != null && !request.getItems().isEmpty()) {
            entity.setTotalAmount(quoteItemService.sumAmount(entity.getId()));
            entity.setDeliveryCycleDays(resolveDeliveryFromItems(entity.getId(), entity.getDeliveryCycleDays()));
            entity.setWarrantyMonths(resolveWarrantyFromItems(entity.getId(), entity.getWarrantyMonths()));
            mapper.updateById(entity);
        }
    }
}
