# Git提交指南

## 1. 初始化Git仓库（如果尚未初始化）

```bash
cd /Users/wurongquan/sensetimeproject/new2/FlightInfoApp
git init
```

## 2. 添加所有文件到暂存区

```bash
git add .
```

## 3. 提交更改

```bash
git commit -m "实现航班状态通知功能"
```

## 4. 连接到GitHub仓库

如果您还没有GitHub仓库，需要先在GitHub上创建一个仓库。

然后将本地仓库连接到GitHub仓库：

```bash
git remote add origin https://github.com/yourusername/your-repo-name.git
```

## 5. 推送到GitHub

```bash
git push -u origin main
```

## 提交的更改摘要

### 新增功能
- 航班状态实时通知（延误、取消、登机口变更）
- 本地数据库存储跟踪的航班
- 后台服务定期检查航班状态
- Firebase云消息集成
- 多语言支持（中英文）
- 勿扰模式设置

### 主要文件
1. 数据模型：
   - `app/src/main/java/com/flightinfo/app/data/model/TrackedFlight.kt`

2. 数据库层：
   - `app/src/main/java/com/flightinfo/app/data/dao/TrackedFlightDao.kt`
   - `app/src/main/java/com/flightinfo/app/data/database/FlightInfoDatabase.kt`
   - `app/src/main/java/com/flightinfo/app/data/database/Converters.kt`

3. 仓储层：
   - `app/src/main/java/com/flightinfo/app/data/repository/TrackedFlightRepository.kt`

4. 依赖注入：
   - `app/src/main/java/com/flightinfo/app/di/DatabaseModule.kt`

5. 服务层：
   - `app/src/main/java/com/flightinfo/app/service/FlightNotificationService.kt`
   - `app/src/main/java/com/flightinfo/app/service/FlightStatusCheckService.kt`

6. 工具类：
   - `app/src/main/java/com/flightinfo/app/utils/FlightTrackingManager.kt`
   - `app/src/main/java/com/flightinfo/app/utils/NotificationHelper.kt`

7. UI组件：
   - `app/src/main/res/layout/item_flight.xml`（添加了跟踪按钮）
   - `app/src/main/res/layout/fragment_notification_settings.xml`
   - `app/src/main/res/drawable/ic_flight.xml`

8. 资源文件：
   - `app/src/main/res/values/strings.xml`（添加了新字符串）
   - `app/src/main/res/values-zh/strings.xml`（添加了中文翻译）

9. 配置文件：
   - `app/build.gradle`（添加了Room和Firebase依赖）
   - `app/src/main/AndroidManifest.xml`（注册了服务）

10. 文档：
    - `FLIGHT_NOTIFICATION_FEATURE.md`（功能说明文档）
    - `PROJECT_DOCUMENTATION.md`（项目文档）

## 注意事项

1. 确保在提交前解决所有编译错误
2. 检查Firebase配置是否正确
3. 验证Room数据库迁移是否正常
4. 测试通知功能在不同设备上的表现