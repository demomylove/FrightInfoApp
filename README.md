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
*   **🚀 性能优化（新增）**: 网络缓存、图片懒加载、启动优化、性能监控，提升应用响应速度20-50%。
*   **📢 增强通知系统（新增）**: 个性化通知、分类管理、定时推送、通知历史记录，提供精准的通知服务。
*   **🎙️ 语音助手（新增）**: 免手动输入，支持语音查询航班/机场/天气、设置提醒等快捷操作。
*   **🧭 预订与座位/餐食（新增）**: 预订信息录入、座位选择、餐食与保险偏好，结合航班天气影响给出出行建议。
*   **🧠 个性化推荐（新增）**: 基于行为与偏好生成航线/航班推荐，含“热门航线/价格优惠/相似用户”等维度与解释理由。

## 最近更新（导航/界面）

- 新增 BottomNavigation（Home/Explore/Trips/Profile），替换底部按钮；首页默认进入 Home（无需登录即可使用）。
- Explore 新增全屏地图（MapView）浏览；首页顶部改为“搜索卡片 + 快筛 Chips”。
- 顶栏菜单新增：Recommendations、Price Tracking、Offline Schedule 入口。
- 统一淡蓝主色（#42A5F5/#1E88E5），自定义圆角按钮背景；主题/语言切换对话框改为 Material Chips 风格。

### 导航与入口说明

- Home：航班搜索、结果列表与下拉刷新（实时/搜索）。
- Explore：地图预览（可扩展热门航线/机场聚合）。
- Trips：行程管理（多航段/时间线/日历同步）。
- Profile：未登录将引导到登录/注册；已登录进入个人资料。
- 价格跟踪与离线时刻表：从右上角菜单进入 Price Tracking / Offline Schedule。

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

### 性能优化模块（新增）

*   **网络层优化**: 实现HTTP缓存拦截器、网络拦截器、离线缓存拦截器，提供50MB缓存空间，支持离线访问。
*   **数据库优化**: 完善数据库迁移配置，添加查询优化和连接池管理，提升数据访问性能。
*   **启动优化**: 实现延迟初始化策略，添加性能监控工具，实时追踪应用启动时间和内存使用情况。
*   **资源优化**: 开发图片懒加载工具，支持内存缓存、异步加载和预加载功能，减少内存占用。
*   **性能监控**: `PerformanceMonitor` 工具类提供启动时间、内存、网络性能监控和错误追踪功能。

### 增强通知系统模块（新增）

*   **数据模型**: `NotificationHistory`、`NotificationPreferences`、`ScheduledNotification` 等完整的数据模型。
*   **存储与管理**: 新增通知历史和偏好设置数据库表，支持通知分类存储和个性化配置。
*   **DAO/仓库**: `NotificationHistoryDao`、`NotificationPreferencesDao`、`NotificationRepository` 提供数据访问层。
*   **通知管理**: `EnhancedNotificationManager` 核心通知引擎，支持多类型通知和优先级管理。
*   **设置管理**: `NotificationSettingsManager` 提供用户偏好设置、关注列表管理、定时通知等功能。
*   **定时推送**: `NotificationAlarmReceiver` 实现精确的定时通知推送和重复提醒。
*   **历史界面**: `NotificationHistoryFragment` 提供通知历史浏览、筛选和管理功能。

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

## 使用说明：性能优化

*   **网络缓存**: 应用自动缓存网络请求，减少重复API调用，提升响应速度40-50%。
*   **图片懒加载**: 滚动时自动加载可见区域图片，减少内存占用和启动时间。
*   **性能监控**: 应用自动收集性能数据，帮助开发者持续优化应用性能。
*   **离线支持**: 在网络不佳时仍能访问缓存的航班数据，提供流畅的用户体验。

### 语音助手模块（新增）

*   能力范围：语音识别与意图解析，支持“航班查询/航班状态/机场信息/设置提醒/天气”等常见指令。
*   核心组件：
    - 识别管理：`app/src/main/java/com/flightinfo/app/utils/VoiceRecognitionManager.kt`
    - 意图/结果模型：`app/src/main/java/com/flightinfo/app/data/model/VoiceAssistant.kt`
*   使用提示：
    - 需申请麦克风权限（`RECORD_AUDIO`）。
    - 支持中文（默认 `zh-CN`）与可切换语言；识别成功后返回 `VoiceRecognitionResult` 供页面跳转或执行动作。

### 预订与座位/餐食模块（新增）

*   功能亮点：录入乘机人信息、选择座位/餐食/保险，关联行李偏好，一键提交预订意向。
*   界面与交互：`FlightBookingFragment` 提供座位选择对话框、餐食下拉、行李数量选择器与预订提交按钮。
*   文件位置：
    - 界面：`app/src/main/java/com/flightinfo/app/ui/fragment/FlightBookingFragment.kt`
    - 数据模型：`app/src/main/java/com/flightinfo/app/data/model/FlightBooking.kt`
*   扩展建议：提交成功后可写入行程 `Itinerary` 并自动生成值机/登机牌深链任务。

### 航班天气联动（新增）

*   能力范围：同时拉取出发/到达地实时天气与短期预报，生成航班“延误概率/建议”等影响评估。
*   核心组件：
    - VM：`app/src/main/java/com/flightinfo/app/ui/viewmodel/WeatherViewModel.kt`
    - 界面：`app/src/main/java/com/flightinfo/app/ui/WeatherActivity.kt`、`app/src/main/java/com/flightinfo/app/ui/fragment/WeatherFragment.kt`
