package com.ieumpyo.ieum.map


import android.util.Log

class CulturalProperty {

    companion object{
        fun getCP(culturalProperty : String?, cpData : Array<String>): String {
            for (data:String in cpData) {
                var dataArray = data.split("|")
                if (culturalProperty != null) {
                    if (dataArray.last().equals(culturalProperty.trim())) {
                        return data
                    }
                }
            }
//            return "35.2052455|126.8117768|ssafy|poi|https://www.ssafy.com/ksp/jsp/swp/swpMain.jsp|1111"
            return ""
        }
    }
}