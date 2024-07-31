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
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Database class with a singleton Instance object.
 */
//這裡是第三站
//第一站 定義了 要儲存的東西
//第二站 定義了 存取他的方式
//這裡 定義了 他要存在哪裡
//當然 存在Database

@Database(entities = [Item::class], version = 1, exportSchema = false)
abstract class InventoryDatabase : RoomDatabase() {

    abstract fun itemDao(): ItemDao

    companion object {
        @Volatile
        private var Instance: InventoryDatabase? = null

        fun getDatabase(context: Context): InventoryDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, InventoryDatabase::class.java, "item_database")
                    /**
                     * Setting this option in your app's database builder means that Room
                     * permanently deletes all data from the tables in your database when it
                     * attempts to perform a migration with no defined migration path.
                     */
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}

// 定義了一個抽象的類 InventoryDatabase 繼承自 RoomDatabase
// 註解 @Database 用來標註 InventoryDatabase 他就是我們要儲存資料的地方
// entities = [Item::class] 代表 要存的每一筆資料是 Item

// 那麼他到底是在哪裡呢? 我們手機內存的哪裡呢?
// 我們不需要知道 具體的工作 我們外包給了 ROOM庫去處理
// 對於 這個專案來說 資料就儲存在這裡
// 要使用資料就要來這邊存取
// 但真正意義上放資料的位置 這邊不知道 也不需要知道
// 這就是為何這是個抽象的類

// 他還有個抽象的函數 代表存取資料的功能
// 就是在 ItemDao中定義的 5個功能



