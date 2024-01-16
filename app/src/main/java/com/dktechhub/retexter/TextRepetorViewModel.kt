package com.dktechhub.retexter

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.Collections.replaceAll
import java.util.Collections.replaceAll
import kotlin.coroutines.CoroutineContext


class TextRepetorViewModel : ViewModel(),mCallback {



    var finalRes= MutableLiveData("")
    var str=MutableLiveData("")
    var nln=false
    val cnt=MutableLiveData(1)
    var stCurr =""
    val textUtils=TextUtils()
    val load = MutableLiveData(false)

      var arr=  ArrayList<String>()

    fun repeat(string: String)
    {
        viewModelScope.launch(Dispatchers.IO) { repeat_back(string) }
    }
    suspend fun repeat_back(string: String)
    {
        val sb = StringBuilder()
        var t2=string
        t2 += if(nln) {
            "\n"
        }else " "
        var x=cnt.value

        while (x!!>0)
        {
        sb.append(t2)
        x--
        }

        viewModelScope.launch(Dispatchers.Main) { finalRes.value=sb.toString() ; sb.clear() }

        //sb.clear()


    }

    fun refreshStyle()
    {
        if(str.value!=stCurr&&str.value!=null)
        {
            stCurr=str.value!!
            viewModelScope.launch(Dispatchers.IO) { applyStyle(stCurr,this@TextRepetorViewModel) }
        }
    }



    private fun applyStyle(string: String, mCallback: mCallback)
    {
        viewModelScope.launch(Dispatchers.Main) { mCallback.onLoaded(textUtils.applyStyle(string)) }
    }

    override fun onLoaded(all: ArrayList<String>) {
        this.arr=all
        load.value=true
    }

    fun reset(){
        finalRes.value=""
        //cnt.value=1
        //str.value=""
    }

}

interface mCallback{
    fun onLoaded( all:ArrayList<String>)

}