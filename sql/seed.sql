SET NAMES utf8mb4;

INSERT INTO sup_supplier (
  supplier_code, supplier_name, supplier_type, status, credit_code,
  legal_person, contact_person, contact_phone, city, cooperation_level, remark
) VALUES
('SUP-2026-0001', '新疆智联设备有限公司', '生产商', 'NORMAL', '91650100MA12345678', '李国强', '王磊', '13800000001', '乌鲁木齐', 'A', '核心硬件供应商'),
('SUP-2026-0002', '上海云启工业技术有限公司', '代理商', 'NORMAL', '91310000MA87654321', '赵海峰', '陈静', '13800000002', '上海', 'B', '自动化产品供应商'),
('SUP-2026-0003', '深圳创源系统集成有限公司', '集成商', 'FROZEN', '91440300MA23456789', '杨涛', '刘娜', '13800000003', '深圳', 'B', '曾出现延期交付');

INSERT INTO sup_product (
  product_code, product_name, product_category, brand, model, unit, status, remark
) VALUES
('PRD-2026-0001', '工业交换机', '网络设备', 'H3C', 'IE4320', '台', 'ENABLED', '用于现场工业网络通信'),
('PRD-2026-0002', '数据采集终端', '采集设备', 'Inspur', 'DAQ-560', '套', 'ENABLED', '支持多协议采集'),
('PRD-2026-0003', 'UPS 电源', '电力设备', 'APC', 'SURT1000', '台', 'ENABLED', '用于关键节点供电保障');

INSERT INTO sup_product_attachment (
  product_id, file_name, file_path, file_type, file_size, attachment_type, remark, uploaded_by
) VALUES
(1, '工业交换机技术白皮书.pdf', '/demo/product/industrial-switch-whitepaper.pdf', 'application/pdf', 245760, '技术资料', '用于招采技术核对', 1),
(2, '数据采集终端接口说明.docx', '/demo/product/daq-api-guide.docx', 'application/vnd.openxmlformats-officedocument.wordprocessingml.document', 128000, '接口文档', '含 Modbus/TCP 说明', 1);

INSERT INTO sup_purchase_req_item (
  req_id, product_id, product_name, specification, unit, qty, budget_price, budget_amount, technical_requirements, remark
) VALUES
(1, 1, '工业交换机', 'IE4320 / 8光16电', '台', 20, 8500.00, 170000.00, '支持工业级宽温和环网冗余', '一期主干网络设备'),
(1, 2, '数据采集终端', 'DAQ-560 / 16路采集', '套', 10, 22000.00, 220000.00, '支持多协议采集和远程升级', '与前端采集点同步建设'),
(2, 3, 'UPS 电源', 'SURT1000 / 在线式', '台', 8, 9500.00, 76000.00, '支持关键节点不少于 2 小时供电', '用于监测点供电保障');

INSERT INTO sup_purchase_req (
  req_no, project_id, project_name, req_title, req_status,
  applicant_id, applicant_name, dept_id, dept_name, required_date, total_amount, remark
) VALUES
('REQ-2026-0001', 101, '智能交调升级项目', '一期网络与采集设备采购', 'APPROVED', 1, '系统管理员', 10, '采购部', '2026-05-15', 680000.00, '项目一期核心设备采购'),
('REQ-2026-0002', 102, '隧道监测改造项目', 'UPS 及网络补强采购', 'DRAFT', 2, '张凯', 20, '项目部', '2026-06-10', 220000.00, '补强类采购需求');

INSERT INTO sup_inquiry (
  inquiry_no, project_id, req_id, inquiry_title, status, deadline_time, created_by, created_dept_id, remark
) VALUES
('INQ-2026-0001', 101, 1, '一期网络与采集设备询价', 'QUOTING', '2026-04-30 18:00:00', 1, 10, '优先选择交付稳定供应商');

INSERT INTO sup_contract (
  contract_no, project_id, project_name, supplier_id, supplier_name, inquiry_id,
  contract_title, contract_amount, tax_rate, sign_date, effective_date, expire_date, contract_status, payment_status, created_by, created_dept_id, remark
) VALUES
('CON-2026-0001', 101, '智能交调升级项目', 1, '新疆智联设备有限公司', 1,
 '一期工业交换机采购合同', 320000.00, 13.00, '2026-04-01', '2026-04-01', '2027-03-31', 'ACTIVE', 'PARTIAL', 1, 10, '已完成首付款');

INSERT INTO sup_quote (
  inquiry_id, supplier_id, supplier_name, quote_status, total_amount, delivery_cycle_days, warranty_months,
  quote_time, price_score, delivery_score, warranty_score, service_score, total_score, compare_rank, remark
) VALUES
(1, 1, '新疆智联设备有限公司', 'SUBMITTED', 390000.00, 12, 24, '2026-04-12 10:00:00', 100.00, 100.00, 80.00, 86.00, 93.20, 1, '本地服务能力强'),
(1, 2, '上海云启工业技术有限公司', 'SUBMITTED', 405000.00, 18, 36, '2026-04-12 11:00:00', 96.30, 66.67, 100.00, 82.00, 88.05, 2, '质保周期更长');

INSERT INTO sup_quote_item (
  quote_id, inquiry_id, product_name, specification, brand, unit, qty, unit_price, line_amount, tax_rate, delivery_cycle_days, warranty_months, remark
) VALUES
(1, 1, '工业交换机', 'IE4320 / 8光16电', 'H3C', '台', 20, 8500.00, 170000.00, 13.00, 12, 24, '含安装调试支持'),
(1, 1, '数据采集终端', 'DAQ-560 / 16路采集', 'Inspur', '套', 10, 22000.00, 220000.00, 13.00, 12, 24, '支持远程升级'),
(2, 1, '工业交换机', 'IE4320 / 8光16电', 'H3C', '台', 20, 8800.00, 176000.00, 13.00, 18, 36, '含延保服务'),
(2, 1, '数据采集终端', 'DAQ-560 / 16路采集', 'Inspur', '套', 10, 22900.00, 229000.00, 13.00, 18, 36, '质保更长');

INSERT INTO sup_supplier_product (
  supplier_id, product_id, supply_status, quote_price, currency, price_effective_date, delivery_cycle_days, warranty_months, min_order_qty, tax_rate, price_remark
) VALUES
(1, 1, 'ACTIVE', 8500.00, 'CNY', '2026-01-01', 12, 24, 1.00, 13.00, '主推工业交换机报价'),
(2, 1, 'ACTIVE', 8800.00, 'CNY', '2026-02-01', 18, 36, 1.00, 13.00, '含延保服务报价');

INSERT INTO sup_supplier_product_price_hist (
  supplier_product_id, quote_price, tax_rate, delivery_cycle_days, warranty_months, effective_date, expire_date, source_type, source_id, remark
) VALUES
(1, 9000.00, 13.00, 15, 18, '2025-10-01', '2025-12-31', 'MANUAL', 1001, '2025年四季度价格'),
(1, 8700.00, 13.00, 13, 24, '2026-01-01', '2026-03-31', 'MANUAL', 1002, '2026年一季度优化价格'),
(1, 8500.00, 13.00, 12, 24, '2026-04-01', NULL, 'MANUAL', 1003, '2026年二季度当前价格'),
(2, 9100.00, 13.00, 20, 24, '2025-11-01', '2026-01-31', 'MANUAL', 1004, '初始报价'),
(2, 8800.00, 13.00, 18, 36, '2026-02-01', NULL, 'MANUAL', 1005, '延保后更新报价');
