<template>
  <div class="main">
    <!-- 面包屑：导航 -->
    <app-link :name="title"></app-link>

    <!-- 搜索表单 -->
    <el-form :inline="true" :model="searchForm" class="demo-form-inline">
      <el-form-item>
        <el-input v-model="searchForm.name" placeholder="用户名"></el-input>
      </el-form-item>
      <el-form-item>
        <el-select v-model="searchForm.sex" placeholder="性别">
          <el-option label="女" value="女"></el-option>
          <el-option label="男" value="男"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="fetchall" icon="el-icon-search">查询</el-button>
        <el-button type="primary" @click="reset" icon="el-icon-refresh-left">重置</el-button>
        <el-button type="primary" @click="addUser" icon="el-icon-document">新增</el-button>
      </el-form-item>
    </el-form>

    <!-- 用户列表页:border加边框，stripe：斑马纹 -->
    <el-table :data="list" border style="width: 100%" height="500px" stripe>
      <!-- 在表格里面插入一列：复选框 -->
       <!-- <el-table-column type="check" label="选中" width="80">
         <template slot-scope="scope">
           <input type="checkbox" v-model="scope.ischeck" />
         </template>
       </el-table-column> -->
      <el-table-column type="index" label="序号" width="80"></el-table-column>
      <el-table-column prop="username" label="账号" width="120"></el-table-column>
      <el-table-column prop="name" label="真实姓名" width="120"></el-table-column>
      <el-table-column prop="sex" label="性别" width="70"></el-table-column>
      <el-table-column prop="phone" label="手机号码" width="140"></el-table-column>
      <el-table-column prop="birthday" label="出生日期" width="120"></el-table-column>
      <el-table-column label="头像" width="80">
        <template slot-scope="scope">
          <!-- scope指整个数据对象。scope.row指当前行数据 -->
          <img :src="scope.row.pic" alt style="width:50px;height:50px;" />
        </template>
      </el-table-column>
      <el-table-column prop="address" label="地址" width="200"></el-table-column>
      <el-table-column label="操作" width="220">
        <template slot-scope="scope">
          <el-button type="primary" icon="el-icon-edit" @click="editUser(scope.row)">编辑</el-button>
          <el-button type="danger" icon="el-icon-delete" @click="delUser(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
