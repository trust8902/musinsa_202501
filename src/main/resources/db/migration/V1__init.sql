CREATE SEQUENCE "PUBLIC"."BRAND_SEQ" START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE "PUBLIC"."CATEGORY_SEQ" START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE "PUBLIC"."PRODUCT_SEQ" START WITH 1 INCREMENT BY 1;

CREATE CACHED TABLE "PUBLIC"."BRAND"(
    "ID" BIGINT NOT NULL,
    "BRAND_NAME" CHARACTER VARYING(100) COMMENT '브랜드명' NOT NULL,
    "UPDATED_AT" TIMESTAMP(6) WITH TIME ZONE COMMENT '갱신일',
    "CREATED_AT" TIMESTAMP(6) WITH TIME ZONE COMMENT '등록일' NOT NULL
);
ALTER TABLE "PUBLIC"."BRAND" ADD CONSTRAINT "PUBLIC"."CONSTRAINT_3" PRIMARY KEY("ID");
ALTER TABLE "PUBLIC"."BRAND" ALTER COLUMN "ID" SET DEFAULT NEXT VALUE FOR "PUBLIC"."BRAND_SEQ";
CREATE CACHED TABLE "PUBLIC"."CATEGORY"(
    "ID" BIGINT NOT NULL,
    "CATEGORY_NAME" CHARACTER VARYING(100) COMMENT '카테고리명' NOT NULL,
    "UPDATED_AT" TIMESTAMP(6) WITH TIME ZONE COMMENT '갱신일',
    "CREATED_AT" TIMESTAMP(6) WITH TIME ZONE COMMENT '등록일' NOT NULL
);
ALTER TABLE "PUBLIC"."CATEGORY" ADD CONSTRAINT "PUBLIC"."CONSTRAINT_31" PRIMARY KEY("ID");
ALTER TABLE "PUBLIC"."CATEGORY" ALTER COLUMN "ID" SET DEFAULT NEXT VALUE FOR "PUBLIC"."CATEGORY_SEQ";
CREATE CACHED TABLE "PUBLIC"."PRODUCT"(
    "ID" BIGINT NOT NULL,
    "CATEGORY_ID" BIGINT,
    "BRAND_ID" BIGINT,
    "PRICE" INTEGER COMMENT '가격' NOT NULL,
    "UPDATED_AT" TIMESTAMP(6) WITH TIME ZONE COMMENT '갱신일',
    "CREATED_AT" TIMESTAMP(6) WITH TIME ZONE COMMENT '등록일' NOT NULL
);
ALTER TABLE "PUBLIC"."PRODUCT" ADD CONSTRAINT "PUBLIC"."CONSTRAINT_1" PRIMARY KEY("ID");
ALTER TABLE "PUBLIC"."PRODUCT" ALTER COLUMN "ID" SET DEFAULT NEXT VALUE FOR "PUBLIC"."PRODUCT_SEQ";
ALTER TABLE "PUBLIC"."PRODUCT" ADD CONSTRAINT "PUBLIC"."FKS6CYDSUALTSRPRVLF2BB3LCAM" FOREIGN KEY("BRAND_ID") REFERENCES "PUBLIC"."BRAND"("ID") NOCHECK;
ALTER TABLE "PUBLIC"."PRODUCT" ADD CONSTRAINT "PUBLIC"."FK1MTSBUR82FRN64DE7BALYMQ9S" FOREIGN KEY("CATEGORY_ID") REFERENCES "PUBLIC"."CATEGORY"("ID") NOCHECK;

INSERT INTO "PUBLIC"."CATEGORY"(`ID`, `CATEGORY_NAME`, `CREATED_AT`) VALUES
(1, '상의', CURRENT_TIMESTAMP()),
(2, '아우터', CURRENT_TIMESTAMP()),
(3, '바지', CURRENT_TIMESTAMP()),
(4, '스니커즈', CURRENT_TIMESTAMP()),
(5, '가방', CURRENT_TIMESTAMP()),
(6, '모자', CURRENT_TIMESTAMP()),
(7, '양말', CURRENT_TIMESTAMP()),
(8, '액세서리', CURRENT_TIMESTAMP());

INSERT INTO "PUBLIC"."BRAND"(`ID`, `BRAND_NAME`, `CREATED_AT`) VALUES
(1, 'A', CURRENT_TIMESTAMP()),
(2, 'B', CURRENT_TIMESTAMP()),
(3, 'C', CURRENT_TIMESTAMP()),
(4, 'D', CURRENT_TIMESTAMP()),
(5, 'E', CURRENT_TIMESTAMP()),
(6, 'F', CURRENT_TIMESTAMP()),
(7, 'G', CURRENT_TIMESTAMP()),
(8, 'H', CURRENT_TIMESTAMP()),
(9, 'I', CURRENT_TIMESTAMP());

INSERT INTO "PUBLIC"."PRODUCT"(`BRAND_ID`, `CATEGORY_ID`, `PRICE`, `CREATED_AT`) VALUES
(1, 1, 11200, CURRENT_TIMESTAMP()),
(1, 2, 5500, CURRENT_TIMESTAMP()),
(1, 3, 4200, CURRENT_TIMESTAMP()),
(1, 4, 9000, CURRENT_TIMESTAMP()),
(1, 5, 2000, CURRENT_TIMESTAMP()),
(1, 6, 1700, CURRENT_TIMESTAMP()),
(1, 7, 1800, CURRENT_TIMESTAMP()),
(1, 8, 2300, CURRENT_TIMESTAMP()),

