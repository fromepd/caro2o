## 开发

```bash
# 安装依赖
npm install

# 建议不要直接使用 cnpm 安装依赖，会有各种诡异的 bug。可以通过如下操作解决 npm 下载速度慢的问题
npm install --registry=https://registry.npmmirror.com

# 也可以直接将 npm 的仓库修改为镜像仓库
npm config set registry https://registry.npmmirror.com

# 启动服务
npm run dev
```

浏览器访问 http://localhost:8080

## 发布

```bash
# 构建测试环境
npm run build:stage

# 构建生产环境
npm run build:prod
```

## 本地开发接口配置

找到 `vue.config.js` 文件中 target 属性修改为对应的后台接口访问地址

```js
    proxy: {
      [process.env.VUE_APP_BASE_API]: {
        target: `http://localhost:9803/`,
        changeOrigin: true,
        pathRewrite: {
          ["^" + process.env.VUE_APP_BASE_API]: "",
        }
      },
    }
```

