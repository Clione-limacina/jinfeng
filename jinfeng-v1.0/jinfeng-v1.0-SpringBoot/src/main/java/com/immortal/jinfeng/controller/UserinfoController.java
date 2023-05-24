package com.immortal.jinfeng.controller;

import com.alibaba.fastjson2.JSONObject;
import com.immortal.jinfeng.entity.Userinfo;
import com.immortal.jinfeng.service.UserinfoService;
import com.immortal.jinfeng.util.CustomizeEntity;
import com.immortal.jinfeng.util.R;
import com.immortal.jinfeng.util.RandomGenerationTool;
import org.apache.catalina.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserinfoController {

    @Resource
    UserinfoService userinfoService;


//    private String localhostImageStoragePath = "F:/jinfeng";
    private String localhostImageStoragePath = "/home/jinfeng/image";
//    private String domainName = "127.0.0.1:8080";
    private String domainName = "8.134.102.99:8080";
    private String prefixURL = "http://" + this.domainName + "/jinfeng";

    //tomcat文件的映射地址,如果要修改这东西，则需要同步修改配置类WebMvcConfig中的映射参数（可以用配置文件抽出去）
    private String serverImageStoragePath = "/jinfeng-topImg/";


    //验证用户名是否存在
    @GetMapping("/checkname")
    public R checkName(String username){
        if(userinfoService.checkName(username)){
            return R.code(3000).message("用户已存在，不允许注册");
        }
        return R.ok().message("允许注册");
    }

    //注册
    @PostMapping("/reg")
    public R reg(@RequestBody Map<String, String> requestBody){
        String username = requestBody.get("username");
        String password = requestBody.get("password");

        if(username == null ||
                (username != null && !username.matches(".{1,20}")) ) {
            return R.code(3000).message("账号的长度应为1~20个字符");
        }else if(password == null){
            return R.code(3000).message("密码不能为空");
        }

        int code = userinfoService.reg(username,password);
        if(code == 2){
            return R.code(3000).message("用户已存在，不允许注册");
        }else if(code == 0){
            return R.code(4000).message("注册失败，请联系后端开发人员");
        }else{
            return R.ok().message("注册成功");
        }
    }

    //登陆
    @GetMapping("/login")
    public R login(String username, String password){
        if(username == null ||
                (username != null && !username.matches(".{1,20}")) ) {
            return R.code(3000).message("账号的长度应为1~20个字符");
        }else if(password == null){
            return R.code(3000).message("密码不能为空");
        }

        Userinfo userinfo = userinfoService.getUserinfoByUsername(username);
        if(userinfo == null){
            return R.code(3000).message("该用户不存在！");
        }else if(!password.equals(userinfo.getPassword())){
            return R.code(3000).message("密码错误");
        }else{
            //登陆成功，生成token
            String token = RandomGenerationTool.getNoWhipptreeUUID();
            //token持久化，实现客户端用cookie操作免登录功能
            userinfo.setToken(token);
            if(userinfoService.updateUserinfoByUid(userinfo) != 1){
                return R.code(4000).message("token持久化失败！请联系后端开发人员");
            }
            return R.ok().message("登陆成功")
                    .put("uid",userinfo.getUid())
                    .put("token",token);
        }

    }

    //验证token是否准确
    @GetMapping("/verify")
    public R verify(String token){
        if(token == null){
            return R.code(3000).message("参数错误！不能为空");
        }

        if(userinfoService.verify(token)){
            return R.ok().message("校验通过");
        }else{
            return R.code(3000).message("校验失败");
        }

    }

    //修改用户的密码
    @PutMapping("/edit/{uid}")
    public R edit(@PathVariable("uid") Long uid,@RequestBody Userinfo userinfo){
        userinfo.setUid(uid);
        if(userinfoService.updateUserinfoByUid(userinfo) == 1){
            return R.ok().message("修改成功'");
        }else{
            return R.code(3000).message("修改失败");
        }

    }

    //获取用户列表
    @GetMapping("/list")
    public R list( int page, int pagesize, String search ) throws ParseException {

        //分页查询用户列表
        JSONObject searchObject = JSONObject.parseObject(search);
        String sex = (String) searchObject.get("sex");
        String name = (String) searchObject.get("name");
        List<Userinfo> data = userinfoService.getUserinfoList(page,pagesize,sex, name);
        List<Map> resultData = new LinkedList<>();
        for (Userinfo item : data) {
            Map resultDataItem = new CustomizeEntity().changeMap(item);
            resultDataItem.put("_id",resultDataItem.get("uid"));
            resultDataItem.remove("uid");
            Date birthday = (Date) resultDataItem.get("birthday");
            if(birthday != null){
                resultDataItem.put("birthday",new SimpleDateFormat("yyyy-MM-dd").format( birthday ));
            }
            resultData.add(resultDataItem);
        };

//        System.out.println(data);

        //查询一共注册了多少个用户
        int total = userinfoService.getUserAmount();

        //一共有多少页
        int pages = (total + 1) / pagesize;
        pages = pages == 0 ? 1 : pages;

        return R.ok().message("允许注册")
                .put("page",page)
                .put("pagesize",pagesize)
                .put("pages",pages)
                .put("total",total)
                .put("data",resultData);
    }

    //添加一个用户(图片上传保存的部分代码是重复的，应该抽离作为一个函数，这部分将在v2版本完成)(初始密码都为123456)（用户命名防止访问的逻辑将在v2版本完善，可能采取特殊字符识别的方式）
    @PostMapping("/add")
    public R addUser(HttpServletRequest request) throws ParseException {

        //参数获取
        Userinfo userinfo = new Userinfo();
        String username = null;
        boolean checkName = true;
        while (checkName){
            username = "新用户-" + System.currentTimeMillis();
            checkName = userinfoService.checkName(username);
        }
        userinfo.setUsername(username);
        userinfo.setPassword("e10adc3949ba59abbe56e057f20f883e");
        userinfo.setName(request.getParameter("name"));
        userinfo.setSex(request.getParameter("sex"));
        userinfo.setPhone(request.getParameter("phone"));
        String birthday = request.getParameter("birthday");
        if(birthday != null && birthday.length() > 8){
            userinfo.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse( birthday ));
        }
        userinfo.setAddress( request.getParameter("address"));

        MultipartHttpServletRequest req = (MultipartHttpServletRequest) request;
        MultipartFile multipartFile = req.getFile("files");
        // 获取上传文件原始名
        String originalName = "https://dummyimage.com/50x50";
        String fileNames = request.getParameter("fileNames") == null ? "默认头像" : request.getParameter("fileNames");
        try{
            if(multipartFile != null){
                originalName = multipartFile.getOriginalFilename();
            }

            String headPortraitUrl = multipartFile != null ? this.saveTopImage(fileNames, originalName, multipartFile) : originalName;
            //调用服务进行用户的添加
            userinfo.setPic(headPortraitUrl);//把图片地址加进去
            userinfo.setFileNames(fileNames);//把图片的名字加进去
            int code = userinfoService.addUser(userinfo);
            if(code == 1){
                return R.ok().message("添加用户成功!");
            }else if(code == 2){
                return R.code(3000).message("用户已存在，不允许添加");
            }else{
                return R.code(4000).message("图片已保存到本地,但用户新的信息存入数据库失败!请联系开发人员");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.code(3000).message("服务器保存图片失败!请联系开发人员.");
        }

    }



    //编辑用户信息(并不符合RESTful接口风格)
    @PutMapping("/editinf")
    public R editUser(HttpServletRequest request) throws ParseException {
        Userinfo userinfo = new Userinfo();

        userinfo.setUid(Long.valueOf(request.getParameter("_id")));
        userinfo.setUpdateDate(new Date(System.currentTimeMillis()));
        userinfo.setName(request.getParameter("name"));
        userinfo.setSex(request.getParameter("sex"));
        userinfo.setPhone(request.getParameter("phone"));
        String birthday = request.getParameter("birthday");
        if(birthday != null && birthday.length() > 8){
            userinfo.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse( birthday ));
        }
        userinfo.setAddress( request.getParameter("address"));

        MultipartHttpServletRequest req = (MultipartHttpServletRequest) request;
        MultipartFile multipartFile = req.getFile("files");
        // 获取上传文件原始名
        String originalName = "https://dummyimage.com/50x50";
        String fileNames = request.getParameter("fileNames") == null || multipartFile == null ? "默认头像" : request.getParameter("fileNames");
        try{
            if(multipartFile != null){
                originalName = multipartFile.getOriginalFilename();
            }

            String headPortraitUrl = multipartFile != null ? this.saveTopImage(fileNames, originalName, multipartFile) : originalName;

            //调用服务进行用户的修改
            userinfo.setPic(headPortraitUrl);//把图片地址加进去
            userinfo.setFileNames(fileNames);//把头像的名字加进去
            //调用服务进行更新
            int code = userinfoService.updateUserinfoByUid(userinfo);
            if(code == 1){
                return R.ok().message("修改用户成功!");
            }else if(code == 0){
                return R.code(3000).message("用户不存在，不允许编辑");
            }else{
                return R.code(4000).message("图片已保存到本地,但用户新的信息存入数据库出现错误!请联系开发人员");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.code(3000).message("服务器保存图片失败!请联系开发人员.");
        }
    }

    //删除用户
    @DeleteMapping("/del/{uid}")
    public R del(@PathVariable("uid") Long uid){
        if(userinfoService.del(uid) == 1){
            return R.ok().message("删除成功");
        }else{
            return R.code(3000).message("用户不存在，删除失败");
        }
    }

    //删除图片
    @DeleteMapping("/delimg")
    public R delimg(@RequestBody String requestBodyStr){
        JSONObject requestBodyObj = JSONObject.parseObject(requestBodyStr);

        Long uid = Long.valueOf( requestBodyObj.get("id").toString() );
        String url = requestBodyObj.get("url").toString();

        if(!"https://dummyimage.com/50x50".equals(url)){
            //不是默认头像，则执行删除
            Userinfo userinfo = new Userinfo();
            userinfo.setUid(uid);
            userinfo.setFileNames("默认头像");
            userinfo.setPic("https://dummyimage.com/50x50");
            String filePath = this.localhostImageStoragePath + "/" + url.substring(this.prefixURL.length() + this.serverImageStoragePath.length());
            if(new File(filePath).delete()){
                //物理文件删除成功，对数据库进行修改
                if(userinfoService.updateUserinfoByUid(userinfo) == 1){
                    return R.ok().message("删除图片成功");
                }else{
                    return R.code(4000).message("数据库操作失败！请联系后端开发人员");
                }
            }else{
                return R.code(40000).message("物理文件删除失败！请联系后端开发人员");
            }
        }

        return R.ok().message("您并没有上传头像，无需删除");

    }

    //查询某个用户信息
    @GetMapping("/getuser/{uid}")
    public R getuser(@PathVariable("uid") Long uid){
        Userinfo userinfo = userinfoService.getUserinfoByUid(uid);
        if(userinfo == null){
            return R.code(3000).message("该用户不存在！");
        }else{
            return R.ok().message("查询成功")
                    .put("data",userinfo);
        }
    }

    /**
     * @Author 作者名
     * @Date 2023/5/23
     * @function 将图片保存在本地
     * @param String fileNames 自定义的图片名称，由客户端提供
     * @param String originalName 原始文件名(带后缀)
     * @param MultipartFile multipartFile 工具
     * @return java.lang.String //返回图片的地址，可直接用src引用的那种
     **/
    public String saveTopImage(String fileNames, String originalName,MultipartFile multipartFile) throws IOException {
        //使用UUID重新命名上传的文件名 + 文件的后缀名
        String newFileName = fileNames + RandomGenerationTool.getNoWhipptreeUUID() + originalName.substring(originalName.lastIndexOf("."));
        //设置上传图片的保存地址,也就是保存在本地的哪个地方
        String realPath = this.localhostImageStoragePath;//F:/campus_image_storage
        //设置上传文件的访问地址,也就是可以通过url访问到的图片地址,拼接参数为请求前缀 + 资源地址在tomcat的映射值 + 文件名
        String headPortraitUrl = this.prefixURL + this.serverImageStoragePath + newFileName;
        File dir = new File(realPath);
        // 如果保存文件的地址不存在，就先创建目录
        if (!dir.exists()) {
            dir.mkdir();
        }
        File file = new File(realPath, newFileName);
        // 使用multipartFile接口的方法上传文件到指定位置
        multipartFile.transferTo(file);
        return headPortraitUrl;
    }

}
