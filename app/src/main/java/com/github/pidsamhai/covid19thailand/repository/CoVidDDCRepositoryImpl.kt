package com.github.pidsamhai.covid19thailand.repository

import androidx.lifecycle.MutableLiveData
import com.github.pidsamhai.covid19thailand.db.dao.TimeLineDao
import com.github.pidsamhai.covid19thailand.db.dao.TodayDao
import com.github.pidsamhai.covid19thailand.db.network.NetWorkDataSource
import com.github.pidsamhai.covid19thailand.network.response.ddc.Data
import com.github.pidsamhai.covid19thailand.network.response.ddc.TimeLine
import com.github.pidsamhai.covid19thailand.network.response.ddc.Today
import com.github.pidsamhai.covid19thailand.network.response.rapid.covid193.base.Datas
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Suppress("FunctionName")
class CoVidDDCRepositoryImpl(
    private val todayDao: TodayDao,
    private val timeLineDao: TimeLineDao,
    private val netWorkDataSource: NetWorkDataSource
) :
    CoVidDDCRepository {

    private val _cacheToday = MutableLiveData<Today>()
    private val _cacheTimeLine = MutableLiveData<TimeLine>()
    private val _cacheTimeLineData = MutableLiveData<List<Data>>()


    init {
        netWorkDataSource.downloadToday.observeForever {
            it?.let {
                roomPersistence(it)
            }
        }
        netWorkDataSource.downloadTimeLine.observeForever {
            it?.let {
                roomPersistence(it)
            }
        }
        todayDao.getToDay().observeForever {
            it?.let {
                _cacheToday.postValue(it)
            }
        }
        timeLineDao.getDatas().observeForever {
            it?.let {
                _cacheTimeLineData.postValue(it)
            }
        }
        timeLineDao.getTimeLine().observeForever {
            it?.let {
                _cacheTimeLine.postValue(it)
            }
        }
        timeLineDao.getTimeLine()
        fetchTodayData()
        fetchTimeline()
    }


    override val today: MutableLiveData<Today>
        get() = _cacheToday

    override val timeLine: MutableLiveData<TimeLine>
        get() = _cacheTimeLine

    override val timeLineData: MutableLiveData<List<Data>>
        get() = _cacheTimeLineData

    private suspend fun _fetchTodayData() {
        netWorkDataSource.fetchToday()
    }

    private suspend fun _fetchTimeLineData(){
        netWorkDataSource.fetchTimeLine()
    }

    override fun fetchTodayData() {
        GlobalScope.launch(Dispatchers.IO) {
            _fetchTodayData()
        }
    }

    override fun fetchTimeline() {
        GlobalScope.launch(Dispatchers.IO) {
            _fetchTimeLineData()
        }
    }

    private fun roomPersistence(data: Any) {
        GlobalScope.launch(Dispatchers.IO) {
            when(data){
                is Today -> {
                    todayDao.upSert(data)
                }
                is TimeLine -> {
                    timeLineDao.upSert(data)
                    data.data?.let { timeLineDao.upSertDatas(it) }
                }
            }
        }
    }
}