package com.pawthunder.currencyexample.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interface to call api requests with retrofit library.
 */
interface RevolutService {

    @GET("latest")
    fun getLatestRates(@Query("base") base: String): Call<RatesResponse>
}
