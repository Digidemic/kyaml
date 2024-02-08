package com.digidemic.kyaml

import org.junit.Test
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before

internal class KyamlTest {

    private lateinit var kyaml: Kyaml

    @Before
    fun setup() {
        kyaml = Kyaml(mockk())
    }

    @Test
    fun `given 4 indentations, smallest being 2, largest being 7, when calling adjustIndentation with 1 prefixedSpaces, then 0 indentation remain`() {

        kyaml.indentation.apply {
            add(Kyaml.Indentation("a", 2))
            add(Kyaml.Indentation("6", 4))
            add(Kyaml.Indentation("c", 6))
            add(Kyaml.Indentation("d", 7))
        }

        kyaml.adjustIndentation(1)

        assertEquals(emptyList<Kyaml.Indentation>(), kyaml.indentation)
    }

    @Test
    fun `given 4 indentations, smallest being 2, largest being 7, when calling adjustIndentation with 2 prefixedSpaces, then 1 indentation remain`() {

        kyaml.indentation.apply {
            add(Kyaml.Indentation("a", 2))
            add(Kyaml.Indentation("6", 4))
            add(Kyaml.Indentation("c", 6))
            add(Kyaml.Indentation("d", 7))
        }

        kyaml.adjustIndentation(2)

        assertEquals(emptyList<Kyaml.Indentation>(), kyaml.indentation)
    }

    @Test
    fun `given 4 indentations, smallest being 2, largest being 7, when calling adjustIndentation with 3 prefixedSpaces, then 1 indentation remain`() {

        kyaml.indentation.apply {
            add(Kyaml.Indentation("a", 2))
            add(Kyaml.Indentation("6", 4))
            add(Kyaml.Indentation("c", 6))
            add(Kyaml.Indentation("d", 7))
        }

        kyaml.adjustIndentation(3)

        assertEquals(listOf(Kyaml.Indentation("a", 2)), kyaml.indentation)
    }

    @Test
    fun `given 4 indentations, smallest being 2, largest being 7, when calling adjustIndentation with 7 prefixedSpaces, then 3 indentations remain`() {

        kyaml.indentation.apply {
            add(Kyaml.Indentation("a", 2))
            add(Kyaml.Indentation("6", 4))
            add(Kyaml.Indentation("c", 6))
            add(Kyaml.Indentation("d", 7))
        }

        kyaml.adjustIndentation(7)

        assertEquals(
            listOf(
                Kyaml.Indentation("a", 2),
                Kyaml.Indentation("6", 4),
                Kyaml.Indentation("c", 6),
            ),
            kyaml.indentation
        )
    }

    @Test
    fun `given 4 indentations, smallest being 2, largest being 7, when calling adjustIndentation with 10 prefixedSpaces, then 4 indentations remain`() {

        kyaml.indentation.apply {
            add(Kyaml.Indentation("a", 2))
            add(Kyaml.Indentation("6", 4))
            add(Kyaml.Indentation("c", 6))
            add(Kyaml.Indentation("d", 7))
        }

        kyaml.adjustIndentation(10)

        assertEquals(
            listOf(
                Kyaml.Indentation("a", 2),
                Kyaml.Indentation("6", 4),
                Kyaml.Indentation("c", 6),
                Kyaml.Indentation("d", 7),
            ),
            kyaml.indentation
        )
    }

    @Test
    fun `given string 1, 2, 3, when calling flowCollectionToList, then return list of 1, 2, 3`() {
        val actual = kyaml.flowCollectionToList("[1, 2, 3]", "[", "]")
        assertEquals(listOf("1","2","3"), actual)
    }

    @Test
    fun `given string 1, 2, 3 with invalid surrounding brackets, when calling flowCollectionToList, then return list of 1, 2, 3 w square bracket`() {
        val actual = kyaml.flowCollectionToList("[1, 2, 3]", "{", "}")
        assertEquals(listOf("[1","2","3]"), actual)
    }

    @Test
    fun `given string 1, 2, 3 each surrounded by quotes, when calling flowCollectionToList, then return list of 1, 2, 3 each surrounded by quotes`() {
        val actual = kyaml.flowCollectionToList("[\"1\", \"2\", \"3\"]", "[", "]")
        assertEquals(listOf("\"1\"","\"2\"","\"3\""), actual)
    }

