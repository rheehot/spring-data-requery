package org.springframework.data.requery.kotlin.repository.sample

import io.requery.kotlin.eq
import io.requery.query.Tuple
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.domain.Sort
import org.springframework.data.repository.query.Param
import org.springframework.data.requery.kotlin.annotation.Query
import org.springframework.data.requery.kotlin.domain.sample.*
import org.springframework.data.requery.kotlin.repository.RequeryRepository
import org.springframework.transaction.annotation.Transactional
import java.util.*
import java.util.stream.Stream

/**
 * UserRepository
 *
 * @author debop@coupang.com
 */
@SuppressWarnings("SpringDataRepositoryMethodReturnTypeInspection")
interface UserRepository: RequeryRepository<UserEntity, Int>, UserRepositoryCustom {

    abstract fun findByLastname(lastname: String): List<UserEntity>

    @Transactional
    abstract override fun findById(primaryKey: Int): Optional<UserEntity>

    abstract override fun deleteById(id: Int)

    abstract fun findByEmailAddress(emailAddress: String): UserEntity

    @Query("select u.* from SD_User u left outer join SD_User m on (u.manager = m.id)")
    abstract fun findAllPaged(pageable: Pageable): Page<UserEntity>

    abstract fun findByEmailAddressAndLastname(emailAddress: String, lastname: String): UserEntity

    abstract fun findByEmailAddressAndLastnameOrFirstname(emailAddress: String, lastname: String, firstname: String): List<UserEntity>

    @Query("select * from SD_User u where u.emailAddress = ?")
    @Transactional(readOnly = true)
    abstract fun findByAnnotatedQuery(emailAddress: String): UserEntity


    /**
     * Method to directly create query from and adding a [Pageable] parameter to be regarded on query execution.
     */
    abstract fun findByLastname(pageable: Pageable, lastname: String): Page<UserEntity>

    /**
     * Method to directly create query from and adding a [Pageable] parameter to be regarded on query execution.
     * Just returns the queried [Page]'s contents.
     */
    abstract fun findByFirstname(firstname: String, pageable: Pageable): List<UserEntity>

    abstract fun findByFirstnameIn(pageable: Pageable, vararg firstnames: String): Page<UserEntity>

    abstract fun findByFirstnameNotIn(firstname: Collection<String>): List<UserEntity>

    @Query("select * from SD_User u where u.firstname like ?")
    abstract fun findByFirstnameLike(firstname: String): List<UserEntity>

    // NOTE: Not supported Named Parameter
    @Query("select * from SD_User u where u.firstname like :firstname%")
    abstract fun findByFirstnameLikeNamed(@Param("firstname") firstname: String): List<UserEntity>

    /**
     * Manipulating query to set all [UserEntity]'s names to the given one.
     */
    @Query("update SD_User u set u.lastname = ?")
    abstract fun renameAllUsersTo(lastname: String)

    @Query("select count(*) from SD_User u where u.firstname = ?")
    abstract fun countWithFirstname(firstname: String): Long?

    @Query("select * from SD_User u where u.lastname = ? or u.firstname = ?")
    abstract fun findByLastnameOrFirstname(/*@Param("firstname") */lastname: String, /*@Param("lastname") */firstname: String): List<UserEntity>

    @Query("select * from SD_User u where u.firstname = ? or u.lastname = ?")
    abstract fun findByLastnameOrFirstnameUnannotated(firstname: String, lastname: String): List<UserEntity>

    abstract fun findByFirstnameOrLastname(firstname: String, lastname: String): List<UserEntity>

    abstract fun findByLastnameLikeOrderByFirstnameDesc(lastname: String): List<UserEntity>

    abstract fun findByLastnameNotLike(lastname: String): List<UserEntity>

    abstract fun findByLastnameNot(lastname: String): List<UserEntity>

    // NOTE: Not supported associated query, use direct join query instead.
    abstract fun findByManagerLastname(name: String): List<UserEntity>

    // NOTE: Not supported associated query, use direct join query instead.
    abstract fun findByColleaguesLastname(lastname: String): List<UserEntity>

    abstract fun findByLastnameNotNull(): List<UserEntity>

    abstract fun findByLastnameNull(): List<UserEntity>

    abstract fun findByEmailAddressLike(email: String, sort: Sort): List<UserEntity>

