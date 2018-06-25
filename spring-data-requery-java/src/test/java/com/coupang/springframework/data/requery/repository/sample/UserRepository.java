package com.coupang.springframework.data.requery.repository.sample;

import com.coupang.springframework.data.requery.annotation.Modifying;
import com.coupang.springframework.data.requery.annotation.Query;
import com.coupang.springframework.data.requery.domain.sample.Role;
import com.coupang.springframework.data.requery.domain.sample.SpecialUser;
import com.coupang.springframework.data.requery.domain.sample.User;
import com.coupang.springframework.data.requery.repository.RequeryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Stream;

/**
 * UserRepository
 *
 * @author debop@coupang.com
 * @since 18. 6. 12
 */
@SuppressWarnings("SpringDataRepositoryMethodReturnTypeInspection")
public interface UserRepository extends RequeryRepository<User, Integer>, UserRepositoryCustom {

    List<User> findByLastname(String lastname);

    @Override
    @Transactional
    java.util.Optional<User> findById(Integer primaryKey);

    @Override
    void deleteById(Integer id);

    User findByEmailAddress(String emailAddress);

//    @Query("select * from SD_User u left outer join u.manager as manager")
//    Page<User> findAllPages(Pageable pageable);

    User findByEmailAddressAndLastname(String emailAddress, String lastname);

    User findByEmailAddressAndLastnameOrFirstname(String emailAddress, String lastname, String username);

    @Query("select * from SD_User u where u.emailAddress = ?")
    @Transactional(readOnly = true)
    User findByAnnotatedQuery(String emailAddress);


    /**
     * Method to directly create query from and adding a {@link Pageable} parameter to be regarded on query execution.
     */
    Page<User> findByLastname(Pageable pageable, String lastname);

    /**
     * Method to directly create query from and adding a {@link Pageable} parameter to be regarded on query execution.
     * Just returns the queried {@link Page}'s contents.
     */
    List<User> findByFirstname(String firstname, Pageable pageable);

    Page<User> findByFirstnameIn(Pageable pageable, String... firstnames);

    List<User> findByFirstnameNotIn(Collection<String> firstname);

    // NOTE: 이건 아마 안될거야 ...
    @Query("select * from SD_User u wehre u.firstname like ?%")
    List<User> findByFirstnameLike(String firstname);

    // NOTE: 이건 아마 안될거야 ...
    @Query("select * from SD_User u wehre u.firstname like :firstname%")
    List<User> findByFirstnameLikeNamed(@Param("firstname") String firstname);


    /**
     * Manipulating query to set all {@link User}'s names to the given one.
     */
    @Modifying
    @Query("update SD_User u set u.lastname = ?")
    void renameAllUsersTo(String lastname);

    @Query("select count(u) from SD_User u where u.firstname = ?")
    Long countWithFirstname(String firstname);

    @Query("select * from SD_User u where u.lastname = ? or u.firstname = ?")
    List<User> findByLastnameOrFirstname(/*@Param("firstname") */String foo, /*@Param("lastname") */String bar);

    @Query("select * from SD_User u where u.lastname = ? or u.firstname = ?")
    List<User> findByLastnameOrFirstnameUnannotated(String firstname, String lastname);

    /**
     * Method to check query creation and named parameter usage go well hand in hand.
     */
    // BUG: @Param 이 있는 경우 @Query의 SQL문을 찾도록만 되어 있다. 이 부분을 수정해야 한다. 
    // List<User> findByFirstnameOrLastname(@Param("lastname") String lastname, @Param("firstname") String firstname);

    List<User> findByLastnameLikeOrderByFirstnameDesc(String lastname);

    List<User> findByLastnameNotLike(String lastname);

    List<User> findByLastnameNot(String lastname);

    List<User> findByManagerLastname(String name);

    // NOTE: Not supported associated query, use direct join query instead.
    // List<User> findByColleaguesLastname(String lastname);

    List<User> findByLastnameNotNull();

    List<User> findByLastnameNull();

    List<User> findByEmailAddressLike(String email, Sort sort);

    List<SpecialUser> findSpecialUsersByLastname(String lastname);

    List<User> findBySpringDataNamedQuery(String lastname);

    List<User> findByLastnameIgnoringCase(String lastname);

    Page<User> findByLastnameIgnoringCase(Pageable pageable, String lastname);

