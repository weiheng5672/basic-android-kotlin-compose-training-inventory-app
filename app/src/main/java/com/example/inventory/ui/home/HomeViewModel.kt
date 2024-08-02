/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.inventory.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventory.data.Item
import com.example.inventory.data.ItemsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * ViewModel to retrieve all items in the Room database.
 */
// HomeViewModel 代表 UI的狀態
// HomeViewModel 是 一個類
// 他的主建構式 需要 傳入 其他的類
// 這就是火星專案中提到的 所謂的 依賴注入
// 他代表UI的狀態 要負責向UI提供 UI應該呈現的東西
// 而那些東西哪來的 他去找 Repository 要的
// 至於 Repository是怎麼要的
// 他不知道 也不需要知道
class HomeViewModel(

    itemsRepository: ItemsRepository

) : ViewModel() {

    /**
     * 保存當前的 HomeScreen UI 狀態。從 [ItemsRepository] 獲取項目列表並映射到 [HomeUiState]
     */
    // HomeViewModel 只有一個屬性
    // 就叫做 homeUiState
    // 變數的類型是 所謂的 StateFlow<HomeUiState>
    // HomeUiState 是專案自定義的 就定義在下面
    // StateFlow 是 別的庫提供的一種東西
    // 這邊先不用管理論上他是什麼
    // 下面可以看到 HomeUiState 有個屬性 itemList
    // 指向 Item 的 List 且預設為空字串
    // StateFlow 就是把整陀東西 封裝起來
    // 方便做些和資料庫的操作有關的事情
    // 而那些事就不是我們需要知道的細節
    val homeUiState: StateFlow<HomeUiState> =

        itemsRepository.getAllItemsStream() // 從 repository 獲取項目的 Flow
            .map { HomeUiState(it) } // 將項目映射到 HomeUiState
            .stateIn(
                scope = viewModelScope, // ViewModel 的生命週期感知範圍
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS), // 分享策略
                initialValue = HomeUiState() // UI 狀態的初始值
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L  // 訂閱的超時持續時間
    }
}

/**
 * 表示 HomeScreen UI 狀態的數據類。
 * [HomeUiState] 保存要顯示在 HomeScreen 上的項目列表。默認初始化為空列表。
 */
data class HomeUiState(val itemList: List<Item> = listOf())
