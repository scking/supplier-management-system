CREATE TABLE IF NOT EXISTS sup_supplier (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  supplier_code VARCHAR(64) NOT NULL,
  supplier_name VARCHAR(255) NOT NULL,
  supplier_type VARCHAR(32) NOT NULL,
  status VARCHAR(32) NOT NULL DEFAULT 'DRAFT',
  credit_code VARCHAR(64),
  legal_person VARCHAR(64),
  contact_person VARCHAR(64),
  contact_phone VARCHAR(32),
  contact_email VARCHAR(128),
  province VARCHAR(64),
  city VARCHAR(64),
  address VARCHAR(255),
  bank_name VARCHAR(128),
  bank_account VARCHAR(128),
  taxpayer_type VARCHAR(32),
  cooperation_level VARCHAR(32),
  remark VARCHAR(500),
  created_by BIGINT,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_by BIGINT,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted TINYINT NOT NULL DEFAULT 0,
  UNIQUE KEY uk_sup_supplier_code (supplier_code),
  UNIQUE KEY uk_sup_supplier_credit_code (credit_code),
  KEY idx_sup_supplier_name (supplier_name),
  KEY idx_sup_supplier_status (status)
);

CREATE TABLE IF NOT EXISTS sup_supplier_qualification (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  supplier_id BIGINT NOT NULL,
  qualification_type VARCHAR(64) NOT NULL,
  qualification_name VARCHAR(128) NOT NULL,
  qualification_no VARCHAR(128),
  issued_by VARCHAR(128),
  issue_date DATE,
  expire_date DATE,
  file_id BIGINT,
  status VARCHAR(32) DEFAULT 'VALID',
  remark VARCHAR(255),
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  KEY idx_sup_supplier_qualification_supplier (supplier_id),
  KEY idx_sup_supplier_qualification_expire (expire_date)
);

CREATE TABLE IF NOT EXISTS sup_product (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  product_code VARCHAR(64) NOT NULL,
  product_name VARCHAR(255) NOT NULL,
  product_category VARCHAR(64),
  brand VARCHAR(64),
  model VARCHAR(128),
  unit VARCHAR(32),
  status VARCHAR(32) NOT NULL DEFAULT 'ENABLED',
  remark VARCHAR(500),
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  UNIQUE KEY uk_sup_product_code (product_code),
  KEY idx_sup_product_name (product_name)
);

CREATE TABLE IF NOT EXISTS sup_product_param (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  product_id BIGINT NOT NULL,
  param_name VARCHAR(128) NOT NULL,
  param_value VARCHAR(255),
  param_unit VARCHAR(32),
  sort_no INT DEFAULT 0,
  KEY idx_sup_product_param_product (product_id)
);

CREATE TABLE IF NOT EXISTS sup_product_attachment (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  product_id BIGINT NOT NULL,
  file_name VARCHAR(255) NOT NULL,
  file_path VARCHAR(500) NOT NULL,
  file_type VARCHAR(128),
  file_size BIGINT DEFAULT 0,
  attachment_type VARCHAR(64),
  remark VARCHAR(255),
  uploaded_by BIGINT,
  uploaded_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  KEY idx_sup_product_attachment_product (product_id)
);

CREATE TABLE IF NOT EXISTS sup_supplier_product (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  supplier_id BIGINT NOT NULL,
  product_id BIGINT NOT NULL,
  supply_status VARCHAR(32) NOT NULL DEFAULT 'ACTIVE',
  quote_price DECIMAL(18,2) DEFAULT 0,
  currency VARCHAR(16) DEFAULT 'CNY',
  price_effective_date DATE,
  delivery_cycle_days INT DEFAULT 0,
  warranty_months INT DEFAULT 0,
  min_order_qty DECIMAL(18,2) DEFAULT 0,
  tax_rate DECIMAL(8,2) DEFAULT 0,
  price_remark VARCHAR(255),
  last_quote_at DATETIME,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  KEY idx_sup_supplier_product_supplier (supplier_id),
  KEY idx_sup_supplier_product_product (product_id)
);

CREATE TABLE IF NOT EXISTS sup_supplier_product_price_hist (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  supplier_product_id BIGINT NOT NULL,
  quote_price DECIMAL(18,2) DEFAULT 0,
  tax_rate DECIMAL(8,2) DEFAULT 0,
  delivery_cycle_days INT DEFAULT 0,
  warranty_months INT DEFAULT 0,
  effective_date DATE,
  expire_date DATE,
  source_type VARCHAR(32),
  source_id BIGINT,
  remark VARCHAR(255),
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  KEY idx_sup_supplier_product_price_hist_rel (supplier_product_id)
);

