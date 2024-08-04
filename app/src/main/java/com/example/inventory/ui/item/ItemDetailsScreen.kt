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

package com.example.inventory.ui.item

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventory.InventoryTopAppBar
import com.example.inventory.R
import com.example.inventory.data.Item
import com.example.inventory.ui.AppViewModelProvider
import com.example.inventory.ui.navigation.NavigationDestination
import com.example.inventory.ui.theme.InventoryTheme
import kotlinx.coroutines.launch

object ItemDetailsDestination : NavigationDestination {
    override val route = "item_details"
    override val titleRes = R.string.item_detail_title
    const val itemIdArg = "itemId"
    val routeWithArgs = "$route/{$itemIdArg}"
}

// 這個畫面比較複雜
// 因為 HomeScreen 可以有很多個 Item
// 數量隨使用者的新增而增加，也隨使用者的刪除而減少
// 每個 Item 的詳細畫面都是獨立的
// 點擊不同的 Item 顯示的畫面內容會根據所選項目而不同
// 儘管如此，ItemDetailsScreen 本身只有一個實例

// 比方說 當我在 HomeScreen 點擊 某個 Item
// 會觸發一個 lambda 他是一個導覽
// 對應的 路由會是 "${ItemDetailsDestination.route}/${it}"
// ${ItemDetailsDestination.route} 是靜態部分
// 也就是 item_details
// 後半段是 動態參數部分 會是由 那個被點的 Item 提供的 自己的ID
// 比方說 123
// 也就是說 完整路由就會是 "item_details/123"
// 再經由 導覽系統去解析
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemDetailsScreen(
    // 導覽到編輯頁面
    navigateToEditItem: (Int) -> Unit,
    // 回到前一頁
    navigateBack: () -> Unit,
    // 外觀
    modifier: Modifier = Modifier,
    // 這個 UI 對應的 狀態
    viewModel: ItemDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    // 從 ViewModel 取得 Flow 轉換成 State
    val uiState by viewModel.uiState.collectAsState()

    // 由於要從資料庫 取得東西 需要 協程
    // 所以需要 一個執行協程的區域
    val coroutineScope = rememberCoroutineScope()

    // 畫面架構
    Scaffold(
        // 標題 以及 回到上一頁的按鈕
        topBar = {
            InventoryTopAppBar(
                title = stringResource(ItemDetailsDestination.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        },

        // 浮動按鈕 點擊後 進入 編輯頁面
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navigateToEditItem(uiState.itemDetails.id) },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .padding(
                        end = WindowInsets.safeDrawing.asPaddingValues()
                            .calculateEndPadding(LocalLayoutDirection.current)
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(R.string.edit_item_title),
                )
            }
        },

        // 外觀
        modifier = modifier,
    ) { innerPadding ->
        // 這個 Composable 是 詳細頁面的核心 Composable的定義在下方
        // 這裡使用 Composable 傳入實參
        ItemDetailsBody(

            // 這就是 要顯示的東西
            itemDetailsUiState = uiState,

            // 這個是 按下後 減1 的動作
            onSellItem = { viewModel.reduceQuantityByOne() },

            // 刪除 並回到上一頁
            onDelete = {
                // Note: If the user rotates the screen very fast, the operation may get cancelled
                // and the item may not be deleted from the Database. This is because when config
                // change occurs, the Activity will be recreated and the rememberCoroutineScope will
                // be cancelled - since the scope is bound to composition.
                coroutineScope.launch {
                    // 調用 viewModel 中的 刪除方法
                    viewModel.deleteItem()
                    navigateBack()
                }
            },

            // 外觀
            modifier = Modifier
                .padding(
                    start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                    top = innerPadding.calculateTopPadding(),
                    end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                )
                .verticalScroll(rememberScrollState())
        )
    }
}

