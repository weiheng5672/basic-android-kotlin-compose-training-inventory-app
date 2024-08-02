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

/**
 * Interface to describe the navigation destinations for the app
 */
interface NavigationDestination {
    /**
     * Unique name to define the path for a composable
     */
    // 用來在程式中表示自己的位址
    val route: String

    /**
     * String resource id to that contains title to be displayed for the screen.
     */
    // 用來參照資源檔，顯示不同文字，比方不同語言
    // 如果沒有這個變數，直接用route
    // 當想改變顯示文字，就得更改route
    val titleRes: Int
}
// 這邊的目的就是 用來表示位址的
// 這個程式 有四個畫面
// 所以會有四個位址
// 也就是 會有四個字串 代表那些位址

// 問題：
// 為什麼要搞一個介面出來呢
// 不過就是四個字串而已 這是在做什麼?
//
// 答案：
// 先說結論
// 利用類別和介面的階層式架構
// 去對 位址 進行 組織和管理
//
// 雖然位址確實只是字串而已
// 但這些字串 是用來指示 各個不同畫面的所在
// 他是有其特殊性的
//
// 這個專案還算簡單
// 一個更複雜的APP 有更多畫面
// 這就是必要的
// 一般會用 列舉類 或 密封類 去實現
// 但這裡沒有 這裡是先在這邊定義一個統一的介面
// 然後在各個UI的檔案中 透過實現這個介面
// 分別去定義 屬於自己位置的字串
// 然後在 InventoryNavHost 中去使用那些 字串
// 同時 除了位址 有些畫面還會額外定義其它自己才有的資訊