    @Test
    fun `given string 1 each surrounded by quotes and 2 spaces from each, when calling flowCollectionToList, then return list of 1 surrounded by quotes and spaces`() {
        val actual = kyaml.flowCollectionToList("[\"  1  \"]", "[", "]")
        assertEquals(listOf("\"  1  \""), actual)
    }

    @Test
    fun `given string with open close brackets, when calling flowCollectionToList, then return a list with 1 empty string`() {
        val actual = kyaml.flowCollectionToList("[]", "[", "]")
        assertEquals(listOf(""), actual)
    }

    @Test
    fun `given value Test, when calling getValueType, then return ValueType STRING`() {
        val actual = kyaml.getValueType("Test")
        assertEquals(Kyaml.ValueType.STRING, actual)
    }

    @Test
    fun `given value 1 surrounded by single quotes, when calling getValueType, then return ValueType STRING`() {
        val actual = kyaml.getValueType("'1'")
        assertEquals(Kyaml.ValueType.STRING, actual)
    }

    @Test
    fun `given value 1 surrounded by double quotes, when calling getValueType, then return ValueType STRING`() {
        val actual = kyaml.getValueType("\"1\"")
        assertEquals(Kyaml.ValueType.STRING, actual)
    }

    @Test
    fun `given value 1, when calling getValueType, then return ValueType INTEGER`() {
        val actual = kyaml.getValueType("1")
        assertEquals(Kyaml.ValueType.INTEGER, actual)
    }

    @Test
    fun `given value -1, when calling getValueType, then return ValueType INTEGER`() {
        val actual = kyaml.getValueType("-1")
        assertEquals(Kyaml.ValueType.INTEGER, actual)
    }

    @Test
    fun `given value 1 with 2 spaces on both sides, when calling getValueType, then return ValueType INTEGER`() {
        val actual = kyaml.getValueType("  1  ")
        assertEquals(Kyaml.ValueType.INTEGER, actual)
    }

    @Test
    fun `given value ~ with 2 spaces on both sides, when calling getValueType, then return ValueType NULL`() {
        val actual = kyaml.getValueType("  ~  ")
        assertEquals(Kyaml.ValueType.NULL, actual)
    }

    @Test
    fun `given value nUlL with 2 spaces on both sides, when calling getValueType, then return ValueType NULL`() {
        val actual = kyaml.getValueType("  nUlL  ")
        assertEquals(Kyaml.ValueType.NULL, actual)
    }

    @Test
    fun `given value 123dot0 with 2 spaces on both sides, when calling getValueType, then return ValueType FLOAT`() {
        val actual = kyaml.getValueType("  123.0  ")
        assertEquals(Kyaml.ValueType.FLOAT, actual)
    }

    @Test
    fun `given value -123dot45 with 2 spaces on both sides, when calling getValueType, then return ValueType FLOAT`() {
        val actual = kyaml.getValueType("  -123.45  ")
        assertEquals(Kyaml.ValueType.FLOAT, actual)
    }

    @Test
    fun `given very large value with 1 space on both sides, when calling getValueType, then return ValueType DOUBLE`() {
        val actual = kyaml.getValueType(" 83724850283478572378234895342345492312345.3 ")
        assertEquals(Kyaml.ValueType.DOUBLE, actual)
    }

    @Test
    fun `given 2147483647 with 1 space on both sides, when calling getValueType, then return ValueType INTEGER`() {
        val actual = kyaml.getValueType(" 2147483647 ")
        assertEquals(Kyaml.ValueType.INTEGER, actual)
    }

    @Test
    fun `given 2147483648 with 1 space on both sides, when calling getValueType, then return ValueType LONG`() {
        val actual = kyaml.getValueType(" 2147483648 ")
        assertEquals(Kyaml.ValueType.LONG, actual)
    }

    @Test
    fun `given tRuE with 1 space on both sides, when calling getValueType, then return ValueType BOOLEAN`() {
        val actual = kyaml.getValueType(" tRuE ")
        assertEquals(Kyaml.ValueType.BOOLEAN, actual)
    }

