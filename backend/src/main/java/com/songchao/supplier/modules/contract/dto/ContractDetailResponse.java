package com.songchao.supplier.modules.contract.dto;

import com.songchao.supplier.modules.contract.entity.Contract;
import com.songchao.supplier.modules.contractattachment.entity.ContractAttachment;

import java.util.List;

public class ContractDetailResponse {
    private Contract contract;
    private List<ContractAttachment> attachments;
    private String reminderStatus;
    private Long daysToExpire;

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public List<ContractAttachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<ContractAttachment> attachments) {
        this.attachments = attachments;
    }

    public String getReminderStatus() {
        return reminderStatus;
    }

    public void setReminderStatus(String reminderStatus) {
        this.reminderStatus = reminderStatus;
    }

    public Long getDaysToExpire() {
        return daysToExpire;
    }

    public void setDaysToExpire(Long daysToExpire) {
        this.daysToExpire = daysToExpire;
    }
}
