/**
 * 页面启动调用
 */
$(function() {
	$("#txtUserName").html($.cookie(COOKIE_STAFF_NAME_KEY));
	// 显示当前年份
	showThisYear();
});

/**
 * 显示今年年份
 */
function showThisYear() {
	$("#thisYear").html(new Date().getFullYear() + "年");
}

/**
 * 重置上传中标通知书按钮
 */
function resetSelectFile() {
	$("#btnSelectFile").html("上传中标通知书");
}

// 上传工具
var uploader = new plupload.Uploader(
		{
			runtimes : 'html5,flash,silverlight,html4',
			browse_button : 'btnSelectFile',
			container : document.getElementById('formAddProject'),
			multi_selection : false,
			multipart : true,
			url : URL_OSS,
			flash_swf_url : getContextRoot() + '/common/plupload/Moxie.swf',
			silverlight_xap_url : getContextRoot()
					+ '/common/plupload/Moxie.xap',
			init : {
				// 上传绑定事件-添加文件
				FilesAdded : function(uploader, files) {
					plupload.each(files, function(file) {
						$("#btnSelectFile").html(file.name);
						$("#imgUploadMark").attr("class","img_upload_mark_success");
					});
				},
				// 上传进度显示
				UploadProgress : function(uploader, file) {
					waiting(file.percent + "%");
				},
				FileUploaded : function(uploader, file, info) {
					run();
					resetSelectFile();
					if (info.status == 200) {
						if (RETURNCODE_SUCCESS == JSON.parse(info.response).returnCode) {
							alert("创建成功！");
							gotoLocalHtml("/project/project_manage.html");
						} else if (RETURNCODE_FAIL == JSON.parse(info.response).returnCode) {
							alert(JSON.parse(info.response).errorCodeDesc);
						}
					} else if (info.status == 203) {
						alert(info.response);
					} else {
						alert(info.response);
					}
				},
				Error : function(uploader, errObject) {
					run();
					alert(errObject.message);
				}
			}
		});
uploader.init();

/**
 * 创建新项目
 */
function btnCreate() {
	if(!preventRapidClick()){
		return;
	}
	// 校验输入
	var num = $("#num").val();
	if(!verifyStringNotEmpty(num, "项目编号", 32, true)){
		return;
	}
	var name = $("#name").val();
	if(!verifyStringNotEmpty(name, "项目名称", 512, true)){
		return;
	}
	var constructerName = $("#constructerName").val();
	if(!verifyStringNotEmpty(constructerName, "建设单位", 512, true)){
		return;
	}
	var designerName = $("#designerName").val();
	if(!verifyStringNotEmpty(designerName, "设计单位", 512, true)){
		return;
	}
	var superviserName = $("#superviserName").val();
	if(!verifyStringNotEmpty(superviserName, "监理单位", 512, true)){
		return;
	}
	var address = $("#address").val();
	if(!verifyStringNotEmpty(address, "工程地点", 512, true)){
		return;
	}
	var type = $("#type").val();
	if(!verifyStringNotEmpty(type, "工程类型", 32, true)){
		return;
	}
	var scale = $("#scale").val();
	if(!verifyStringNotEmpty(scale, "规模", 1024, true)){
		return;
	}
	var bidPrice = $("#bidPrice").val();
	if(!verifyMoneyGt0(bidPrice, "中标价", true)){
		return;
	}
	var bidDuration = $("#bidDuration").val();
	if (!verifyNotEmpty(bidDuration, "中标工期", true)) {
		return;
	}

	var selectedFile = $("#btnSelectFile").html();
	if (selectedFile == '上传中标通知书') {
		alert("请选择中标通知书！");
		$("#inputSelectFile").val("");
		return;
	}
	var project = {
		num : num,
		name : name,
		constructerName : constructerName,
		designerName : designerName,
		superviserName : superviserName,
		address : address,
		type : type,
		scale : scale,
		bidDuration : bidDuration,
		bidPrice : bidPrice
	}
	ajaxAcquireLetterOfAcceptanceUpToken(project, function(data) {
		uploader.bind('BeforeUpload', function(uploader, file) {
			// 进行参数配置
			new_multipart_params = {
				'key' : data.dir + "/" + $("#num").val().trim() + "/"
						+ data.uuid + "_" + file.name,
				'OSSAccessKeyId' : data.OSSAccessKeyId,
				'policy' : data.policy,
				'success_action_status' : "200",
				'Signature' : data.Signature,
				'x-oss-security-token' : data.securityToken,
				'callback' : data.callback,
				'Content-Disposition':"attachment; filename="+file.name
			};
			uploader.settings.url=data.ossUrl;
			uploader.setOption({
				'multipart_params' : new_multipart_params
			});
			waiting();
		});
		uploader.start();
	}, function(data) {
	}, true, waiting, run, true);
}