    @Test
    fun `given FaLSe with 1 space on both sides, when calling getValueType, then return ValueType BOOLEAN`() {
        val actual = kyaml.getValueType(" FaLSe ")
        assertEquals(Kyaml.ValueType.BOOLEAN, actual)
    }

    @Test
    fun `given values test1 and test2, when calling getSequenceType, then return ValueType STRING no nulls`() {
        val actual = kyaml.getSequenceType(
            listOf(
                "test1",
                "test2",
            )
        )
        assertEquals(Kyaml.SequenceType(
            containsNull = false,
            elementType = Kyaml.ValueType.STRING
        ), actual)
    }

    @Test
    fun `given values 1 2 and 3 all surrounded with double quotes, when calling getSequenceType, then return ValueType STRING no nulls`() {
        val actual = kyaml.getSequenceType(
            listOf(
                "\"1\"",
                "\"2\"",
                "\"3\"",
            )
        )
        assertEquals(Kyaml.SequenceType(
            containsNull = false,
            elementType = Kyaml.ValueType.STRING
        ), actual)
    }

    @Test
    fun `given values 1 2 and 3 all surrounded with single quotes, when calling getSequenceType, then return ValueType STRING no nulls`() {
        val actual = kyaml.getSequenceType(
            listOf(
                "'1'",
                "'2'",
                "'3'",
            )
        )
        assertEquals(Kyaml.SequenceType(
            containsNull = false,
            elementType = Kyaml.ValueType.STRING
        ), actual)
    }


    @Test
    fun `given single string test test, when calling getSequenceType, then return ValueType STRING no nulls`() {
        val actual = kyaml.getSequenceType(
            listOf(
                "test test",
            )
        )
        assertEquals(Kyaml.SequenceType(
            containsNull = false,
            elementType = Kyaml.ValueType.STRING
        ), actual)
    }

    @Test
    fun `given single string null, when calling getSequenceType, then return ValueType STRING with nulls`() {
        val actual = kyaml.getSequenceType(
            listOf(
                "null",
            )
        )
        assertEquals(Kyaml.SequenceType(
            containsNull = true,
            elementType = Kyaml.ValueType.STRING
        ), actual)
    }

    @Test
    fun `given single string ~, when calling getSequenceType, then return ValueType STRING with nulls`() {
        val actual = kyaml.getSequenceType(
            listOf(
                "~",
            )
        )
        assertEquals(Kyaml.SequenceType(
            containsNull = true,
            elementType = Kyaml.ValueType.STRING
        ), actual)
    }

    @Test
    fun `given values test1, null, and test2, when calling getSequenceType, then return ValueType STRING with nulls`() {
        val actual = kyaml.getSequenceType(
            listOf(
                "test1",
                "null",
                "test2",
            )
        )
        assertEquals(Kyaml.SequenceType(
            containsNull = true,
            elementType = Kyaml.ValueType.STRING
        ), actual)
    }

    @Test
    fun `given values 1, 2, 1dot1, 3, when calling getSequenceType, then return ValueType FLOAT no nulls`() {
        val actual = kyaml.getSequenceType(
            listOf(
                "1",
                "2",
                "1.1",
                "3",
            )
        )
        assertEquals(Kyaml.SequenceType(
            containsNull = false,
            elementType = Kyaml.ValueType.FLOAT
        ), actual)
    }

    @Test
    fun `given values 1, null, 1dot1, 3, when calling getSequenceType, then return ValueType FLOAT with nulls`() {
        val actual = kyaml.getSequenceType(
            listOf(
                "1",
                "null",
                "1.1",
                "3",
            )
        )
        assertEquals(Kyaml.SequenceType(
            containsNull = true,
            elementType = Kyaml.ValueType.FLOAT
        ), actual)
    }

    @Test
    fun `given values 1, null, 1dot1, test1, 3, when calling getSequenceType, then return ValueType FLOAT with nulls`() {
        val actual = kyaml.getSequenceType(
            listOf(
                "1",
                "null",
                "1.1",
                "test1",
                "3",
            )
        )
        assertEquals(Kyaml.SequenceType(
            containsNull = true,
            elementType = Kyaml.ValueType.STRING
        ), actual)
    }

