CREATE
DATABASE IF NOT EXISTS auction_management;
USE
auction_management;

CREATE TABLE product_type
(
    id   INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE product
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    price DOUBLE NOT NULL,
    status     VARCHAR(255) NOT NULL,
    id_loai_sp INT,
    FOREIGN KEY (id_loai_sp) REFERENCES product_type (id) ON DELETE SET NULL
);

INSERT INTO product_type (name)
VALUES ('Điện thoại'),
       ('Tủ lạnh'),
       ('Tivi'),
       ('Máy giặt');

INSERT INTO product (name, price, status, id_loai_sp)
VALUES ('iPhone 13', 12000000, 'Chờ duyệt', 1),
       ('iPhone 15 plus', 15000000, 'Chờ duyệt', 1),
       ('Tủ lạnh Toshiba', 2200000, 'Đang đấu giá', 2),
       ('iPhone Xs', 12000000, 'Chờ duyệt', 1),
       ('Tivi Sony 42 inch', 12800000, 'Đã bán', 3),
       ('Máy giặt LG 6kg', 8900000, 'Chờ duyệt', 4),
       ('iPhone 17 Pro Max', 26900000, 'Đang đấu giá', 1);