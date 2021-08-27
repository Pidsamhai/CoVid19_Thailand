package com.github.pidsamhai.covid19thailand.network.response.ddc


class TodayResponse : ArrayList<Today>() {
    val today: Today?
        get() = this.getOrNull(0)
}

class TodayByProvinceResponse : ArrayList<TodayByProvince>()