package com.example.moviedetails.Repository

import android.content.Context
import com.example.moviedetails.API.ApiBuilder
import com.example.moviedetails.Model.Guest
import com.example.moviedetails.showToast
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class OtherRepository @Inject constructor(
    private val apiBuilder: ApiBuilder,
    @ApplicationContext private val context: Context
) {
    var guestSessionId: Guest? = null

    suspend fun getGuestSessionId() = withContext(Dispatchers.IO) {
        try {
            val response = apiBuilder.getGuestSessionId()
            if (response.isSuccessful) {
                guestSessionId = response.body()!!
            } else {
                showToast(context)
            }
        } catch (e: IOException) {
            showToast(context)
        } catch (e: HttpException) {
            showToast(context)
        }
    }
}