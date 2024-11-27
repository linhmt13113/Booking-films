package com.uilover.project2002.data.model

data class Seat(var status:SeatStatus,var name:String){

    enum class SeatStatus{
        AVAILABLE,SELECTED,UNAVAILABLE
    }
}
