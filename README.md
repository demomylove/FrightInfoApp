# FlightInfoApp 项目文档

## 项目概述

FlightInfoApp 是一个基于 Android MVVM 架构的实时航班信息查询应用程序。该应用为用户提供准确、及时的航班状态、时刻表和详细航班信息，帮助优化旅行规划和机场导航体验。

### 项目背景与价值

现代旅行中，乘客经常面临以下挑战：
- **信息获取困难**：难以获取实时、准确的航班状态更新
- **界面体验差**：缺乏直观、易用的航班搜索和跟踪界面
- **离线能力不足**：无法在网络不佳时访问基础航班数据

FlightInfoApp 通过提供一个现代化、响应式的移动应用解决方案来解决这些痛点。

## 核心功能特性

### 🔍 航班搜索功能
- **快速搜索**：支持通过航班号快速查找航班信息
- **智能匹配**：自动识别航空公司代码和航班号格式
- **搜索历史**：保存用户搜索记录，提升使用体验

### 📡 实时航班跟踪
- **状态更新**：提供实时航班状态监控（准点、延误、取消、登机等）
- **自动刷新**：定期自动更新航班信息
- **手动刷新**：支持下拉刷新手动获取最新数据

### 💡 直观用户界面
- **Material Design**：采用 Google Material Design 3 设计语言
- **色彩编码**：使用颜色快速识别航班状态
  - 🟢 准点：绿色
  - 🟡 延误：橙色  
  - 🔴 取消：红色
  - 🔵 登机：蓝色
  - 🟣 起飞/飞行中：紫色
- **响应式布局**：适配不同屏幕尺寸和方向

### 📊 详细航班信息
每个航班卡片显示：
- 航班号和航空公司信息
- 出发/到达时间和机场
- 当前状态（带颜色标识）
- 登机口和航站楼信息
- 机型信息

### 🔄 离线支持
- **模拟数据**：在 API 不可用时提供模拟航班数据
- **错误处理**：优雅处理网络连接问题
- **用户反馈**：清晰的加载状态和错误提示

### 🌍 多语言支持（新功能）
- **中英文切换**：支持中文和英文界面语言切换
- **动态语言切换**：应用运行时实时切换语言
- **持久化设置**：语言选择自动保存，下次启动时恢复
- **完整国际化**：所有界面文本均支持多语言显示（153个字符串资源）
- **系统适配**：自动适配系统语言设置

### ✈️ 机场查询功能（新功能）
- **全球机场**：支持搜索全球主要机场信息
- **智能搜索**：支持按机场名称、城市、IATA代码搜索
- **详细信息**：显示机场完整信息（名称、代码、位置、坐标）
- **选择使用**：查询结果可直接用于航班搜索
- **响应式界面**：优化的搜索体验和结果显示

### 💰 价格跟踪功能（新功能）
- **价格监控**：实时监控航班价格变化
- **历史数据**：查看价格历史趋势
- **提醒设置**：价格变动通知功能
- **数据可视化**：图表展示价格变化趋势

## 技术架构

### 整体架构设计

FlightInfoApp 采用 **MVVM (Model-View-ViewModel)** 架构模式，确保代码的可维护性、可测试性和关注点分离。

```
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│      View       │◄──►│    ViewModel     │◄──►│      Model      │
│   (Activity/    │    │  (Business       │    │   (Repository/  │
│   Fragment)     │    │   Logic)         │    │    API/Data)    │
└─────────────────┘    └──────────────────┘    └─────────────────┘
```

### 技术栈组件

#### 核心技术
- **开发语言**：Kotlin - 现代化、安全的 Android 开发语言
- **架构模式**：MVVM + Repository Pattern - 清晰的层次分离
- **界面框架**：Material Design Components + ViewBinding - 现代化 UI
- **网络通信**：Retrofit + OkHttp + Gson - 高效的网络请求处理
- **异步处理**：Kotlin Coroutines + Flow - 响应式编程
- **依赖注入**：Hilt (Dagger) - 自动化依赖管理
- **导航组件**：Navigation Component - 现代化应用导航
- **国际化支持**：Android Resource System + LocaleManager - 多语言切换
- **数据持久化**：SharedPreferences + Room Database - 设置和数据存储

#### 版本要求
- **Android Studio**：Hedgehog (2023.1.1) 或更高版本
- **最低 SDK**：API 21 (Android 5.0)
- **目标 SDK**：API 34 (Android 14)
- **Kotlin 版本**：1.9.0+
- **Java 版本**：8+