    @Test
    fun `given values 328497587923456443, when calling getSequenceType, then return ValueType LONG no nulls`() {
        val actual = kyaml.getSequenceType(
            listOf(
                "328497587923456443",
            )
        )
        assertEquals(Kyaml.SequenceType(
            containsNull = false,
            elementType = Kyaml.ValueType.LONG
        ), actual)
    }

    @Test
    fun `given values 1, 2, 328497587923456443, 3, when calling getSequenceType, then return ValueType LONG no nulls`() {
        val actual = kyaml.getSequenceType(
            listOf(
                "1",
                "2",
                "328497587923456443",
                "3",
            )
        )
        assertEquals(Kyaml.SequenceType(
            containsNull = false,
            elementType = Kyaml.ValueType.LONG
        ), actual)
    }

    @Test
    fun `given values 1, 2dot2, a double, 3dot4, when calling getSequenceType, then return ValueType DOUBLE no nulls`() {
        val actual = kyaml.getSequenceType(
            listOf(
                "1",
                "2.2",
                "328497587923456443234235345634567345643567456784565876767458566.2",
                "3.4",
            )
        )
        assertEquals(Kyaml.SequenceType(
            containsNull = false,
            elementType = Kyaml.ValueType.DOUBLE
        ), actual)
    }

    @Test
    fun `given values 1, 2dot2, a double, null, 3dot4, when calling getSequenceType, then return ValueType DOUBLE with nulls`() {
        val actual = kyaml.getSequenceType(
            listOf(
                "1",
                "2.2",
                "328497587923456443234235345634567345643567456784565876767458566.2",
                "null",
                "3.4",
            )
        )
        assertEquals(Kyaml.SequenceType(
            containsNull = true,
            elementType = Kyaml.ValueType.DOUBLE
        ), actual)
    }

    @Test
    fun `given values 1, true, a double, null, 3dot4, when calling getSequenceType, then return ValueType STRING with nulls`() {
        val actual = kyaml.getSequenceType(
            listOf(
                "1",
                "true",
                "328497587923456443234235345634567345643567456784565876767458566.2",
                "null",
                "3.4",
            )
        )
        assertEquals(Kyaml.SequenceType(
            containsNull = true,
            elementType = Kyaml.ValueType.STRING
        ), actual)
    }

    @Test
    fun `given single string true, when calling getSequenceType, then return ValueType BOOLEAN no nulls`() {
        val actual = kyaml.getSequenceType(
            listOf(
                "true",
            )
        )
        assertEquals(Kyaml.SequenceType(
            containsNull = false,
            elementType = Kyaml.ValueType.BOOLEAN
        ), actual)
    }

    @Test
    fun `given values true, FAlSe, TrUe, when calling getSequenceType, then return ValueType BOOLEAN no nulls`() {
        val actual = kyaml.getSequenceType(
            listOf(
                "true",
                "FAlSe",
                "TrUe",
            )
        )
        assertEquals(Kyaml.SequenceType(
            containsNull = false,
            elementType = Kyaml.ValueType.BOOLEAN
        ), actual)
    }

    @Test
    fun `given values true, FAlSe, NUlL, TrUe, when calling getSequenceType, then return ValueType BOOLEAN with nulls`() {
        val actual = kyaml.getSequenceType(
            listOf(
                "true",
                "FAlSe",
                "NUlL",
                "TrUe",
            )
        )
        assertEquals(Kyaml.SequenceType(
            containsNull = true,
            elementType = Kyaml.ValueType.BOOLEAN
        ), actual)
    }

    @Test
    fun `given values 1, 2, 3, when calling getSequenceType, then return ValueType INTEGER no nulls`() {
        val actual = kyaml.getSequenceType(
            listOf(
                "1",
                "2",
                "3",
            )
        )
        assertEquals(Kyaml.SequenceType(
            containsNull = false,
            elementType = Kyaml.ValueType.INTEGER
        ), actual)
    }

    @Test
    fun `given values 1, 2, null, 3, when calling getSequenceType, then return ValueType INTEGER with nulls`() {
        val actual = kyaml.getSequenceType(
            listOf(
                "1",
                "2",
                "null",
                "3",
            )
        )
        assertEquals(Kyaml.SequenceType(
            containsNull = true,
            elementType = Kyaml.ValueType.INTEGER
        ), actual)
    }

