# spring-data-requery

Spring Data for [requery](http://requery.io)

[requery](http://requery.io)는 대부분의 DB를 지원하는 Lightweight ORM Library 입니다. 
또한 Java, Kotlin, Android 등 JVM 환경과 Android 환경 모두를 지원하여, 가벼우면서, 성능 좋은 라이브러리입니다.

참고: [Requery Overview](doc/Requery%20Overview.pdf)


## 제공 모듈

현재 branch 2.x 에서 제공하는 기능은 spring boot 2.0.x 을 사용하여 제작되었습니다. 
향후 branch 1.x 에서는 spring boot 1.x 기반으로 제공될 예정입니다. 

### spring-data-requery

Java를 이용한 Backend 개발 시, Spring Data의 다양한 조회를 자동 생성하는 기능을 제공합니다.

* Query By Property
* Query By Native Query
* Query By Example 

#### 제한 사항
 
현재 requery 기능의 한계로 다음과 같은 기능은 지원되지 않습니다.

* Named Parameter (추후 지원 예정, 또는 spring-data-jdbc를 이용할 예정)
* Association property 에 대한 질의 생성 (requery가 JOIN에 대해서는 선언적 정의를 지원하지 않으므로, default method로 구현해야 합니다)


### spring-boot-starter-data-requery

spring-data-requery 를 Spring Boot 환경에서 쉽게 사용할 수 있도록 Auto Configuration 기능을 제공합니다.

spring boot 환경설정에서 다음과 같이 설정하면 쉽게 사용 가능합니다.

```properties
# spring-data-requery
spring.data.requery.modelName = org.springframework.boot.autoconfigure.data.requery.domain.Models.DEFAULT
spring.data.requery.batchUpdateSize = 50
spring.data.requery.statementCacheSize = 512
spring.data.requery.tableCreationMode = CREATE_NOT_EXISTS

spring.datasource.hikari.driver-class-name = org.h2.Driver
spring.datasource.hikari.jdbc-url = jdbc:h2:mem:test
spring.datasource.hikari.username = sa
```
 

### spring-data-requery-kotlin

spring-data-requery-kotlin 은 kotlin 언어를 위해 독립적으로 제작, 지원됩니다. Domain Entity 정의 시 
Kotlin은 Java와 달리 Interface로 정의해야 하고, 이를 field가 아는 method로 인식하게 되어, 내부 구현 시 Java와 혼용하기 어렵습니다.
그리고, primitive type에 대한 처리가 Java와는 달라 추가적인 작업이 필요했습니다.

사용법은 Java보다 조금 더 간편합니다만, 다음과 같은 default method를 이용하여 custom query를 만들 때에 주의할 점이 있습니다.

Java에서는 `default` keyword를 지정하면 되지만, Kotlin에서는 `@JvmDefault` annotation을 지정해 주어야 합니다.

```kotlin
    @JvmDefault
    fun findColleaguesFor(userId: Int?): List<UserEntity> {
        return operations
            .select(UserEntity::class)
            .join(User_Colleagues::class).on(UserEntity::id eq User_Colleagues.USER_ID)
            .where(User_Colleagues.FRIEND_ID.eq(userId))
            .get()
            .toList()
    }
``` 

### spring-boot-starter-data-requery-kotlin

추후 지원 예정 


### requery-phoenix

곧 지원 예정  

[Apache Phoenix](https://phoenix.apache.org/)를 requery를 이용하여 사용하기 위한 라이브러리입니다. 
HBase 가 `INSERT`, `UPDATE`  대신 `UPSERT`  만을 지원하므로, Apache Phoenix용 SQL문을 생성하도록 하는 라이브러리입니다.