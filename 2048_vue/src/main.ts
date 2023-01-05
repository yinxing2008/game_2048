import { createApp } from 'vue';
import App from './App.vue';
import router from './router';

// 移动端适配
import 'lib-flexible/flexible.js';

createApp(App).use(router).mount('#app');