    List<User> findByLastnameIgnoringCaseLike(String lastname);

    List<User> findByLastnameAndFirstnameAllIgnoringCase(String lastname, String firstname);

    List<User> findByAgeGreaterThanEqual(int age);

    List<User> findByAgeLessThanEqual(int age);

    @Query("select u.lastname from SD_User u group by u.lastname")
    Page<String> findByLastnameGrouped(Pageable pageable);

    // DATAJPA-117
    @Query(value = "SELECT * FROM SD_User WHERE lastname = ?1")
    List<User> findNativeByLastname(String lastname);

    // DATAJPA-132
    List<User> findByActiveTrue();

    // DATAJPA-132
    List<User> findByActiveFalse();

    @Query("select u.colleagues from User u where u = ?1")
    List<User> findColleaguesFor(User user);

    // DATAJPA-188
    List<User> findByCreatedAtBefore(Date date);

    // DATAJPA-188
    List<User> findByCreatedAtAfter(Date date);

    // DATAJPA-180
    List<User> findByFirstnameStartingWith(String firstname);

    // DATAJPA-180
    List<User> findByFirstnameEndingWith(String firstname);

    // DATAJPA-180
    List<User> findByFirstnameContaining(String firstname);

    @Query(value = "SELECT 1 FROM SD_User")
    List<Integer> findOnesByNativeQuery();

    // DATAJPA-231
    long countByLastname(String lastname);

    // DATAJPA-231
    int countUsersByFirstname(String firstname);

    // DATAJPA-920
    boolean existsByLastname(String lastname);

    // DATAJPA-391
    @Query("select u.firstname from User u where u.lastname = ?1")
    List<String> findFirstnamesByLastname(String lastname);

    // DATAJPA-415
    // Collection<User> findByIdIn(@Param("ids") Integer... ids);

    // DATAJPA-461
    @Query("select u from User u where u.id in ?1")
    Collection<User> findByIdsCustomWithPositionalVarArgs(Integer... ids);

    // DATAJPA-461
    @Query("select u from User u where u.id in :ids")
    Collection<User> findByIdsCustomWithNamedVarArgs(@Param("ids") Integer... ids);

    // DATAJPA-415
    @Modifying
    @Query("update #{#entityName} u set u.active = :activeState where u.id in :ids")
    void updateUserActiveState(@Param("activeState") boolean activeState, @Param("ids") Integer... ids);

    // DATAJPA-405
    List<User> findAllByOrderByLastnameAsc();

    // DATAJPA-454
    List<User> findByBinaryData(byte[] data);

    // DATAJPA-486
    Slice<User> findSliceByLastname(String lastname, Pageable pageable);

    // DATAJPA-496
    List<User> findByAttributesIn(Set<String> attributes);

    // DATAJPA-460
    Long removeByLastname(String lastname);

    // DATAJPA-460
    List<User> deleteByLastname(String lastname);


    @Query(value = "select * from SD_User where u u.firstname like ?%", countProjection = "u.firstname")
    Page<User> findAllByFirstnameLike(String firstname, Pageable page);


    User findFirstByOrderByAgeDesc();

    User findFirst1ByOrderByAgeDesc();

    User findTopByOrderByAgeDesc();

    User findTopByOrderByAgeAsc();

    User findTop1ByOrderByAgeAsc();

    List<User> findTop2ByOrderByAgeDesc();

    List<User> findFirst2ByOrderByAgeDesc();

    List<User> findFirst2UsersBy(Sort sort);

    List<User> findTop2UsersBy(Sort sort);

    Page<User> findFirst3UsersBy(Pageable page);

    Page<User> findFirst2UsersBy(Pageable page);

    Slice<User> findTop3UsersBy(Pageable page);

    Slice<User> findTop2UsersBy(Pageable page);


    @Query("select u.binaryData from SD_User u where u.id = ?")
    byte[] findBinaryDataByIdNative(Integer id);