<template slot-scope="scope">
          <input type="checkbox" />全选
        </template>
    <!-- 分页 -->
    <el-pagination
      v-show="pages > 1"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="page"
      :page-sizes="[10, 20, 30, 40]"
      :page-size="pagesize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
    ></el-pagination>

    <!-- 对话框：修改密码 -->
    <el-dialog :title="isAdd ? '添加用户' : '编辑用户'" :visible.sync="dialogFormVisible" width="600px">
      <el-form
        :rules="rules"
        :model="ruleForm"
        label-position="right"
        label-width="100px"
        class="demo-ruleForm"
        ref="ruleForm"
      >
        <el-form-item label="真实姓名" prop="name">
          <el-input
            v-model="ruleForm.name"
            autocomplete="off"
            placeholder="请输入姓名"
            style="width:300px"
          ></el-input>
        </el-form-item>
        <el-form-item label="性别" prop="sex">
          <el-select v-model="ruleForm.sex" placeholder="性别">
            <el-option label="女" value="女"></el-option>
            <el-option label="男" value="男"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="手机号码" prop="phone">
          <el-input
            v-model.trim="ruleForm.phone"
            autocomplete="off"
            style="width:300px"
            placeholder="请输入手机号码"
          ></el-input>
        </el-form-item>
        <el-form-item label="出生日期" prop="birthday">
          <el-date-picker
            v-model="ruleForm.birthday"
            format="yyyy 年 MM 月 dd 日"
            value-format="yyyy-MM-dd"
            type="date"
            placeholder="选择日期"
          ></el-date-picker>
        </el-form-item>
        <el-form-item prop="address" label="地址">
          <el-input
            v-model="ruleForm.address"
            autocomplete="off"
            style="width:300px"
            placeholder="请输入地址"
          ></el-input>
        </el-form-item>
        <el-form-item prop="pic" label="上传头像">
          <el-upload
            class="upload-demo"
            ref="upload"
            action="/dev-api/user/add"
            :file-list="fileList"
            :http-request="httpRequest"
            :on-preview="handlePreview"
            :on-remove="handleRemove"
            :on-change="onChange"
            :before-remove="beforeRemove"
            :on-exceed="handleExceed"
            multiple
            :limit="1"
            name="photos"
            :auto-upload="false"
          >
            <el-button size="small" type="primary">点击上传</el-button>
            <!-- <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div> -->
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="isAdd ? add() : edit()">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import appLink from "@/components/link";
import usersApi from "@/api/usersApi";
export default {
  data() {
    var validatePsw = (rule, value, callback) => {
      /*
        value:该表单的value值
        callback：回调函数，返回提示信息
      */
      let reg = /1[3-9]\d{9}/;
      if (reg.test(value) && value.length == 11) {
        callback();
      } else {
        callback(new Error("请输入正确的手机号码"));
      }
    };

    return {
      title: "用户管理",
      searchForm: {}, //查询条件
      list: [
        //渲染的列表
        // {
        //   _id: "5f1dcb1f3f1eb819289bb899",
        //   username: "Martinez",
        //   name: "赖静",
        //   birthday: "2000-03-13",
        //   sex: "男",
        //   phone: "18400857527",
        //   address: "河北省 廊坊市 香河县",
        //   pic: "http://dummyimage.com/50x50"
        // }
      ],
      // currentPage4: 4,
      page: 1, //当前页
      pagesize: 10, //一页多少条
      total: 0, //总条数
      pages: 0, //总页数
      dialogFormVisible: false, //弹窗开关
      rules: {
        name: [
          { required: true, message: "姓名不能为空", trigger: "blur" } //真实姓名不能为空
        ],
        phone: [
          { required: true, message: "手机号码不能为空", trigger: "blur" }, //用户名不能为空
          { validator: validatePsw, trigger: "blur" } //验证用户名是否存在
        ]
      }, //表单验证规则
      ruleForm: {
        name: "",
        sex: "",
        phone: "",
        birthday: "",
        address: ""
      },
      isAdd: true, //对话框类别初始值 true:添加用户
      fileList: [
        //存储图片的地方：已上传图片
        // {
        //   name: "food.jpeg",
        //   url:
        //     "https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg?imageMogr2/thumbnail/360x360/format/webp/quality/100"
        // }
        // {
        //   name: "food2.jpeg",
        //   url:
        //     "https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg?imageMogr2/thumbnail/360x360/format/webp/quality/100"
        // }
      ]
    };
  },

  components: { appLink },

  methods: {
    //功能：进入页面就发送请求，获取用户列表渲染
    async fetchall() {
      try {
        let p = await usersApi.getList(
          this.page,
          this.pagesize,
          this.searchForm
        );
        // console.log(p.data);
        if (p.data.flag) {
          //查询到了数据
          this.list = p.data.data; //数据部分
          this.total = p.data.total; //总条数
          this.pages = p.data.pages;
        } else {
          this.$message({
            message: "找不到该数据哦，换个条件试试",
            type: "warnning"
          });
        }
      } catch (err) {
        console.log(err);
      }
    },

    //功能：点击重置按钮清空表单数据
    reset() {
      this.searchForm = {};
    },

    //功能：点击打开新增弹窗
    addUser() {
      this.isAdd = true; //添加用户，开关设置为true
      this.dialogFormVisible = true;
      // this.ruleForm = {}; //每次打开弹窗都清空表单:只能清空普通表单内容
      this.fileList = []; //清空图片列表
      this.$nextTick(() => {//延时定时器
        // this.$nextTick()它是一个异步事件，当渲染结束 之后 ，它的回调函数才会被执行
        // 弹出窗口打开之后 ，需要加载Dom, 就需要花费一点时间，我们就应该等待它加载完dom之后，再进行调用resetFields方法，重置表单和清除样式
        this.$refs["ruleForm"].resetFields();
      });
    },

    //功能：点击打开编辑的弹窗
    editUser(row) {
      console.log(row, 999);
      // console.log(row._id, 789);
      this.isAdd = false; //编辑弹窗设置为false
      this.fileList = [];
      this.dialogFormVisible = true; //点击打开弹窗
      this.ruleForm = row;
      let obj = {
        name: this.ruleForm.fileNames,
        url: this.ruleForm.pic
      };
      this.fileList.push(obj); //设置已上传文件的回显
      // usersApi.getuser(row._id).then(res => {
      //   // console.log(res.data);
      //   //http://120.76.247.5:2002/uploads/timg-1595902489886.jpg
      //   this.ruleForm = res.data.data;
      // let obj = {
      //   name: this.ruleForm.fileNames,
      //   url: this.ruleForm.pic
      // };
      // this.fileList.push(obj); //设置已上传文件的回显
      // });
    },

    //功能：点击确定按钮，修改用户信息
    edit() {
      // console.log("修改用户数据");
      this.$refs.upload.submit(); //提交图片
      //正则校验通过：运行上传数据添加新用户
      let form = new FormData();
      form.append("username", this.ruleForm.username);
      form.append("name", this.ruleForm.name);
      form.append("sex", this.ruleForm.sex);
      form.append("phone", this.ruleForm.phone);
      form.append("birthday", this.ruleForm.birthday);
      form.append("address", this.ruleForm.address);
      form.append("_id", this.ruleForm._id);
      // form.append("ruleForm", JSON.stringify(this.ruleForm));

      // console.log(this.fileList);
      this.fileList.forEach(file => {
        //接口那边：req.files
        form.append("files", file.raw); //此处一定是append file.raw 上传文件只需维护fileList file.raw.name要加上
        form.append("fileNames", file.name);
      });

      //把数据以form-data的形式发送给后端
      usersApi.editUser(form).then(res => {
        // console.log(res.data);
        if (res.data.flag) {
          //修改成功
          this.$message({
            message: "修改成功",
            type: "success"
          });

          //关闭弹窗并刷新页面
          this.dialogFormVisible = false;
          this.fetchall();
        } else {
          //修改失败
          this.$message({
            message: "修改失败",
            type: "error"
          });
        }
      });
    },

    //功能：删除一个用户
    delUser(row) {
      // console.log(row._id);
      this.$confirm("此操作将永久删除该用户吗, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          //点击确定：真的删除
          usersApi.delUser(row._id).then(res => {
            if (res.data.flag) {
              //修改成功
              this.$message({
                message: "删除成功",
                type: "success"
              });

              //刷新页面
              this.fetchall();
            } else {
              //修改失败
              this.$message({
                message: "删除失败",
                type: "error"
              });
            }
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除"
          });
        });
    },

    //功能：分页功能1
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`);
      this.pagesize = val; //每页多少条
      this.page = 1; //如果切换每页多少条，回到第一页开始看
      this.fetchall();
    },

    //功能：分页功能2
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
      this.page = val;
      this.fetchall();
    },

    //功能：添加新用户：确定的时候，把内容提交给后台
    add() {
      this.$refs.ruleForm.validate(async res => {
        console.log(res);
        if (res) {
          //手动提交图片的关键
          this.$refs.upload.submit(); //提交图片
          //正则校验通过：运行上传数据添加新用户
          let form = new FormData();
          form.append("name", this.ruleForm.name);
          form.append("sex", this.ruleForm.sex);
          form.append("phone", this.ruleForm.phone);
          form.append("birthday", this.ruleForm.birthday);
          form.append("address", this.ruleForm.address);
          // form.append("pic", this.ruleForm.address);

          console.log(this.fileList);
          this.fileList.forEach(file => {
            form.append("files", file.raw); //此处一定是append file.raw 上传文件只需维护fileList file.raw.name要加上
            form.append("fileNames", file.name);
          });
          // console.log(form);
          try {
            let p = await usersApi.addUser(form);
            console.log(p.data);
            if (p.data.flag) {
              //提交成功
              this.$message({
                message: "添加成功",
                type: "success"
              });
              this.dialogFormVisible = false; //关闭弹窗
            } else {
              this.$message({
                message: "添加失败",
                type: "error"
              });
            }
          } catch (err) {
            console.log(err);
          }
        } else {
          //校验没通过：不能提交
          this.$message({
            message: "注意书写条件哦",
            type: "error"
          });
        }
      });
    },

    //功能：文件上传(上传头像)
    httpRequest(file) {
      // console.log(file, 111);
    },
    onChange(file, fileList) {
      this.fileList = fileList;
    },
    //功能：删除已上传图片
    handleRemove(file, fileList) {
      //删除文件
      // console.log(file, fileList, 222);
      let url = file.url;
      console.log(url, this.ruleForm._id, 789);
      usersApi.delPic(this.ruleForm._id, url).then(res => {
        console.log(res.data, 999);
        if (res.data.flag) {
          this.$message({
            message: "删除成功",
            type: "success"
          });
        } else {
          this.$message({
            message: "删除失败",
            type: "error"
          });
        }
      });
    },
    handlePreview(file) {
      console.log(file, 333);
    },
    //功能：选择图片超过规定提示
    handleExceed(files, fileList) {
      this.$message.warning(
        `当前限制选择 1 个文件，本次选择了 ${
          files.length
        } 个文件，共选择了 ${files.length + fileList.length} 个文件`
      );
    },
    //功能：删除图片前提示
    beforeRemove(file, fileList) {
      return this.$confirm(`确定移除 ${file.name}？`);
    }
  },

  created() {
    this.fetchall(); //进入页面就发起请求
  }
};
</script>

<style>
.el-form {
  margin-top: 20px;
}
.el-pagination {
  text-align: center;
  margin-top: 30px;
}
.el-pagination .el-pager li {
  width: 40px;
  height: 40px;
  line-height: 40px;
  text-align: center;
  background: #ccc;
  margin-right: 5px;
}
.el-pagination .el-pager .active {
  background: #58bc58;
  color: #fff;
  cursor: pointer;
}
</style>