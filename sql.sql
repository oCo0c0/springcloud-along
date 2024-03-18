## 建表语句
CREATE TABLE shop_order (
                            oid BIGINT AUTO_INCREMENT PRIMARY KEY,
                            uid INT,
                            username VARCHAR(255),
                            pid INT,
                            pname VARCHAR(255),
                            pprice DOUBLE,
                            number INT
);

CREATE TABLE shop_product (
                              pid INT AUTO_INCREMENT PRIMARY KEY,
                              pname VARCHAR(255),
                              pprice DOUBLE,
                              stock INT
);

CREATE TABLE shop_user (
                           uid INT AUTO_INCREMENT PRIMARY KEY,
                           username VARCHAR(255),
                           password VARCHAR(255),
                           telephone VARCHAR(255)
);