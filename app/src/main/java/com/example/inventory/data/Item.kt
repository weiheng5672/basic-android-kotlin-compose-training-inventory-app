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

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity data class represents a single row in the database.
 */
@Entity(tableName = "items")
data class Item(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val price: Double,
    val quantity: Int
)

// 這個檔案 是深入認識這個專案的第一站
// 這個專案是在介紹 使用 ROOM庫 去儲存資料
// 使得 APP關閉後 資料能夠被保存
// 這就是 所謂的 資料持續性
// 既然要儲存資料
// 就要先定義 要儲存的東西
// 我要儲存一張表 items
// 這個表格的欄位有 id name price quantity

// 使用 類 代表 表格
// 類的每個屬性 代表 表格的每一欄
// 類的實例 代表 每一筆資料

// 使用 註解  @Entity 及 @PrimaryKey
// 指明 我定義的類 和 ROOM庫 的 關係

// @Entity 用來註解 data class Item
// 代表 這個類 就是 ROOM庫 的表格

// @PrimaryKey 用來註解 val id
// 代表 ROOM會認定 他是這個表格的 主鍵
