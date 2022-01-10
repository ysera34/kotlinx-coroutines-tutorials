package org.inframincer.rss

import org.junit.Assert
import org.junit.Test

class CollectionTest {

    @Test
    fun mapToTest() {
        val stringList1 = listOf("AAA", "BBB")
        val stringList2 = mutableListOf("CCC")
        stringList1.mapTo(stringList2) {
            it.toLowerCase()
        }

        Assert.assertEquals(
            listOf("CCC", "aaa", "bbb"),
            stringList2,
        )
    }

    @Test
    fun flatMapTest1() {
        val list = listOf(listOf("A", "B", "C"), listOf("D", "E", "F"), listOf("G", "H", "I"))

        val resultList1 = list.flatMap {
            it
        }

        Assert.assertEquals(
            listOf("A", "B", "C", "D", "E", "F", "G", "H", "I"),
            resultList1,
        )

        val resultList2 = list.flatMap {
            it.take(1)
        }

        Assert.assertEquals(
            listOf("A", "D", "G"),
            resultList2,
        )
    }

    @Test
    fun flatMapTest2() {
        val stringList = listOf("AAA", "BBB")
        val resultList = stringList.flatMap {
            it.toLowerCase().toList()
        }

        Assert.assertEquals(
            listOf('a', 'a', 'a', 'b', 'b', 'b'),
            resultList,
        )
    }
}
