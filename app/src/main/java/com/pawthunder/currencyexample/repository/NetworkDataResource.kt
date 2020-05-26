package com.pawthunder.currencyexample.repository

import androidx.lifecycle.MutableLiveData
import com.pawthunder.currencyexample.util.AppExecutors
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

abstract class NetworkDataResource<RequestType, ResultType>(
    private val appExecutors: AppExecutors
) {

    fun loadData(result: MutableLiveData<ResultType>) {
        if (shouldRequest()) {
            appExecutors.networkIO().execute {
                val request = requestFromNetwork()
                request.enqueue(object : Callback<RequestType> {
                    override fun onFailure(call: Call<RequestType>, throwable: Throwable) {
                        onFailedRequest(throwable)
                        loadDatabaseResult(result)
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
                            result.postValue(saveCallResult(body))
                        }
                    }
                })

            }
        } else {
            loadDatabaseResult(result)
        }
    }

    private fun loadDatabaseResult(result: MutableLiveData<ResultType>) {
        appExecutors.diskIO().execute {
            result.postValue(loadFromDatabase())
        }
    }

    abstract fun shouldRequest(): Boolean

    abstract fun loadFromDatabase(): ResultType

    abstract fun requestFromNetwork(): Call<RequestType>

    abstract fun saveCallResult(response: RequestType): ResultType

    abstract fun onFailedRequest(throwable: Throwable)
}
