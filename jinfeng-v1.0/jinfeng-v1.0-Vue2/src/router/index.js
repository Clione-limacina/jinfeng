import Vue from "vue";
import VueRouter from "vue-router";
// import login from '@/views/login.vue';//不能省略vue后缀
//登陆组件
// import Login from '@/views/login';//如果文件夹里面是index.vue 可以省略不写
// //注册组件
// import Reg from '@/views/reg';//如果文件夹里面是index.vue 可以省略不写
// import Home from '@/views/home';//如果文件夹里面是index.vue 可以省略不写
// import appGoods from '@/views/appGoods';
// import appUsers from '@/views/appUsers';
// import appOrders from '@/views/appOrders';
// import appShops from '@/views/appShops';
//vue路由懒加载
const Login = () => import("@/views/login");
const Reg = () => import("@/views/reg");
const Home = () => import("@/views/home");
const appGoods = () => import("@/views/appGoods");
const appUsers = () => import("@/views/appUsers");
const appOrders = () => import("@/views/appOrders");
const appShops = () => import("@/views/appShops");

// 解决ElementUI导航栏中的vue-router在3.0版本以上重复点菜单报错问题
// const originalPush = VueRouter.prototype.push
// VueRouter.prototype.push = function push(location) {
//   return originalPush.call(this, location).catch(err => err)
// }

Vue.use(VueRouter);

/*
  views：放页面组件：整个页面-在路由表里面直接用到的
  components:放公共，复用性很高的子组件:表格组件
  layout:布局组件:左边菜单。顶部。顶部
  contains：比components大一些的复合组件

  views > layout > contains > components
*/
const routes = [
  {
    path: "/",
    redirect: "/home",
    // beforeEnter(to,from,next) {

    // }
  },
  {
    path: "/login",
    name: "login",
    component: Login,
    meta: {
      //元信息
      permission: false, //不需要拦截
    },
  },
  {
    path: "/reg",
    name: "reg",
    component: Reg,
    meta: {
      //元信息
      permission: false, //不需要拦截
    },
  },
  {
    path: "/home",
    name: "home",
    component: Home,
    redirect: "/home/users",
    children: [
      //设置子路由
      {
        path: "goods",
        name: "goods",
        component: appGoods,
        meta: {
          //元信息
          permission: true, //需要拦截
          title: "商品管理",
        },
      },
      {
        path: "users",
        name: "users",
        component: appUsers,
        meta: {
          title: "用户管理",
        },
      },
      {
        path: "orders",
        name: "orders",
        component: appOrders,
        meta: {
          title: "订单管理",
        },
      },
      {
        path: "shops",
        name: "shops",
        component: appShops,
        meta: {
          title: "商铺管理",
        },
      },
    ],
  },
];

const router = new VueRouter({
  mode: "history", //路由模式，默认是 hash
  routes,
});

export default router;
