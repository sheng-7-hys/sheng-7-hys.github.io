# blog 系统使用说明

## 1. 项目概述

### 技术栈

| 层级 | 技术 |
|------|------|
| 后端框架 | Java 11 + Spring Boot 2.7.x |
| 持久层 | MyBatis-Plus 3.5.5 |
| 数据库 | MySQL 8.0 |
| 前端模板 | Thymeleaf（HTML + 原生 JavaScript） |
| 密码加密 | BCrypt（Spring Security Crypto） |
| 构建工具 | Maven |

### 核心功能

- **用户模块**：注册、登录（Session 维护）、退出、个人信息修改
- **文章模块**：发布、编辑、删除、列表分页查询（分类筛选）、详情查看（阅读量自增）
- **分类模块**：新增、修改、删除分类（管理员权限）
- **评论模块**：发表评论、回复评论、嵌套展示

### 改动说明

**仅新增功能，未修改任何原有 UI**：

- 新增 `blog-backend/` 目录，包含完整的 Spring Boot 项目
- 原 Jekyll 静态站点文件（`_config.yml`、`_posts/`、`assets/`、`_includes/`）完全保留，未做任何修改
- Spring Boot 版本通过 `Thymeleaf` 模板完全复刻原 Jekyll 站点的视觉效果（莫兰迪粉配色方案、汉堡菜单、文章卡片样式等）

---

## 2. 环境搭建

### 硬件/系统要求

| 要求项 | 最低配置 |
|--------|---------|
| 操作系统 | Windows 10 / macOS 10.15 / Ubuntu 20.04 |
| JDK | 11 或以上（推荐 OpenJDK 11 / Oracle JDK 11） |
| MySQL | 8.0 或以上 |
| Maven | 3.6 或以上 |
| 内存 | 2 GB 以上 |

### 依赖安装步骤

#### 1. JDK 安装与配置

