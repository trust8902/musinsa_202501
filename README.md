### 1. 구현 범위에 대한 설명
H2 DB를 활용하여 3가지의 테이블(category, brand, product)을 생성하였고, 

미리 사전에 제시된 데이터를 담아서 구현 1~3은 QueryDSL로 구현하였습니다.
구현 4는 JPA로 CRUD를 구현하였습니다.

#### 구현 1.
카테고리별 최저가격과 브랜드, 상품가격, 총액을 조회해야 하여 ROW_NUMBER()를 활용하여 
GROUP BY 대비 불필요한 로직을 줄이되 결과는 동일하게 반환되도록 하고자 하였습니다.

QueryDSL로 조회 시 Brand, Category에서 각각 name도 조회해야 하기 때문에 inner join을 추가했습니다.

filter를 추가해서 ROW_NUMBER() 값이 1인 경우로 범위를 줄였습니다.

조회된 결과는 LowestCategoryTempProductDto로 반환받아서 이를 LowestCategoryProductResponseDto에 구현한 fromEntities() 메서드에서 가공하여 LowestCategoryProductResponseDto에 넣을 수 있도록 처리하였습니다.

#### 구현 2.

#### 구현 3.
구현1과 비슷하게 ROW_NUMBER()를 활용했습니다.

최초 PathVariable로 categoryName을 입력받은 후 이를 QueryDSL의 where로 categoryName을 넘겨서 조회하는것으로 방향을 잡고 구현했습니다.

조회시 ROW_NUMBER()를 각각 최저값 구분용(rowNumberAsc), 최고값 구분용(rowNumberDesc)으로 두가지를 추가했습니다.
rowNumberAsc, rowNumberDesc가 1인 경우만 최저값, 최고값 레코드로 보고 filter 하도록 제한했습니다.

조회결과는 CategoryExtreamsBrandTempDto로 임시로 반환하도록 한 후
각각 최저가, 최고가 브랜드 데이터를 ExtreamsBrand 넣은 후

CategoryExtreamsBrandResponseDto에 각각 최저가, 최고가 브랜드를 넣어서 반환하도록 처리하였습니다.

Native Query 사용을 고민하였으나 QueryDSL을 활용하는 편이 유지보수성에 용이하고 디버깅도 수월하기 때문에 QueryDSL을 활용하는것으로 결정했습니다.

다만, ROW_NUMBER() == 1L로 비교하는 구문을 구현 1도 마찬가지입니다만
filter 대신 where로 처리하는 것을 시도했으나 H2 DB를 사용하며 Window Function을 사용할 수 없다고 나오는 문제가 발생되어 해결을 위해 다방면으로 노력해봤으나 해결방법을 찾기에는 시간적으로 여유가 없어서 우선 filter로 구현한점을 감안해주시길 바랍니다.

#### 구현 4.
CRUD 구현의 경우 JPA로 구현하였습니다.

### 2. 코드 빌드, 테스트, 실행 방법
* JDK 21
* IntelliJ에서 실행하는 기준으로 아래와 같이 정리하였습니다.
* DB는 프로젝트 실행 시 flyway로 테이블과 데이터가 생성되므로 별도로 생성해줄 필요는 없습니다. 
* Gradle PlugIn을 동기화 해줍니다.
* Gradle - Tasks - other - kaptKotlin을 실행하여 Query Type을 생성해줍니다.
* ShopApplication을 지정하고 프로젝트를 실행
* 또는 터미널에서 프로젝트 루트로 이동하여 ./gradlew bootRun 로 프로젝트를 실행하면 됩니다.
* 테스트는 메일에 첨부된 postman 백업파일을 postman에 import 하여 테스트하실 수 있습니다.

### 3. 기타 추가 정보
* TO DO