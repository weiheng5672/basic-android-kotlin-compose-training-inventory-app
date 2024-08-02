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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventory.InventoryTopAppBar
import com.example.inventory.R
import com.example.inventory.data.Item
import com.example.inventory.ui.AppViewModelProvider
import com.example.inventory.ui.item.formatedPrice
import com.example.inventory.ui.navigation.NavigationDestination
import com.example.inventory.ui.theme.InventoryTheme

// 定義 物件HomeDestination 實現 介面NavigationDestination
// 這裡沒有而外定義其他資訊
// 這個部分就是 用來在 NavHost 指示 HomeScreen的位址
object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

/**
 * Entry route for Home screen
 */

// 這邊關注兩個重點
// 第一 導覽
// 第二 從ViewModel 獲得狀態
// 剩下的東西雖然也很重要
// 就是如何使用 各種Composable 呈現畫面
// 但那就是藝術層面的問題了
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(

    navigateToItemEntry: () -> Unit,
    navigateToItemUpdate: (Int) -> Unit,
    modifier: Modifier = Modifier,

    // 在安卓的體系裡 ViewModel 本身雖然是個類
    // 但他卻無法自行處理 需要傳入參數的建構式
    // 需要透過 所謂的 viewModel factory 去幫助他
    // 我一直覺得這是一種深層次的缺陷 但反正目前他就是要這麼做
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)

    // 本專案中 所有的 建構式需要傳入參數的 ViewModel
    // 都是在 AppViewModelProvider 統一進行

) {

    // 前面提過 viewModel 透過 Repository 向 資料庫取得資料
    // 取得的資料基於一些因素 是一種比較特殊的 資料型態 Flow
    // 但是在這邊
    // 一來透過 .collectAsState() 將Flow 變成 State
    // 二來透過 by委託屬性的方式
    // homeUiState 操作裡來就像個 一般的 HomeUiState
    // 至此 其餘就是如何顯示畫面的問題
    // 包括 導覽、AppBar的設定
    val homeUiState by viewModel.homeUiState.collectAsState()

    // 這個變數就和AppBar的設定有關 它其實可以省略掉
    // 直接在需要的地方 使用 TopAppBarDefaults.enterAlwaysScrollBehavior()
    // 但這樣可以增加可讀性
    // 強調了這個 Composable 的 TopAppBar 要是可以動的
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    // Scaffold 是 Jetpack Compose 提供的一個 Composable
    // 用來設定整個畫面的框架，包括應用程式的標題欄（App Bar）、底部的浮動式按鈕等
    // 簡化畫面佈局的設置，它會自動處理不同組件之間的佈局問題
    Scaffold(

        // 外觀設定，這裡使用 modifier 來處理滾動行為的佈局設定
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),

        // 應用程式的標題欄（App Bar）這裡使用 InventoryTopAppBar 作為標題欄的實作
        // InventoryTopAppBar 是本專案自訂義的Composable  
        topBar = {
            InventoryTopAppBar(
                // AppBar顯示的文字，使用資源字符串來設置標題
                title = stringResource(HomeDestination.titleRes),
                // 此頁面不支持返回操作
                canNavigateBack = false,
                // 滾動行為設定 根據上面 它是能動的
                scrollBehavior = scrollBehavior
            )
        },

        // 浮動式按鈕
        floatingActionButton = {

            FloatingActionButton(
                // 按鈕被點擊時的行為
                onClick = navigateToItemEntry,
                // 按鈕的形狀
                shape = MaterialTheme.shapes.medium,
                // 設定按鈕的外觀，這裡的 Modifier 會根據 WindowInsets 設置內邊距
                modifier = Modifier
                    .padding(
                        end = WindowInsets.safeDrawing.asPaddingValues()
                            .calculateEndPadding(LocalLayoutDirection.current)
                    )
            ) {
                //按鈕的圖示
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.item_entry_title)
                )
            }

        },

    ) {

        innerPadding ->
        // Scaffold 的內容部分
        // innerPadding 是 Scaffold 自動計算出來的內邊距
        // 避免被 topBar 和 floatingActionButton 遮擋
        HomeBody(

            // 就是這裡 由於 homeUiState 使用了by委託屬性的方式
            // 即使從 viewModel 取得的是所謂的 Flow
            // 並轉換成所謂的 State 這種高度封裝的形式
            // 這邊使用 homeUiState 依然像 使用一個 HomeUiState類型的變數
            itemList = homeUiState.itemList,

            // 點擊項目時的處理函數
            // 這個是資訊卡被點擊時 要做的動作
            onItemClick = navigateToItemUpdate,
            
            // 設置 HomeBody 的外觀和填滿整個可用區域
            modifier = modifier.fillMaxSize(),
            
            // 設置內容的內邊距
            //確保內容不會被 topBar 和 floatingActionButton 遮擋
            contentPadding = innerPadding,

        )

    }

}

@Composable
private fun HomeBody(
    itemList: List<Item>,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        if (itemList.isEmpty()) {
            Text(
                text = stringResource(R.string.no_item_description),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(contentPadding),
            )
        } else {
            InventoryList(
                itemList = itemList,
                onItemClick = { onItemClick(it.id) },
                contentPadding = contentPadding,
                modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_small))
            )
        }
    }
}

@Composable
private fun InventoryList(
    itemList: List<Item>,
    onItemClick: (Item) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding
    ) {
        items(items = itemList, key = { it.id }) { item ->
            InventoryItem(item = item,
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_small))
                    // 這部分是個關鍵
                    // 他表示 這個Composable 是可以被點擊的
                    // 被點擊的時候 會 執行 onItemClick(item)
                    // 它是外部傳進來的 lambda
                    .clickable { onItemClick(item) }

            )
        }
    }
}

@Composable
private fun InventoryItem(
    item: Item, modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier, elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small))
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleLarge,
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = item.formatedPrice(),
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Text(
                text = stringResource(R.string.in_stock, item.quantity),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeBodyPreview() {
    InventoryTheme {
        HomeBody(listOf(
            Item(1, "Game", 100.0, 20), Item(2, "Pen", 200.0, 30), Item(3, "TV", 300.0, 50)
        ), onItemClick = {})
    }
}

@Preview(showBackground = true)
@Composable
fun HomeBodyEmptyListPreview() {
    InventoryTheme {
        HomeBody(listOf(), onItemClick = {})
    }
}

@Preview(showBackground = true)
@Composable
fun InventoryItemPreview() {
    InventoryTheme {
        InventoryItem(
            Item(1, "Game", 100.0, 20),
        )
    }
}
