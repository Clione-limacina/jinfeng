<template>
  <div>
    <!-- 3.调用组件 -->
    <!-- 头部区域 -->
    <!-- <div class="header">header</div> -->
    <app-head></app-head>
    <!-- 左侧导航区域 -->
    <!-- <div class="navbar">左侧导航区域</div> -->
    <app-navbar></app-navbar>
    <!-- 右侧主区域 -->
    <!-- <div class="main">右侧主区域</div> -->
    <!-- <app-main></app-main> -->
    <!-- 设置路由出口 -->
    <router-view></router-view>
  </div>
</template>

<script>
// 1、先引入
import appHead from "@/components/AppHeader";
import AppNavbar from "@/components/AppNavbar";
import AppMain from "@/components/AppMain";
export default {
  data() {
    return {};
  },
  //2.注册
  components: { appHead, AppNavbar, AppMain },

  methods: {}
};
</script>

<style scoped>
/* @import "~@/assets/css/home.css"; */
/* 头部区域 */
.header {
  position: absolute;
  line-height: 50px;
  padding: 0px;
  top: 0px;
  left: 0px;
  right: 0px;
  background-color: #2d3a4b;
}
/* 左侧导航栏 */
.navbar {
  position: absolute;
  width: 230px;
  top: 50px;
  left: 0px;
  bottom: 0px;
  overflow-y: auto;
  background-color: #545c64;
}
/* 右侧主区域 */
.main {
  position: absolute;
  top: 50px;
  left: 230px;
  right: 0px;
  bottom: 0px;
  padding: 10px;
  overflow-y: auto;
  /* background-color: red; */
}
</style>