/**
 * hybrid方案公共js
 * @authors huangh (work.hugo.huang@gmail.com)
 * @date    2016-03-03 14:58:18
 * @version v0.1
 */
(function(window) {
    //发布时关闭debug开关
    var DEBUG = true;

    var callbacks = {};

    //标识
    var guid = 0;

    /**
     * 方便在各个平台中看到完整的 log
     */
    function log() {
        if (DEBUG) {
            console.log.call(console, Array.prototype.join.call(arguments, ' '));
        }
    }

    //判断平台
    var ua = navigator.userAgent;
    var ANDROID = /android/i.test(ua);
    var IOS = /iphone|ipad/i.test(ua);

    /**
     * 平台相关的 Web 与 Native 单向通信方法
     */
    function invoke(cmd) {
        log('invoke', cmd);

        if (ANDROID) {
            //由android webview拦截
            prompt(cmd);
        } else if (IOS) {
            //ios webview拦截
            location.href = 'bridge://' + cmd;
        } else {
            // statement
        }
    }

    var Bridge = {
        callByJS: function(opt) {
            log('callByJS', JSON.stringify(opt));

            //组装jsonobject
            var input = {};
            input.name = opt.name;
            input.token = ++guid;
            input.param = opt.param || {};

            //保存回调
            callbacks[input.token] = opt.callback;

            invoke(JSON.stringify(input));
        },

        callByNative: function(opt) {
            log('callByNative', JSON.stringify(opt));

            var callback = callbacks[opt.token]; //获取对应的回调方法

            var script = opt.script || '';
            var ret = opt.ret || {};

            //native主动调用web
            if (script) {
                log('callByNative script', script);

                try {
                    invoke(JSON.stringify({
                        token: opt.token,
                        ret: eval(script)
                    }));
                } catch (e) {
                    console.error(e);
                }
            } else if (callback) { //native把结果返回给web
                callback(ret);

                try {
                    delete callback;
                    log(callbacks);
                } catch (e) {
                    console.error(e);
                }
            }
        }
    }

    //文档声明全局变量
    window.Bridge = Bridge;
    window.__log = log;

    log('Bridge.js', 'init finish.');
})(window)
