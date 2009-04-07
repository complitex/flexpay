
<%@ taglib prefix="s" uri="/struts-tags" %>

<div id="mainBlock">

    <s:form id="inputForm" onsubmit="return false;">
        <table cellspacing="2" cellpadding="2" width="80%">
            <tr>
                <td nowrap="nowrap" id="fileRaw">
                    <s:text name="eirc.file" />&nbsp;<s:file id="upload" name="upload" label="File" required="true" size="75" onchange="setFile($(this));" />
                </td>
            </tr>
        </table>
        <input id="uploadBtn" type="button" value="<s:text name="common.add_to_upload" />" class="btn-exit" onclick="submitForm();" />
    </s:form>

    <div id="mainBlock"></div>

</div>

<script type="text/javascript">

    var file = null;

    var unsuccess = 0;
    var success = 0;
    var total = 0;

    function setFile(obj) {
        file = obj;
    }

    function setNewBlock(index) {
        return '<form id="uploadForm' + index + '" action="<s:url action="doSpFileUploadAjax" namespace="/eirc" includeParams="none" />"'
                + 'method="POST" enctype="multipart/form-data" target="uploadFrame' + index + '" style="display:none;">\n'
                + '</form>\n'
                + '<div id="ajaxResponse' + index + '" style="color:#ff0000;"></div>\n';
    }

    function addBlock() {

        $("#mainBlock").append(setNewBlock(total));

        var uploadForm = $("#uploadForm" + total).append(file);

        $("#fileRaw").append(file.clone());
        file.attr("id", "upload" + total);
        $("#mainBlock").append(
                $('<iframe id="uploadFrame' + total + '" name="uploadFrame' + total + '"></iframe>').css({"display": "none"})
        );

    }

    function eraseForm() {
        $("#upload").val("");
    }

    function validateForm() {
        var v = $("#upload").val();
        if (v == null || v == "") {
            alert("<s:text name="common.file.upload.error.empty_file_field" />");
            return false;
        }
        return true;
    }

    var uploaded = true;
    var stack = new Array();
    var uploadingId = -1;
    var started = false;
    var wait = false;

    function submitForm() {

        if (!validateForm()) {
            return;
        }

        stack.push(total);
        addBlock();
        eraseForm();

        if (!uploaded || (uploaded && wait)) {
            var fileValue = $("#upload" + total).val();
            var index = fileValue.lastIndexOf("\\") + ($.browser.msie ? 1 : 0);
            $("#ajaxResponse" + total).text(
                    "<s:text name="common.file" /> \"" + fileValue.substring(index) + "\": <s:text name="common.file_upload.progress_bar.waiting" />"
                    );
        }
        total++;

        f();

    }

    var uploadingFilename;

    function upload() {
        if (uploaded && !wait) {
            uploaded = false;
            uploadingId = stack[0];
            var fileValue = $("#upload" + uploadingId).val();
            var index = fileValue.lastIndexOf("\\") + ($.browser.msie ? 1 : 0);
            uploadingFilename = "<s:text name="common.file" /> \"" + fileValue.substring(index) + "\": ";
            Array.remove(stack, 0);
            $("#uploadForm" + uploadingId).submit();
            setTimeout(getProgress, 1000);
        }
    }

    function getProgress() {
        $.post("<s:url action="fileUploadProgress" namespace="/common" includeParams="none" />", {},
                function (data, textStatus) {
                    if (textStatus == "success" && data != null && data != "") {
                        var ajaxResponse = $("#ajaxResponse" + uploadingId).
                                text(uploadingFilename + "<s:text name="common.file_upload.progress_bar.loading" /> " + data + "%");
                        if (data == "100") {
                            uploaded = true;
                            wait = true;
                            ajaxResponse.text(uploadingFilename + "<s:text name="common.file_upload.progress_bar.processing" />");
                            curRetry = 0;
                            setTimeout(uploadWait, 100);;
                            return true;
                        }
                        setTimeout(getProgress, 1000);
                    }
                    return false;
                });
    }

    var retries = 5;
    var curRetry = 0;

    function waitFunction() {
        if (!wait) {
            return;
        }
        curRetry++;
        var frame = window.frames["uploadFrame" + uploadingId];
        if (frame != null) {
            var bodyFrame = frame.document.body;
            var ajaxResponse = $("#ajaxResponse" + uploadingId);
            if (curRetry <= retries) {
                if (bodyFrame != null && bodyFrame.innerHTML != null && bodyFrame.innerHTML != "") {
                    if (bodyFrame.innerHTML == "success") {
                        ajaxResponse.css({"color": "#008000"}).text(uploadingFilename + "<s:text name="common.file_upload.progress_bar.loaded" />");
                        success++;
                        wait = false;
                    } else if (bodyFrame.innerHTML == "error") {
                        ajaxResponse.text(uploadingFilename + "<s:text name="common.file_upload.progress_bar.error" />");
                        wait = false;
                        unsuccess++;
                    }
                }
            }
        }
        if (stack.length == 0 && !wait) {
            started = false;
            window.onbeforeunload = "";
        }
        uploadWait();
    }

    function uploadWait() {
        if (stack.length > 0) {
            if (wait) {
                setTimeout(waitFunction, 1500);
            } else {
                upload();
            }
        } else {
            if (wait) {
                setTimeout(waitFunction, 1500);
            }
        }
    }

    function f() {
        if (!started) {
            started = true;
            window.onbeforeunload = closeIt;
            upload();
        }
    }

    function closeIt() {
        return FP.formatI18nMessage("<s:text name="common.file_upload.confirm_exit" />", [success, unsuccess, total - success - unsuccess - 1]);
    }

</script>
