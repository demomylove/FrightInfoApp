# FlightInfoApp 项目文档

## 项目概述

FlightInfoApp 是一个基于 Android MVVM 架构的实时航班信息查询应用程序。该应用为用户提供准确、及时的航班状态、时刻表和详细航班信息，旨在优化用户的旅行规划和机场导航体验。

### 项目背景与价值

现代旅行中，乘客经常面临信息获取困难、界面体验差、离线能力不足等挑战。FlightInfoApp 通过提供一个现代化、响应式的移动应用解决方案来解决这些痛点。

## 核心功能特性

*   **👤 用户个人中心**: 统一管理个人信息、偏好设置、收藏的航班和历史记录。
*   **🌳 碳足迹计算**: 估算飞行碳排放量，并提供环保评级，引导绿色出行。
*   **🔍 航班搜索与跟踪**: 支持按航班号快速搜索，并提供实时状态更新（准点、延误、取消等）。
*   **📊 详细航班信息**: 展示航班号、航司、时间、机场、登机口、航站楼及机型等全面信息。
*   **✈️ 机场查询**: 支持搜索全球主要机场信息，并可直接用于航班搜索。
*   **💰 价格跟踪**: 监控航班价格变化，查看历史趋势，并支持价格变动提醒。
*   **🗺️ 航线网络图**: 在地图上直观展示全球机场分布与航线连接。
*   **💡 直观用户界面**: 采用 Material Design 3 设计，通过色彩编码快速识别航班状态。
*   **🔄 离线支持**: 在网络不佳时可访问基础航班数据。
*   **🌍 多语言支持**: 支持中英文界面，并可根据系统语言自动适配。
*   **🧳 行程管理（新增）**: 支持行程单聚合（多航段）、待办与时间线视图、日历同步、一键跳转值机/登机牌。

## 技术架构概览

FlightInfoApp 采用现代化的 **MVVM (Model-View-ViewModel)** 架构模式，确保了代码的可维护性、可测试性和层次分离。

*   **开发语言**: Kotlin
*   **核心技术**: Android Jetpack (Lifecycle, ViewModel, Navigation), Coroutines, Hilt, Retrofit, Room.
*   **界面框架**: Material Design Components, ViewBinding.

### 行程管理模块（新增）

*   **数据模型**: `Itinerary`、`ItinerarySegment`、`TripTask`（路径：`app/src/main/java/com/flightinfo/app/data/model/Itinerary.kt`）。
*   **存储与迁移**: 新增 `itineraries` 表（Room），数据库版本升级至 `7`，迁移 `MIGRATION_6_7`（路径：`data/database/FlightInfoDatabase.kt`）。
*   **DAO/仓库**: `ItineraryDao`、`ItineraryRepository`（路径：`data/dao/ItineraryDao.kt`、`data/repository/ItineraryRepository.kt`）。
*   **状态与界面**: `ItineraryViewModel` + `ItineraryFragment`，提供时间线、值机/登机牌深链按钮、日历同步按钮。
*   **系统集成**: `CalendarSyncManager` 写入系统日历，`DeepLinkHelper` 打开值机/登机牌链接。

## 使用说明：行程管理

*   **入口**: 导航图已注册 `itineraryFragment`，可从现有页面通过 `findNavController().navigate(R.id.itineraryFragment)` 进入。
*   **首次体验**: 若无数据，应用会自动生成示例行程（含 1 段航班与 2 个待办）。
*   **日历同步**: 点击“同步到日历”，首次将请求 `READ_CALENDAR`/`WRITE_CALENDAR` 权限；成功后会创建：
    - 一个主行程事件（覆盖整个行程时段）
    - 每个航段一个事件（起飞/到达时段、航司/航站楼/登机口等备注）
    - 每个待办一个提醒事件（默认 0 分钟前提醒，可在系统日历中调整）
*   **值机/登机牌**: 点击“去值机”“查看登机牌”将通过外部浏览器打开链接（依据行程中 `checkInUrl`/`boardingPassUrl`）。

## 开发者提示：行程与预订联动

* 在用户完成航班预订后，可生成 `Itinerary` 并保存至仓库，默认附加“出发前往机场”“在线值机”等待办。
* 如需根据航司/IATA 规则构造更可靠的值机/登机牌链接，可在 `DeepLinkHelper` 统一封装。
* 后续可在首页或个人中心添加“行程管理”入口按钮，以提升发现性。

## 使用场景

*   **商务旅客**: 快速查询航班状态，合理安排行程，应对延误。
*   **机场工作人员**: 查询航班详细信息，为乘客提供准确服务。
*   **航空爱好者**: 跟踪感兴趣的航班，了解航空业信息。

## 未来发展规划

*   **航班预订集成**：与航空公司预订系统对接。
*   **性能优化**：持续提升应用启动和响应速度。
*   **无障碍功能**：提升应用可访问性，服务更广泛的用户群体。
*   **平台扩展**：探索 iOS、Web 等多平台版本。

## 总结

FlightInfoApp 是一个功能完整、架构清晰的现代化 Android 应用程序。它不仅解决了用户在航班信息查询方面的实际需求，也展现了 Android 开发的最佳实践，为未来的功能扩展奠定了坚实的基础。

## 贡献指南

我们欢迎各种形式的贡献。如果您有兴趣，请查阅项目的 Issues 或提交 Pull Request。

## 许可证

本项目采用 **MIT 许可证**。
