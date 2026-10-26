# WinXin Blog

前后端分离的个人博客系统（单体架构）· Spring Boot 4.1.2 + Java 21（虚拟线程）+ MyBatis-Plus 3.5.18 + MySQL 8 / Redis + Vue 3 + Element Plus。

> 当前为**基础骨架**：分层结构、统一响应、全局异常、CORS、接口文档、Axios 封装、路由布局已就绪并通过验证；业务逻辑按约定由开发者自行实现。

## 快速开始

```bash
# 后端（需 JDK 21+、Maven 3.9+；本机无 MySQL/Redis 也可启动——连接为懒加载）
cd backend
mvn spring-boot:run
# 验证：http://localhost:8080/ping
# 接口文档：http://localhost:8080/swagger-ui.html

# 前端（需 Node 20.19+/22+）
cd frontend
npm install     # 他人 clone 后请用 npm ci
npm run dev
# 访问：http://localhost:5173/health —— 页面会自动跨域调用 /ping 验证全链路
```

首次建库：执行 [docs/sql/init.sql](docs/sql/init.sql)（不建库不影响启动，仅数据访问时需要）。

## 文档

| 文档 | 内容 |
|---|---|
| [docs/01-技术文档.md](docs/01-技术文档.md) | 架构说明、目录结构、开发规范、Git 分支策略、跨域与部署、**Spring Boot 4 vs 3.x 差异**、验证记录 |
| [docs/02-依赖清单.md](docs/02-依赖清单.md) | 全部依赖的版本号与 Boot 4 兼容性依据（含 Knife4j 未采用的原因），供 review 后锁定 |
| [docs/03-接口文档.md](docs/03-接口文档.md) | 认证/文章/用户/评论/AI 五模块接口契约（只定义未实现） |
| [docs/sql/init.sql](docs/sql/init.sql) | 建表初稿：RBAC 五表 + 文章/评论/分类/标签 |

## 目录

```
backend/    # 后端 Spring Boot 工程
frontend/   # 前端 Vite + Vue3 + TS 工程
docs/       # 技术文档 / 依赖清单 / 接口文档 / SQL
```
