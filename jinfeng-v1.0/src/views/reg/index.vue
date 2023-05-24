<template>
  <!-- 注册页组件 -->
  <div class="main">
    <div class="login-form">
      <h2 class="login-title">金丰会员管理系统-注册</h2>
      <el-form
        :model="ruleForm"
        :rules="rules"
        ref="form"
        label-width="100px"
        class="demo-ruleForm"
      >
        <el-form-item label="用户名" prop="name">
          <el-input v-model="ruleForm.name"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="ruleForm.password" type="password"></el-input>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="submitForm()" style="float:right;">注册</el-button>
          <el-button @click="gotoLogin" style="float:right;margin-right:5px">登陆</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
//npm i js-md5 -S
import usersApi from "@/api/usersApi"; //引入接口
export default {
  data() {
    var validateName = (rule, value, callback) => {
      /*
        value:该表单的value值
        callback：回调函数，返回提示信息
      */
      usersApi
        .checkName(value)
        .then(res => {
          // console.log(res);
          if (res.data.flag) {
            //可以注册
            callback();
          } else {
            //已存在，不可以注册
            callback(new Error("该用户名已存在"));
          }
        })
        .catch(err => {
          callback(new Error("服务器繁忙，稍后再试"));
        });
    };
    return {
      ruleForm: {
        name: "",
        password: ""
      },
      rules: {
        //正则
        name: [
          { required: true, message: "用户名不能为空", trigger: "blur" }, //用户名不能为空
          { validator: validateName, trigger: "blur" } //验证用户名是否存在
          //   { min: 3, max: 5, message: "长度在 3 到 5 个字符", trigger: "blur" }
        ],
        password: [{ required: true, message: "密码不能为空", trigger: "blur" }]
      }
    };
  },

  components: {},

  methods: {
    //功能：注册功能
    submitForm() {
      // this.$refs[formName].validate(valid => {
      this.$refs.form.validate(async valid => {
        // console.log(valid);valid是一个布尔值，如果为true就意味着上面的表单都验证通过，否则不通过
        if (valid) {
          //发送ajax
          try {
            let psw = this.$md5(this.ruleForm.password);
            // console.log(psw);
            let p = await usersApi.reg(this.ruleForm.name, psw);
            // console.log(p.data);
            if (p.data.flag) {
              //注册成功
              this.$message({
                message: "注册成功",
                type: "success"
              });
              //注册成功，跳转到登陆页面
              this.$router.push({
                path: "/login",
                query: { name: this.ruleForm.name }
              });
            } else {
              //注册失败
              this.$message({
                message: "注册失败",
                type: "error"
              });
            }
          } catch (err) {
            // console.log(err);
            this.$message({
              message: "服务器异常",
              type: "error"
            });
          }
        } else {
        }
      });
    },
    gotoLogin() {
      this.$router.push("/login");
    }
  },

  watch: {
    ruleForm: {
      deep: true,
      handler: function(val) {
        // console.log(val);
        //保留7天免登陆的提示框
        if (val.checked) {
          this.$message({
            message: "请不要在公共场合使用该功能",
            type: "error"
          });
        }
      }
    }
  },

  created() {
    // usersApi.checkName("malin").then(res => {
    //   console.log(res);
    // });
  }
};
</script>

<style scoped>
.login-form {
  width: 350px;
  /* 上下间隙150px, 左右自动 */
  margin: 160px auto;
  /*透明背景色*/
  background-color: rgb(255, 255, 255, 0.8);
  padding: 30px;
  padding-right: 60px;
  /* 圆角 */
  border-radius: 20px;
}
.main {
  position: absolute;
  width: 100%;
  height: 100%;
  background: url("../../assets/img/login.jpg");
  overflow: hidden;
}
/* 标题 */
.login-title {
  text-align: center;
  color: #303133;
  margin-bottom: 20px;
}
</style>