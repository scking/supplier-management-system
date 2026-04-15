package com.songchao.supplier;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan({
        "com.songchao.supplier.modules",
        "com.songchao.supplier.audit"
})
public class SupplierManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(SupplierManagementApplication.class, args);
    }
}
