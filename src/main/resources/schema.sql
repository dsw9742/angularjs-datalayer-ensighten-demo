DROP TABLE IF EXISTS PRODUCTS;

CREATE TABLE PRODUCTS(
  ID BIGINT PRIMARY KEY,
  NAME VARCHAR(256),
  DESCRIPTION VARCHAR(512),
  PRODUCT_URL VARCHAR(1028),
  IMAGE_URL VARCHAR(1028),
  THUMBNAIL_URL VARCHAR(1028),
  MANUFACTURER VARCHAR(256),
  SKU VARCHAR(128),
  COLOR VARCHAR(56),
  SIZE VARCHAR(12),
  PRICE DECIMAL(20, 2),
  CATEGORY VARCHAR(12)
);