CREATE TABLE IF NOT EXISTS sup_purchase_req (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  req_no VARCHAR(64) NOT NULL,
  project_id BIGINT,
  project_name VARCHAR(255),
  req_title VARCHAR(255) NOT NULL,
  req_status VARCHAR(32) NOT NULL DEFAULT 'DRAFT',
  applicant_id BIGINT,
  applicant_name VARCHAR(64),
  dept_id BIGINT,
  dept_name VARCHAR(64),
  required_date DATE,
  total_amount DECIMAL(18,2) DEFAULT 0,
  remark VARCHAR(500),
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  UNIQUE KEY uk_sup_purchase_req_no (req_no),
  KEY idx_sup_purchase_req_project (project_id),
  KEY idx_sup_purchase_req_status (req_status)
);

CREATE TABLE IF NOT EXISTS sup_purchase_req_item (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  req_id BIGINT NOT NULL,
  product_id BIGINT,
  product_name VARCHAR(255) NOT NULL,
  specification VARCHAR(255),
  unit VARCHAR(32),
  qty DECIMAL(18,2) DEFAULT 0,
  budget_price DECIMAL(18,2) DEFAULT 0,
  budget_amount DECIMAL(18,2) DEFAULT 0,
  technical_requirements VARCHAR(500),
  remark VARCHAR(255),
  KEY idx_sup_purchase_req_item_req (req_id)
);

CREATE TABLE IF NOT EXISTS sup_inquiry (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  inquiry_no VARCHAR(64) NOT NULL,
  project_id BIGINT,
  req_id BIGINT,
  inquiry_title VARCHAR(255) NOT NULL,
  status VARCHAR(32) NOT NULL DEFAULT 'DRAFT',
  deadline_time DATETIME,
  recommended_supplier_id BIGINT,
  recommended_reason VARCHAR(500),
  created_by BIGINT,
  created_dept_id BIGINT,
  remark VARCHAR(255),
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY uk_sup_inquiry_no (inquiry_no),
  KEY idx_sup_inquiry_req (req_id),
  KEY idx_sup_inquiry_status (status)
);

CREATE TABLE IF NOT EXISTS sup_quote (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  inquiry_id BIGINT NOT NULL,
  supplier_id BIGINT NOT NULL,
  supplier_name VARCHAR(255) NOT NULL,
  quote_status VARCHAR(32) NOT NULL DEFAULT 'DRAFT',
  total_amount DECIMAL(18,2) DEFAULT 0,
  delivery_cycle_days INT DEFAULT 0,
  warranty_months INT DEFAULT 0,
  quote_time DATETIME,
  price_score DECIMAL(8,2) DEFAULT 0,
  delivery_score DECIMAL(8,2) DEFAULT 0,
  warranty_score DECIMAL(8,2) DEFAULT 0,
  service_score DECIMAL(8,2) DEFAULT 0,
  total_score DECIMAL(8,2) DEFAULT 0,
  compare_rank INT DEFAULT 0,
  file_id BIGINT,
  remark VARCHAR(255),
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  KEY idx_sup_quote_inquiry (inquiry_id),
  KEY idx_sup_quote_supplier (supplier_id)
);

CREATE TABLE IF NOT EXISTS sup_quote_item (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  quote_id BIGINT NOT NULL,
  inquiry_id BIGINT NOT NULL,
  product_name VARCHAR(255) NOT NULL,
  specification VARCHAR(255),
  brand VARCHAR(128),
  unit VARCHAR(32),
  qty DECIMAL(18,2) DEFAULT 0,
  unit_price DECIMAL(18,2) DEFAULT 0,
  line_amount DECIMAL(18,2) DEFAULT 0,
  tax_rate DECIMAL(8,2) DEFAULT 0,
  delivery_cycle_days INT DEFAULT 0,
  warranty_months INT DEFAULT 0,
  remark VARCHAR(255),
  KEY idx_sup_quote_item_quote (quote_id),
  KEY idx_sup_quote_item_inquiry (inquiry_id)
);

