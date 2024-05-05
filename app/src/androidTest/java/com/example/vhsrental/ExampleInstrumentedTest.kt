package com.example.vhsrental

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.vhsrental", appContext.packageName)
    }

    @Test
    fun patternMatchingEmail() {
        val regex = """([^\s@]+)@([^\s@]+)\.([^\s@]+)""".toRegex()
        assert(regex.matches("xhulka@fi.muni.cz"))
        assert(regex.matches("x.hulka@fi.muni.cz"))
        assert(regex.matches("a@b.c"))
        assert(!regex.matches("a@.c"))
        assert(!regex.matches("a@."))
        assert(!regex.matches("a@b."))
        assert(!regex.matches("email"))
        assert(!regex.matches("a@b"))
        assert(!regex.matches("email@domain"))
        assert(!regex.matches("email@smth@domain"))
        assert(!regex.matches("em ail@domain"))
        assert(!regex.matches("email@doma in"))
        assert(!regex.matches("em ail@doma in"))
    }
}