create database product_db;

use product_db;

CREATE TABLE `categories`
(
    `id`         varchar(36) NOT NULL,
    `name`       varchar(255) DEFAULT NULL,
    `parent_id`  varchar(255) DEFAULT NULL,
    `deleted`    tinyint(1)   DEFAULT NULL,
    `created_at` datetime     DEFAULT NULL,
    `created_by` varchar(255) DEFAULT NULL,
    `updated_at` datetime     DEFAULT NULL,
    `updated_by` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
);
INSERT INTO `categories`
VALUES ('28f5f2a9-529c-11f1-92d6-0250e40fc952',
        'Electronic devices', NULL,
        0, '2026-05-18 16:30:08',
        'SYSTEM', '2026-05-18 16:30:08',
        'SYSTEM'),
       ('5baad789-529c-11f1-92d6-0250e40fc952',
        'Watches', '28f5f2a9-529c-11f1-92d6-0250e40fc952',
        0, '2026-05-18 16:31:33', 'SYSTEM',
        '2026-05-18 16:31:33', 'SYSTEM');

CREATE TABLE `products`
(
    `id`          varchar(36)  NOT NULL,
    `name`        varchar(255) NOT NULL,
    `price`       decimal(19, 2) DEFAULT NULL,
    `stock`       int            DEFAULT NULL,
    `category_id` varchar(36)    DEFAULT NULL,
    `deleted`     tinyint(1)     DEFAULT NULL,
    `created_at`  datetime       DEFAULT NULL,
    `created_by`  varchar(255)   DEFAULT NULL,
    `updated_at`  datetime       DEFAULT NULL,
    `updated_by`  varchar(255)   DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `category_id` (`category_id`),
    CONSTRAINT `products_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`)
);

INSERT INTO `products`
VALUES ('019e57fb-282d-7ccf-8006-402ce83934f0', 'Chuot may tinh', 1000.00, 100, '28f5f2a9-529c-11f1-92d6-0250e40fc952',
        0, '2026-05-24 10:47:41', 'SYSTEM', '2026-05-24 10:47:41', 'SYSTEM')
     , ('019e57fc-bef3-7ea1-9081-f66ee03d31b7', 'Ban phim may tinh', 2000.00, 80,'28f5f2a9-529c-11f1-92d6-0250e40fc952',
        0, '2026-05-24 10:47:41', 'SYSTEM', '2026-05-24 10:47:41', 'SYSTEM')