    @Test
    fun `given test123, when calling isKeyValid, then return true`() {
        assertTrue(kyaml.isKeyValid("test123"))
    }

    @Test
    fun `given Test123, when calling isKeyValid, then return true`() {
        assertTrue(kyaml.isKeyValid("Test123"))
    }

    @Test
    fun `given _Test123, when calling isKeyValid, then return false`() {
        assertFalse(kyaml.isKeyValid("_Test123"))
    }

    @Test
    fun `given 1Test123, when calling isKeyValid, then return false`() {
        assertFalse(kyaml.isKeyValid("1Test123"))
    }

    @Test
    fun `given string test1 test2 test3, when calling adjustValueQuotes, then return test1 test2 test3`() {
        val actual = kyaml.adjustValueQuotes("test1 test2 test3")

        assertEquals("test1 test2 test3", actual)
    }

    @Test
    fun `given string test1 test2 test3 surrounded by single quotes and 2 spaces on each side, when calling adjustValueQuotes, then return test1 test2 test3`() {
        val actual = kyaml.adjustValueQuotes("  'test1 test2 test3'  ")

        assertEquals("test1 test2 test3", actual)
    }

    @Test
    fun `given string test1 test2 test3 surrounded by double quotes and 2 spaces on each side, when calling adjustValueQuotes, then return test1 test2 test3`() {
        val actual = kyaml.adjustValueQuotes("  \"test1 test2 test3\"  ")

        assertEquals("test1 test2 test3", actual)
    }

    @Test
    fun `given string test1 test2 test3 string with double quote ending in single and 2 spaces on each side, when calling adjustValueQuotes, then return test1 test2 test3 starting double quote ending single`() {
        val actual = kyaml.adjustValueQuotes("  \"test1 test2 test3'  ")

        assertEquals("\"test1 test2 test3'", actual)
    }

    @Test
    fun `given string don't surrounded by double quotes and 2 spaces on each side, when calling adjustValueQuotes, then return don't`() {
        val actual = kyaml.adjustValueQuotes("  \"don't\"  ")

        assertEquals("don't", actual)
    }

    @Test
    fun `given string dont with double quotes surrounded by double quotes and 2 spaces on each side, when calling adjustValueQuotes, then return dont with double quotes`() {
        val actual = kyaml.adjustValueQuotes("  \"don\"t\"  ")

        assertEquals("don\"t", actual)
    }

    @Test
    fun `given string dont with double quotes surrounded by single quotes and 2 spaces on each side, when calling adjustValueQuotes, then return dont with double quotes`() {
        val actual = kyaml.adjustValueQuotes("  'don\"t'  ")

        assertEquals("don\"t", actual)
    }

    @Test
    fun `given string dont with double quotes surrounded by single quotes and 2 spaces on each side before and after the quotes, when calling adjustValueQuotes, then return dont with double quotes and 2 spaces on each side`() {
        val actual = kyaml.adjustValueQuotes("  '  don\"t  '  ")

        assertEquals("  don\"t  ", actual)
    }

    @Test
    fun `given string don't surrounded by single quotes and 2 spaces on each side, when calling adjustValueQuotes, then return don't`() {
        val actual = kyaml.adjustValueQuotes("  'don't'  ")

        assertEquals("don't", actual)
    }

    @Test
    fun `given indentations (a, 2), (6, 4), (c, 6), (d, 7) and activeSequenceList is key1, key2, key3, when calling applyGatheredSequenceValues, then indentations now (a, 2), (6, 4), (c, 6) and activeSequenceList now empty`() {
        kyaml.indentation.apply {
            add(Kyaml.Indentation("a", 2))
            add(Kyaml.Indentation("6", 4))
            add(Kyaml.Indentation("c", 6))
            add(Kyaml.Indentation("d", 7))
        }

        kyaml.activeSequenceList.apply {
            add("key1")
            add("key2")
            add("key3")
        }

        kyaml.applyGatheredSequenceValues()

        assertEquals(emptyList<String>(), kyaml.activeSequenceList)

        assertEquals(
            listOf(
                Kyaml.Indentation("a", 2),
                Kyaml.Indentation("6", 4),
                Kyaml.Indentation("c", 6),
            ),
            kyaml.indentation
        )
    }

