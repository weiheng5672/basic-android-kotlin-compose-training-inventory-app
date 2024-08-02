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

            // HomeScreen 有兩個形參
            // 分別用來指示 兩個可被點擊的地方 被點擊後的行為
            // 之所以講的那麼抽象 是因為這邊就顯示出這些信息而已
            // 實際上 那兩個地方 在HomeScreen 有定義清楚
            // 他們被點擊後 會觸發 navController
            // 導覽至另一個畫面
            HomeScreen(

                // 被按下後會導覽至 ItemEntryDestination.route 也就是 ItemEntryScreen
                navigateToItemEntry = { navController.navigate(ItemEntryDestination.route) },

                //
                navigateToItemUpdate = {
                    navController.navigate("${ItemDetailsDestination.route}/${it}")
                }
            )

        }

        // ItemEntryDestination.route 就是 ItemEntryScreen 的位址
        // 這邊再強調，在安卓Compose導覽的邏輯中
        // 就是要在這邊指定 各個composable 的位址
        composable(route = ItemEntryDestination.route) {

            ItemEntryScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )

        }


        composable(
            route = ItemDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(ItemDetailsDestination.itemIdArg) {
                type = NavType.IntType
            })
        ) {
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
