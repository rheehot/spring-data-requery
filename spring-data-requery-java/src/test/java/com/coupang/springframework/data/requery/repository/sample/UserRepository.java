package com.coupang.springframework.data.requery.repository.sample;

import com.coupang.springframework.data.requery.annotation.Modifying;
import com.coupang.springframework.data.requery.annotation.Query;
import com.coupang.springframework.data.requery.domain.sample.SpecialUser;
import com.coupang.springframework.data.requery.domain.sample.User;
import com.coupang.springframework.data.requery.repository.RequeryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * UserRepository
 *
 * @author debop@coupang.com
 * @since 18. 6. 12
 */
public interface UserRepository extends RequeryRepository<User, Integer> {

    List<User> findByLastname(String lastname);


    @Query("select * from SD_User u where u.emailAddress = ?1")
    @Transactional(readOnly = true)
    User findByAnnotatedQuery(String emailAddress);


    /**
     * Manipulating query to set all {@link User}'s names to the given one.
     *
     * @param lastname
     */
    @Modifying
    @Query("update SD_User u set u.lastname = ?1")
    void renameAllUsersTo(String lastname);

    @Query("select count(u) from SD_User u where u.firstname = ?1")
    Long countWithFirstname(String firstname);


    /**
     * Method to check query creation and named parameter usage go well hand in hand.
     *
     * @param lastname
     * @param firstname
     * @return
     */
    List<User> findByFirstnameOrLastname(@Param("lastname") String lastname, @Param("firstname") String firstname);

    List<User> findByLastnameLikeOrderByFirstnameDesc(String lastname);

    List<User> findByLastnameNotLike(String lastname);

    List<User> findByLastnameNot(String lastname);

    List<User> findByManagerLastname(String name);

    List<User> findByColleaguesLastname(String lastname);

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
    Collection<User> findByIdIn(@Param("ids") Integer... ids);

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
}
