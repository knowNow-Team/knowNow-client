package com.example.konwnow.ui.view.group

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.konwnow.R
import com.example.konwnow.data.model.dto.FolderDTO
import com.example.konwnow.data.model.dto.Words
import com.example.konwnow.ui.adapter.GroupsAdapter
import com.example.konwnow.ui.adapter.WordsAdapter
import com.example.konwnow.ui.view.MainActivity

class GroupActivity : AppCompatActivity() {
    var btnBack : ImageButton? = null
    private lateinit var rvGroups : RecyclerView
    private var groupsList = arrayListOf<FolderDTO>()
    private lateinit var groupsAdapter : GroupsAdapter
    //rivate val defalutGroups =resources.getStringArray(R.array.groups_list)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group)

        requsetGroups()

        btnBack = findViewById(R.id.ib_back)
        btnBack!!.setOnClickListener{
            finish()
        }

    }

    private fun requsetGroups() {
        groupsList.clear()

        groupsAdapter = GroupsAdapter()
        rvGroups = findViewById(R.id.rv_groups)
        rvGroups.layoutManager = GridLayoutManager(this, 2)

        groupsList.add(FolderDTO("전체"))
        groupsList.add(FolderDTO("틀린 문제 "))
        groupsList.add(FolderDTO("휴지통"))
        groupsList.add(FolderDTO("토익 영단어"))
        groupsList.add(FolderDTO("영어2"))
        groupsList.add(FolderDTO("영어회화"))
        groupsList.add(FolderDTO("공무원시험"))
        groupsList.add(FolderDTO("토익 영단어"))
        groupsList.add(FolderDTO("영어2"))
        groupsList.add(FolderDTO("영어회화"))
        groupsList.add(FolderDTO("공무원시험"))

        groupsAdapter.groupsUpdateList(groupsList)
        rvGroups.adapter = groupsAdapter
        groupsAdapter.notifyDataSetChanged()
    }


}