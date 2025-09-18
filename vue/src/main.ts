// import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'

//引入ElementPlus
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

// 引入图标
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

const app = createApp(App)

app.use(createPinia())
app.use(router)
app.use(ElementPlus)

// 注册图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

app.mount('#app')
