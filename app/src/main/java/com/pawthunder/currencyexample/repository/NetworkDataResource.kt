package com.pawthunder.currencyexample.repository

import androidx.lifecycle.MutableLiveData
import com.pawthunder.currencyexample.util.AppExecutors
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

abstract class NetworkDataResource<RequestType, ResultType>(
    private val appExecutors: AppExecutors,
    private val result: MutableLiveData<ResultType>,
    private val shouldPostValue: MutableLiveData<Boolean>? = null
) {

    fun loadData() {
        if (shouldRequest()) {
            appExecutors.networkIO().execute {
                if (result.value == null) {
                    loadDatabaseResult()
                }

                val request = requestFromNetwork()
                request.enqueue(object : Callback<RequestType> {
                    override fun onFailure(call: Call<RequestType>, throwable: Throwable) {
                        onFailedRequest(throwable)
                        loadDatabaseResult()
                    }

                    override fun onResponse(
                        call: Call<RequestType>,
                        response: Response<RequestType>
                    ) {
                        val body = response.body()

                        if (body == null) {
                            loadFromDatabase()
                            return
                        }

                        appExecutors.diskIO().execute {
                            postValue(saveCallResult(body))
                        }
                    }
                })
            }
        } else {
            loadDatabaseResult()
        }
    }

    private fun loadDatabaseResult() {
        appExecutors.diskIO().execute {
            postValue(loadFromDatabase())
        }
    }

    private fun postValue(value: ResultType) {
        if (shouldPostValue?.value != false)
            result.postValue(value)
    }

    abstract fun shouldRequest(): Boolean

    abstract fun loadFromDatabase(): ResultType

    abstract fun requestFromNetwork(): Call<RequestType>

    abstract fun saveCallResult(response: RequestType): ResultType

    abstract fun onFailedRequest(throwable: Throwable)
}
