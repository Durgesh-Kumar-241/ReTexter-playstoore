package com.dktechhub.retexter

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TextRepetorViewModel : ViewModel() {
    var finalRes= MutableLiveData("")
    var str=MutableLiveData("")
    var nln=false
    val cnt=MutableLiveData(10)
    fun repeat()
    {
        val sb = StringBuilder()
        var t2=str.value
        t2 += if(nln) {
            "\n"
        }else " "
        var x=cnt.value

        while (x!!>0)
        {
        sb.append(t2)
        x--;
        }

        finalRes.value=sb.toString()
        Log.d("xxx0",finalRes.value.toString())
        sb.clear()

    }
}