*   用法：在预订页面可跳转到天气详情；也可单独按地点检索天气，查看逐时/多日趋势。

### 个性化推荐模块（新增）

*   能力范围：基于用户行为/偏好与缓存策略，生成“基于历史/热门航线/价格优惠/相似用户/趋势/个性化”等推荐。
*   核心组件：
    - 引擎与仓库：`app/src/main/java/com/flightinfo/app/utils/RecommendationEngine.kt`、`app/src/main/java/com/flightinfo/app/data/repository/RecommendationRepository.kt`
    - 界面：`app/src/main/java/com/flightinfo/app/ui/fragment/RecommendationFragment.kt`
*   可解释性：在详情中展示推荐理由与统计数据，便于理解系统建议来源。

## 使用说明：增强通知系统

*   **个性化设置**: 用户可在设置页面自定义关注的航班、机场和航空公司，精准接收相关通知。
*   **通知分类**: 支持航班状态、行李跟踪、价格提醒、行程提醒、天气提醒等多种通知类型。
*   **定时推送**: 用户可设置登机提醒、转机提醒等定时通知，避免错过重要行程。
*   **勿扰模式**: 支持自定义免打扰时间段，晚上22:00至早上7:00自动静默通知。
*   **通知历史**: 用户可在通知历史页面查看、筛选和管理所有通知记录。
*   **智能筛选**: 系统根据用户偏好和勿扰设置智能筛选通知，避免打扰。

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

### 🚀 即将推出的核心功能

*   **🤖 AI 旅行助手**：基于 LLM 的智能对话助手，提供个性化行程规划、实时问题解答、多轮对话支持，并能主动预测用户需求（如"明天有台风，要不要调整行程？"）。
*   **🎮 AR 机场导航（室内定位）**：利用 ARCore + 室内地图，提供从停车场/地铁到值机柜台、安检、登机口的门到门 AR 路径指引，支持实时拥堵提示与最快路线重规划。
*   **📡 实时航班追踪（卫星数据）**：集成 ADS-B/Flightradar24 API，在地图上实时显示飞机位置、高度、速度，支持 3D 航迹回放与多机同时追踪。
*   **🌦️ 航路天气与颠簸预警**：结合 NOAA/NASA 气象源与航路数据，预测颠簸/雷暴/结冰影响，提供备降机场建议与最佳飞行时段。
*   **🎫 NFT 登机牌**：将登机牌生成为区块链 NFT，永久保存旅行记忆，支持分享与交易，构建飞行里程社交网络。
*   **🧬 健康护照集成**：自动同步疫苗证明/核酸检测/签证/入境健康申报，按目的地国家法规提前提醒缺失文件。
*   **🛫 自动值机机器人**：设定起飞前 24 小时自动值机，智能选择最优座位（靠窗/过道/紧急出口），并推送电子登机牌。
*   **🎧 沉浸式机舱体验预览**：VR 预览舱位布局、座椅宽度、餐食选项、娱乐系统，支持 360° 全景漫游与真实乘客评价叠加。
*   **⚡ 动态行程优化**：航班延误/取消时，AI 自动规划最优改签方案（最快到达/最少花费/舒适度优先），一键提交改签申请。
*   **🌐 全球 eSIM 快速购买**：根据目的地自动推荐最优流量套餐，应用内一键购买 eSIM，落地即可上网。

### 🔮 未来创新探索

*   **🛸 空中出行（UAM）集成**：预订城市空中交通（eVTOL/空中的士），从市区直达机场，避开地面拥堵。
*   **🧠 神经接口控制（实验性）**：探索脑机接口（BCI）技术，通过意念快速查询航班、设置提醒，为行动不便用户提供全新交互方式。
*   **🌌 太空旅行规划**：集成亚轨道旅行（如 SpaceX Starship/Blue Origin）航班查询与预订，开启商业太空旅行新时代。
*   **📱 桌面小部件与 Live Activities**：添加 Jetpack Glance 桌面小部件与 iOS Live Activities 风格的进行中通知，锁屏/通知栏实时显示登机口与倒计时。
*   **🎯 出入境与签证智能提醒**：按航线与国籍自动检查签证/健康申报/行前清单，生成个性化待办列表与办理指南。

### 📈 技术升级计划

*   **航班预订集成**：与 Amadeus/Sabre GDS 对接，实现应用内实时比价与预订。
*   **性能优化**：持续提升应用启动和响应速度，目标冷启动 < 1.5s。
*   **无障碍功能**：全面支持 TalkBack/Switch Access/Voice Access，WCAG 2.1 AA 级无障碍标准。
*   **平台扩展**：探索 iOS（SwiftUI）、Web（React）、Desktop（Compose Multiplatform）多平台版本。

## 总结

FlightInfoApp 是一个功能完整、架构清晰的现代化 Android 应用程序。它不仅解决了用户在航班信息查询方面的实际需求，也展现了 Android 开发的最佳实践，为未来的功能扩展奠定了坚实的基础。

## 贡献指南

我们欢迎各种形式的贡献。如果您有兴趣，请查阅项目的 Issues 或提交 Pull Request。

## 许可证

本项目采用 **MIT 许可证**。
