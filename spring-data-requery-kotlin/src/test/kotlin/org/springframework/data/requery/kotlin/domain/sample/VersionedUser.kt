//package org.springframework.data.requery.kotlin.domain.sample
//
//import io.requery.Entity
//import io.requery.Generated
//import io.requery.Key
//import io.requery.Version
//import org.springframework.data.requery.kotlin.domain.AbstractPersistable
//import java.util.Date
//
///**
// * org.springframework.data.requery.kotlin.domain.sample.VersionedUser
// *
// * @author debop
// */
//@Entity
//abstract class VersionedUser: AbstractPersistable<Long>() {
//
//    @get:Key
//    @get:Generated
//    abstract override val id: Long
//
//    @Version
//    @JvmField
//    final var version: Long = 0
//
//    abstract var name: String
//
//    abstract var email: String
//
//    abstract var birthday: Date?
//}