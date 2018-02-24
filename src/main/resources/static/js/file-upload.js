$(function() {
    //上传文件按钮
    $("#uploadBtn").click(function() {
        // 进度条归零
        $("#progressBar").width("0%");
        // 上传按钮禁用
        $("#uploadBtn").attr("disabled", true);
        // 进度条显示
        $("#progressBar").parent().show();
        $("#progressBar").parent().addClass("active");
        upload("带进度条的文件上传");
    })
    function refreshBtn() {
        setTimeout(function() {
            $("#uploadBtn").text("上传文件");
            $("#uploadBtn").removeAttr("disabled");
        },
        1500);
    }
    function upload(name) {
        var formData = new FormData();
        formData.append('file', $('#file')[0].files[0]);
        formData.append('name', name);
        function onprogress(evt) {
            // 写要实现的内容
            var progressBar = $("#progressBar");
            if (evt.lengthComputable) {
                var completePercent = Math.round(evt.loaded / evt.total * 100);
                progressBar.width(completePercent + "%");
                $("#progressBar").text(completePercent + "%");
            }
        }
        var xhr_provider = function() {
            var xhr = jQuery.ajaxSettings.xhr();
            if (onprogress && xhr.upload) {
                xhr.upload.addEventListener('progress', onprogress, false);
            }
            return xhr;
        };
        $.ajax({
            url: '/upload',
            type: 'POST',
            cache: false,
            data: formData,
            processData: false,
            contentType: false,
            xhr: xhr_provider,
            success: function(data) {
                console.info(data);
                result = $.parseJSON(data);
                if (result.code == "0") {
                    $("#uploadBtn").text("上传成功");
                    setTimeout(function() {
                        $("#uploadBtn").text("上传文件");
                    },
                    1000);
                } else if (result.code == "-4") {
                    $("#uploadBtn").text("不支持的文件类型");
                } else {
                    $("#uploadBtn").text(result.data);
                }
                // 进度条隐藏
                $("#progressBar").parent().hide();
                refreshBtn();
            },
            error: function(data) {
                console.info(data);
                refreshBtn();
            }
        })
    }
})