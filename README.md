# Nova Admin

基于 Spring Boot 3 + Vue 的**企业后台管理系统脚手架**（个人实战项目，可本地演示）。

提供 JWT 鉴权、AOP 操作审计、统一响应、文件上传与数据统计等通用能力；落地**组织人事**与**教务**两个业务模块，采用 Nginx 前后端分离部署。

> 定位是可扩展的管理后台骨架，不是完整多租户 SaaS。多租户 / 计费等能力见下方 Roadmap（尚未实现）。  
> 前端为 **Vue 构建产物**（`nginx-1.22.0-web/html/`），源码未纳入本仓库。

## 简历描述（可直接粘贴）

**一句话：**

> 基于 Spring Boot 3 + Vue 的企业后台脚手架，实现 JWT 鉴权、AOP 操作日志、统一异常与 OSS 上传；落地部门/员工（主子表事务）、班级学员及统计报表，Nginx 前后端分离部署。

**要点 bullet（按需裁剪）：**

- 设计并实现 JWT 登录鉴权与拦截器校验，非法/缺失 Token 统一返回 401
- 基于自定义注解 + Spring AOP 记录增删改操作日志（操作人、参数、返回值、耗时）并落库
- 员工管理采用主子表事务（员工 + 工作经历），配合 PageHelper 条件分页查询
- 集成阿里云 OSS 完成文件上传；Nginx 静态托管前端并反向代理 `/api` 到后端

## 项目亮点

1. **鉴权闭环**：登录签发 JWT，拦截器统一校验，业务侧可取当前用户
2. **可观测操作审计**：`@Log` + AOP，无需在每个 Controller 手写日志
3. **统一响应与异常**：`Result` 封装 + 全局异常处理，接口风格一致
4. **主子表事务**：员工与工作经历同事务写入，保证数据一致性
5. **前后端分离**：Vue 产物 + Nginx（`:90`）反代 Spring Boot（`:8080`）

## 界面预览

本地启动后访问 `http://localhost:90`，建议截取登录页、员工列表、数据报表，保存到 [`docs/screenshots/`](docs/screenshots/)（命名建议见该目录说明），再按需加到本 README：

```markdown
![登录页](docs/screenshots/login.png)
![员工列表](docs/screenshots/emp-list.png)
![数据报表](docs/screenshots/report.png)
```

## 演示账号

导入 [`nova_admin.sql`](nova_admin.sql) 后，可用任意员工账号登录（密码均为初始演示值）：

| 用户名 | 密码 | 说明 |
|--------|------|------|
| `songjiang` | `123456` | 演示账号 |
| `lujunyi` | `123456` | 演示账号 |

> 演示数据为明文密码，仅用于本地学习，不要用于生产。

## 技术栈

| 层级 | 技术 |
|------|------|
| 后端 | Java 17、Spring Boot 3.5、MyBatis、PageHelper、MySQL、JWT、Spring AOP |
| 存储 / 文件 | MySQL 8、阿里云 OSS |
| 前端 / 网关 | Vue 打包产物、Nginx（静态托管 + `/api` 反向代理） |
| 构建 | Maven |

## 系统架构

```
浏览器 ──► Nginx (:90)
              ├── /        → 前端静态资源 (html/)
              └── /api/*   → Spring Boot (:8080) ──► MySQL (nova_admin)
```

## 能力一览

### 通用底座

| 能力 | 说明 |
|------|------|
| 登录鉴权 | 账号密码登录签发 JWT；拦截器统一校验，非法/缺失 Token 返回 401 |
| 操作审计 | 自定义 `@Log` + AOP，记录操作人、参数、返回值、耗时并落库 |
| 统一响应 | `Result` 封装 + 全局异常处理 |
| 分页查询 | PageHelper 多条件分页 |
| 文件上传 | 阿里云 OSS，按日期目录 + UUID 命名 |
| 前后端分离 | Nginx 托管前端，反代 API 到后端 |

### 样例业务模块

| 模块 | 说明 |
|------|------|
| 组织人事 | 部门 CRUD；员工 CRUD、工作经历（主子表事务）、条件分页 |
| 教务业务 | 班级 / 学员管理与条件分页 |
| 数据统计 | 员工职位/性别、班级人数、学员学历等报表接口 |

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
│   ├── conf/nginx.conf
│   └── html/
├── docs/screenshots/           # README 截图
├── .env.example                # 环境变量模板
└── nova_admin.sql              # 数据库初始化脚本
```

## 快速开始

### 1. 初始化数据库

```bash
mysql -u root -p < nova_admin.sql
```

或手动创建库后导入：

```sql
CREATE DATABASE IF NOT EXISTS nova_admin DEFAULT CHARACTER SET utf8mb4;
```

### 2. 配置后端

参考 [`.env.example`](.env.example)。可通过环境变量覆盖，例如 PowerShell：

```bash
$env:DB_USERNAME="root"
$env:DB_PASSWORD="your-password"
$env:JWT_SECRET="replace-with-a-long-random-string"
$env:OSS_ACCESS_KEY_ID="..."
$env:OSS_ACCESS_KEY_SECRET="..."
$env:OSS_BUCKET="your-bucket-name"
```

也可直接编辑 `nova-admin/src/main/resources/application.yml`（本地密钥请用 `application-local.yml`，已 gitignore）。

### 3. 启动后端

```bash
cd nova-admin
mvn spring-boot:run
```

默认端口：`http://localhost:8080`

### 4. 启动前端（Nginx）

使用 `nginx-1.22.0-web` 下的配置启动 Nginx，浏览器访问 `http://localhost:90`。  
`/api` 已反向代理到后端 `8080`。

本地需自行安装 Nginx（仓库不包含 `nginx.exe` 等第三方二进制）。

## Roadmap（未实现）

后续可按此顺序增强脚手架能力：

1. **RBAC**：角色 / 菜单 / 接口权限
2. **系统字典 / 配置中心**
3. **简易多租户**（可选）：`tenant_id` + 登录选租户

## 说明

本仓库为个人学习与实战作品，用于练习企业级后台开发链路与可扩展项目结构。采用 [MIT License](LICENSE)。
