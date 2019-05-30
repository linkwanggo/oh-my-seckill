// 存放主要的交互逻辑JS代码
// js 模块化

phonePattern = /^1[34578]\d{9}$/;

var seckill = {
    // 封装秒杀ajax的URL
    URL: {
        now: function () {
            return "/seckill/time/now";
        },
        exposer: function (seckillId) {
            return "/seckill/" + seckillId + "/exposer";
        },
        execution: function (seckillId, md5) {
            return "/seckill/" + seckillId + "/" + md5 + "/execution";
        }
    },
    // 手机号验证逻辑
    validatePhone: function(phone) {
        return phonePattern.test(phone);
    },
    // 执行秒杀
    handleSeckill: function(seckillId, node) {

        node.hide().html("<button class='btn btn-primary btn-lg' id='killBtn'>开始秒杀</button>");
        // 获取秒杀url
        $.post(seckill.URL.exposer(seckillId), {}, function (result) {
            if (result && result["success"]) {
                var data = result['data'];
                if (data["exposed"]) {
                    // 秒杀已开启
                    var md5 = data["md5"];
                    var killUrl = seckill.URL.execution(seckillId, md5);
                    // 为按钮绑定点击事件
                    $("#killBtn").one("click", function () {
                        // 执行秒杀请求
                        //1. 先禁用按钮
                        $(this).addClass('disable');
                        // 发送秒杀请求
                        $.post(killUrl, {}, function (result) {
                            if (result && result["success"]) {
                                var data = result["data"];
                                var state = data["state"];
                                var stateInfo = data["stateInfo"];
                                //显示秒杀结果
                                node.html("<span class='label label-success'>" + stateInfo + "</span>");
                            }
                        })
                    })
                } else {
                    // 秒杀未开启
                    var startTime = data["start"];
                    var endTime = data["end"];
                    var nowTime = data["now"];
                    seckill.countdown(seckillId, nowTime, startTime, endTime);
                }
            }
        });
        node.show();
    },
    // 秒杀时间计时
    countdown: function(seckillId, nowTime, startTime, endTime) {
        console.log(seckillId + '_' + nowTime + '_' + startTime + '_' + endTime);
        var seckillBox = $("#countdown");
        // 时间判断
        if (nowTime > endTime) {
            // 秒杀结束
            seckillBox.html("秒杀结束!")
        } else if (nowTime < startTime) {
            // 秒杀未开始 显示秒杀开始时间倒计时
            var killTime = new Date(startTime + 1000);
            seckillBox.countdown(killTime, function (event) {
                // 时间格式转化
                var format = event.strftime("秒杀倒计时：%D天 %H时 %M分 %S秒");
                seckillBox.html(format);
            }).on("finish.countdown", function () {
                // 获取秒杀地址 控制显示逻辑 执行秒杀
                seckill.handleSeckill(seckillId, seckillBox);
            })
        } else {
            // 秒杀开始
            seckill.handleSeckill(seckillId, seckillBox);
        }
    },
    // 详情页秒杀逻辑
    detail: {
        init: function (params) {
            // 手机验证和登录 计时交互
            // 规划我们的交互流程
            // 在cookie中查找手机号
            var killPhone = $.cookie("killPhone");
            // 验证cookie中的手机号是否合法
            if (!seckill.validatePhone(killPhone)) {
                // 弹出手机号输入框
                var killPhoneModal = $("#killPhoneModal");
                killPhoneModal.modal({
                    show: true, // 显示弹出层
                    backdrop: "static", // 禁止鼠标点击其他位置关闭
                    keyboard: false // 关闭键盘事件
                });
                $("#killPhoneBtn").click(function () {
                    var inputPhone = $("#killPhoneKey").val();
                    if (seckill.validatePhone(inputPhone)) {
                        // 将手机号写入cookie
                        $.cookie("killPhone", inputPhone, {expires: 7, path:"/seckill"});
                        // 刷新页面
                        window.location.reload();
                    } else {
                        $("#killPhoneMessage").hide().html("<label>手机号错误</label>").show(200);
                    }
                })
            }
            // 已经登录
            // 开始秒杀交互
            var seckillId = params["seckillId"];
            var startTime = params["startTime"];
            var endTime = params["endTime"];
            $.get(seckill.URL.now(), {}, function (result) {
                if (result && result['success']) {
                    var nowTime = result['data'];
                    seckill.countdown(seckillId, nowTime, startTime, endTime);
                }
            })
        }
    }
};