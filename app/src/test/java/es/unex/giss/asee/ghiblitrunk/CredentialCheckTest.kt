package es.unex.giss.asee.ghiblitrunk

import es.unex.giss.asee.ghiblitrunk.utils.CredentialCheck
import org.junit.Assert.assertEquals
import org.junit.Test

class CredentialCheckTest {

    @Test
    fun testLoginValidCredentials() {
        val result = CredentialCheck.login("validUsername", "validPassword")
        assertEquals(CredentialCheck.CredentialError.Success, result.error)
    }

    @Test
    fun testLoginBlankUsername() {
        val result = CredentialCheck.login("", "validPassword")
        assertEquals(CredentialCheck.CredentialError.UsernameError, result.error)
    }

    @Test
    fun testLoginShortUsername() {
        val result = CredentialCheck.login("abc", "validPassword")
        assertEquals(CredentialCheck.CredentialError.UsernameError, result.error)
    }

    @Test
    fun testLoginBlankPassword() {
        val result = CredentialCheck.login("validUsername", "")
        assertEquals(CredentialCheck.CredentialError.PasswordError, result.error)
    }

    @Test
    fun testLoginShortPassword() {
        val result = CredentialCheck.login("validUsername", "abc")
        assertEquals(CredentialCheck.CredentialError.PasswordError, result.error)
    }

    @Test
    fun testJoinValidCredentials() {
        val result = CredentialCheck.join("FirstName", "SecondName", "validUsername", "validEmail@example.com", "validPassword1", "validPassword1")
        assertEquals(CredentialCheck.CredentialError.Success, result.error)
    }

    @Test
    fun testContainsNumbersAndMayus() {
        val result = CredentialCheck.containsNumbersAndMayus("ValidPassword123")
        assertEquals(true, result)
    }

    @Test
    fun testContainsNumbersAndMayusInvalid() {
        val result = CredentialCheck.containsNumbersAndMayus("invalidpassword")
        assertEquals(false, result)
    }

    @Test
    fun testPasswordOk() {
        val result = CredentialCheck.passwordOk("validPassword", "validPassword")
        assertEquals(CredentialCheck.CredentialError.Success, result.error)
    }

    @Test
    fun testPasswordMismatch() {
        val result = CredentialCheck.passwordOk("invalidPassword", "validPassword")
        assertEquals(CredentialCheck.CredentialError.LoginError, result.error)
    }
}
