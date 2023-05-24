//引入request对象
// import request from '../utils/request';//相对路径
import request from '@/utils/request';//js后缀可以省略
let baseurl = process.env.VUE_APP_BASE_API;
//发起请求，前面一定要写一段代理的路径
request({
    method: 'get',
    url: baseurl + '/db/sport.json'//我在8888端口，访问8001的数据，存在跨域
}).then(res => {
    console.log(res.data, 444);
}).catch(err => {
    console.log(err);
});

request({
    method: 'get',
    url: baseurl + '/db/tech.json'//我在8888端口，访问8001的数据，存在跨域
}).then(res => {
    console.log(res.data, 555);
}).catch(err => {
    console.log(err);
});

//获取开发环境下的常量
console.log(process.env.VUE_APP_PORT, 999);
console.log(process.env.VUE_APP_SERVICE_URL);