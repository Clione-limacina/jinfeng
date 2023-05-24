<template>
  <div class="header">
    <!-- 头部左侧Logo和标题 -->
    <a href="#/">
      <img class="logo" src="@/assets/img/logo.png" width="35px" />
      <span class="company">金丰会员管理系统</span>
    </a>

    <!-- 下拉菜单 -->
    <el-dropdown @command="handleCommand">
      <span class="el-dropdown-link">
        个人中心
        <i class="el-icon-arrow-down el-icon--right"></i>
      </span>
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item command="edit">修改密码</el-dropdown-item>
        <el-dropdown-item command="logout">退出</el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>

    <!-- 对话框：修改密码 -->
    <el-dialog title="修改密码" :visible.sync="dialogFormVisible" width="450px">
      <el-form
        :rules="rules"
        :model="ruleForm"
        label-position="right"
        label-width="80px"
        class="demo-ruleForm"
        ref="ruleForm"
      >
        <el-form-item label="旧密码" prop="oldpsw">
          <el-input
            v-model="ruleForm.oldpsw"
            autocomplete="off"
            placeholder="请输入你的旧密码"
            style="width:300px"
            type="password"
          ></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="newpsw">
          <el-input
            v-model="ruleForm.newpsw"
            autocomplete="off"
            style="width:300px"
            placeholder="请输入你要设置的新密码"
            type="password"
          ></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="checkpsw">
          <el-input
            v-model="ruleForm.checkpsw"
            autocomplete="off"
            style="width:300px"
            placeholder="请再次输入你要设置的新密码"
            type="password"
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="editpsw">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { logOut, getUser } from "@/utils/auth"; //引入cookie的方法
import usersApi from "@/api/usersApi";
export default {
  data() {
    //功能：验证旧密码是否正确
    var validatePsw = (rule, value, callback) => {
      /*
        value:该表单的value值:密码
        callback：回调函数，返回提示信息
      */
      let psw = this.$md5(value);
      usersApi
        .login(this.userinf.username, psw)
        .then(res => {
          // console.log(res);
          if (res.data.flag) {
            //登陆成功:旧密码是正确的
            callback();
          } else {
            //登陆失败：旧密码不对
            callback(new Error("您的旧密码错误"));
          }
        })
        .catch(err => {
          callback(new Error("服务器繁忙，稍后再试"));
        });
    };
    //功能：验证新密码
    var validatePass = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入密码"));
      } else {
        if (this.ruleForm.checkpsw !== "") {
          this.$refs.ruleForm.validateField("checkpsw");
        }
        callback();
      }
    };
    //功能：验证确认密码
    var validatePass2 = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请再次输入密码"));
      } else if (value !== this.ruleForm.newpsw) {
        callback(new Error("两次输入密码不一致!"));
      } else {
        callback();
      }
    };
    // var validatecheckPsw = (rule, value, callback) => {
    //   /*
    //     value:该表单的value值:确认密码
    //     callback：回调函数，返回提示信息
    //   */
    //   if (value != this.ruleForm.newpsw) {
    //     //相等：校验通过
    //     callback(new Error("密码不一致"));
    //   } else {
    //     callback();
    //   }
    // };
    return {
      dialogFormVisible: false, //对话框的显示状态：false：不显示
      ruleForm: {
        oldpsw: "", //旧密码
        newpsw: "", //新密码
        checkpsw: "" //确认密码
      },
      userinf: {},
      rules: {
        //正则
        oldpsw: [
          { required: true, message: "旧密码不能为空", trigger: "blur" }, //用户名不能为空
          { validator: validatePsw, trigger: "blur" } //验证用户名是否存在
          //   { min: 3, max: 5, message: "长度在 3 到 5 个字符", trigger: "blur" }
        ],
        newpsw: [
          // { required: true, message: "新密码不能为空", trigger: "blur" },
          // {
          //   min: 3,
          //   max: 15,
          //   message: "长度在 3 到 15 个字符",
          //   trigger: "blur"
          // }
          { validator: validatePass, trigger: "blur" }
        ],
        checkpsw: [
          // { required: true, message: "确认密码不能为空", trigger: "blur" },
          { validator: validatePass2, trigger: "blur" }
        ]
      }
    };
  },

  components: {},

  methods: {
    //功能：下拉菜单的触发
    handleCommand(command) {
      // console.log(command);
      if (command == "edit") {
        //修改密码
        this.dialogFormVisible = true;
      } else if (command == "logout") {
        //退出
        // console.log("退出了");
        logOut(); //删除本地cookie的数据
        this.$message({
          message: "退出成功",
          type: "success",
          duration: 2000
        });

        //跳转到登陆页
        this.$router.push("/login");
      }
    },

    //功能：点击确定按钮，确定修改密码
    editpsw() {
      this.$refs.ruleForm.validate(async res => {
        // console.log(res);
        if (res) {
          //表单正则校验通过：发送ajax进行密码的修改
          //获取：uid、username、新密码，传给接口
          try {
            let psw = this.$md5(this.ruleForm.checkpsw);
            let p = await usersApi.editPsw(
              this.userinf.username,
              psw,
              this.userinf.uid
            );
            // console.log(p);
            if (p.data.flag) {
              //修改成功
              this.dialogFormVisible = false;
              this.$message({
                message: "修改成功",
                type: "success"
              });
              //跳到登陆页
              this.$router.push("/login");
              //清除本地存储的用户信息
              logOut();
            } else {
              //修改失败
              this.$message({
                message: "修改失败",
                type: "error"
              });
            }
          } catch (err) {
            console.log(err);
          }
        } else {
          return;
        }
      });
    }
  },

  //进入页面就获取用户信息
  created() {
    let userinf = getUser(); //字符串 {username:xxx,uid:xxx}
    if (userinf) {
      this.userinf = JSON.parse(userinf);
    }
  }

  // beforeRouteUpdate() {

  // },
  // beforeRouteEnter() {

  // },
  // beforeRouteLeave() {

  // }
};
</script>

<style scoped>
.logo {
  vertical-align: middle;
  /* 上 右 下 左 */
  padding: 0 10px 0 40px;
}
.company {
  position: absolute;
  color: white;
}

.el-dropdown-link {
  cursor: pointer;
  color: #fff;
}
.el-icon-arrow-down {
  font-size: 12px;
}
.el-dropdown {
  float: right;
  padding-right: 20px;
}
</style>