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
*   **🧳 行李跟踪（新增）**: 支持行李标签号查询、实时状态跟踪、航班关联查询、行李位置更新和状态推送。
*   **☁️ 多设备同步（新增）**: 云端数据同步、跨设备行程共享、家庭账户管理、离线缓存与冲突解决。

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

### 行李跟踪模块（新增）

*   **数据模型**: `BaggageItem`、`BaggageTrackingResponse`、`BaggageType`、`BaggageStatus`（路径：`app/src/main/java/com/flightinfo/app/data/model/BaggageTracking.kt`）。
*   **存储与迁移**: 新增 `baggage_items` 表（Room），数据库版本升级至 `8`，迁移 `MIGRATION_7_8`（路径：`data/database/FlightInfoDatabase.kt`）。
*   **DAO/仓库**: `BaggageDao`、`BaggageRepository`（路径：`data/dao/BaggageDao.kt`、`data/repository/BaggageRepository.kt`）。
*   **状态与界面**: `BaggageViewModel` + `BaggageFragment`，提供行李列表、状态跟踪、位置更新、扫码查询等功能。
*   **依赖注入**: 在 `DatabaseModule` 中添加 `BaggageDao` 提供者，支持 Hilt 依赖注入。

### 多设备同步模块（新增）

*   **数据模型**: `SyncRecord`、`FamilyAccount`、`FamilyMember`、`SharedItinerary`、`DeviceInfo`（路径：`app/src/main/java/com/flightinfo/app/data/model/CloudSync.kt`）。
*   **存储与迁移**: 新增 `sync_records`、`family_accounts`、`family_members`、`shared_itineraries` 表，数据库版本升级至 `11`，迁移 `MIGRATION_8_9`、`MIGRATION_9_10`、`MIGRATION_10_11`。
*   **云端API**: `CloudSyncApi` 提供用户认证、设备管理、数据同步、家庭账户管理、行程共享等接口。
*   **核心服务**: `CloudSyncService` 处理周期性同步、上传下载、冲突解决；`CrossDeviceSharingManager` 管理跨设备共享；`OfflineCacheManager` 处理离线缓存。
*   **状态与界面**: `SyncSettingsViewModel` + `SyncSettingsFragment`，提供同步设置、设备管理、家庭账户管理等功能。
*   **依赖注入**: `CloudSyncModule` 提供云端同步相关的依赖注入配置。

## 使用说明：行程管理

*   **入口**: 导航图已注册 `itineraryFragment`，可从现有页面通过 `findNavController().navigate(R.id.itineraryFragment)` 进入。
*   **首次体验**: 若无数据，应用会自动生成示例行程（含 1 段航班与 2 个待办）。
*   **日历同步**: 点击“同步到日历”，首次将请求 `READ_CALENDAR`/`WRITE_CALENDAR` 权限；成功后会创建：
    - 一个主行程事件（覆盖整个行程时段）
    - 每个航段一个事件（起飞/到达时段、航司/航站楼/登机口等备注）
    - 每个待办一个提醒事件（默认 0 分钟前提醒，可在系统日历中调整）
*   **值机/登机牌**: 点击"去值机""查看登机牌"将通过外部浏览器打开链接（依据行程中 `checkInUrl`/`boardingPassUrl`）。

## 使用说明：行李跟踪

*   **入口**: 导航图已注册 `baggageFragment`，可从现有页面通过 `findNavController().navigate(R.id.baggageFragment)` 进入。
*   **行李查询**: 支持通过行李标签号手动输入或扫码查询行李状态。
*   **状态跟踪**: 实时显示行李状态（已托运、已装载、运输中、已卸载、传送带上、已送达等）。
*   **航班关联**: 可按航班号查询该航班的所有行李信息。
*   **位置更新**: 支持手动更新行李位置和状态信息。
*   **推送通知**: 行李状态变更时发送通知提醒。

## 使用说明：多设备同步

*   **入口**: 导航图需注册 `syncSettingsFragment`，可从设置页面进入同步管理。
*   **账户管理**: 支持用户注册登录、设备注册、多设备管理。
*   **自动同步**: 可设置自动同步间隔（15-120分钟），支持WiFi优先同步策略。
*   **数据类型**: 可选择同步的数据类型（行程、行李、追踪航班、收藏航班等）。
*   **家庭账户**: 支持创建家庭账户、邀请成员加入、管理成员权限。
*   **行程共享**: 支持将行程分享给家庭成员或指定用户，设置查看/编辑权限和过期时间。
*   **离线缓存**: 数据自动缓存到本地，网络恢复后自动同步。
*   **冲突解决**: 自动检测和解决数据冲突，支持保留本地/远程/合并策略。

## 开发者提示：行程与预订联动

* 在用户完成航班预订后，可生成 `Itinerary` 并保存至仓库，默认附加"出发前往机场""在线值机"等待办。
* 如需根据航司/IATA 规则构造更可靠的值机/登机牌链接，可在 `DeepLinkHelper` 统一封装。
* 后续可在首页或个人中心添加"行程管理"入口按钮，以提升发现性。

## 开发者提示：行李跟踪集成

* 在用户办理值机手续后，可自动创建 `BaggageItem` 记录并关联到对应航班。
* 支持与航空公司行李跟踪系统对接，实现实时状态同步。
* 扫码功能可集成 `zxing` 或 `ML Kit` 等二维码扫描库。
* 状态推送可利用 `WorkManager` 实现后台轮询或与推送服务集成。

## 开发者提示：多设备同步集成

* 云端同步需要配置后端服务器，实现 `CloudSyncApi` 定义的接口。
* 建议使用 JWT 令牌进行用户认证，并实现令牌自动刷新机制。
* 数据冲突解决策略可根据业务需求调整，支持用户手动选择解决方案。
* 离线缓存使用 GZIP 压缩，可显著减少存储空间占用。
* 家庭账户功能支持成员权限管理，可扩展更细粒度的权限控制。
* 分享链接可集成深链接（Deep Link）功能，支持通过链接直接访问共享内容。

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