CREATE TABLE IF NOT EXISTS sup_contract (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  contract_no VARCHAR(64) NOT NULL,
  project_id BIGINT,
  project_name VARCHAR(255),
  supplier_id BIGINT NOT NULL,
  supplier_name VARCHAR(255) NOT NULL,
  inquiry_id BIGINT,
  contract_title VARCHAR(255) NOT NULL,
  contract_amount DECIMAL(18,2) DEFAULT 0,
  tax_rate DECIMAL(8,2) DEFAULT 0,
  sign_date DATE,
  effective_date DATE,
  expire_date DATE,
  contract_status VARCHAR(32) NOT NULL DEFAULT 'DRAFT',
  payment_status VARCHAR(32) DEFAULT 'UNPAID',
  created_by BIGINT,
  created_dept_id BIGINT,
  remark VARCHAR(500),
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY uk_sup_contract_no (contract_no),
  KEY idx_sup_contract_supplier (supplier_id),
  KEY idx_sup_contract_project (project_id)
);

CREATE TABLE IF NOT EXISTS sup_contract_attachment (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  contract_id BIGINT NOT NULL,
  file_name VARCHAR(255) NOT NULL,
  file_path VARCHAR(500) NOT NULL,
  file_type VARCHAR(128),
  file_size BIGINT DEFAULT 0,
  uploaded_by BIGINT,
  uploaded_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  KEY idx_sup_contract_attachment_contract (contract_id)
);

CREATE TABLE IF NOT EXISTS sup_performance (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  contract_id BIGINT NOT NULL,
  project_id BIGINT,
  supplier_id BIGINT NOT NULL,
  performance_type VARCHAR(32) NOT NULL,
  node_name VARCHAR(128) NOT NULL,
  planned_date DATE,
  actual_date DATE,
  status VARCHAR(32) NOT NULL DEFAULT 'PENDING',
  responsible_user_id BIGINT,
  description VARCHAR(500),
  remark VARCHAR(255),
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  KEY idx_sup_performance_contract (contract_id),
  KEY idx_sup_performance_supplier (supplier_id)
);

CREATE TABLE IF NOT EXISTS sup_supplier_evaluation (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  supplier_id BIGINT NOT NULL,
  project_id BIGINT,
  contract_id BIGINT,
  quality_score DECIMAL(6,2) DEFAULT 0,
  delivery_score DECIMAL(6,2) DEFAULT 0,
  service_score DECIMAL(6,2) DEFAULT 0,
  price_score DECIMAL(6,2) DEFAULT 0,
  compliance_score DECIMAL(6,2) DEFAULT 0,
  total_score DECIMAL(6,2) DEFAULT 0,
  rating VARCHAR(32),
  evaluation_user_id BIGINT,
  evaluation_time DATETIME,
  remark VARCHAR(500),
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  KEY idx_sup_supplier_evaluation_supplier (supplier_id)
);

CREATE TABLE IF NOT EXISTS sup_risk_warning (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  warning_type VARCHAR(64) NOT NULL,
  supplier_id BIGINT,
  project_id BIGINT,
  biz_id BIGINT,
  warning_level VARCHAR(32) NOT NULL,
  warning_title VARCHAR(255) NOT NULL,
  warning_content VARCHAR(1000),
  status VARCHAR(32) NOT NULL DEFAULT 'OPEN',
  trigger_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  resolved_time DATETIME,
  handler_id BIGINT,
  remark VARCHAR(255),
  KEY idx_sup_risk_warning_supplier (supplier_id),
  KEY idx_sup_risk_warning_status (status)
);

CREATE TABLE IF NOT EXISTS sup_operation_log (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  trace_id VARCHAR(64),
  module_name VARCHAR(64) NOT NULL,
  biz_type VARCHAR(64),
  biz_id BIGINT,
  operation_type VARCHAR(64) NOT NULL,
  operation_desc VARCHAR(255),
  request_method VARCHAR(16),
  request_uri VARCHAR(255),
  operator_id BIGINT,
  operator_dept_id BIGINT,
  operator_name VARCHAR(64),
  operator_ip VARCHAR(64),
  before_data JSON,
  after_data JSON,
  result_status VARCHAR(32) NOT NULL,
  error_message VARCHAR(500),
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  KEY idx_sup_operation_log_module (module_name),
  KEY idx_sup_operation_log_operator (operator_id),
  KEY idx_sup_operation_log_created (created_at)
);
