import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
//完整引入方式：缺点，文件较大，最好换成按需引入
// import ElementUI from 'element-ui';//引入elementUI的html、js
// import 'element-ui/lib/theme-chalk/index.css';//引入element的css
// Vue.use(ElementUI);//启用element插件:UI框架-用户界面构建

//不是按需引入：还是引入整个element-ui。只是解构使用而已
// import { Dialog } from 'element-ui';
// Vue.use(Dialog);

//按需引入方式一：单个组件逐个引入，麻烦
// import Dialog from 'element-ui/lib/dialog.js';//js
// import 'element-ui/lib/theme-chalk/dialog.css';//css
// Vue.use(Dialog);

//按需引入方式二:写在这里代码太多
// import { Dialog } from 'element-ui';
// Vue.component(Dialog.name, Dialog);
// Vue.use(Dialog);

//按需引入方式三:推荐，把需要用的组件统一引入进来，再放到main.js
import element from './components/element'
Vue.use(element);

import md5 from 'js-md5';//单向加密
Vue.prototype.$md5 = md5;//单向加密



import './permission';//路由守卫==路由拦截

Vue.config.productionTip = process.env.NODE_ENV == 'production';//false:有警告(开发过程要警告：方便调试)
// console.log(process.env.NODE_ENV);//开发环境：development;生成环境:production
new Vue({
  router,
  store,
  render: h => h(App)
}).$mount("#app");
