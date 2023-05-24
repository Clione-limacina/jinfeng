//存放所有关于用户的接口
import request from '@/utils/request';

//调试代理是否成功
// request.get('/user/checkname', {
//     params: {
//         username: 'malin'
//     }
// })

export default {
    //功能:验证用户名是否存在
    checkName(username) {
        return request.get('/user/checkname', {
            params: {
                username
            }
        })
    },

    //功能：注册
    reg(username, password) {
        return request.post('/user/reg', {
            username,
            password
        })
    },

    //功能：登陆
    login(username, password) {
        return request.get('/user/login', {
            params: {
                username,
                password
            }
        })
    },

    //功能：修改密码
    editPsw(username, password, uid) {
        return request.put('/user/edit/' + uid, {
            username,
            password
        })
    },

    //功能:校验token
    checkToken(token) {
        return request.get('/user/verify', {
            params: {
                token,
            }
        })
    },

    //功能：获取用户列表
    getList(page, pagesize, search) {
        return request.get('/user/list', {
            params: {
                page,//当前第n页
                pagesize,//一页显示xx条
                search //查询条件
            }
        })
    },

    //功能：添加新用户
    addUser(form) {
        console.log(form, 999);
        return request({
            method: 'post',
            url: '/user/add',
            headers: { 'Content-Type': 'multipart/form-data' },
            data: form
        })
    },

    //功能：查询某个用户信息
    getuser(id) {
        return request.get('/user/getuser/' + id);
    },

    //功能：编辑用户信息
    editUser(form) {
        return request({
            method: 'put',
            url: '/user/editinf',
            headers: {
                'Content-Type': 'multipart/form-data',
                // 'Authorization' : 'xxxx'
            },
            data: form
        })
    },

    //功能：删除图片
    delPic(id, url) {
        return request({
            method: 'delete',
            url: '/user/delimg',
            data: {
                id,
                url
            }
        })
    },

    //功能：删除用户
    delUser(id) {
        return request({
            method: 'delete',
            url: '/user/del/' + id
        })
    }
}