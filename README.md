# Nova Admin

[![Java](https://img.shields.io/badge/Java-17-orange)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5-brightgreen)](https://spring.io/projects/spring-boot)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

基于 Spring Boot 3 + Vue 的**企业后台管理系统脚手架**（个人实战项目）。

实现 JWT 鉴权、AOP 操作日志、统一异常与 OSS 上传；落地部门/员工（主子表事务）、班级学员及统计报表，Nginx 前后端分离部署。

> 仓库地址：https://github.com/closetruth/nova-admin  
> 定位是可扩展的管理后台骨架，不是完整多租户 SaaS。前端为 Vue **构建产物**（源码未纳入本仓库）。

## 技术栈

| 层级 | 技术 |
|------|------|
| 后端 | Java 17、Spring Boot 3.5、MyBatis、PageHelper、MySQL、JWT、Spring AOP |
| 存储 / 文件 | MySQL 8、阿里云 OSS |
| 前端 / 网关 | Vue 打包产物、Nginx（静态托管 + `/api` 反向代理） |
| 构建 | Maven |

## 项目亮点

- **JWT 鉴权闭环**：登录签发 Token，拦截器统一校验，非法/缺失返回 401
- **AOP 操作审计**：自定义 `@Log` 注解，记录操作人、参数、返回值、耗时并落库
- **统一响应与异常**：`Result` 封装 + 全局异常处理
- **主子表事务**：员工与工作经历同事务写入，配合 PageHelper 条件分页
- **前后端分离**：Nginx 托管前端静态资源，反向代理 `/api` 到 Spring Boot

## 系统架构

```
浏览器 ──► Nginx (:90)
              ├── /        → 前端静态资源 (html/)
              └── /api/*   → Spring Boot (:8080) ──► MySQL (nova_admin)
```

## 功能模块

| 类型 | 模块 | 说明 |
|------|------|------|
| 底座 | 登录鉴权 | 账号密码登录，JWT 拦截器校验 |
| 底座 | 操作审计 | `@Log` + AOP，增删改接口自动记日志 |
| 底座 | 文件上传 | 阿里云 OSS，按日期目录 + UUID 命名 |
| 业务 | 组织人事 | 部门 CRUD；员工 CRUD、工作经历（主子表） |
| 业务 | 教务管理 | 班级 / 学员管理与条件分页 |
| 业务 | 数据统计 | 员工职位/性别、班级人数、学员学历等报表 |

## 界面预览

本地启动后访问 `http://localhost:90`。截图可放在 [`docs/screenshots/`](docs/screenshots/)（`login.png` / `emp-list.png` / `report.png`），补充后可在此展示：

```markdown
![登录页](docs/screenshots/login.png)
![员工列表](docs/screenshots/emp-list.png)
![数据报表](docs/screenshots/report.png)
```

## 演示账号

### MySQL（后端内置）

| 用户名 | 密码 |
|--------|------|
| `root` | `1234` |

数据库名：`nova_admin`。可通过环境变量 `DB_USERNAME` / `DB_PASSWORD` 覆盖。

### 登录账号

导入 [`nova_admin.sql`](nova_admin.sql) 后可用：

| 用户名 | 密码 |
|--------|------|
| `songjiang` | `123456` |
| `lujunyi` | `123456` |

> 仅用于本地演示，请勿用于生产环境。

## 快速开始

### 1. 初始化数据库

```bash
mysql -u root -p1234 < nova_admin.sql
```

### 2. 配置环境变量

后端已内置 `root` / `1234`，本地可直接启动。其他项参考 [`.env.example`](.env.example)。PowerShell 示例：

```powershell
$env:JWT_SECRET="replace-with-a-long-random-string"
$env:OSS_ACCESS_KEY_ID="..."
$env:OSS_ACCESS_KEY_SECRET="..."
$env:OSS_BUCKET="your-bucket-name"
```

### 3. 启动后端

```bash
cd nova-admin
mvn spring-boot:run
```

后端默认：`http://localhost:8080`

### 4. 启动前端（Nginx）

使用 `nginx-1.22.0-web` 下的配置启动 Nginx，浏览器访问 `http://localhost:90`。  
`/api` 已反向代理到后端 `8080`。本地需自行安装 Nginx（仓库不含 `nginx.exe`）。

## 项目结构

```
.
├── nova-admin/                 # Spring Boot 后端
│   └── src/main/java/com/nova/admin/
│       ├── controller/         # API
│       ├── service/            # 业务
│       ├── mapper/             # MyBatis
│       ├── pojo/               # 实体 / DTO / 统一响应
│       ├── interceptor/        # JWT 拦截器
│       ├── app/                # AOP 操作日志
│       ├── anno/               # @Log
│       ├── utils/              # JWT、OSS、ThreadLocal
│       ├── config/             # Web 配置
│       └── exception/          # 全局异常处理
├── nginx-1.22.0-web/           # 前端产物 + Nginx 配置
├── docs/screenshots/           # 界面截图（可选）
├── .env.example                # 环境变量模板
└── nova_admin.sql              # 数据库初始化脚本
```

## Roadmap

1. RBAC（角色 / 菜单 / 接口权限）
2. 系统字典 / 配置中心
3. 简易多租户（可选）

## License

[MIT](LICENSE)