    @Query("select * from SD_User u where u.emailAddress = ?")
    Optional<User> findOptionalByEmailAddress(String emailAddress);

//    // DATAJPA-564
//    @Query("select u from User u where u.firstname = ?#{[0]} and u.firstname = ?1 and u.lastname like %?#{[1]}% and u.lastname like %?2%")
//    List<User> findByFirstnameAndLastnameWithSpelExpression(String firstname, String lastname);
//
//    // DATAJPA-564
//    @Query("select u from User u where u.lastname like %:#{[0]}% and u.lastname like %:lastname%")
//    List<User> findByLastnameWithSpelExpression(@Param("lastname") String lastname);
//
//    // DATAJPA-564
//    @Query("select u from User u where u.firstname = ?#{'Oliver'}")
//    List<User> findOliverBySpELExpressionWithoutArgumentsWithQuestionmark();
//
//    // DATAJPA-564
//    @Query("select u from User u where u.firstname = :#{'Oliver'}")
//    List<User> findOliverBySpELExpressionWithoutArgumentsWithColon();
//
//    // DATAJPA-564
//    @Query("select u from User u where u.age = ?#{[0]}")
//    List<User> findUsersByAgeForSpELExpressionByIndexedParameter(int age);
//
//    // DATAJPA-564
//    @Query("select u from User u where u.firstname = :firstname and u.firstname = :#{#firstname}")
//    List<User> findUsersByFirstnameForSpELExpression(@Param("firstname") String firstname);
//
//    // DATAJPA-564
//    @Query("select u from User u where u.emailAddress = ?#{principal.emailAddress}")
//    List<User> findCurrentUserWithCustomQuery();
//
//    // DATAJPA-564
//    @Query("select u from User u where u.firstname = ?1 and u.firstname=?#{[0]} and u.emailAddress = ?#{principal.emailAddress}")
//    List<User> findByFirstnameAndCurrentUserWithCustomQuery(String firstname);
//
//    // DATAJPA-564
//    @Query("select u from User u where u.firstname = :#{#firstname}")
//    List<User> findUsersByFirstnameForSpELExpressionWithParameterVariableOnly(@Param("firstname") String firstname);
//
//    // DATAJPA-564
//    @Query("select u from User u where u.firstname = ?#{[0]}")
//    List<User> findUsersByFirstnameForSpELExpressionWithParameterIndexOnly(String firstname);
//
//    // DATAJPA-564
//    @Query(
//        value = "select * from (" +
//                "select u.*, rownum() as RN from (" +
//                "select * from SD_User ORDER BY ucase(firstname)" +
//                ") u" +
//                ") where RN between ?#{ #pageable.offset +1 } and ?#{#pageable.offset + #pageable.pageSize}",
//        countQuery = "select count(u.id) from SD_User u", nativeQuery = true)
//    Page<User> findUsersInNativeQueryWithPagination(Pageable pageable);
//
//    // DATAJPA-1140
//    @Query("select u from User u where u.firstname =:#{#user.firstname} and u.lastname =:lastname")
//    List<User> findUsersByUserFirstnameAsSpELExpressionAndLastnameAsString(@Param("user") User user,
//                                                                           @Param("lastname") String lastname);
//
//    // DATAJPA-1140
//    @Query("select u from User u where u.firstname =:firstname and u.lastname =:#{#user.lastname}")
//    List<User> findUsersByFirstnameAsStringAndUserLastnameAsSpELExpression(@Param("firstname") String firstname,
//                                                                           @Param("user") User user);
//
//    // DATAJPA-1140
//    @Query("select u from User u where u.firstname =:#{#user.firstname} and u.lastname =:#{#lastname}")
//    List<User> findUsersByUserFirstnameAsSpELExpressionAndLastnameAsFakeSpELExpression(@Param("user") User user,
//                                                                                       @Param("lastname") String lastname);
//
//    // DATAJPA-1140
//    @Query("select u from User u where u.firstname =:#{#firstname} and u.lastname =:#{#user.lastname}")
//    List<User> findUsersByFirstnameAsFakeSpELExpressionAndUserLastnameAsSpELExpression(
//        @Param("firstname") String firstname, @Param("user") User user);
//
//    // DATAJPA-1140
//    @Query("select u from User u where u.firstname =:firstname")
//    List<User> findUsersByFirstnamePaginated(Pageable page, @Param("firstname") String firstname);
//
//    // DATAJPA-629
//    @Query("select u from #{#entityName} u where u.firstname = ?#{[0]} and u.lastname = ?#{[1]}")
//    List<User> findUsersByFirstnameForSpELExpressionWithParameterIndexOnlyWithEntityExpression(String firstname,
//                                                                                               String lastname);

