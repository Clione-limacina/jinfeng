<template>
  <div class="navbar">
    <!-- 默认情况elementUI导航是没有路由功能，记得要加router属性，并且布尔值true进去,router前面要加冒号 -->
    <el-menu
      :router="true"
      :default-active="$route.path"
      class="el-menu-vertical-demo"
      background-color="#545c64"
      text-color="#fff"
      active-text-color="#ffd04b"
    >
      <el-menu-item :index="item.path" v-for="item in menuList" :key="item.path">
        <i :class="item.icon"></i>
        <!-- <router-link tag="span" :to="item.path">{{ item.title }}</router-link> -->
        <span slot="title">{{ item.title }}</span>
      </el-menu-item>
      <!-- <el-menu-item index="2">
        <i class="el-icon-bank-card"></i>
        <span slot="title">商品管理</span>
      </el-menu-item>
      <el-menu-item index="3">
        <i class="el-icon-shopping-cart-2"></i>
        <span slot="title">订单管理</span>
      </el-menu-item>
      <el-menu-item index="4">
        <i class="el-icon-house"></i>
        <span slot="title">商铺管理</span>
      </el-menu-item>-->
    </el-menu>
  </div>
</template>

<script>
export default {
  data() {
    return {
      // istrue:true,
      menuList: [
        //菜单列表
        {
          path: "/home/users",
          icon: "el-icon-user-solid",
          title: "用户管理"
        },
        {
          path: "/home/goods",
          icon: "el-icon-bank-card",
          title: "商品管理"
        },
        {
          path: "/home/orders",
          icon: "el-icon-shopping-cart-2",
          title: "订单管理"
        },
        {
          path: "/home/shops",
          icon: "el-icon-house",
          title: "商铺管理"
        }
      ]
    };
  },

  components: {},

  methods: {
    // change(path) {
    //   console.log(path);
    // }
  },
  created() {
    // console.log(this.$route, 999);
  }
};
</script>

<style scoped>
/* .el-menu .el-menu-item {
  height: 80px;
} */
</style>