@Composable
private fun ItemDetailsBody(
    // 要顯示的東西
    itemDetailsUiState: ItemDetailsUiState,
    // 按下後 減1 的動作
    onSellItem: () -> Unit,
    // 刪除 並回到上一頁
    onDelete: () -> Unit,
    // 外觀
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
    ) {

        // 管理刪除確認對話框的顯示
        // rememberSaveable 用於 在畫面重組時 保留狀態
        // mutableStateOf(false) 創建了一個可變的狀態對象
        // 該對象能夠在狀態改變時通知 Compose框架 更新界面
        // 這意味著 當 deleteConfirmationRequired 的值改變時
        // 畫面 會根據 新的狀態 重組
        // by 的用途在於 即使 false 這個布林值 被封裝在一堆東西的裡面
        // 程式碼在其他地方使用到 該變數的時候
        // 依然像使用一個普通的 類型為 布林值的變數
        var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }

        // 要顯示的東西
        ItemDetails(
            item = itemDetailsUiState.itemDetails.toItem(), modifier = Modifier.fillMaxWidth()
        )

        // 按下後 減1 的動作
        Button(
            onClick = onSellItem,
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.small,
            enabled = !itemDetailsUiState.outOfStock
        ) {
            Text(stringResource(R.string.sell))
        }

        // 刪除 並回到上一頁
        OutlinedButton(
            // 點擊這個按鈕 不會馬上刪除
            // 會執行下面的動作 彈出確認視窗
            onClick = { deleteConfirmationRequired = true },
            shape = MaterialTheme.shapes.small,

            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.delete))
        }

        // 如果該值為真 彈出視窗 再確認是否刪除
        if (deleteConfirmationRequired) {

            DeleteConfirmationDialog(

                // 確認刪除
                onDeleteConfirm = {

                    // 這個值要重設回 false 視窗才會關閉
                    deleteConfirmationRequired = false

                    // 使用 ItemDetailsBody 的地方 傳入的 lambda
                    // 會調用 viewModel 中的 刪除方法
                    onDelete()
                },

                // 取消刪除
                onDeleteCancel = {

                    // 這個值要重設回 false 視窗才會關閉
                    deleteConfirmationRequired = false

                    // 之後什麼都不做
                },

                // 外觀
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))

            )
        }

    }
}


@Composable
fun ItemDetails(
    item: Item, modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier, colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_medium)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
        ) {
            ItemDetailsRow(
                labelResID = R.string.item,
                itemDetail = item.name,
                modifier = Modifier.padding(
                    horizontal = dimensionResource(
                        id = R.dimen
                            .padding_medium
                    )
                )
            )
            ItemDetailsRow(
                labelResID = R.string.quantity_in_stock,
                itemDetail = item.quantity.toString(),
                modifier = Modifier.padding(
                    horizontal = dimensionResource(
                        id = R.dimen
                            .padding_medium
                    )
                )
            )
            ItemDetailsRow(
                labelResID = R.string.price,
                itemDetail = item.formatedPrice(),
                modifier = Modifier.padding(
                    horizontal = dimensionResource(
                        id = R.dimen
                            .padding_medium
                    )
                )
            )
        }

    }
}

@Composable
private fun ItemDetailsRow(
    @StringRes labelResID: Int, itemDetail: String, modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Text(text = stringResource(labelResID))
        Spacer(modifier = Modifier.weight(1f))
        Text(text = itemDetail, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun DeleteConfirmationDialog(
    // 確認刪除
    onDeleteConfirm: () -> Unit,
    // 不要刪除
    onDeleteCancel: () -> Unit,
    // 外觀
    modifier: Modifier = Modifier

) {

    AlertDialog(

        // 當用戶嘗試關閉對話框（例如按下外部區域或按下返回鍵）時觸發的回調
        // 你可以在這裡定義對話框關閉時的行為
        // 這裡不執行任何操作，對話框不能被關閉
        onDismissRequest = { /* Do nothing */ },

        // 標題
        title = { Text(stringResource(R.string.attention)) },

        // 內文
        text = { Text(stringResource(R.string.delete_question)) },

        // 外觀
        modifier = modifier,

        // 不要刪除
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text(text = stringResource(R.string.no))
            }
        },

        // 確認刪除
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text(text = stringResource(R.string.yes))
            }
        }

    )

}

@Preview(showBackground = true)
@Composable
fun ItemDetailsScreenPreview() {
    InventoryTheme {
        ItemDetailsBody(ItemDetailsUiState(
            outOfStock = true, itemDetails = ItemDetails(1, "Pen", "$100", "10")
        ), onSellItem = {}, onDelete = {})
    }
}
