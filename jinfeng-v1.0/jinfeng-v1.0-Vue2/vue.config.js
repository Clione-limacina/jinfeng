module.exports = {
    devServer: {
        port: Number(process.env.VUE_APP_PORT), // 默认：8080，端口号，如果端口号被占用，会自动提升1
        host: "localhost", //主机名， 127.0.0.1， 真机 0.0.0.0
        https: false, //协议
        open: false, //启动服务时自动打开浏览器访问
        proxy: {
            // '/dev-api': {
            [process.env.VUE_APP_BASE_API]: {
                target: process.env.VUE_APP_SERVICE_URL,//要代理的目标服务器地址
                changOrigin: true,//是否要代理
                pathRewrite: { //接口路径：http://localhost:8001/db/sport.json
                    //真实路径：http://localhost:8888/api/db/sport.json
                    //过程：http://localhost:8001/api/db/sport.json -> http://localhost:8001/db/sport.json
                    // '^/dev-api': '/'
                    ['^' + process.env.VUE_APP_BASE_API]: '/'
                }
            },
            '/weibo': {
                target: 'https://m.weibo.cn/',//要代理的目标服务器地址
                changOrigin: true,//是否要代理
                pathRewrite: { //接口路径：https://m.weibo.cn/api/config/list
                    //真实路径：http://localhost:8888/weibo/api/config/list
                    //过程：http://localhost:8001/weibo/api/config/list -> http://localhost:8001/db/sport.json
                    '^/weibo': '/'
                }
            },
            //https://cnodejs.org/api/v1/topics
            '/dev-node': {
                target: 'https://cnodejs.org',//要代理的目标服务器地址
                changOrigin: true,//是否要代理
                pathRewrite: {
                    '^/dev-node': '/'
                }
            }
        },
    },
    lintOnSave: false, // 关闭格式检查
    productionSourceMap: false, // 打包时不会生成 .map 文件，加快打包速度

}

// let c = 'class';
// let num = 37;
// let obj = {
//     [c]: 'h52002',
//     total: num
// }