**Windows：**
1. 下载 [OpenJDK 11](https://adoptium.net/) 并安装
2. 配置环境变量：
   - `JAVA_HOME` → JDK 安装路径（如 `C:\Program Files\Eclipse Adoptium\jdk-11.0.x`）
   - `Path` 追加 `%JAVA_HOME%\bin`
3. 验证：`java -version`

**Linux / macOS：**
```bash
# Ubuntu/Debian
sudo apt install openjdk-11-jdk

# macOS (Homebrew)
brew install openjdk@11

# 验证
java -version
```

#### 2. MySQL 8.0 安装

**Windows：** 从 [MySQL 官网](https://dev.mysql.com/downloads/installer/) 下载 MySQL Installer，按向导安装。

**Linux：**
```bash
sudo apt install mysql-server
sudo systemctl start mysql
sudo mysql_secure_installation
```

**macOS：**
```bash
brew install mysql
brew services start mysql
```

#### 3. Maven 安装与配置

**Windows：**
1. 下载 [Maven](https://maven.apache.org/download.cgi) 并解压
2. 配置 `Path` → 追加 Maven 的 `bin` 目录
3. 验证：`mvn -v`

**Linux / macOS：**
```bash
# Ubuntu/Debian
sudo apt install maven

# macOS
brew install maven

# 验证
mvn -v
```

---

## 3. 数据库配置

### 修改数据库连接信息

打开 `blog-backend/src/main/resources/application.yml`，修改以下配置项：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/blog_db?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
    username: root        # 修改为你的 MySQL 用户名
    password: 123456      # 修改为你的 MySQL 密码
```

> **注意**：数据库名称固定为 `blog_db`，由 SQL 脚本自动创建，无需手动创建。

### 执行 SQL 脚本

SQL 脚本路径：`blog-backend/sql/blog.sql`

**方式一：命令行执行**
```bash
mysql -u root -p < blog-backend/sql/blog.sql
# 输入 MySQL 密码后执行
```

**方式二：Navicat / DBeaver 等可视化工具执行**
1. 连接 MySQL 服务器
2. 打开 SQL 文件 `blog-backend/sql/blog.sql`
3. 点击「运行」或「执行」

**方式三：MySQL 命令行客户端**
```sql
source /path/to/blog-backend/sql/blog.sql;
```

---

## 4. 项目启动

### 方式一：IntelliJ IDEA 启动

1. 打开 IDEA，选择 `File → Open`，打开 `blog-backend` 目录
2. 等待 Maven 下载依赖（首次需要较长时间）
3. 找到 `src/main/java/com/sheng/blog/BlogApplication.java`
4. 右键点击 → `Run 'BlogApplication'`
5. 看到 `Started BlogApplication` 日志即为启动成功

### 方式二：Maven 命令启动

```bash
cd blog-backend
mvn spring-boot:run
```

### 方式三：打包后运行

```bash
cd blog-backend
mvn clean package -DskipTests
java -jar target/blog-1.0.0.jar
```

### 访问地址

| 页面 | 地址 |
|------|------|
| 首页 | http://localhost:8080/ |
| 关于我 | http://localhost:8080/about |
| 文章归档 | http://localhost:8080/archives |
| 登录 | http://localhost:8080/login |
| 注册 | http://localhost:8080/register |

---

## 5. 功能使用说明

### 管理员账号登录

| 项目 | 值 |
|------|-----|
| 用户名 | `admin` |
| 密码 | `123456` |
| 角色 | 管理员（可管理所有文章、分类） |

另有一个普通管理员账号：用户名 `sheng`，密码 `123456`。

**登录步骤：**
1. 访问 http://localhost:8080/login
2. 输入用户名 `admin`，密码 `123456`
3. 点击「登录」

登录成功后，导航菜单（左侧汉堡菜单）将显示更多选项。

---

### 用户模块操作

#### 注册账号
1. 访问 http://localhost:8080/register
2. 填写用户名（2-20 字符）、密码（不少于 6 位）、邮箱（可选）
3. 点击「注册」
4. 注册成功后自动跳转登录页

#### 退出登录
- 点击左侧汉堡菜单 → 「退出登录」
- 或直接访问 http://localhost:8080/logout

#### 修改个人信息
1. 登录后，点击汉堡菜单 → 「个人信息」
2. 修改邮箱或头像 URL
3. 点击「保存修改」

---

### 文章模块操作

#### 发布文章
1. 登录后，点击汉堡菜单 → 「发布文章」
2. 填写标题、选择分类、输入内容
3. 选择「立即发布」或「保存草稿」
4. 点击「发布」

#### 编辑文章
1. 进入文章详情页（点击文章标题）
2. 点击「✏️ 编辑」按钮（仅文章作者或管理员可见）
3. 修改内容后点击「保存修改」

#### 删除文章
1. 进入文章详情页
2. 点击「🗑️ 删除」按钮（仅文章作者或管理员可见）
3. 在确认对话框中点击「确定」

#### 按分类筛选文章
- 在首页分类筛选栏点击对应分类标签，即可筛选显示该分类下的文章

---

### 分类管理操作（仅管理员）

1. 登录管理员账号后，点击汉堡菜单 → 「分类管理」
2. 在输入框中输入新分类名称，点击「+ 新增分类」
3. 点击分类列表中的「编辑」按钮，可修改分类名称
4. 点击「删除」按钮可删除分类（该分类下有文章时无法删除）

---

### 评论模块操作

#### 发表评论
1. 进入任意文章详情页
2. 滚动到页面底部评论区
3. 在「✍️ 发表评论」文本框中输入内容
4. 点击「发表评论」（需登录）

#### 回复评论
1. 在评论区找到要回复的评论
2. 点击该评论下方的「回复」按钮
3. 在弹出的输入框中填写回复内容
4. 点击「发送回复」

---

## 6. 注意事项

### 关于原有 UI 无改动

- 原有 Jekyll 站点的所有文件（`_config.yml`、`assets/`、`_includes/`、`_posts/`、`index.md`、`about.md`、`archives.md`）均**未做任何修改**
- Spring Boot 版博客在 `blog-backend/` 目录中独立运行，通过 Thymeleaf 模板复刻相同的视觉效果
- 两套系统互不影响：Jekyll 版部署在 GitHub Pages，Spring Boot 版本地运行

### 数据库编码与时区配置

- 数据库编码：`utf8mb4`（支持中文和 Emoji）
- MySQL 时区配置：

```bash
# 在 MySQL 命令行中执行：
SET GLOBAL time_zone = 'Asia/Shanghai';
SET time_zone = 'Asia/Shanghai';
```

或在 MySQL 配置文件（`my.cnf` / `my.ini`）中添加：
```ini
[mysqld]
default-time-zone = Asia/Shanghai
```

### 常见问题排查

#### Q1：启动时报 `Communications link failure`（数据库连接失败）

**检查步骤：**
1. 确认 MySQL 已启动：`sudo systemctl status mysql`（Linux）或任务管理器查看（Windows）
2. 确认 `application.yml` 中的 `username` 和 `password` 正确
3. 确认数据库 `blog_db` 已创建（执行 SQL 脚本）
4. 确认 MySQL 端口为 `3306`（默认）

#### Q2：登录失败（用户名或密码错误）

**检查步骤：**
1. 确认已执行 SQL 脚本并成功插入初始用户数据
2. 检查 `user` 表中是否存在 `admin` 用户：`SELECT * FROM user WHERE username='admin';`
3. 默认密码为 `123456`，区分大小写
4. 若密码哈希有误，可在 Java 代码中重新生成：
   ```java
   System.out.println(new BCryptPasswordEncoder().encode("123456"));
   ```
   然后更新数据库：`UPDATE user SET password='新哈希值' WHERE username='admin';`

#### Q3：文章发布失败（提示"内容不能为空"）

**检查步骤：**
1. 确认文章标题和内容均已填写
2. 确认已登录（未登录时发布会跳转到登录页）

#### Q4：分类管理入口不可见

**原因：** 分类管理仅限管理员账号（`role=1`）访问。  
**解决：** 使用 `admin` 账号登录，或将目标账号的 `role` 字段更新为 `1`：
```sql
UPDATE user SET role=1 WHERE username='你的用户名';
```

#### Q5：端口 8080 被占用

在 `application.yml` 中修改端口：
```yaml
server:
  port: 8081  # 改为其他未被占用的端口
```

#### Q6：页面样式显示异常

**检查步骤：**
1. 确认访问地址为 `http://localhost:8080/`（带末尾斜杠）
2. 清除浏览器缓存（Ctrl+Shift+R）
3. 检查 `blog-backend/src/main/resources/static/css/main.css` 文件是否存在

---

## 附：项目目录结构

```
blog-backend/
├── pom.xml                          # Maven 构建文件
├── sql/
│   └── blog.sql                     # 数据库初始化脚本
└── src/main/
    ├── java/com/sheng/blog/
    │   ├── BlogApplication.java      # 启动类
    │   ├── common/
    │   │   ├── BlogException.java    # 业务异常类
    │   │   ├── GlobalExceptionHandler.java  # 全局异常处理
    │   │   └── Result.java           # 统一响应封装
    │   ├── config/
    │   │   └── MybatisPlusConfig.java  # MyBatis-Plus 配置
    │   ├── controller/
    │   │   ├── ArticleController.java  # 文章控制器
    │   │   ├── CategoryController.java # 分类控制器
    │   │   ├── CommentController.java  # 评论控制器
    │   │   └── UserController.java     # 用户控制器
    │   ├── dto/
    │   │   ├── ArticleDTO.java
    │   │   ├── LoginDTO.java
    │   │   └── RegisterDTO.java
    │   ├── entity/
    │   │   ├── Article.java
    │   │   ├── Category.java
    │   │   ├── Comment.java
    │   │   └── User.java
    │   ├── mapper/
    │   │   ├── ArticleMapper.java
    │   │   ├── CategoryMapper.java
    │   │   ├── CommentMapper.java
    │   │   └── UserMapper.java
    │   ├── service/
    │   │   ├── ArticleService.java
    │   │   ├── CategoryService.java
    │   │   ├── CommentService.java
    │   │   ├── UserService.java
    │   │   └── impl/
    │   │       ├── ArticleServiceImpl.java
    │   │       ├── CategoryServiceImpl.java
    │   │       ├── CommentServiceImpl.java
    │   │       └── UserServiceImpl.java
    │   └── vo/
    │       ├── ArticleVO.java
    │       └── CommentVO.java
    └── resources/
        ├── application.yml           # 应用配置
        ├── mapper/
        │   ├── ArticleMapper.xml     # 文章 SQL 映射
        │   └── CommentMapper.xml     # 评论 SQL 映射
        ├── static/
        │   └── css/
        │       └── main.css          # 主样式表（复刻原 UI）
        └── templates/
            ├── index.html            # 首页
            ├── about.html            # 关于我
            ├── archives.html         # 文章归档
            ├── article/
            │   ├── detail.html       # 文章详情
            │   ├── edit.html         # 文章编辑
            │   └── publish.html      # 文章发布
            ├── category/
            │   └── manage.html       # 分类管理
            ├── fragments/
            │   ├── common.html       # 导航/页脚 Fragment
            │   └── head.html         # <head> Fragment
            └── user/
                ├── login.html        # 登录
                ├── profile.html      # 个人信息
                └── register.html     # 注册
```
