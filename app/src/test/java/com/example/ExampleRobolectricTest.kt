package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.User
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("JOMAMBO", appName)
    }

    @Test
    fun `user referral code generation`() {
        val code = User.generateReferralCode()
        assertNotNull(code)
        assertEquals(6, code.length)
        assertTrue(code.all { it.isLetterOrDigit() })
    }

    @Test
    fun `naira conversion calculation`() {
        val coins = 1000L
        val naira = coins * 0.10 // 100 coins = 10 Naira -> 1 coin = 0.10 Naira
        assertEquals(100.0, naira, 0.001)
    }
}