    abstract fun findSpecialUsersByLastname(lastname: String): List<SpecialUer>

    //    List<UserEntity> findBySpringDataNamedQuery(String lastname);

    abstract fun findByLastnameIgnoringCase(lastname: String): List<UserEntity>

    abstract fun findByLastnameIgnoringCase(pageable: Pageable, lastname: String): Page<UserEntity>

    abstract fun findByLastnameIgnoringCaseLike(lastname: String): List<UserEntity>

    abstract fun findByLastnameAndFirstnameAllIgnoringCase(lastname: String, firstname: String): List<UserEntity>

    abstract fun findByAgeGreaterThanEqual(age: Int): List<UserEntity>

    abstract fun findByAgeLessThanEqual(age: Int): List<UserEntity>

    @Query("select u.lastname from SD_User u group by u.lastname ")
    abstract fun findByLastnameGrouped(pageable: Pageable): Page<String>

    // DATAJPA-117
    @Query(value = "SELECT * FROM SD_User WHERE lastname = ?1")
    abstract fun findNativeByLastname(lastname: String): List<UserEntity>

    // DATAJPA-132
    abstract fun findByActiveTrue(): List<UserEntity>

    // DATAJPA-132
    abstract fun findByActiveFalse(): List<UserEntity>

    // HINT: @Query 를 쓰던가, requery api 를 사용하던가 같다.
    // @Query("select u.* from SD_User u inner join User_Colleagues uc on (u.id = uc.SD_UserId1) where uc.SD_UserId2 = ?")
    fun findColleaguesFor(userId: Int?): List<UserEntity> {
        return operations
            .select(UserEntity::class)
            .join(User_Colleagues::class).on(UserEntity::id eq User_Colleagues.FRIEND_ID)
            .where(User_Colleagues.USER_ID.eq(userId))
            .get()
            .toList()
    }

    // DATAJPA-188
    abstract fun findByCreatedAtBefore(date: Date): List<UserEntity>

    // DATAJPA-188
    abstract fun findByCreatedAtAfter(date: Date): List<UserEntity>

    // DATAJPA-180
    abstract fun findByFirstnameStartingWith(firstname: String): List<UserEntity>

    // DATAJPA-180
    abstract fun findByFirstnameEndingWith(firstname: String): List<UserEntity>

    // DATAJPA-180
    abstract fun findByFirstnameContaining(firstname: String): List<UserEntity>

    @Query(value = "SELECT 1 FROM SD_User")
    abstract fun findOnesByNativeQuery(): List<Tuple>

    // DATAJPA-231
    abstract fun countByLastname(lastname: String): Long

    // DATAJPA-231
    abstract fun countUsersByFirstname(firstname: String): Int

    // DATAJPA-920
    abstract fun existsByLastname(lastname: String): Boolean

    // DATAJPA-391
    @Query("select u.firstname from SD_User u where u.lastname = ?")
    abstract fun findFirstnamesByLastname(lastname: String): List<String>

    // DATAJPA-415
    abstract fun findByIdIn(vararg ids: Int): Collection<UserEntity>

    // DATAJPA-461
    @Query("select * from SD_User u where u.id in ?")
    abstract fun findByIdsCustomWithPositionalVarArgs(vararg ids: Int): Collection<UserEntity>

    // DATAJPA-461
    @Query("select * from SD_User u where u.id in ?")
    abstract fun findByIdsCustomWithNamedVarArgs(vararg ids: Int): Collection<UserEntity>


    // NOTE: Not supported Spring expression
    // DATAJPA-415

    @Query("update SD_User u set u.active = ? where u.id in ?")
    abstract fun updateUserActiveState(activeState: Boolean, vararg ids: Int)

    // DATAJPA-405
    abstract fun findAllByOrderByLastnameAsc(): List<UserEntity>

    // DATAJPA-454
    abstract fun findByBinaryData(data: ByteArray): List<UserEntity>

    // DATAJPA-486
    abstract fun findSliceByLastname(lastname: String, pageable: Pageable): Slice<UserEntity>

    //    // DATAJPA-496
    //    List<UserEntity> findByAttributesIn(Set<String> attributes);

    // DATAJPA-460
    abstract fun removeByLastname(lastname: String): Int?