    List<User> findByAgeIn(Collection<Integer> ages);

    List<User> queryByAgeIn(Integer[] ages);

    List<User> queryByAgeInOrFirstname(Integer[] ages, String firstname);

    @Query("select * from SD_User")
    Stream<User> findAllByCustomQueryAndStream();

    Stream<User> readAllByFirstnameNotNull();

    @Query("select * from SD_User")
    Stream<User> streamAllPaged(Pageable pageable);

    List<User> findByLastnameNotContaining(String part);

    // NOTE: Not Supported

//    List<User> findByRolesContaining(AbstractRole role);
//
//    List<User> findByRolesNotContaining(AbstractRole role);
//
//    List<User> findByRolesNameContaining(String name);


//    // DATAJPA-1179
//    @Query("select u from User u where u.firstname = :#{#firstname} and u.firstname = :#{#firstname}")
//    List<User> findUsersByDuplicateSpel(@Param("firstname") String firstname);
//
//    List<RolesAndFirstname> findRolesAndFirstnameBy();

    @Query("select * from SD_User u where u.age = ?")
    List<User> findByStringAge(String age);


    // NOTE: Not Supported

//    // DATAJPA-1185
//    <T> Stream<T> findAsStreamByFirstnameLike(String name, Class<T> projectionType);
//
//    // DATAJPA-1185
//    <T> List<T> findAsListByFirstnameLike(String name, Class<T> projectionType);

    @Query("SELECT u.firstname, u.lastname from SD_User u WHERE u.id=?")
    NameOnly findByNativeQuery(Integer id);

    @Query("SELECT u.emailaddress from SD_User u WHERE u.id=?")
    EmailOnly findEmailOnlyByNativeQuery(Integer id);

    // NOTE: Not Supported

//    // DATAJPA-1235
//    @Query("SELECT u FROM User u where u.firstname >= ?1 and u.lastname = '000:1'")
//    List<User> queryWithIndexedParameterAndColonFollowedByIntegerInString(String firstname);
//
//    // DATAJPA-1233
//    @Query(value = "SELECT u FROM User u ORDER BY CASE WHEN (u.firstname  >= :name) THEN 0 ELSE 1 END, u.firstname")
//    Page<User> findAllOrderedBySpecialNameSingleParam(@Param("name") String name, Pageable page);
//
//    // DATAJPA-1233
//    @Query(value = "SELECT u FROM User u WHERE :other = 'x' ORDER BY CASE WHEN (u.firstname  >= :name) THEN 0 ELSE 1 END, u.firstname")
//    Page<User> findAllOrderedBySpecialNameMultipleParams(@Param("name") String name, @Param("other") String other, Pageable page);
//
//    // DATAJPA-1233
//    @Query(value = "SELECT u FROM User u WHERE ?2 = 'x' ORDER BY CASE WHEN (u.firstname  >= ?1) THEN 0 ELSE 1 END, u.firstname")
//    Page<User> findAllOrderedBySpecialNameMultipleParamsIndexed(String name, String other, Pageable page);

    // DATAJPA-928
    Page<User> findByNativeNamedQueryWithPageable(Pageable pageable);

    // DATAJPA-928
    @Query(value = "SELECT firstname FROM SD_User ORDER BY UCASE(firstname)", countQuery = "SELECT count(*) FROM SD_User")
    Page<String> findByNativeQueryWithPageable(@Param("pageable") Pageable pageable);

    // DATAJPA-1273
    List<NameOnly> findByNamedQueryWithAliasInInvertedOrder();

    // DATAJPA-1301
    @Query("select firstname as firstname, lastname as lastname from SD_User u where u.firstname = 'Oliver'")
    Map<String, Object> findMapWithNullValues();

    // DATAJPA-1307
    @Query(value = "select * from SD_User u where u.emailAddress = ?")
    User findByEmailNativeAddressJdbcStyleParameter(String emailAddress);

    // NOTE: Not Supported
//    // DATAJPA-1334
//    List<NameOnlyDto> findByNamedQueryWithConstructorExpression();


    interface RolesAndFirstname {

        String getFirstname();

        Set<Role> getRoles();
    }


    interface NameOnly {

        String getFirstname();

        String getLastname();
    }

    interface EmailOnly {

        String getEmailAddress();
    }
}