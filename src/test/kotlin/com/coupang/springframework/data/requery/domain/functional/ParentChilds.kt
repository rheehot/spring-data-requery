package com.coupang.springframework.data.requery.domain.functional

import com.coupang.springframework.data.requery.domain.AbstractPersistable
import io.requery.*


@Entity
abstract class AbstractFuncParent: AbstractPersistable<Long>() {

    @get:Key
    @get:Generated
    abstract override val id: Long?

    @get:Column
    abstract var name: String?

    @get:ManyToOne(cascade = [CascadeAction.DELETE, CascadeAction.SAVE])
    @get:ForeignKey(delete = ReferentialAction.SET_NULL, update = ReferentialAction.RESTRICT)
    abstract var child: AbstractFuncChild?

}

@Entity
abstract class AbstractFuncParentNoCascade: AbstractPersistable<Long>() {
    @get:Key
    @get:Generated
    abstract override val id: Long?

    @get:OneToOne(cascade = [CascadeAction.NONE])
    @get:ForeignKey(delete = ReferentialAction.SET_NULL, update = ReferentialAction.RESTRICT)
    abstract var oneToOne: AbstractFuncChildOneToOneNoCascade?

    @get:ManyToOne
    @get:ForeignKey(delete = ReferentialAction.SET_NULL, update = ReferentialAction.RESTRICT)
    abstract var manyToOne: AbstractFuncChildManyToOneNoCascade?

    @get:OneToMany(cascade = [CascadeAction.NONE])
    abstract val oneToMany: MutableList<AbstractFuncChildOneToManyNoCascade>

    @get:ManyToMany(cascade = [CascadeAction.NONE])
    @get:JunctionTable
    abstract val manyToMany: MutableList<AbstractFuncChildManyToManyNoCascade>
}

@Entity
abstract class AbstractFuncChild: AbstractPersistable<Long>() {

    @get:Key
    @get:Generated
    abstract override val id: Long?

    @get:Column
    abstract var name: String?
}

@Entity
abstract class AbstractFuncChildOneToOneNoCascade: AbstractPersistable<Long>() {

    @get:Key
    @get:Generated
    abstract override val id: Long?

    @get:Column
    abstract var attribute: String?
}

@Entity
abstract class AbstractFuncChildOneToManyNoCascade: AbstractPersistable<Long>() {

    @get:Key
    @get:Generated
    abstract override val id: Long?

    @get:Column
    abstract var attribute: String?

    @get:ManyToOne(cascade = [CascadeAction.NONE])
    @get:ForeignKey(delete = ReferentialAction.SET_NULL, update = ReferentialAction.RESTRICT)
    abstract var parent: AbstractFuncParentNoCascade?
}

@Entity
abstract class AbstractFuncChildManyToOneNoCascade: AbstractPersistable<Long>() {

    @get:Key
    @get:Generated
    abstract override val id: Long?

    @get:Column
    abstract var attribute: String?
}

@Entity
abstract class AbstractFuncChildManyToManyNoCascade: AbstractPersistable<Long>() {

    @get:Key
    @get:Generated
    abstract override val id: Long?

    @get:Column
    abstract var attribute: String?

    @get:ManyToMany(mappedBy = "manyToMany", cascade = [CascadeAction.NONE])
    abstract val parents: MutableList<AbstractFuncParentNoCascade>
}