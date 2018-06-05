package com.coupang.springframework.data.requery.domain.functional

import com.coupang.springframework.data.requery.domain.AbstractPersistable
import io.requery.*


@Entity
abstract class AbstractFuncParent: AbstractPersistable<Long>() {

    @get:Key
    @get:Generated
    abstract val id: Long?

    @get:Column
    abstract var name: String?

    @get:ManyToOne(cascade = [CascadeAction.DELETE])
    @get:ForeignKey(delete = ReferentialAction.SET_NULL, update = ReferentialAction.RESTRICT)
    abstract var child: AbstractFuncChild?

}

@Entity(cacheable = false)
abstract class AbstractFuncParentNoCascade: Persistable {
    @get:Key
    abstract var id: Long?

    @get:OneToOne(cascade = [CascadeAction.NONE])
    @get:ForeignKey(delete = ReferentialAction.SET_NULL, update = ReferentialAction.RESTRICT)
    abstract var oneToOne: AbstractFuncChildOneToOneNoCascade

    @get:ManyToOne(cascade = [CascadeAction.NONE])
    @get:ForeignKey(delete = ReferentialAction.SET_NULL, update = ReferentialAction.RESTRICT)
    abstract var manyToOne: AbstractFuncChildManyToOneNoCascade

    @get:OneToMany(cascade = [CascadeAction.NONE])
    abstract val oneToMany: MutableList<AbstractFuncChildOneToManyNoCascade>

    @get:ManyToMany(cascade = [CascadeAction.NONE])
    @get:JunctionTable
    abstract val manyToMany: MutableList<AbstractFuncChildManyToManyNoCascade>
}

@Entity
abstract class AbstractFuncChild: Persistable {

    @get:Key
    abstract var id: Long?

    @get:Column
    abstract var name: String?
}

@Entity(cacheable = false)
abstract class AbstractFuncChildOneToOneNoCascade: Persistable {

    @get:Key
    abstract var id: Long?

    @get:Column
    abstract var attribute: String?
}

@Entity(cacheable = false)
abstract class AbstractFuncChildOneToManyNoCascade: Persistable {

    @get:Key
    abstract var id: Long?

    @get:Column
    abstract var attribute: String?

    @get:ManyToOne(cascade = [CascadeAction.NONE])
    @get:ForeignKey(delete = ReferentialAction.SET_NULL, update = ReferentialAction.RESTRICT)
    abstract var parent: AbstractFuncParentNoCascade
}

@Entity(cacheable = false)
abstract class AbstractFuncChildManyToOneNoCascade: Persistable {

    @get:Key
    abstract var id: Long?

    @get:Column
    abstract var attribute: String?
}

@Entity(cacheable = false)
abstract class AbstractFuncChildManyToManyNoCascade: Persistable {

    @get:Key
    abstract var id: Long?

    @get:Column
    abstract var attribute: String?

    @get:ManyToMany(mappedBy = "manyToMany", cascade = [CascadeAction.NONE])
    abstract val parents: MutableList<AbstractFuncParentNoCascade>
}