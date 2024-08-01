Inventory app
==================================

Solution code for Android Basics with Compose.

Introduction
------------

This app is an Inventory tracking app. Demos how to add, update, sell, and delete items from the local database.
This app demonstrated the use of Android Jetpack component [Room](https://developer.android.com/training/data-storage/room) database.
The app also leverages [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel),
[Flow](https://developer.android.com/kotlin/flow),
and [Navigation](https://developer.android.com/topic/libraries/architecture/navigation/).

Pre-requisites
--------------

You need to know:
- How to create and use composables.
- How to navigate between composables, and pass data between them.
- How to use architecture components including ViewModel, Flow, StateFlow and StateUi.
- How to use coroutines for long-running tasks.
- SQLite database and the SQLite query language


Getting Started
---------------

1. Download and run the app.


程式碼觀看順序
============
1. Item
2. ItemDao
3. InventoryDatabase

4. ItemsRepository
5. OfflineItemsRepository

6. AppContainer
7. InventoryApplication

8. InventoryApp
9. InventoryNavGraph
10. NavigationDestination

11. HomeScreen、HomeViewModel
12. ItemEntryScreen、ItemEntryViewModel
13. ItemDetailsScreen、ItemDetailsViewModel
14. ItemEditScreen、ItemEditViewModel
15. AppViewModelProvider