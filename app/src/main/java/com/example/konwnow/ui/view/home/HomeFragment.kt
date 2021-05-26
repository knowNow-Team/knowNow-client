package com.example.konwnow.ui.view.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Switch
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.konwnow.R
import com.example.konwnow.data.remote.dto.WordBook
import com.example.konwnow.data.remote.dto.Words
import com.example.konwnow.ui.adapter.WordsAdapter
import com.example.konwnow.ui.view.MainActivity
import com.example.konwnow.ui.view.group.GroupActivity
import com.example.konwnow.utils.Constants
import com.example.konwnow.utils.WORDBOOK
import com.example.konwnow.viewmodel.WordBookViewModel


class HomeFragment : Fragment() {

    private lateinit var v: View
    private lateinit var switch: Switch
    private lateinit var groupButton: TextView
    private lateinit var detailButton : ImageButton
    private lateinit var rvWords: RecyclerView
    private lateinit var wordsAdapter: WordsAdapter
    private var wordsList = arrayListOf<WordBook.GetAllWordResponseData>()
    private lateinit var workBookViewModel: WordBookViewModel

    private lateinit var wordBookIDlist : List<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_home, container, false)

        switch = v.findViewById(R.id.switch_hide)
        rvWords = v.findViewById(R.id.rv_home_words) as RecyclerView
        switch.isChecked = true

        setSwitch()
        setButton()


        return v
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1004 && data != null){
            val datas = data!!.getStringArrayListExtra("selected")
            val firstTitle = data!!.getStringExtra("first")
            wordBookIDlist = datas!!.toList()

            if(datas!!.get(0) == WORDBOOK.TRASH_BOOK_ID){
                groupButton.text = firstTitle
                requestTrashWord()
            }else{
                if(wordBookIDlist!!.size == 1){
                    groupButton.text = firstTitle
                }else{
                    groupButton.text = "${firstTitle} 외 ${(wordBookIDlist?.size)?.minus(1)}"
                }
                requestAllWord()
            }
            setRecycler()
        }
    }

    private fun setRecycler() {
        wordsAdapter = WordsAdapter()
        wordsAdapter.wordsUpdateList(wordsList)

        val swipeHelperCallBack = SwipeHelperCallBack().apply {
            setClamp(200f)
        }
        val itemTouchHelper = ItemTouchHelper(swipeHelperCallBack)
        itemTouchHelper.attachToRecyclerView(rvWords)

        rvWords.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = wordsAdapter
            setOnTouchListener { _, _ ->
                swipeHelperCallBack.removePreviousClamp(rvWords)
                false
            }
        }
    }


    private fun requestAllWord() {
        workBookViewModel = ViewModelProvider(this, defaultViewModelProviderFactory).get(WordBookViewModel::class.java)
        workBookViewModel.getWordDataResponse().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                Log.d(Constants.TAG, "단어 가져오기 성공!")
                var allWord = ArrayList<Words.Word>()
                allWord.clear()
                //TODO: filter 확인

            } else {
                Log.d(Constants.TAG, "단어장 get response null!")
            }
        })
        workBookViewModel.getAllWord(MainActivity.getUserData().loginToken,wordBookIDlist.joinToString(","))
    }


    private fun requestTrashWord() {
        workBookViewModel = ViewModelProvider(this, defaultViewModelProviderFactory).get(WordBookViewModel::class.java)
        workBookViewModel.getWordDataResponse().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                Log.d(Constants.TAG, "휴지통 가져오기 성공!")
                var allWord = ArrayList<Words.Word>()
                allWord.clear()
                //TODO: filter 확인

            } else {
                Log.d(Constants.TAG, "휴지통 get response null!")
            }
        })
        workBookViewModel.getTrashWord(MainActivity.getUserData().loginToken)
    }


    private fun setSwitch() {
        switch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                wordsAdapter.toggleUpdate(true)
                wordsAdapter.notifyDataSetChanged()
            } else {
                wordsAdapter.toggleUpdate(false)
                wordsAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun setButton() {
        groupButton = v.findViewById(R.id.tv_group_text)
        groupButton.setOnClickListener {
            activity?.let {
                val intent = Intent(context, GroupActivity::class.java)
                startActivityForResult(intent,1004)
            }
        }

        detailButton = v.findViewById(R.id.ib_detail_setting)
        detailButton.setOnClickListener {
            activity?.let {
                val intent = Intent(context, DetailSettingActivity::class.java)
                startActivity(intent)
            }
        }

    }
}