### 模块架构详解

#### 1. 数据层 (Data Layer)
负责数据获取、处理和存储：

- **`FlightInfo.kt`**：航班数据模型，定义航班信息的数据结构
- **`FlightApiService.kt`**：Retrofit API 接口，定义网络请求方法
- **`FlightRepository.kt`**：数据仓库，统一数据访问入口，实现数据源抽象
- **`AirportRepository.kt`**：机场数据仓库，处理机场信息查询
- **`TrackedFlightDao.kt`**：数据库访问对象，管理价格跟踪数据
- **`FlightInfoDatabase.kt`**：Room 数据库配置，提供数据持久化

#### 2. 表现层 (Presentation Layer)
负责用户界面和用户交互：

- **`MainActivity.kt`**：主界面容器，管理整体应用布局和语言切换
- **`FlightSearchViewModel.kt`**：业务逻辑处理和 UI 状态管理
- **`AirportViewModel.kt`**：机场查询业务逻辑处理
- **`FlightListFragment.kt`**：航班列表显示，处理列表相关交互
- **`AirportLookupFragment.kt`**：机场查询界面，处理机场搜索和选择
- **`PriceTrackingFragment.kt`**：价格跟踪界面，显示价格趋势和设置提醒
- **`FlightAdapter.kt`**：RecyclerView 适配器，处理列表项显示
- **`AirportAdapter.kt`**：机场列表适配器，处理机场搜索结果显示

#### 3. 服务层 (Service Layer)
负责后台服务和数据处理：

- **`FlightStatusCheckService.kt`**：航班状态检查服务
- **`FlightNotificationService.kt`**：航班通知服务
- **`NotificationHelper.kt`**：通知管理助手类

#### 4. 依赖注入模块 (DI Module)
负责依赖关系配置：

- **`NetworkModule.kt`**：Hilt 依赖注入配置，管理网络相关依赖
- **`DatabaseModule.kt`**：数据库依赖注入配置

#### 5. 工具类 (Utils)
提供通用功能：

- **`Resource.kt`**：数据状态包装器，统一处理成功、错误、加载状态
- **`LocaleManager.kt`**：语言管理器，处理多语言切换和持久化
- **`FlightTrackingManager.kt`**：航班跟踪管理器，管理后台跟踪服务

## 项目结构

### 标准 Android 项目结构

本项目严格遵循 Android 开发标准，采用标准的目录结构：

