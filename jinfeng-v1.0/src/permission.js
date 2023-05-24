//路由守卫

import router from '@/router';//引入router对象
import { getToken } from '@/utils/auth';//cookie的方法
import usersApi from '@/api/usersApi';//验证token ajax
/*
权限校验：
    通过router路由前置钩子函数 beforeEach() ，
    在跳转路由前进行拦截判断是否已经登录，
    如果已登录，则进行路由跳转，如果没有则回到登录页
*/
router.beforeEach(async (to, from, next) => {
    /*
            to:即将要进入的路由，路由对象
            from:准备离开的路由，路由对象
            next：是否能进入下一个钩子
        */
    // console.log(to, 111);
    // console.log(from, 222);
    // next();
    // if (to.path == '/login' || to.path == '/reg') {
    if (to.meta.permission == false) {
        //注册或登录页面:不需要拦截
        next();
    } else {
        //需要拦截
        let token = getToken();
        if (token) {
            //有token：发送token给后端校验
            try {
                let p = await usersApi.checkToken(token);
                if (p.data.flag) {
                    //校验通过
                    next();
                } else {
                    next('/login');
                }
            } catch (err) {
                next('/login');
            }
        } else {
            //没有token:跳回登陆页
            next('/login');//相当于 this.$router.push('/login')
        }
    }
})


