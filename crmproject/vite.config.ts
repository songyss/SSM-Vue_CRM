import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
<<<<<<< HEAD
//import vueJsx from '@vitejs/plugin-vue-jsx'
=======
import vueJsx from '@vitejs/plugin-vue-jsx'
>>>>>>> 0e5380c6c0bb9606823c793a4ff3b5bb711a6a04
import vueDevTools from 'vite-plugin-vue-devtools'

// https://vite.dev/config/
export default defineConfig({
<<<<<<< HEAD
  plugins: [vue(), vueDevTools()],
  server: {
    host: true, // 允许所有网络接口访问（手机可通过局域网IP访问）
    port: 8081, // 端口号（避免与后端SSM的8080冲突）
    allowedHosts: true,
    strictPort: true, // 端口被占用时自动退出，避免冲突
  },
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
=======
  plugins: [
    vue(),
    vueJsx(),
    vueDevTools(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
>>>>>>> 0e5380c6c0bb9606823c793a4ff3b5bb711a6a04
    },
  },
})