    @Test
    fun `given indentations (a, 2) and activeSequenceList is key1, when calling applyGatheredSequenceValues, then indentation and activeSequenceList now empty`() {
        kyaml.indentation.apply {
            add(Kyaml.Indentation("a", 2))
        }

        kyaml.activeSequenceList.apply {
            add("key1")
        }

        kyaml.applyGatheredSequenceValues()

        assertEquals(emptyList<String>(), kyaml.activeSequenceList)

        assertEquals(
            emptyList<Kyaml.Indentation>(),
            kyaml.indentation
        )
    }

    @Test
    fun `given default indentation list and activeSequenceList, when calling applyGatheredSequenceValues, then indentation and activeSequenceList now empty`() {
        kyaml.applyGatheredSequenceValues()

        assertEquals(emptyList<String>(), kyaml.activeSequenceList)

        assertEquals(
            mutableListOf<Kyaml.Indentation>(),
            kyaml.indentation
        )
    }

    @Test
    fun `given empty string, when calling getPrefixedSpacesCount, then return 0`() {
        val actual = kyaml.getPrefixedSpacesCount("")

        assertEquals(0, actual)
    }

    @Test
    fun `given Key1, when calling getPrefixedSpacesCount, then return 0`() {
        val actual = kyaml.getPrefixedSpacesCount("Key1")

        assertEquals(0, actual)
    }

    @Test
    fun `given Key2 with 1 space prior, when calling getPrefixedSpacesCount, then return 1`() {
        val actual = kyaml.getPrefixedSpacesCount(" Key2")

        assertEquals(1, actual)
    }

    @Test
    fun `given Key3 with 2 spaces prior and 2 spaces after, when calling getPrefixedSpacesCount, then return 2`() {
        val actual = kyaml.getPrefixedSpacesCount("  Key3 ")

        assertEquals(2, actual)
    }

    @Test
    fun `given a list with key and 1 value with a comment, when calling getValueFromSeparatedKeyPair, then return the value before the comment`() {
        val keyValue = listOf(
            "keyHere",
            "Some random string # and a comment that shouldn't be included"
        )

        val actual = kyaml.getValueFromSeparatedKeyPair(keyValue, ".")

        assertEquals("Some random string", actual)
    }

    @Test
    fun `given a empty list, when calling getValueFromSeparatedKeyPair, then return an empty string`() {
        val keyValue = emptyList<String>()

        val actual = kyaml.getValueFromSeparatedKeyPair(keyValue, ".")

        assertEquals("", actual)
    }

    @Test
    fun `given a list with only a key, when calling getValueFromSeparatedKeyPair, then return an empty string`() {
        val keyValue = listOf(
            "keyHere",
        )

        val actual = kyaml.getValueFromSeparatedKeyPair(keyValue, ".")

        assertEquals("", actual)
    }

    @Test
    fun `given a list with a key and value each of 1 word, when calling getValueFromSeparatedKeyPair, then return the value`() {
        val keyValue = listOf(
            "keyHere",
            "Value1"
        )

        val actual = kyaml.getValueFromSeparatedKeyPair(keyValue, ".")

        assertEquals("Value1", actual)
    }

    @Test
    fun `given a list with a key and 2 value, when calling getValueFromSeparatedKeyPair, then return the 2 values as a single string separated by the joinDelimiter`() {
        val keyValue = listOf(
            "keyHere",
            "Value1",
            "Value2",
        )

        val actual = kyaml.getValueFromSeparatedKeyPair(keyValue, ".")

        assertEquals("Value1.Value2", actual)
    }

    @Test
    fun `given a list with a key and 3 value, when calling getValueFromSeparatedKeyPair, then return the 3 values as a single string separated by the joinDelimiter`() {
        val keyValue = listOf(
            "keyHere",
            "Value1",
            "Value2",
            "Value3",
        )

        val actual = kyaml.getValueFromSeparatedKeyPair(keyValue, "-")

        assertEquals("Value1-Value2-Value3", actual)
    }

    @Test
    fun `given a list with a key and 3 value each with spaces, when calling getValueFromSeparatedKeyPair, then return the 3 values as a single string separated by the joinDelimiter and with the spaces`() {
        val keyValue = listOf(
            " keyHere ",
            " Value1 ",
            " Value2 ",
            " Value3 ",
        )

        val actual = kyaml.getValueFromSeparatedKeyPair(keyValue, "-_-")

        assertEquals("Value1 -_- Value2 -_- Value3", actual)
    }

