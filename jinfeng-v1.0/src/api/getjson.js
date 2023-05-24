//引入request对象
// import request from '../utils/request';//相对路径
import request from '@/utils/request';//js后缀可以省略

request({
    method: 'get',
    url: '/dev-api/db/sport.json'//我在8888端口，访问8001的数据，存在跨域
}).then(res => {
    console.log(res.data, 444);
}).catch(err => {
    console.log(err);
});

//api/config/list

request({
    method: 'get',
    url: '/weibo/api/config/list'//我在8888端口，访问8001的数据，存在跨域
}).then(res => {
    console.log(res.data, 555);
}).catch(err => {
    console.log(err);
});

//api/v1/topics
//发起请求:localhost:8888/dev-node/api/v1/topics
//目标接口：https://cnodejs.org/api/v1/topics
//代理：1、替换主机：https://cnodejs.org/dev-node/api/v1/topics
//2、替换路径：https://cnodejs.org/api/v1/topics
request({
    method: 'get',
    url: '/dev-node/api/v1/topics'//我在8888端口，访问8001的数据，存在跨域
}).then(res => {
    console.log(res.data, 666);
}).catch(err => {
    console.log(err);
});