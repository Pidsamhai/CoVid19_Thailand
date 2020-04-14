package com.github.pidsamhai.covid19thailand.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.pidsamhai.covid19thailand.db.dao.RapidDao
import com.github.pidsamhai.covid19thailand.db.network.NetWorkDataSource
import com.github.pidsamhai.covid19thailand.network.response.rapid.covid193.Static
import com.github.pidsamhai.covid19thailand.network.response.rapid.covid193.base.Datas
import com.github.pidsamhai.covid19thailand.network.response.rapid.covid193.history.History
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Suppress("FunctionName")
class RapidRepositoryImpl(
    private val rapidDao: RapidDao,
    private val netWorkDataSource: NetWorkDataSource
) : RapidRepository {

    private val _cacheCountries: MutableLiveData<List<String>> = MutableLiveData()
    private val _cacheStatic: MutableLiveData<Datas> = MutableLiveData()
    private val _cacheHistories: MutableLiveData<List<History>> = MutableLiveData()

    override val countries: MutableLiveData<List<String>>
        get() = _cacheCountries

    override val static: MutableLiveData<Datas>
        get() = _cacheStatic

    override val histories: MutableLiveData<List<History>>
        get() = _cacheHistories

    init {

        netWorkDataSource.downloadStatic.observeForever {
            it?.let {
                roomPersistence(it)
            }
        }

        netWorkDataSource.downloadHistory.observeForever {
            it?.let {
                historyPersistence(it.second, it.first)
            }
        }

        netWorkDataSource.downloadStatics.observeForever {
            it?.let {
                roomPersistence(it)
            }
        }

        rapidDao.getCountries().observeForever {
            it?.let {
                _cacheCountries.postValue(it)
            }
        }

        fetchStaticsData()
    }

    private fun roomPersistence(data: Any) {
        GlobalScope.launch(Dispatchers.IO) {
            when (data) {
                is Static -> {
                    data.parameters?.country?.let {
                        data.pk = it
                    }
                    rapidDao.upSert(data)
                }
                else -> {
                    @Suppress("UNCHECKED_CAST")
                    rapidDao.upSertStatics(data as List<Datas>)
                }
            }
        }
    }

    private fun historyPersistence(country: String, history: List<History>) {
        GlobalScope.launch(Dispatchers.IO) {
            rapidDao.dropHistory(country)
            rapidDao.upSertHistories(history)
        }
    }

    private suspend fun _fetchHistories(country: String) {
        netWorkDataSource.fetchHistories(country)
    }

    override suspend fun fetchHistories(country: String) {
        _fetchHistories(country)
    }


    override fun fetchStaticsData() {
        GlobalScope.launch(Dispatchers.IO) {
            _fetchStatics()
        }
    }

    private suspend fun _fetchStatics() {
        netWorkDataSource.fetchStatics()
    }

    override suspend fun getStatic(country: String): LiveData<out Datas> {
        return withContext(Dispatchers.IO) {
            return@withContext rapidDao.getStatic(country)
        }
    }

    override suspend fun getHistory(country: String): LiveData<out List<History>> {
        return withContext(Dispatchers.IO) {
            _fetchHistories(country)
            return@withContext rapidDao.getHistory(country)
        }
    }
}