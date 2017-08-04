/*需要jquery支持*/

/**
 * 缓冲-等待
 */
function waiting() {
	$(".loading").css("display", "block");
}

/**
 * 缓冲-等待
 */
function waiting(hint) {
	$(".loading").contents().find("#hint").html(hint);
	$(".loading").css("display", "block");
}

/**
 * 缓冲结束-正常运行
 */
function run() {
	$(".loading").contents().find("#hint").html("");
	$(".loading").css("display", "none");
}

