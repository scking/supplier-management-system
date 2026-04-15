package com.songchao.supplier.modules.contractattachment.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.songchao.supplier.common.exception.BizException;
import com.songchao.supplier.modules.contractattachment.dto.ContractAttachmentSaveRequest;
import com.songchao.supplier.modules.contractattachment.entity.ContractAttachment;
import com.songchao.supplier.modules.contractattachment.mapper.ContractAttachmentMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ContractAttachmentService {
    private final ContractAttachmentMapper mapper;
    private final Path uploadRoot = Paths.get(System.getProperty("user.dir"), "uploads", "contracts");

    public ContractAttachmentService(ContractAttachmentMapper mapper) {
        this.mapper = mapper;
    }

    public List<ContractAttachment> list(Long contractId) {
        LambdaQueryWrapper<ContractAttachment> wrapper = new LambdaQueryWrapper<>();
        if (contractId != null) {
            wrapper.eq(ContractAttachment::getContractId, contractId);
        }
        wrapper.orderByDesc(ContractAttachment::getId);
        return mapper.selectList(wrapper);
    }

    public ContractAttachment create(ContractAttachmentSaveRequest request) {
        ContractAttachment entity = new ContractAttachment();
        fill(entity, request);
        entity.setUploadedAt(LocalDateTime.now().toString());
        mapper.insert(entity);
        return entity;
    }

    public ContractAttachment update(Long id, ContractAttachmentSaveRequest request) {
        ContractAttachment entity = getById(id);
        fill(entity, request);
        mapper.updateById(entity);
        return entity;
    }

    public void delete(Long id) {
        mapper.deleteById(getById(id).getId());
    }

    private void fill(ContractAttachment entity, ContractAttachmentSaveRequest request) {
        entity.setContractId(request.getContractId());
        entity.setFileName(request.getFileName());
        entity.setFilePath(request.getFilePath());
        entity.setFileType(request.getFileType());
        entity.setFileSize(request.getFileSize());
        entity.setUploadedBy(request.getUploadedBy());
    }

    public ContractAttachment upload(Long contractId, MultipartFile file, Long uploadedBy) throws IOException {
        if (contractId == null) {
            throw new BizException("合同ID不能为空");
        }
        if (file == null || file.isEmpty()) {
            throw new BizException("上传文件不能为空");
        }

        Files.createDirectories(uploadRoot);
        String originalFilename = StringUtils.hasText(file.getOriginalFilename()) ? file.getOriginalFilename() : "contract-file";
        String extension = "";
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex >= 0) {
            extension = originalFilename.substring(dotIndex);
        }
        String storedName = UUID.randomUUID().toString().replace("-", "") + extension;
        Path targetPath = uploadRoot.resolve(storedName);
        file.transferTo(targetPath);

        ContractAttachment entity = new ContractAttachment();
        entity.setContractId(contractId);
        entity.setFileName(originalFilename);
        entity.setFilePath(targetPath.toString());
        entity.setFileType(file.getContentType());
        entity.setFileSize(file.getSize());
        entity.setUploadedBy(uploadedBy);
        entity.setUploadedAt(LocalDateTime.now().toString());
        mapper.insert(entity);
        return entity;
    }

    public ContractAttachment getById(Long id) {
        ContractAttachment attachment = mapper.selectById(id);
        if (attachment == null) {
            throw new BizException("合同附件不存在");
        }
        return attachment;
    }
}
