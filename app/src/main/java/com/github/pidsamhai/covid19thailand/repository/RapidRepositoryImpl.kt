package com.github.pidsamhai.covid19thailand.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.pidsamhai.covid19thailand.db.dao.CountriesDao
import com.github.pidsamhai.covid19thailand.db.dao.StaticDao
import com.github.pidsamhai.covid19thailand.db.network.NetWorkDataSource
import com.github.pidsamhai.covid19thailand.network.response.rapid.country.Countries
import com.github.pidsamhai.covid19thailand.network.response.rapid.fuckyou.Static
import com.github.pidsamhai.covid19thailand.network.response.rapid.hell.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class RapidRepositoryImpl(
    private val staticDao: StaticDao,
    private val countriesDao: CountriesDao,
    private val netWorkDataSource: NetWorkDataSource
) : RapidRepository {

    private val _cacheCountries: MutableLiveData<List<String>> = MutableLiveData()
    private val _cacheStatic: MutableLiveData<Response> = MutableLiveData()

    override val countries: MutableLiveData<List<String>>
        get() = _cacheCountries

    override val static: MutableLiveData<Response>
        get() = _cacheStatic

    init {
        netWorkDataSource.downloadStatic.observeForever {
            it?.let {
                roomPersistence(it)
            }
        }
        netWorkDataSource.downloadCountries.observeForever {
            it?.let {
                roomPersistence(it)
            }
        }

        netWorkDataSource.downloadStatics.observeForever {
            it?.let {
                roomPersistence(it)
            }
        }

        staticDao.getCountries().observeForever {
            it?.let {
                _cacheCountries.postValue(it)
            }
        }

//        staticDao.getStatic().observeForever {
//            it?.let {
//                _cacheStatic.postValue(it)
//            }
//        }

        GlobalScope.launch(Dispatchers.IO) {
//            _fetchCountries()
            _fetchStatics()
        }
    }

    private fun roomPersistence(data: Any) {
        GlobalScope.launch(Dispatchers.IO) {
            when (data) {
                is Static -> {
                    data.parameters?.country?.let {
                        data.pk = it
                    }
                    staticDao.upSert(data)
                }
                is Countries -> {
                    countriesDao.upSert(data)
                }
                else -> {
                    @Suppress("UNCHECKED_CAST")
                    staticDao.upSertStatics(data as List<Response>)
                }
            }
        }
    }

    private suspend fun _fetchCountries() {
        netWorkDataSource.fetchCountires()
    }

    override suspend fun fetchCountries() {
        _fetchCountries()
    }

    private suspend fun fetchStatic(country: String) {
        netWorkDataSource.fetchStatic(country)
    }

    override fun getStatic(country: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val _temp = staticDao.getStatics(country).value
            _temp?.let {
                _cacheStatic.postValue(it)
            }
        }
    }

    private suspend fun _fetchStatics() {
        netWorkDataSource.fetchStatics()
    }

    override suspend fun getStatic2(country: String): LiveData<out Static> {
        return withContext(Dispatchers.IO){
            fetchStatic(country)
            return@withContext staticDao.getStaticReal(country)
        }
    }
}