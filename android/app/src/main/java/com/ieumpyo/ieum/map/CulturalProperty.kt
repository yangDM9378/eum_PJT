package com.ieumpyo.ieum.map


import android.util.Log

class CulturalProperty {

    companion object{

        fun getCP(culturalProperty : String, cpData : List<String>): String {
            val tmp=culturalProperty.split("|")
            for (data:String in cpData) {
                var dataArray = data.split("|")
                if (tmp != null) {

                    Log.d("CulturalProperty",dataArray.last().toString()+"  "+culturalProperty?.last().toString())

                    if (dataArray.last().equals(tmp.last())) {
                        return data
                    }
                }
            }
//            return "35.2052455|126.8117768|ssafy|poi|https://www.ssafy.com/ksp/jsp/swp/swpMain.jsp|1111"
            return ""
        }
    }
}