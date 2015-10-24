DROP TABLE IF EXISTS PRODUCTS;
DROP TABLE IF EXISTS SHOPPING_CARTS;
DROP TABLE IF EXISTS SHOPPING_CART_ITEMS;
DROP TABLE IF EXISTS ROLES;
DROP TABLE IF EXISTS USERS;
DROP TABLE IF EXISTS USERS_ROLES;

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
  CATEGORY VARCHAR(12),
  PRICE DECIMAL(20, 2)
);

CREATE TABLE SHOPPING_CARTS(
  ID CHAR(36) PRIMARY KEY,
  BASE_PRICE DECIMAL(20, 2),
  VOUCHER_CODE VARCHAR(128),
  VOUCHER_DISCOUNT DECIMAL(3, 2),
  CURRENCY VARCHAR(64),
  TAX_RATE DECIMAL(4, 2),
  SHIPPING DECIMAL(20, 2),
  SHIPPING_METHOD VARCHAR(64),
  PRICE_WITH_TAX DECIMAL(20, 2),
  CART_TOTAL DECIMAL(20, 2),
  UPDATED TIMESTAMP
);

CREATE TABLE SHOPPING_CART_ITEMS(
  CART_ID CHAR(36),
  PRODUCT_ID BIGINT,
  QUANTITY INT,
  PRIMARY KEY(CART_ID, PRODUCT_ID)
);

CREATE TABLE ROLES(
  ID INT PRIMARY KEY,
  NAME VARCHAR(64)
);

CREATE TABLE USERS(
  ID CHAR(36) PRIMARY KEY,
  USERNAME VARCHAR(64),
  PASSWORD VARCHAR(255),
  ENABLED BOOLEAN
);

CREATE TABLE USERS_ROLES(
  USER_ID CHAR(36),
  ROLE_ID INT,
  PRIMARY KEY(USER_ID, ROLE_ID)
);