//发送请求
import request from '@/utils/request';//后缀是js可以省略

//方法1
request.get('http://localhost:8888/data.json').then(res => {
    console.log(res.data, 111);
}).catch(err => {
    console.log(err);
});

//方法2
request({
    method: 'get',
    url: 'http://localhost:8888/data.json'
}).then(res => {
    console.log(res.data, 222);
}).catch(err => {
    console.log(err);
});

//方法3：工作用的方法
export default {
    getData() {
        return request({ //发起请求得到一个promise对象
            method: 'get',
            url: 'http://localhost:8888/data.json'
        });
    },
    addUser() {

    },
    deleId(id) {

    },
    editId(id) {

    }
}