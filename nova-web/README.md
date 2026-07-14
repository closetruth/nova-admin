# Nova Web

Nova Admin 前端（Vue 3 + Vite + Element Plus + ECharts），对接 `tlias-web-management` / Spring Boot 后端 API。

## 开发

```bash
cd nova-web
npm install
npm run dev
```

开发服务器：`http://localhost:5173`，通过 `/api` 代理到 `http://localhost:8080`。

## 构建并发布到 Nginx

```bash
npm run build
# 将 dist/* 复制到 ../nginx-1.22.0-web/html/
```

访问：`http://localhost:90`（需 Nginx 按现有 conf 启动）。
