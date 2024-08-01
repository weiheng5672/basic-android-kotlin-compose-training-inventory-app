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

package com.example.inventory.data

import android.content.Context

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val itemsRepository: ItemsRepository
}

/**
 * [AppContainer] implementation that provides instance of [OfflineItemsRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [ItemsRepository]
     */
    override val itemsRepository: ItemsRepository by lazy {
        OfflineItemsRepository(InventoryDatabase.getDatabase(context).itemDao())
    }
}
// 所謂的容器 在這裡就是個介面
// 他有一個屬性 類型是 ItemsRepository
// 用我們剛剛的比方 這相當於一個部門
// 而且是一個部門的職能
// 表示 AppContainer這個部門 要去做 ItemsRepository 這件事

// AppDataContainer 就相當於 實現了那個職能的 部門
// 他的主建構式中的 Context 是和系統有關的東西 先不用管
// 有個 ItemsRepository 類型的屬性就代表 有個員工負責做那件事
// 而這個員工就是 OfflineItemsRepository
// lazy表示 他沒工作就沒薪水拿
// 他會利用 InventoryDatabase.getDatabase(context).itemDao()
// 去實現他的工作

