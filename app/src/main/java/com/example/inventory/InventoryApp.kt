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

package com.example.inventory

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.inventory.R.string
import com.example.inventory.ui.navigation.InventoryNavHost

/**
 * Top level composable that represents screens for the application.
 */
// 如上所述 這裡是所謂的 Top level composable
// UI 就是呈現在手機螢幕上的畫面
// 而這裡就是本專案中 UI的最上層
// 意思就是 呼叫InventoryApp
// 就相當於 呼叫了所有的UI
// 呼叫的InventoryApp 就是 MainActivity
// 這個函數內 只有一個東西 InventoryNavHost
// 這個APP 有四個畫面
// InventoryNavHost 能夠根據情況
// 去顯示不同的畫面
@Composable
fun InventoryApp(navController: NavHostController = rememberNavController()) {
    InventoryNavHost(navController = navController)
}

/**
 * 顯示應用程式的標題欄（App Bar），並根據需要顯示返回導航圖標。
 *
 * 這個組件是一個 UI 元件，通常顯示在應用程式的頂部，並用於顯示標題和提供導航功能。
 * 在實務中，應用程式的畫面邊上通常會有一個固定的區域，顯示當前頁面資訊或提供導航選擇。
 * 在這個專案中，有些畫面的標題欄可能會根據需要變動，而有些則保持固定。
 * 使用這個 Composable 函數時，可以根據需要設置標題、是否顯示返回圖標、滾動行為等。
 *
 * @param title 標題文本。
 * @param canNavigateBack 是否顯示返回導航圖標。
 * @param modifier 用於修飾組件的外觀和佈局。
 * @param scrollBehavior 滾動行為，可以為 null。
 * @param navigateUp 返回按鈕點擊後的操作，默認為空函數。
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryTopAppBar(
    title: String,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    navigateUp: () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = { Text(title) },
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(string.back_button)
                    )
                }
            }
        }
    )
}
