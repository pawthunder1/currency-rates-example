package com.pawthunder.currencyexample.repository

import androidx.lifecycle.MutableLiveData
import com.pawthunder.currencyexample.util.AppExecutors
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Resource for loading data from database or network.
 * @constructor For creating NetworkDataResource with all essential properties
 * @property appExecutors executors for network and disk tasks running on different thread.
 * @property result LiveData for posting result.
 * @property shouldPostValue Livedata indicating if value should be posted.
 */
abstract class NetworkDataResource<RequestType, ResultType>(
    private val appExecutors: AppExecutors,
    private val result: MutableLiveData<ResultType>,
    private val shouldPostValue: MutableLiveData<Boolean>? = null
) {

    /**
     * Load data from network or database. Depends on network and UI conditions.
     */
    fun loadData() {
        if (shouldRequest()) {
            appExecutors.networkIO().execute {
                if (result.value == null) {
                    // load data from database if output is empty
                    loadDatabaseResult()
                }

                val request = requestFromNetwork()
                request.enqueue(object : Callback<RequestType> {
                    override fun onFailure(call: Call<RequestType>, throwable: Throwable) {
                        onFailedRequest(throwable)
                        // load data from database if request failed
                        loadDatabaseResult()
                    }

                    override fun onResponse(
                        call: Call<RequestType>,
                        response: Response<RequestType>
                    ) {
                        val body = response.body()

                        if (body == null) {
                            // load data from database if network response is null
                            loadFromDatabase()
                            return
                        }

                        appExecutors.diskIO().execute {
                            // load data from network if request was successful
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
