CREATE TABLE krasnopolskyi_kzvo.table_goods (
                                                id INT PRIMARY KEY,
                                                name VARCHAR(64) NOT NULL,
                                                price DECIMAL(10,2) NOT NULL,
                                                type_of_goods TINYINT(1) NOT NULL CHECK (type_of_goods IN (0, 1))
);