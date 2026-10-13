package com.example

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun simulation_year_start() {
        val startYear = 2000
        val targetYear = 2026
        assertTrue(startYear < targetYear)
        assertEquals(26, targetYear - startYear)
    }
}
