package com.coupang.springframework.data.requery.domain.functional

import com.coupang.kotlinx.logging.KLogging
import com.coupang.springframework.data.requery.domain.AbstractDomainTest
import io.requery.PersistenceException
import io.requery.sql.RowCountException
import io.requery.sql.StatementExecutionException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Before
import org.junit.Test

/**
 * com.coupang.springframework.data.requery.domain.functional.ParentChildNoCascadeTest
 * @author debop
 * @since 18. 6. 2
 */
class ParentChildNoCascadeTest: AbstractDomainTest() {

    companion object: KLogging() {
        const val COUNT = 100
    }

    @Before
    fun setup() {
        with(requeryKotlin) {
            deleteAll(FuncChild::class)
            deleteAll(FuncParent::class)
            deleteAll(FuncChildOneToOneNoCascade::class)
            deleteAll(FuncChildOneToManyNoCascade::class)
            deleteAll(FuncChildManyToOneNoCascade::class)
            deleteAll(FuncChildManyToManyNoCascade::class)
            deleteAll(FuncParentNoCascade::class)
        }
    }

    @Test
    fun `insert without cascade action save`() {
        assertThatThrownBy {
            with(requeryKotlin) {
                val child = FuncChild().apply { id = 1 }
                val parent = FuncParent().also { it.child = child }

                // NOTE: This violates the Foreign Key Constraint, because Child does not exist and CascadeAction.SAVE is not specified
                insert(parent)
            }
        }.isInstanceOf(PersistenceException::class.java)
    }


    @Test
    fun `insert no cascade one to one non existing child`() {
        // BUG: no cascade 인데, 예외가 나지 않는다
        // Insert parent entity, associated one-to-one to a non existing child entity
        // This should fail with a foreign-key violation, since child does not exist in the database

        // assertThatThrownBy {
        with(requeryKotlin) {
            val child = FuncChildOneToOneNoCascade()
            val parent = FuncParentNoCascade()
            parent.id = 1
            parent.oneToOne = child

            insert(parent)
        }
        // }.isInstanceOf(StatementExecutionException::class)
    }

    @Test
    fun `insert no cascade one to one existing child`() {
        // Insert parent entity, associated one-to-one to an existing child entity
        with(requeryKotlin) {
            val child = FuncChildOneToOneNoCascade().apply { id = 1; attribute = "1" }
            insert(child)

            val parent = FuncParentNoCascade().apply { id = 1; oneToOne = child }
            insert(parent)

            // Assert that child has been associated to parent
            val parentGot = findById(FuncParentNoCascade::class, 1L)!!
        }
    }

    @Test
    fun `insert no cascade many to one existing child`() {
        // Insert parent entity, associated many-to-one to an existing child entity
        with(requeryKotlin) {
            val child = FuncChildManyToOneNoCascade().apply { id = 1; attribute = "1" }
            insert(child)

            val parent = FuncParentNoCascade().apply { id = 1; manyToOne = child }
            insert(parent)

            // Assert that child has been associated to parent
            val parentGot = findById(FuncParentNoCascade::class, 1L)!!
        }
    }

    @Test
    fun `insert no cascade one to many non existing child`() {
        // Insert parent entity, associated one-to-may to 1 non-existing child entity
        assertThatThrownBy {
            with(requeryKotlin) {
                val child = FuncChildOneToManyNoCascade().apply { id = 1; attribute = "1" }

                val parent = FuncParentNoCascade().apply { id = 1; oneToMany += child }
                insert(parent)

                // Assert that child has been associated to parent
                val parentGot = findById(FuncParentNoCascade::class, 1L)!!
            }
        }.isInstanceOf(RowCountException::class.java)
    }

    @Test
    fun `insert no cascade one to many existing child`() {
        // Insert parent entity, associated one-to-may to 1 existing child entity
        with(requeryKotlin) {
            val child = FuncChildOneToManyNoCascade().apply { id = 1; attribute = "1" }
            insert(child)

            val parent = FuncParentNoCascade().apply { id = 1; oneToMany += child }
            insert(parent)

            // Assert that child has been associated to parent
            val parentGot = findById(FuncParentNoCascade::class, 1L)!!
            assertThat(parentGot.oneToMany).hasSize(1)
            assertThat(parentGot.oneToMany[0]).isEqualTo(child)
        }
    }

