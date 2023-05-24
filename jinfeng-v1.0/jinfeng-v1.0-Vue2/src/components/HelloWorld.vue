<template>
  <div class="hello">
    <el-button>默认按钮</el-button>
    <el-button type="primary">主要按钮</el-button>
    <el-button type="success">成功按钮</el-button>
    <el-button type="info">信息按钮</el-button>
    <el-button type="warning">警告按钮</el-button>
    <el-button type="danger">危险按钮</el-button>

    <!-- 轮播图 -->
    <el-carousel indicator-position="outside">
      <el-carousel-item v-for="item in 4" :key="item">
        <h3>{{ item }}</h3>
      </el-carousel-item>
    </el-carousel>
  </div>
</template>

<script>
//引入就是调用，调用test.js文件
import testApi from "@/api/test";
import "@/api/getjson2";
export default {
  name: "HelloWorld",
  props: {
    msg: String
  },
  data() {
    return {
      list: []
    };
  },
  methods: {
    async getList() {
      try {
        let p = await testApi.getData(); //调用api接口
        console.log(p.data, 333);
        this.list = p.data;
      } catch (err) {
        console.log(err);
      }
    }
  },

  created() {
    this.getList(); //进入页面就发送请求
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->

<style scoped lang="scss">
// scoped:这里面的样式只在本组件生效，局部作用域
//lang设置用sass写样式
h1 {
  span {
    width: 10px;
    height: 20px;
  }
}
.el-carousel__item h3 {
  color: #475669;
  font-size: 18px;
  opacity: 0.75;
  line-height: 300px;
  margin: 0;
}

.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n + 1) {
  background-color: #d3dce6;
}
</style>