(2, 1, 10500, CURRENT_TIMESTAMP()),
(2, 2, 5900, CURRENT_TIMESTAMP()),
(2, 3, 3800, CURRENT_TIMESTAMP()),
(2, 4, 9100, CURRENT_TIMESTAMP()),
(2, 5, 2100, CURRENT_TIMESTAMP()),
(2, 6, 2000, CURRENT_TIMESTAMP()),
(2, 7, 2000, CURRENT_TIMESTAMP()),
(2, 8, 2200, CURRENT_TIMESTAMP()),

(3, 1, 10000, CURRENT_TIMESTAMP()),
(3, 2, 6200, CURRENT_TIMESTAMP()),
(3, 3, 3300, CURRENT_TIMESTAMP()),
(3, 4, 9200, CURRENT_TIMESTAMP()),
(3, 5, 2200, CURRENT_TIMESTAMP()),
(3, 6, 1900, CURRENT_TIMESTAMP()),
(3, 7, 2200, CURRENT_TIMESTAMP()),
(3, 8, 2100, CURRENT_TIMESTAMP()),

(4, 1, 10100, CURRENT_TIMESTAMP()),
(4, 2, 5100, CURRENT_TIMESTAMP()),
(4, 3, 3000, CURRENT_TIMESTAMP()),
(4, 4, 9500, CURRENT_TIMESTAMP()),
(4, 5, 2500, CURRENT_TIMESTAMP()),
(4, 6, 1500, CURRENT_TIMESTAMP()),
(4, 7, 2400, CURRENT_TIMESTAMP()),
(4, 8, 2000, CURRENT_TIMESTAMP()),

(5, 1, 10700, CURRENT_TIMESTAMP()),
(5, 2, 5000, CURRENT_TIMESTAMP()),
(5, 3, 3800, CURRENT_TIMESTAMP()),
(5, 4, 9900, CURRENT_TIMESTAMP()),
(5, 5, 2300, CURRENT_TIMESTAMP()),
(5, 6, 1800, CURRENT_TIMESTAMP()),
(5, 7, 2100, CURRENT_TIMESTAMP()),
(5, 8, 2100, CURRENT_TIMESTAMP()),

(6, 1, 11200, CURRENT_TIMESTAMP()),
(6, 2, 7200, CURRENT_TIMESTAMP()),
(6, 3, 4000, CURRENT_TIMESTAMP()),
(6, 4, 9300, CURRENT_TIMESTAMP()),
(6, 5, 2100, CURRENT_TIMESTAMP()),
(6, 6, 1600, CURRENT_TIMESTAMP()),
(6, 7, 2300, CURRENT_TIMESTAMP()),
(6, 8, 1900, CURRENT_TIMESTAMP()),

(7, 1, 10500, CURRENT_TIMESTAMP()),
(7, 2, 5800, CURRENT_TIMESTAMP()),
(7, 3, 3900, CURRENT_TIMESTAMP()),
(7, 4, 9000, CURRENT_TIMESTAMP()),
(7, 5, 2200, CURRENT_TIMESTAMP()),
(7, 6, 1700, CURRENT_TIMESTAMP()),
(7, 7, 2100, CURRENT_TIMESTAMP()),
(7, 8, 2000, CURRENT_TIMESTAMP()),

(8, 1, 10800, CURRENT_TIMESTAMP()),
(8, 2, 6300, CURRENT_TIMESTAMP()),
(8, 3, 3100, CURRENT_TIMESTAMP()),
(8, 4, 9700, CURRENT_TIMESTAMP()),
(8, 5, 2100, CURRENT_TIMESTAMP()),
(8, 6, 1600, CURRENT_TIMESTAMP()),
(8, 7, 2000, CURRENT_TIMESTAMP()),
(8, 8, 2000, CURRENT_TIMESTAMP()),

(9, 1, 11400, CURRENT_TIMESTAMP()),
(9, 2, 6700, CURRENT_TIMESTAMP()),
(9, 3, 3200, CURRENT_TIMESTAMP()),
(9, 4, 9500, CURRENT_TIMESTAMP()),
(9, 5, 2400, CURRENT_TIMESTAMP()),
(9, 6, 1700, CURRENT_TIMESTAMP()),
(9, 7, 1700, CURRENT_TIMESTAMP()),
(9, 8, 2400, CURRENT_TIMESTAMP());

-- 시퀀스 값 조정을 위한 변수 생성
SET @max_brand_id = (SELECT COALESCE(MAX(ID)+1, 1) FROM "PUBLIC"."BRAND");
SET @max_category_id = (SELECT COALESCE(MAX(ID)+1, 1) FROM "PUBLIC"."CATEGORY");
SET @max_product_id = (SELECT COALESCE(MAX(ID)+1, 1) FROM "PUBLIC"."PRODUCT");

-- 시퀀스 값 재설정
ALTER SEQUENCE "PUBLIC"."BRAND_SEQ" RESTART WITH @max_brand_id;
ALTER SEQUENCE "PUBLIC"."CATEGORY_SEQ" RESTART WITH @max_category_id;
ALTER SEQUENCE "PUBLIC"."PRODUCT_SEQ" RESTART WITH @max_product_id;