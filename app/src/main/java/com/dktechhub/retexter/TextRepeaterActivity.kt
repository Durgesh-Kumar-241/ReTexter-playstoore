package com.dktechhub.retexter



import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.dktechhub.retexter.databinding.ActivityTextRepeaterBinding
import com.google.android.material.bottomsheet.BottomSheetDialog


class TextRepeaterActivity : AppCompatActivity(),View.OnClickListener,AdapterView.OnItemClickListener {
    lateinit var viewModel:TextRepetorViewModel
    lateinit var dialog: BottomSheetDialog



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val textRepeaterBinding:ActivityTextRepeaterBinding = DataBindingUtil.setContentView(this,R.layout.activity_text_repeater)
        viewModel= ViewModelProvider(this)[TextRepetorViewModel::class.java]
        textRepeaterBinding.viewModel=viewModel

        val fin_t = findViewById<TextView>(R.id.fin_text)
        val cp =findViewById<Button>(R.id.button)
        val share =findViewById<Button>(R.id.button2)
        val style =findViewById<Button>(R.id.button3)
        val rep =findViewById<Button>(R.id.button4)

        dialog = BottomSheetDialog(this)
        dialog.setContentView(R.layout.activity_list_item)
        val listView = dialog.findViewById<View>(R.id.listViewBtmSheet) as BottomSheetListView?
        val arr: ArrayAdapter<String> = ArrayAdapter<String>(
            this,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            viewModel.arr
        )
        //arr.se
        if (listView != null) {
            listView.adapter=arr
            listView.onItemClickListener=this
        }


        viewModel.load.observe(this) {
            if (it) {
                arr.clear()
                arr.addAll(viewModel.arr)
                viewModel.load.value = false
            }
        }



        viewModel.finalRes.observe(this) {
           // fin_t.text = it
            cp.isEnabled= it.isNotEmpty()
            share.isEnabled=it.isNotEmpty()

            fin_t.text = it
        }

        cp.setOnClickListener (this)
        share.setOnClickListener (this)
        rep.setOnClickListener(this)
        style.setOnClickListener (this)



        viewModel.str.observe(this) {
            rep.isEnabled = it.isNotEmpty()&& viewModel.cnt.value!! >0
            style.isEnabled=it.isNotEmpty()
            //inp.setText(it)
        }
        //inp.addTextChangedListener(onTextChanged = { charSequence: CharSequence?, i: Int, i1: Int, i2: Int -> if(viewModel.str.value!=charSequence.toString()){viewModel.str.value=charSequence.toString()} } )

        viewModel.cnt.observe(this) {
            //cnt.setText(it.toString())
            rep.isEnabled= it>0&& viewModel.str.value?.isNotEmpty() ?: false
        }
        //cnt.addTextChangedListener(onTextChanged = { charSequence: CharSequence?, i: Int, i1: Int, i2: Int -> validate(charSequence)} )

    }






    fun copyToClipboard() {
        val clipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clipData =
            ClipData.newPlainText("text", viewModel.finalRes.value)
        clipboardManager.setPrimaryClip(clipData)
        //Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show()
    }

    fun shareText() {
        val intent = Intent()
        intent.setAction(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_TEXT, viewModel.finalRes.value)
        intent.setType("text/plain")
        startActivity(Intent.createChooser(intent, "Share generated text"))
    }

    override fun onClick(v: View?) {
        val id= v?.id
        when(id)
        {
            R.id.button4-> viewModel.str.value?.let { viewModel.repeat(it) }
            R.id.button->copyToClipboard()
            R.id.button2->shareText()
            R.id.button3->showStyles()
        }

    }

    fun showStyles()
    {
        viewModel.refreshStyle()

        dialog.show()
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (parent != null) {
           val s= parent.adapter.getItem(position) as String
            //viewModel.str.value=s
            dialog.hide()
            if(s.isNotEmpty()&& viewModel.cnt.value!! >0)
            {
                viewModel.repeat(s)
            }
        }
    }


}