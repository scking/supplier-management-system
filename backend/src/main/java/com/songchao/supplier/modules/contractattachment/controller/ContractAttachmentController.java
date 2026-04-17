package com.songchao.supplier.modules.contractattachment.controller;

import com.songchao.supplier.audit.operationlog.annotation.OperationLogRecord;
import com.songchao.supplier.common.api.ApiResponse;
import com.songchao.supplier.common.exception.BizException;
import com.songchao.supplier.modules.contractattachment.dto.ContractAttachmentSaveRequest;
import com.songchao.supplier.modules.contractattachment.entity.ContractAttachment;
import com.songchao.supplier.modules.contractattachment.service.ContractAttachmentService;
import com.songchao.supplier.security.auth.AuthContext;
import com.songchao.supplier.security.permission.RequirePermission;
import jakarta.validation.Valid;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/contract-attachments")
public class ContractAttachmentController {
    private final ContractAttachmentService service;

    public ContractAttachmentController(ContractAttachmentService service) {
        this.service = service;
    }

    @GetMapping
    @RequirePermission("supplier:contract:attachment:view")
    public ApiResponse<List<ContractAttachment>> list(@RequestParam(required = false) Long contractId) {
        return ApiResponse.ok(service.list(contractId));
    }

    @PostMapping
    @RequirePermission("supplier:contract:attachment:create")
    @OperationLogRecord(module = "合同管理", operation = "CREATE_ATTACHMENT", bizType = "CONTRACT_ATTACHMENT", description = "新增合同附件")
    public ApiResponse<ContractAttachment> create(@Valid @RequestBody ContractAttachmentSaveRequest request) {
        return ApiResponse.ok(service.create(request));
    }

    @PutMapping("/{id}")
    @RequirePermission("supplier:contract:attachment:create")
    @OperationLogRecord(module = "合同管理", operation = "UPDATE_ATTACHMENT", bizType = "CONTRACT_ATTACHMENT", description = "编辑合同附件")
    public ApiResponse<ContractAttachment> update(@PathVariable Long id, @Valid @RequestBody ContractAttachmentSaveRequest request) {
        return ApiResponse.ok(service.update(id, request));
    }

    @PostMapping("/upload")
    @RequirePermission("supplier:contract:attachment:create")
    @OperationLogRecord(module = "合同管理", operation = "UPLOAD_ATTACHMENT", bizType = "CONTRACT_ATTACHMENT", description = "上传合同附件")
    public ApiResponse<ContractAttachment> upload(
            @RequestParam Long contractId,
            @RequestParam("file") MultipartFile file
    ) throws Exception {
        Long uploadedBy = AuthContext.get() != null ? AuthContext.get().userId() : 1L;
        return ApiResponse.ok(service.upload(contractId, file, uploadedBy));
    }

    @GetMapping("/{id}/download")
    @RequirePermission("supplier:contract:attachment:view")
    public ResponseEntity<Resource> download(
            @PathVariable Long id,
            @RequestParam(defaultValue = "false") boolean inline
    ) {
        ContractAttachment attachment = service.getById(id);
        Path filePath = Path.of(attachment.getFilePath());
        if (!filePath.toFile().exists()) {
            throw new BizException("附件文件不存在", 404);
        }
        Resource resource = new FileSystemResource(filePath);
        MediaType mediaType = MediaTypeFactory.getMediaType(attachment.getFileName()).orElse(MediaType.APPLICATION_OCTET_STREAM);
        String disposition = inline ? "inline" : "attachment";
        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, disposition + "; filename*=UTF-8''" + java.net.URLEncoder.encode(attachment.getFileName(), StandardCharsets.UTF_8))
                .body(resource);
    }

    @DeleteMapping("/{id}")
    @RequirePermission("supplier:contract:attachment:create")
    @OperationLogRecord(module = "合同管理", operation = "DELETE_ATTACHMENT", bizType = "CONTRACT_ATTACHMENT", description = "删除合同附件")
    public ApiResponse<Boolean> delete(@PathVariable Long id) {
        service.delete(id);
        return ApiResponse.ok(true);
    }
}