    // DATAJPA-460
    abstract fun deleteByLastname(lastname: String): Int?


    @Query(value = "select * from SD_User u where u.firstname like ?")
    abstract fun findAllByFirstnameLike(firstname: String, page: Pageable): Page<UserEntity>


    abstract fun findFirstByOrderByAgeDesc(): UserEntity

    abstract fun findFirst1ByOrderByAgeDesc(): UserEntity

    abstract fun findTopByOrderByAgeDesc(): UserEntity

    abstract fun findTopByOrderByAgeAsc(): UserEntity

    abstract fun findTop1ByOrderByAgeAsc(): UserEntity

    abstract fun findTop2ByOrderByAgeDesc(): List<UserEntity>

    abstract fun findFirst2ByOrderByAgeDesc(): List<UserEntity>

    abstract fun findFirst2UsersBy(sort: Sort): List<UserEntity>

    abstract fun findTop2UsersBy(sort: Sort): List<UserEntity>

    abstract fun findFirst3UsersBy(page: Pageable): Page<UserEntity>

    abstract fun findFirst2UsersBy(page: Pageable): Page<UserEntity>

    abstract fun findTop3UsersBy(page: Pageable): Slice<UserEntity>

    abstract fun findTop2UsersBy(page: Pageable): Slice<UserEntity>


    @Query("select u.binaryData from SD_User u where u.id = ?")
    abstract fun findBinaryDataByIdNative(id: Int?): ByteArray

    @Query("select * from SD_User u where u.emailAddress = ?")
    abstract fun findOptionalByEmailAddress(emailAddress: String): Optional<UserEntity>

    //    // DATAJPA-564
    //    @Query("select u from UserEntity u where u.firstname = ?#{[0]} and u.firstname = ?1 and u.lastname like %?#{[1]}% and u.lastname like %?2%")
    //    List<UserEntity> findByFirstnameAndLastnameWithSpelExpression(String firstname, String lastname);
    //
    //    // DATAJPA-564
    //    @Query("select u from UserEntity u where u.lastname like %:#{[0]}% and u.lastname like %:lastname%")
    //    List<UserEntity> findByLastnameWithSpelExpression(@Param("lastname") String lastname);
    //
    //    // DATAJPA-564
    //    @Query("select u from UserEntity u where u.firstname = ?#{'Oliver'}")
    //    List<UserEntity> findOliverBySpELExpressionWithoutArgumentsWithQuestionmark();
    //
    //    // DATAJPA-564
    //    @Query("select u from UserEntity u where u.firstname = :#{'Oliver'}")
    //    List<UserEntity> findOliverBySpELExpressionWithoutArgumentsWithColon();
    //
    //    // DATAJPA-564
    //    @Query("select u from UserEntity u where u.age = ?#{[0]}")
    //    List<UserEntity> findUsersByAgeForSpELExpressionByIndexedParameter(int age);
    //
    //    // DATAJPA-564
    //    @Query("select u from UserEntity u where u.firstname = :firstname and u.firstname = :#{#firstname}")
    //    List<UserEntity> findUsersByFirstnameForSpELExpression(@Param("firstname") String firstname);
    //
    //    // DATAJPA-564
    //    @Query("select u from UserEntity u where u.emailAddress = ?#{principal.emailAddress}")
    //    List<UserEntity> findCurrentUserWithCustomQuery();
    //
    //    // DATAJPA-564
    //    @Query("select u from UserEntity u where u.firstname = ?1 and u.firstname=?#{[0]} and u.emailAddress = ?#{principal.emailAddress}")
    //    List<UserEntity> findByFirstnameAndCurrentUserWithCustomQuery(String firstname);
    //
    //    // DATAJPA-564
    //    @Query("select u from UserEntity u where u.firstname = :#{#firstname}")
    //    List<UserEntity> findUsersByFirstnameForSpELExpressionWithParameterVariableOnly(@Param("firstname") String firstname);
    //
    //    // DATAJPA-564
    //    @Query("select u from UserEntity u where u.firstname = ?#{[0]}")
    //    List<UserEntity> findUsersByFirstnameForSpELExpressionWithParameterIndexOnly(String firstname);
    //
    //    // DATAJPA-564
    //    @Query(
    //        value = "select * from (" +
    //                "select u.*, rownum() as RN from (" +
    //                "select * from SD_User ORDER BY ucase(firstname)" +
    //                ") u" +
    //                ") where RN between ?#{ #pageable.offset +1 } and ?#{#pageable.offset + #pageable.pageSize}",
    //        countQuery = "select count(u.id) from SD_User u", nativeQuery = true)
    //    Page<UserEntity> findUsersInNativeQueryWithPagination(Pageable pageable);
    //
    //    // DATAJPA-1140
    //    @Query("select u from UserEntity u where u.firstname =:#{#user.firstname} and u.lastname =:lastname")
    //    List<UserEntity> findUsersByUserFirstnameAsSpELExpressionAndLastnameAsString(@Param("user") UserEntity user,
    //                                                                           @Param("lastname") String lastname);
    //
    //    // DATAJPA-1140
    //    @Query("select u from UserEntity u where u.firstname =:firstname and u.lastname =:#{#user.lastname}")
    //    List<UserEntity> findUsersByFirstnameAsStringAndUserLastnameAsSpELExpression(@Param("firstname") String firstname,
    //                                                                           @Param("user") UserEntity user);
    //
    //    // DATAJPA-1140
    //    @Query("select u from UserEntity u where u.firstname =:#{#user.firstname} and u.lastname =:#{#lastname}")
    //    List<UserEntity> findUsersByUserFirstnameAsSpELExpressionAndLastnameAsFakeSpELExpression(@Param("user") UserEntity user,
    //                                                                                       @Param("lastname") String lastname);
    //
    //    // DATAJPA-1140
    //    @Query("select u from UserEntity u where u.firstname =:#{#firstname} and u.lastname =:#{#user.lastname}")
    //    List<UserEntity> findUsersByFirstnameAsFakeSpELExpressionAndUserLastnameAsSpELExpression(
    //        @Param("firstname") String firstname, @Param("user") UserEntity user);
    //
    //    // DATAJPA-1140
    //    @Query("select u from UserEntity u where u.firstname =:firstname")
    //    List<UserEntity> findUsersByFirstnamePaginated(Pageable page, @Param("firstname") String firstname);
    //
    //    // DATAJPA-629
    //    @Query("select u from #{#entityName} u where u.firstname = ?#{[0]} and u.lastname = ?#{[1]}")
    //    List<UserEntity> findUsersByFirstnameForSpELExpressionWithParameterIndexOnlyWithEntityExpression(String firstname,
    //                                                                                               String lastname);

