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

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Database access object to access the Inventory database
 */
// 第一站 在Item.kt
// 這裡是 第二站
// DAO 是所謂的 資料存取物件
// 但很顯然 他是個介面 完全就不是 所謂的 具體的物件
@Dao
interface ItemDao {

    @Query("SELECT * from items ORDER BY name ASC")
    fun getAllItems(): Flow<List<Item>>

    @Query("SELECT * from items WHERE id = :id")
    fun getItem(id: Int): Flow<Item>

    // Specify the conflict strategy as IGNORE, when the user tries to add an
    // existing Item into the database Room ignores the conflict.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Item)

    @Update
    suspend fun update(item: Item)

    @Delete
    suspend fun delete(item: Item)
}

// Kotlin函數 與 ROOM庫內封裝的功能
// 透過 註解 聯繫起來
// 這個程式本身只要定義需要做什麼
// 具體怎麼去做 外包給ROOM庫 去實現
// 註解 就好像是 外包的記號
// 指明了 這一部分的功能會由其他庫去實現

// 本程式定義了 5個功能
// 1.取得所有資料
// 2.給定id 取得對應的資料
// 3.插入資料
// 4.更新資料
// 5.刪除資料