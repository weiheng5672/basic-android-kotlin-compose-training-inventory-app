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

import kotlinx.coroutines.flow.Flow

class OfflineItemsRepository(private val itemDao: ItemDao) : ItemsRepository {
    override fun getAllItemsStream(): Flow<List<Item>> = itemDao.getAllItems()

    override fun getItemStream(id: Int): Flow<Item?> = itemDao.getItem(id)

    override suspend fun insertItem(item: Item) = itemDao.insert(item)

    override suspend fun deleteItem(item: Item) = itemDao.delete(item)

    override suspend fun updateItem(item: Item) = itemDao.update(item)
}
// ItemsRepository 是介面
// 而這個類 OfflineItemsRepository 是實現那介面的 類
// 也就是說 其實 OfflineItemsRepository 才是那個手下
// ItemsRepository 相當於 工作項目
// OfflineItemsRepository 才是真正執行工作項目的人
// 他的主建構式 有ItemDao 類型的變數
// 使用到 OfflineItemsRepository 的地方 將會在 AppContainer
// 那裡會傳入一個 InventoryDatabase 資料庫的實例
// 去使用 ItemDao的函數