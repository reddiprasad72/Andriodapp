package com.bluboy.android

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Capermint on 07,October,2019
 * Capermint technologies
 * android@caperminttechnologies.com
 */

@RunWith(MockitoJUnitRunner::class)
class ApplicationTest : BaseTest() {

    @Test
    fun testOne(){
        println("Hello")
    }

    /*@Test
    fun testGetUserlist(){
        runBlocking {
            val user = User(
                1,
                "abc.png",
                "abc@test.com",
                "Android",
                "test"
            )
            val userList = arrayListOf(user)
            val resDefered = CompletableDeferred<UserListRS>(UserListRS(userList))
            whenever(homeApiService.getUserList(1)).thenReturn(resDefered)
            whenever(homeRemoteDS.getUserList(UserListPRQ(1))).thenReturn(resDefered)
            whenever(homeRepository.getUserList(UserListPRQ(1))).thenReturn(Either.Right(UserListRS(arrayListOf(user))))

            homeRepository.getUserList(UserListPRQ(1)).either(
                {
                    println("Error")
                }, {
                    println("User list found")
                    TestCase.assertEquals("User list is empty", true, it.userList?.isEmpty() == true)
                }
            )
        }
    }*/


}