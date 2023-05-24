//封装cookie的方法
let tokenkey = 'jinfeng-mms-token';
let userkey = 'jinfeng-user';

function setCookie(key, value, iDay) {
    //key:键名   value：键值    iDay：失效时间
    //document.cookie = 'name=malin;expires=20190527;path=/';
    var now = new Date();
    now.setDate(now.getDate() + iDay); //iDay:5天后失效， -1：立即失效
    if (iDay) {
        document.cookie = key + '=' + value + ';expires=' + now + ';path=/';
    } else {
        document.cookie = key + '=' + value + ';path=/';
    }

}

function getCookie(key) {
    //获取cookie值
    var str = document.cookie;//name=malin; psw=123456
    var arr = str.split('; '); //[name=malin,psw=123456]
    for (var ele of arr) {
        var arr2 = ele.split('='); //[name,malin]
        if (key == arr2[0]) {
            return arr2[1];
        }
    }
}

function removeCookie(key) {
    //删除cookie。把这个值变成失效
    setCookie(key, '', -1); //设置成过去的时间
}

//设置token
export function setToken(value, days) {
    //value:传入的token值;days:保留的天数
    setCookie(tokenkey, value, days);
}

//设置用户名
export function setUser(value, days) {
    //value:传入的用户信息;days:保留的天数
    setCookie(userkey, value, days);
}

//获取token
export function getToken() {
    return getCookie(tokenkey);
}

//获取用户名
export function getUser() {
    return getCookie(userkey);
}

//退出
export function logOut() {
    removeCookie(tokenkey);
    removeCookie(userkey);

}