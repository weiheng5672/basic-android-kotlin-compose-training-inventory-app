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

package com.example.inventory.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.inventory.ui.home.HomeDestination
import com.example.inventory.ui.home.HomeScreen
import com.example.inventory.ui.item.ItemDetailsDestination
import com.example.inventory.ui.item.ItemDetailsScreen
import com.example.inventory.ui.item.ItemEditDestination
import com.example.inventory.ui.item.ItemEditScreen
import com.example.inventory.ui.item.ItemEntryDestination
import com.example.inventory.ui.item.ItemEntryScreen

/**
 * Provides Navigation graph for the application.
 */
// 4個 composable 對應的就是 4個畫面
// composable 需要傳入的引數 就是 他的位址
// 所謂的位址 就是 一個字串
// 在這個專案裡 用來代表 composable的位置
// 程式的其他部分都是透過 所謂的navController 參照這個地方
@Composable
fun InventoryNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        //預設顯示 HomeScreen
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        // 比方說 這邊就表示 HomeDestination.route
        // 就是 HomeScreen 這個 composable 的位址
        // 也就是當我 在其他地方指示 我要去HomeDestination.route
        // 他就會從那個地方 跳轉到 HomeScreen
        composable(route = HomeDestination.route) {

            // HomeScreen 需要兩個參數，分別處理兩個點擊事件
            // 在 HomeScreen 中定義了這兩個可點擊的區域及其行為
            // 他們被點擊後 會觸發 navController
            // 導覽至另一個畫面
            HomeScreen(

                // 這個點擊事件 會導覽至 ItemEntryScreen
                // 因為 ItemEntryDestination.route 是 ItemEntryScreen的位址
                // 在 HomeScreen 中 這個點擊事件 是 浮動式按鈕需要的
                navigateToItemEntry = { navController.navigate(ItemEntryDestination.route) },

                // 如果畫面需要根據不同的 ID 顯示不同的內容，
                // 這樣的字符串模板可以幫助你方便地構造導航路由
                // 在 HomeScreen 中 這個點擊事件 是 各個不同的資訊卡 需要的
                navigateToItemUpdate = {
                    navController.navigate("${ItemDetailsDestination.route}/${it}")
                }
            )

        }

        // ItemEntryDestination.route 就是 ItemEntryScreen 的位址
        // 在安卓Compose導覽的邏輯中
        // 就是要在這邊指定 各個composable 的位址
        composable(route = ItemEntryDestination.route) {

            ItemEntryScreen(

                // 點擊後返回上一層
                navigateBack = { navController.popBackStack() },


                onNavigateUp = { navController.navigateUp() }
            )

        }


        // 因為有很多個 Item，每個 Item 都有一個唯一的 ID
        // 這個畫面需要根據 Item 的 ID 顯示不同的內容
        // 當用戶點擊不同的 Item 時，會傳遞該 Item 的 ID 作為導航參數
        // 導航到這個畫面的位址會包含這個 ID，例如 "item_details/123"
        composable(

            // 導航系統會將 "item_details/123" 與 ItemDetailsDestination.routeWithArgs（即 "item_details/{itemId}"）進行匹配。
            // 導航系統識別到 {itemId} 部分需要解析
            route = ItemDetailsDestination.routeWithArgs,

            // 他負責解析
            // 之所以要解析是因為 Item 的 ID 原本是整數
            // 但是 在形成 路由(位址) 時 被轉變成字串
            // 又但是 在 ItemDetailsScreen 中
            // 我們又需要 他變回去整數 好讓我們從資料庫中將他提取出來
            arguments = listOf(

                // 這行代碼告訴導航系統
                // 這個 Composable 需要一個名為 itemId 的參數
                navArgument(
                    ItemDetailsDestination.itemIdArg
                ) {
                    //指定了參數的類型為 Int
                    type = NavType.IntType
                }
            )
            // 當導航系統將 arguments 傳遞給 Composable
            // 這些 arguments 會被存儲在 一個 Bundle 中
            // 這個 組合函數 對應的 ViewModel
            // 會透過 SavedStateHandle 從 那個 Bundle 中提取 arguments

        ) {

            // 形如 "item_details/123" 這種路由都會被導覽到
            // ItemDetailsScreen 這個 組合函數
            ItemDetailsScreen(
                navigateToEditItem = { navController.navigate("${ItemEditDestination.route}/$it") },
                navigateBack = { navController.navigateUp() }
            )
        }


        composable(
            route = ItemEditDestination.routeWithArgs,
            arguments = listOf(navArgument(ItemEditDestination.itemIdArg) {
                type = NavType.IntType
            })
        ) {
            ItemEditScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }

    }
}