    @Test
    fun `given a list with a key and 3 values but the second contains a comment caracter, when calling getValueFromSeparatedKeyPair, then return the first 2 values up till the comment`() {
        val keyValue = listOf(
            " keyHere ",
            " Some string ",
            " and another # with a comment. ",
            " This value should not show ",
        )

        val actual = kyaml.getValueFromSeparatedKeyPair(keyValue, "-_-")

        assertEquals("Some string -_- and another", actual)
    }

    @Test
    fun `given a list with a key and a value containing the joinDelimiter and a comment, when calling getValueFromSeparatedKeyPair, then return the values with the joinDelimiter and before the comment`() {
        val joinDelimiter = ":"
        val keyPairString = "colors: Primary: Red, Yellow, Blue # Not including secondary"
        val split = keyPairString.split(joinDelimiter)

        val actual = kyaml.getValueFromSeparatedKeyPair(split, joinDelimiter)

        assertEquals("Primary: Red, Yellow, Blue", actual)
    }

    @Test
    fun `given indentations (a, 2), (6, 4), (c, 6), (d, 7), when calling getKeyWithNesting, then return adot6dotcdotd`() {
        kyaml.indentation.apply {
            add(Kyaml.Indentation("a", 2))
            add(Kyaml.Indentation("6", 4))
            add(Kyaml.Indentation("c", 6))
            add(Kyaml.Indentation("d", 7))
        }

        val actual = kyaml.getKeyWithNesting()

        assertEquals("a.6.c.d", actual)
    }

    @Test
    fun `given indentations (adotb, 2), (6, 4), (cdote, 6), (d, 7), when calling getKeyWithNesting, then return adotbdot6dotcdotedotd`() {
        kyaml.indentation.apply {
            add(Kyaml.Indentation("a.b", 2))
            add(Kyaml.Indentation("6", 4))
            add(Kyaml.Indentation("c.e", 6))
            add(Kyaml.Indentation("d", 7))
        }

        val actual = kyaml.getKeyWithNesting()

        assertEquals("a.b.6.c.e.d", actual)
    }

    @Test
    fun `given indentations (a, 2), (6, 4), when calling getKeyWithNesting, then return adot6`() {
        kyaml.indentation.apply {
            add(Kyaml.Indentation("a", 2))
            add(Kyaml.Indentation("6", 4))
        }

        val actual = kyaml.getKeyWithNesting()

        assertEquals("a.6", actual)
    }

    @Test
    fun `given indentations (a, 2), when calling getKeyWithNesting, then return a`() {
        kyaml.indentation.apply {
            add(Kyaml.Indentation("a", 2))
        }

        val actual = kyaml.getKeyWithNesting()

        assertEquals("a", actual)
    }

    @Test
    fun `given no indentations, when calling getKeyWithNesting, then return an empty string`() {
        val actual = kyaml.getKeyWithNesting()

        assertEquals("", actual)
    }

    @Test
    fun `given no indentations and passing key1, when calling getKeyWithNesting, then return key1`() {
        val actual = kyaml.getKeyWithNesting("key1")

        assertEquals("key1", actual)
    }

    @Test
    fun `given indentations (a, 2) and passing key1, when calling getKeyWithNesting, then return adotkey1`() {
        kyaml.indentation.apply {
            add(Kyaml.Indentation("a", 2))
        }

        val actual = kyaml.getKeyWithNesting("key1")

        assertEquals("a.key1", actual)
    }

    @Test
    fun `given indentations (a, 2), (6, 4), (c, 6), (d, 7) and passing key1, when calling getKeyWithNesting, then return adot6dotcdotddotkey1`() {
        kyaml.indentation.apply {
            add(Kyaml.Indentation("a", 2))
            add(Kyaml.Indentation("6", 4))
            add(Kyaml.Indentation("c", 6))
            add(Kyaml.Indentation("d", 7))
        }

        val actual = kyaml.getKeyWithNesting("key1")

        assertEquals("a.6.c.d.key1", actual)
    }
}