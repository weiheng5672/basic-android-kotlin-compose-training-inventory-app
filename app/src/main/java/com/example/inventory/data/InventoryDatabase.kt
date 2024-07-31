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

    //companion object 在 Kotlin 中是一種靜態的類成員，用於實現類的功能
    //在這裡，它用來提供 InventoryDatabase 類的單例實例
    companion object {

        //Instance變數 指向 InventoryDatabase 的單例實例，初始為 null
        //@Volatile 註解確保了 Instance 變數在多執行緒環境下的可見性
        //所有執行緒看到的 Instance 變數都是最新的。
        @Volatile
        private var Instance: InventoryDatabase? = null

        // getDatabase 方法用於獲取資料庫實例 
        // Context 是 Android 應用程序中一個核心概念，它提供了對應用程序和系統資源的訪問
        fun getDatabase(context: Context): InventoryDatabase {
            
            // 如果 Instance 已經初始化，則返回該實例
            return Instance ?: synchronized(this) {

                // 如果 Instance 為 null
                // 使用 Room.databaseBuilder 創建新的資料庫實例
                Room.databaseBuilder(context, InventoryDatabase::class.java, "item_database")
                    .fallbackToDestructiveMigration()  // 當資料庫遷移路徑未定義時會刪除所有數據並重新建立資料庫
                    .build() 
                    .also { Instance = it } // 確保在創建新實例後將其賦值給 Instance，以便未來的請求可以重複使用這個實例
            }
        }
    }
    //這樣的設計確保了 InventoryDatabase 類的實例在應用中是唯一的（單例模式）
    //避免了多次創建資料庫實例，從而提高了效能和資源使用效率

    
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



