# GitHub代码上传指南

## 1. 在GitHub上创建新仓库

1. 登录您的GitHub账户。
2. 点击右上角的"+"号，然后选择"New repository"。

   

3. 在"Repository name"中输入您的项目名称，例如`FlightInfoApp`。
4. 选择"Public"或"Private"（公共或私有）。
5. 点击"Create repository"。

   

## 2. 初始化本地Git仓库

1. 打开终端并导航到您的项目目录：
   ```bash
   cd /Users/wurongquan/sensetimeproject/new2/FlightInfoApp
   ```

2. 初始化Git仓库：
   ```bash
   git init
   ```

## 3. 添加并提交您的代码

1. 添加所有文件到暂存区：
   ```bash
   git add .
   ```

2. 提交您的更改：
   ```bash
   git commit -m "实现航班状态通知功能"
   ```

## 4. 连接到GitHub仓库

1. 复制您在GitHub上创建的仓库的URL。

   

2. 将本地仓库连接到GitHub仓库：
   ```bash
   git remote add origin https://github.com/yourusername/FlightInfoApp.git
   ```

## 5. 推送您的代码到GitHub

```bash
git push -u origin main
```

## 完成

现在您的代码已成功上传到GitHub！您可以在您的GitHub个人资料页面上看到新的仓库。