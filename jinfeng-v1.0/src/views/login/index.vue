<template>
  <!-- 登陆页组件 -->
  <div class="main">
    <div class="login-form">
      <h2 class="login-title">金丰会员管理系统-登陆</h2>
      <el-form
        :model="ruleForm"
        :rules="rules"
        ref="ruleForm"
        label-width="100px"
        class="demo-ruleForm"
      >
        <el-form-item label="用户名" prop="name">
          <el-input v-model="ruleForm.name"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="ruleForm.password" type="password"></el-input>
        </el-form-item>
        <!-- 有些组件给他绑定事件不成功的时候，记得要加.native修饰符:作用就是把事件绑定到组件渲染的元素身上 -->
        <el-form-item prop="keep">
          <!-- <input type="checkbox" @click="keep()" name="" id=""> -->
          <el-checkbox v-model="ruleForm.checked" @click.native="keep($event)">保留7天免登陆</el-checkbox>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="submitForm('ruleForm')" style="float:right;">登陆</el-button>
          <el-button @click="gotoReg" style="float:right;margin-right:5px">注册</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import usersApi from "@/api/usersApi"; //引入接口
import { setToken, setUser } from "@/utils/auth"; //引入cookie设置的相关方法
export default {
  data() {
    return {
      ruleForm: {
        name: "",
        password: "",
        checked: false
      },
      rules: {
        //正则
        name: [
          { required: true, message: "用户名不能为空", trigger: "blur" }
          //   { min: 3, max: 5, message: "长度在 3 到 5 个字符", trigger: "blur" }
        ],
        password: [{ required: true, message: "密码不能为空", trigger: "blur" }]
      }
    };
  },

  components: {},

  methods: {
    //功能：登陆
    submitForm(formName) {
      this.$refs[formName].validate(async valid => {
        if (valid) {
          // alert("submit!");
          //验证通过，可以发送请求
          try {
            let psw = this.$md5(this.ruleForm.password);
            let p = await usersApi.login(this.ruleForm.name, psw);
            // console.log(p.data);
            if (p.data.flag) {
              //登陆成功:把token和用户信息存到本地(cookie)，跳转到首页
              this.$message({
                message: "登陆成功",
                type: "success"
              });

              //存储token和用户信息到本地
              let userinf = {
                username: this.ruleForm.name,
                uid: p.data.uid
              };
              if (this.ruleForm.checked) {
                //需要保持7天

                setToken(p.data.token, 7);
                setUser(JSON.stringify(userinf), 7);
              } else {
                //存成会话级别:关掉浏览器就删除
                setToken(p.data.token);
                setUser(JSON.stringify(userinf));
              }

              //跳转到首页
              this.$router.push("/home");
            } else {
              //登陆失败
              this.$message({
                message: "登陆失败",
                type: "error"
              });
            }
          } catch (err) {}
        } else {
          // console.log("error submit!!");
          this.$message({
            message: "服务器问题",
            type: "error"
          });
          return false;
        }
      });
    },
    gotoReg() {
      this.$router.push("/reg");
    },

    //功能：解决复选框的坑
    keep(ev) {
      if (ev.target.tagName == "INPUT") return;
      console.log(123);
      if (!this.ruleForm.checked) {
        this.$message({
          message: "请不要在公共场合使用该功能",
          type: "error"
        });
      }
    }
  },

  watch: {
    // checked: {
    //   handler: function(newval) {
    //     console.log(newval);
    //     //保留7天免登陆的提示框
    //     if (!newval.checked) {
    //       this.$message({
    //         message: "请不要在公共场合使用该功能",
    //         type: "error"
    //       });
    //     }
    //   }
    // }
  },

  created() {
    // console.log(this.$route);
    if (this.$route.query) {
      this.ruleForm.name = this.$route.query.name;
    }
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