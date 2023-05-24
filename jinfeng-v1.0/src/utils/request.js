//二次封装axios
import axios from 'axios';
// 按需导入 ElementUI 组件
import { Loading, Message } from 'element-ui'
import router from '@/router/index';

//封装
let request = axios.create({//request==axios
    baseURL: '/jinfeng/jinfeng',//基础路径
    timeout: 3000,//请求超时时间：3s；如果3s后都没有响应，我就断开请求
    //工作之后：一般发请求需要带token过去:token如果不设置是没有响应的
    // headers:{'Authorization' : 'sdadasfugsajgajaf6576a5af5af6asf'}
})

// 加载数据时打开和关闭动效对象
const loading = {
    loadingInstance: null,  // Loading实例
    open: function () { // 打开加载
        console.log('加载中', this.loadingInstance)
        if (this.loadingInstance === null) { // 创建单例, 防止切换路由重复加载
            console.log('创建加载实例..')
            this.loadingInstance = Loading.service({
                text: '拼命加载中',
                target: '.main', // 效果显示区域
                background: 'rgba(0, 0, 0, 0.5)' // 加载效果
            })
        }
    },
    close: function () { // 关闭加载
        if (this.loadingInstance !== null) {
            this.loadingInstance.close()
            console.log('结束加载')
        }
        this.loadingInstance = null // 关闭后实例重新赋值null, 下次让它重新创建
    }
}

// 请求拦截器
request.interceptors.request.use(config => {
    loading.open() // 打开加载效果
    return config
}, error => {
    // 出现异常
    loading.close() // 关闭加载效果
    return Promise.reject(error);
})
// 响应拦截器
request.interceptors.response.use(response => {
    loading.close() // 关闭加载效果
    return response
}, error => {
    loading.close() // 关闭加载效果
    console.log('response error', error.response.status);//404-找不到页面；500；505
    let status = error.response.status;//获取状态码
    //对异常做统一处理(所有的ajax都有这个效果)
    if (status == 404) {
        // Message({
        //     message: '找不到该页面',
        //     type: 'error'
        // })

        // this.$router.push()
        router.push('/404');
    } else if (status == 500) {
        // Message({
        //     message: '服务器异常，稍后再试',
        //     type: 'error'
        // })
        router.push('/500');
    }
    return Promise.reject(error);
})


//get请求
// axios.get('/user');// /user  /api/user

export default request;//封装好就导出request