    abstract fun findByAgeIn(ages: Collection<Int>): List<UserEntity>

    abstract fun queryByAgeIn(ages: Array<Int>): List<UserEntity>

    abstract fun queryByAgeInOrFirstname(ages: Array<Int>, firstname: String): List<UserEntity>

    @Query("select * from SD_User")
    abstract fun findAllByCustomQueryAndStream(): Stream<UserEntity>

    abstract fun readAllByFirstnameNotNull(): Stream<UserEntity>

    @Query("select * from SD_User")
    abstract fun streamAllPaged(pageable: Pageable): Stream<UserEntity>

    abstract fun findByLastnameNotContaining(part: String): List<UserEntity>

    // NOTE: Not Supported

    fun findByRolesContaining(role: Role): List<UserEntity> {
        return operations
            .select(UserEntity::class)
            .join(UserEntity_RoleEntity::class).on(UserEntity_RoleEntity.SD_USER_ID.eq(UserEntity.ID)
                                                       .and(UserEntity_RoleEntity.SD_ROLES_ID.eq(role.id)))
            .get()
            .toList()
    }

    fun findByRolesNotContaining(role: Role): List<UserEntity> {
        return operations
            .select(UserEntity::class)
            .distinct()
            .leftJoin(UserEntity_RoleEntity::class).on(UserEntity_RoleEntity.SD_USER_ID.eq(UserEntity.ID))
            .where(UserEntity_RoleEntity.SD_ROLES_ID.ne(role.id).or(UserEntity_RoleEntity.SD_ROLES_ID.isNull()))
            .get()
            .toList()
    }

    fun findByRolesNameContaining(roleName: String): List<UserEntity> {
        return operations
            .select(UserEntity::class)
            .join(UserEntity_RoleEntity::class).on(UserEntity_RoleEntity.SD_USER_ID.eq(UserEntity.ID))
            .join(RoleEntity::class).on(UserEntity_RoleEntity.SD_ROLES_ID.eq(RoleEntity.ID))
            .where(RoleEntity.NAME.eq(roleName))
            .get()
            .toList()
    }