- **src/main/**：主要源代码目录
- **src/test/**：单元测试目录（JUnit 测试）
- **src/androidTest/**：插装测试目录（Android 环境下的集成测试）

这种结构确保：
1. **KAPT 构建兼容性**：避免 Kotlin 注解处理器构建失败
2. **IDE 支持**：Android Studio 完全识别和支持
3. **Gradle 构建**：标准构建脚本无需额外配置
4. **团队协作**：开发者熟悉的标准结构

### 目录组织原则

- **按功能分层**：数据层、表现层、业务层清晰分离
- **包结构合理**：遵循反向域名约定 `com.flightinfo.app`
- **资源分类**：drawable、layout、values 等资源按类型组织
- **测试完整性**：为每个源码包提供对应的测试目录

```
FlightInfoApp/
├── app/
│   ├── src/
│   │   ├── main/                            # 主源代码目录
│   │   │   ├── java/com/flightinfo/app/
│   │   │   │   ├── FlightInfoApplication.kt  # 应用程序入口
│   │   │   │   ├── data/                     # 数据层
│   │   │   │   │   ├── api/                  # API 接口定义
│   │   │   │   │   │   └── FlightApiService.kt
│   │   │   │   │   ├── model/                # 数据模型
│   │   │   │   │   │   └── FlightInfo.kt
│   │   │   │   │   └── repository/           # 数据仓库
│   │   │   │   │       └── FlightRepository.kt
│   │   │   │   ├── di/                       # 依赖注入
│   │   │   │   │   └── NetworkModule.kt
│   │   │   │   ├── ui/                       # 用户界面层
│   │   │   │   │   ├── MainActivity.kt
│   │   │   │   │   ├── adapter/              # 适配器
│   │   │   │   │   ├── fragment/             # 片段
│   │   │   │   │   └── viewmodel/            # 视图模型
│   │   │   │   └── utils/                    # 工具类
│   │   │   │       └── Resource.kt
│   │   │   ├── res/                          # 资源文件
│   │   │   │   ├── drawable/                 # 图标和背景
│   │   │   │   ├── layout/                   # 布局文件
│   │   │   │   ├── mipmap-anydpi-v26/        # 应用图标
│   │   │   │   ├── navigation/               # 导航配置
│   │   │   │   ├── values/                   # 值资源
│   │   │   │   └── xml/                      # XML 配置
│   │   │   └── AndroidManifest.xml           # 应用清单
│   │   ├── test/                            # 单元测试目录
│   │   │   └── java/com/flightinfo/app/     # 单元测试源码
│   │   └── androidTest/                     # 插装测试目录
│   │       └── java/com/flightinfo/app/     # 插装测试源码
│   └── build.gradle                         # 应用构建配置
├── gradle.properties                        # Gradle 属性配置
├── README.md                                # 项目说明文档
└── PROJECT_DOCUMENTATION.md                # 项目技术文档
```

## 使用场景

### 商务旅客
- 快速查询航班状态，合理安排行程
- 实时监控航班延误，及时调整计划
- 获取登机口信息，减少机场候机时间

### 机场工作人员
- 查询航班详细信息，为乘客提供准确服务
- 监控航班状态变化，协调地面服务

### 航空爱好者
- 跟踪感兴趣的航班
- 了解不同航空公司的航班信息
- 学习航空业相关知识

## 快速开始

### 前置要求

#### 系统要求
- **操作系统**: Windows 10+, macOS 10.15+, 或 Linux (Ubuntu 18.04+)
- **内存**: 最低 8GB RAM，推荐 16GB+
- **存储**: 至少 5GB 可用空间

#### 开发环境
- **Android Studio**: Hedgehog (2023.1.1) 或更高版本
- **Android SDK**: API 21 (Android 5.0) - API 34 (Android 14)
- **Kotlin**: 1.9.0+
- **Java**: JDK 8 或更高版本
- **Git**: 版本控制

### 环境搭建

#### 1. 安装 Android Studio
```bash
# 下载 Android Studio
# 访问: https://developer.android.com/studio
# 按照安装向导完成安装
```

#### 2. 配置 Android SDK
```bash
# 在 Android Studio 中打开 SDK Manager
# 安装以下组件:
# - Android SDK Platform-Tools
# - Android SDK Build-Tools
# - Android 14.0 (API 34)
# - Android 13.0 (API 33)
# - Android 12.0 (API 31)
# - Android 11.0 (API 30)
# - Intel x86 Emulator Accelerator (HAXM installer)
```

#### 3. 克隆项目
```bash
# 克隆项目到本地
git clone <repository-url>
cd FlightInfoApp
```

#### 4. 配置项目
```bash
# 在项目根目录执行
./gradlew clean
./gradlew build
```

### 构建和运行

#### 开发构建
```bash
# 构建项目
./gradlew build

# 运行单元测试
./gradlew test

# 运行插装测试
./gradlew connectedAndroidTest

# 安装调试版本到设备
./gradlew installDebug
```

#### 生产构建
```bash
# 构建发布版本
./gradlew assembleRelease

# 构建并签名发布版本
./gradlew bundleRelease
```

### 项目依赖

#### 核心库依赖
```kotlin
// Android Core
implementation 'androidx.core:core-ktx:1.12.0'
implementation 'androidx.appcompat:appcompat:1.6.1'
implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
implementation 'com.google.android.material:material:1.11.0'

// Architecture Components
implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0'
implementation 'androidx.lifecycle:lifecycle-livedata-ktx:2.7.0'
implementation 'androidx.navigation:navigation-fragment-ktx:2.7.6'
implementation 'androidx.navigation:navigation-ui-ktx:2.7.6'

// Network
implementation 'com.squareup.retrofit2:retrofit:2.9.0'
implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
implementation 'com.squareup.okhttp3:okhttp:4.12.0'
implementation 'com.squareup.okhttp3:logging-interceptor:4.12.0'

// Dependency Injection
implementation 'com.google.dagger:hilt-android:2.48'
kapt 'com.google.dagger:hilt-compiler:2.48'

// Database
implementation 'androidx.room:room-runtime:2.6.1'
implementation 'androidx.room:room-ktx:2.6.1'
kapt 'androidx.room:room-compiler:2.6.1'

// Coroutines
implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3'
implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3'

// Testing
testImplementation 'junit:junit:4.13.2'
testImplementation 'org.mockito:mockito-core:4.11.0'
testImplementation 'org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3'
androidTestImplementation 'androidx.test.ext:junit:1.1.5'
androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'
```

#### 开发工具依赖
```kotlin
// Build Tools
buildToolsVersion '34.0.0'
compileSdk 34

// Kotlin
kotlinVersion '1.9.0'

// Gradle Plugins
id 'com.android.application'
id 'org.jetbrains.kotlin.android'
id 'kotlin-kapt'
id 'dagger.hilt.android.plugin'
id 'androidx.navigation.safeargs.kotlin'
```

## 常见问题

### 构建问题

#### 1. Gradle 构建失败
**问题**: `./gradlew build` 失败，提示依赖解析错误

**解决方案**:
```bash
# 清理项目
./gradlew clean

# 清理 Gradle 缓存
./gradlew --refresh-dependencies

# 重新构建
./gradlew build
```

#### 2. Hilt 注入失败
**问题**: 运行时出现 `Hilt` 相关错误

**解决方案**:
1. 检查 `@HiltAndroidApp` 注解是否在 Application 类中
2. 确认所有依赖模块正确配置
3. 清理并重新构建项目

#### 3. 数据库迁移问题
**问题**: Room 数据库版本升级失败

**解决方案**:
```kotlin
// 在数据库类中添加迁移逻辑
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // 迁移逻辑
    }
}
```

### 运行时问题

#### 1. 网络请求失败
**问题**: API 调用返回错误或超时

**解决方案**:
1. 检查网络连接
2. 确认 API 端点配置正确
3. 查看日志中的详细错误信息

#### 2. 内存泄漏
**问题**: 应用运行一段时间后崩溃

**解决方案**:
1. 使用 Android Profiler 检查内存使用
2. 确保 `ViewModel` 正确清理
3. 检查协程是否正确取消

#### 3. 界面渲染问题
**问题**: RecyclerView 卡顿或显示异常

**解决方案**:
1. 优化 `ViewHolder` 实现
2. 使用 `DiffUtil` 减少不必要的刷新
3. 检查布局层级是否过深

### 开发环境问题

#### 1. Android Studio 同步失败
**问题**: Gradle 同步失败

**解决方案**:
```bash
# 删除 .gradle 和 .idea 目录
rm -rf .gradle
rm -rf .idea

# 重新同步项目
./gradlew clean build
```

#### 2. 模拟器启动失败
**问题**: Android 模拟器无法启动

**解决方案**:
1. 检查 HAXM 是否安装
2. 确认 AVD 配置正确
3. 尝试使用不同的系统镜像

### 测试数据
应用目前使用模拟航班数据进行演示，包括：
- **CA1234**：中国国际航空 (北京首都 → 上海虹桥) - 准点
- **MU5678**：中国东方航空 (上海虹桥 → 深圳宝安) - 延误 30 分钟
- **CZ9012**：中国南方航空 (广州白云 → 北京首都) - 登机中
- **CA1001**：中国国际航空 (北京首都 → 洛杉矶) - 飞行中
- **UA857**：美国联合航空 (旧金山 → 东京成田) - 准点

## 未来发展规划

### 功能增强
- **航班预订集成**：与航空公司预订系统对接
- **推送通知**：航班状态变化实时通知
- **收藏功能**：用户可收藏常用航班
- **离线缓存**：使用 Room 数据库实现数据持久化
- **航线地图**：可视化航班路线
- **价格跟踪**：监控票价变化趋势

### 技术优化
- **性能优化**：提升应用启动速度和响应速度
- **国际化支持**：多语言界面支持
- **无障碍功能**：提升应用可访问性
- **测试覆盖**：增加单元测试和 UI 测试

### 平台扩展
- **iOS 版本开发**：使用 React Native 或 Flutter
- **Web 版本**：基于 Progressive Web App (PWA)
- **桌面应用**：Electron 或原生桌面应用

## 技术亮点

### 1. 现代化架构
- 采用业界认可的 MVVM 架构模式
- 完整的依赖注入实现
- 响应式编程思想

### 2. 用户体验
- Material Design 3 设计语言
- 流畅的动画和过渡效果
- 直观的状态反馈

### 3. 代码质量
- Kotlin 现代化语法特性
- 清晰的代码组织结构
- 良好的错误处理机制

### 4. 可维护性
- 模块化设计
- 单一职责原则
- 易于测试和扩展

## 总结

FlightInfoApp 是一个功能完整、架构清晰的现代化 Android 应用程序。它不仅解决了用户在航班信息查询方面的实际需求，更展现了当前 Android 开发的最佳实践。通过采用 MVVM 架构、Kotlin 协程、Hilt 依赖注入等现代技术，该项目为进一步的功能扩展和技术优化奠定了坚实的基础。

该应用具有良好的可扩展性和可维护性，能够适应不断变化的业务需求和技术发展趋势，是一个值得参考和学习的优秀 Android 项目案例。

## 贡献指南

### 如何贡献

我们欢迎所有形式的贡献！请遵循以下步骤：

#### 1. 报告问题
- 使用 GitHub Issues 报告 bug 或提出功能建议
- 提供详细的复现步骤和期望行为
- 包含相关的日志信息、截图或视频

#### 2. 提交代码
1. **Fork 项目**
   ```bash
   # Fork 项目到你的 GitHub 账户
   ```

2. **克隆本地仓库**
   ```bash
   git clone https://github.com/your-username/FlightInfoApp.git
   cd FlightInfoApp
   ```

3. **创建功能分支**
   ```bash
   git checkout -b feature/your-feature-name
   ```

4. **开发并测试**
   ```bash
   # 运行测试
   ./gradlew test
   
   # 构建项目
   ./gradlew build
   ```

5. **提交更改**
   ```bash
   git add .
   git commit -m "feat: add your feature description"
   ```

6. **推送分支**
   ```bash
   git push origin feature/your-feature-name
   ```

7. **创建 Pull Request**
   - 在 GitHub 上创建 PR
   - 详细描述你的更改
   - 关联相关的 Issue

### 代码规范

#### Kotlin 代码风格
- 使用 Kotlin 官方编码规范
- 函数和变量使用驼峰命名法
- 常量使用大写字母和下划线
- 类名使用帕斯卡命名法

#### Git 提交规范
使用 [Conventional Commits](https://www.conventionalcommits.org/) 规范：

```bash
# 功能
feat: add new flight search feature

# 修复
fix: resolve flight status display issue

# 文档
docs: update API documentation

# 样式
style: format code according to style guide

# 重构
refactor: improve repository pattern implementation

# 测试
test: add unit tests for flight search

# 构建
build: update gradle dependencies

# 性能
perf: optimize flight list loading speed
```

#### 代码审查标准
- 所有代码变更必须经过审查
- 确保测试覆盖率不低于 80%
- 代码必须通过所有静态分析工具
- 更新相关文档

### 开发指南

#### 添加新功能
1. 在 `feature/` 分支上开发
2. 编写单元测试和集成测试
3. 更新相关文档
4. 提交 PR 进行审查

#### 修复 Bug
1. 创建 `bugfix/` 分支
2. 编写复现测试
3. 修复问题并验证
4. 提交 PR 进行审查

#### 架构原则
- 遵循 MVVM 架构模式
- 使用依赖注入 (Hilt)
- 实现 Repository 模式
- 遵循单一职责原则

## 许可证

本项目采用 **MIT 许可证** - 详见 [LICENSE](LICENSE) 文件。

### 许可证摘要
- ✅ 商业使用
- ✅ 修改
- ✅ 分发
- ✅ 私人使用
- ❗ 责任免除
- ❗ 需要包含许可证和版权声明

### 第三方许可证
本项目使用以下第三方库，请遵守其各自的许可证：

- [Android Jetpack](https://developer.android.com/jetpack) - Apache 2.0
- [Retrofit](https://square.github.io/retrofit/) - Apache 2.0
- [Hilt](https://dagger.dev/hilt/) - Apache 2.0
- [Room](https://developer.android.com/jetpack/androidx/releases/room) - Apache 2.0
- [Material Components](https://material.io/develop/android/docs/getting-started) - Apache 2.0

## 联系我们

### 项目维护者
- **维护者**: 开发团队
- **邮箱**: dev@flightinfo.com
- **GitHub**: [@flightinfo-team](https://github.com/flightinfo-team)

### 技术支持
- **问题反馈**: [GitHub Issues](https://github.com/flightinfo/FlightInfoApp/issues)
- **功能请求**: [GitHub Discussions](https://github.com/flightinfo/FlightInfoApp/discussions)
- **邮件支持**: support@flightinfo.com

### 社区
- **技术博客**: [https://blog.flightinfo.com](https://blog.flightinfo.com)
- **开发者社区**: [https://community.flightinfo.com](https://community.flightinfo.com)
- **Twitter**: [@flightinfo_dev](https://twitter.com/flightinfo_dev)

---

**最后更新**: 2024年8月24日