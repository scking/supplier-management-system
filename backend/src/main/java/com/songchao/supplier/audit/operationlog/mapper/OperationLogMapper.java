package com.songchao.supplier.audit.operationlog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.songchao.supplier.audit.operationlog.entity.OperationLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OperationLogMapper extends BaseMapper<OperationLog> {
}
