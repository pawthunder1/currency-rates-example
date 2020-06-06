package com.pawthunder.currencyexample.repository

import androidx.lifecycle.MutableLiveData
import com.pawthunder.currencyexample.util.AppExecutors
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Resource for loading data from database or network.
 * @constructor For creating NetworkDataResource with all essential properties.
 * @property appExecutors executors for network and disk tasks running on different thread.
 * @property result LiveData for posting result.
 */
abstract class NetworkDataResource<RequestType, ResultType>(
    private val appExecutors: AppExecutors,
    private val result: MutableLiveData<ResultType>
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

    /**
     * Perform additional changes to output value and post it to result.
     * @param value Final value send as output.
     */
    abstract fun postValue(value: ResultType)

    /**
     * Method returns if output value should be requested from network or not
     * @return Boolean value if true data should be requested from network.
     */
    abstract fun shouldRequest(): Boolean

    /**
     * Method create query and request data from local database
     * @return Return loaded data from database.
     */
    abstract fun loadFromDatabase(): ResultType

    /**
     * Method creating call which will be requested from network through retrofit
     * @return Call for requesting data from network.
     */
    abstract fun requestFromNetwork(): Call<RequestType>

    /**
     * Network response was successful and can be saved into local database.
     * @param response Success response from network containing data.
     * @return Data saved into local database.
     */
    abstract fun saveCallResult(response: RequestType): ResultType

    /**
     * Network request failed with [Throwable]. User should be notified and error should be
     * resolved.
     * @param throwable containing message about network error.
     */
    abstract fun onFailedRequest(throwable: Throwable)
}
