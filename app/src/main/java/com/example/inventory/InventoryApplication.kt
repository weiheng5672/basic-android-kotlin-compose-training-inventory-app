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

import android.app.Application
import com.example.inventory.data.AppContainer
import com.example.inventory.data.AppDataContainer

class InventoryApplication : Application() {

    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}

// 火星專案說過
// 所謂的容器 基於一些原因 要放在 Application 類 的裡面
// container 是 指向容器的變數
// lateinit 表示 他在宣告的時候 還沒有被初始化
// 當需要容器的時候
// 實現 AppContainer 的 AppDataContainer
// 才會被初始化 指派給 container

// 看到這邊 還記得容器 裝了什麼?
// 容器相當於部門 它裡面裝的就是員工 也就是 Repository
// 那個員工 並不自己完成所有工作 而是外包給 ROOM
// 去實現存放資料的具體細節