    //    // DATAJPA-1179
    //    @Query("select u from UserEntity u where u.firstname = :#{#firstname} and u.firstname = :#{#firstname}")
    //    List<UserEntity> findUsersByDuplicateSpel(@Param("firstname") String firstname);
    //
    fun findRolesAndFirstnameBy(): List<UserEntity> {
        return operations
            .select(UserEntity::class)
            .get()
            .toList()
    }

    @Query("select * from SD_User u where u.age = ?")
    abstract fun findByStringAge(age: String): List<UserEntity>


    //    // DATAJPA-1185
    abstract fun <T> findAsStreamByFirstnameLike(name: String, projectionType: Class<T>): Stream<T>

    //
    //    // DATAJPA-1185
    abstract fun <T> findAsListByFirstnameLike(name: String, projectionType: Class<T>): List<T>

    @Query("SELECT u.firstname, u.lastname from SD_User u WHERE u.id=?")
    abstract fun findByNativeQuery(id: Int?): NameOnly

    //
    @Query("SELECT u.emailaddress from SD_User u WHERE u.id=?")
    abstract fun findEmailOnlyByNativeQuery(id: Int?): EmailOnly

    // NOTE: Not Supported

    //    // DATAJPA-1235
    @Query("SELECT u.* FROM SD_User u where u.firstname >= ? and u.lastname = '000:1'")
    abstract fun queryWithIndexedParameterAndColonFollowedByIntegerInString(firstname: String): List<UserEntity>


    // DATAJPA-1233
    @Query(value = "SELECT u.* FROM SD_User u ORDER BY CASE WHEN (u.firstname  >= ?) THEN 0 ELSE 1 END, u.firstname")
    abstract fun findAllOrderedBySpecialNameSingleParam(name: String, page: Pageable): Page<UserEntity>


    // DATAJPA-1233
    @Query(value = "SELECT u.* FROM SD_User u WHERE 'x' = ? ORDER BY CASE WHEN (u.firstname  >= ?) THEN 0 ELSE 1 END, u.firstname")
    abstract fun findAllOrderedBySpecialNameMultipleParams(other: String, name: String, page: Pageable): Page<UserEntity>

    // DATAJPA-1233
    @Query(value = "SELECT u.* FROM SD_User u WHERE 'x' = ? ORDER BY CASE WHEN (u.firstname  >= ?) THEN 0 ELSE 1 END, u.firstname")
    abstract fun findAllOrderedBySpecialNameMultipleParamsIndexed(other: String, name: String, page: Pageable): Page<UserEntity>

    //    // DATAJPA-928
    @Query(value = "SELECT u.* FROM SD_User u")
    abstract fun findByNativQueryWithPageable(pageable: Pageable): Page<UserEntity>

    // DATAJPA-928
    @Query(value = "SELECT firstname FROM SD_User ORDER BY UCASE(firstname)", countQuery = "SELECT count(*) FROM SD_User")
    abstract fun findAsStringByNativeQueryWithPageable(pageable: Pageable): Page<String>

    //    // DATAJPA-1273
    //    List<NameOnly> findByNamedQueryWithAliasInInvertedOrder();

    // DATAJPA-1301
    @Query("select u.firstname as firstname, u.lastname as lastname from SD_User u where u.firstname = 'Debop'")
    abstract fun findTupleWithNullValues(): Tuple

    // DATAJPA-1307
    @Query(value = "select u.* from SD_User u where u.emailAddress = ?")
    abstract fun findByEmailNativeAddressJdbcStyleParameter(emailAddress: String): UserEntity

    // NOTE: Not Supported
    // DATAJPA-1334
    fun findByNamedQueryWithConstructorExpression(): List<RolesAndFirstname> {
        return operations
            .select(UserEntity::class)
            .get()
            .toList()
            .map { it -> it as RolesAndFirstname }
    }

    interface RolesAndFirstname {

        val firstname: String

        val roles: Set<Role>
    }


    interface NameOnly {

        val firstname: String

        val lastname: String
    }

    interface EmailOnly {

        val emailAddress: String
    }

}