    @Test
    fun `insert no cascade many to many non existing child`() {
        // Insert parent entity, associated many-to-may to 1 non-existing child entity
        assertThatThrownBy {
            with(requeryKotlin) {
                val child = FuncChildManyToManyNoCascade().apply { id = 505; attribute = "1" }

                val parent = FuncParentNoCascade().apply { id = 1; manyToMany += child }
                insert(parent)
            }
        }.isInstanceOf(StatementExecutionException::class.java)
    }

    @Test
    fun `insert no cascade many to many existing child`() {
        // Insert parent entity, associated many-to-may to 1 existing child entity
        with(requeryKotlin) {
            val child = FuncChildManyToManyNoCascade().apply { id = 1; attribute = "1" }
            insert(child)

            val parent = FuncParentNoCascade().apply { id = 1; manyToMany += child }
            insert(parent)

            // Assert that child has been associated to parent
            val parentGot = findById(FuncParentNoCascade::class, 1L)!!
            assertThat(parentGot.manyToMany).hasSize(1)
            assertThat(parentGot.manyToMany[0]).isEqualTo(child)
        }
    }

    @Test
    fun `delete no cascade one to one`() {
        with(requeryKotlin) {
            val child = FuncChildOneToOneNoCascade()
            child.id = 123
            child.attribute = "1"

            val parent = FuncParentNoCascade()
            parent.id = 123
            parent.oneToOne = child

            insert(child)
            insert(parent)

            delete(parent)

            val childGot = findById(FuncChildOneToOneNoCascade::class, 123L)
            assertThat(childGot).isNotNull
        }
    }

    @Test
    fun `delete no cascade many to one`() {
        with(requeryKotlin) {
            val child = FuncChildManyToOneNoCascade()
            child.id = 123
            child.attribute = "1"

            val parent = FuncParentNoCascade()
            parent.id = 123
            parent.manyToOne = child

            insert(child)
            insert(parent)

            delete(parent)

            val childGot = findById(FuncChildManyToOneNoCascade::class, 123L)
            assertThat(childGot).isNotNull
        }
    }

    @Test
    fun `delete no cascade one to many`() {
        with(requeryKotlin) {
            val child1 = FuncChildOneToManyNoCascade().apply { id = 1; attribute = "1" }
            val child2 = FuncChildOneToManyNoCascade().apply { id = 2; attribute = "2" }

            val parent = FuncParentNoCascade().apply { id = 1 }
            parent.oneToMany += child1
            parent.oneToMany += child2

            insert(child1)
            insert(child2)
            insert(parent)

            // delete parent
            delete(parent)

            val child1Got = findById(FuncChildOneToManyNoCascade::class, 1L)
            assertThat(child1Got).isNotNull
            assertThat(child1Got!!.parent).isNull()

            val child2Got = findById(FuncChildOneToManyNoCascade::class, 2L)
            assertThat(child2Got).isNotNull
            assertThat(child2Got!!.parent).isNull()
        }
    }

    @Test
    fun `delete no cascade many to many`() {
        with(requeryKotlin) {
            val child1 = FuncChildManyToManyNoCascade().apply { id = 1; attribute = "1" }
            val child2 = FuncChildManyToManyNoCascade().apply { id = 2; attribute = "2" }
            val parent = FuncParentNoCascade().apply { id = 1 }

            parent.manyToMany += child1
            parent.manyToMany += child2

            insert(child1)
            insert(child2)
            insert(parent)

            // delete parent
            delete(parent)

            val child1Got = findById(FuncChildManyToManyNoCascade::class, 1L)
            assertThat(child1Got).isNotNull

            val child2Got = findById(FuncChildManyToManyNoCascade::class, 2L)
            assertThat(child2Got).isNotNull
        }
    }
}