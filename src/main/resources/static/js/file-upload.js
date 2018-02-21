$(function() {
    // 批量导入按钮
    $("#batchImportBtn").click(function(){
        $('#batchImportModal').modal('show');
    })
    var base = $("#base").val().trim();
    // 上传按钮
    $("#batchUploadBtn").attr('disabled', true);
    // 上传文件按钮点击的时候
    $("#batchUploadBtn").click(function() {
        // 进度条归零
        $("#progressBar").width("0%");
        // 上传按钮禁用
        $(this).attr('disabled', true);
        // 进度条显示
        $("#progressBar").parent().show();
        $("#progressBar").parent().addClass("active");
        // 上传文件
        UpladFile();
    })

    // 文件修改时
    $("#batchFile").change(function() {
        $("#batchUploadBtn").val("上传");
        $("#progressBar").width("0%");
        var file = $(this).prop('files');
        if (file.length != 0) {
            $("#batchUploadBtn").attr('disabled', false);
        }

    });

    function UpladFile() {
        var fileObj = $("#batchFile").get(0).files[0]; // js 获取文件对象
        console.info("上传的文件："+fileObj);
        var FileController = base + "/admin/user/upload"; // 接收上传文件的后台地址
        // FormData 对象
        var form = new FormData();
        // form.append("author", "hooyes"); // 可以增加表单数据
        form.append("file", fileObj); // 文件对象
        // XMLHttpRequest 对象
        var xhr = new XMLHttpRequest();
        xhr.open("post", FileController, true);
        xhr.onload = function() {
            // ShowSuccess("上传完成");
            alert("上传完成");
            $("#batchUploadBtn").attr('disabled', false);
            $("#batchUploadBtn").val("上传");
            $("#progressBar").parent().removeClass("active");
            $("#progressBar").parent().hide();
            //$('#myModal').modal('hide');
        };
        xhr.upload.addEventListener("progress", progressFunction, false);
        xhr.send(form);
    }
    ;
    function progressFunction(evt) {
        var progressBar = $("#progressBar");
        if (evt.lengthComputable) {
            var completePercent = Math.round(evt.loaded / evt.total * 100)+ "%";
            progressBar.width(completePercent);
            $("#batchUploadBtn").val("正在上传 " + completePercent);
        }
    };
}