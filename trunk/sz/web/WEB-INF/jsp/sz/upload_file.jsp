
<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<s:form id="inputForm">
    <table cellspacing="2" cellpadding="2" width="80%">
        <tr>
            <td width="30%" nowrap="nowrap">
                <s:text name="year" />&nbsp;<s:select id="year" name="year"
                          required="true"
                          list="#{(curYear - 1):(curYear - 1),curYear:curYear}"
                          value="curYear" />
            </td>
            <td width="30%" nowrap="nowrap">
                <s:text name="month" />&nbsp;<s:select id="month" name="month" list="months" value="curMonth" required="true" />
            </td>
            <td align="right" nowrap="nowrap">
                <s:text name="sz.oszn" />&nbsp;<s:select id="osznId" name="osznId" list="osznList" listKey="id" listValue="description" required="true" />
            </td>
        </tr>
        <tr>
            <td colspan="3" id="fileRaw">
                <s:text name="common.file" />&nbsp;<s:file id="upload" name="upload" label="File" required="true" size="75" />
            </td>
        </tr>
    </table>
    <input id="uploadBtn" type="button" value="<s:text name="common.add_to_upload" />" class="btn-exit" />
</s:form>

<div id="mainBlock"></div>

<script type="text/javascript">

    function FPFileUploadForm(formId, submitId, options) {

        options = options || {};

        options.formId = formId;

        options = $.extend({
            action : "",
            mainBlockId : "mainBlock",
            fileBlockId : "fileRaw",
            fileId : "upload",
            retries : 5
        }, options);

        this.options = options;
        fileBlock = $("#" + options.fileBlockId);

        mainBlock = $("#" + options.mainBlockId);

        file = null;

        stack = new Array();
        unsuccess = 0;
        success = 0;
        total = 0;
        uploaded = true;
        uploadingId = -1;
        started = false;
        wait = false;
        uploadingFilename = "";
        curRetry = 0;

        responseId = "ajaxResponse";
        uploadFormId = "uploadForm";
        uploadFrameId = "uploadFrame";
        fileId = "upload";

        successResponse = "success";
        errorResponse = "error";

        uploadForms = [];
        responses = [];
        uploadFrames = [];

        $("#" + submitId).attr("onclick", "FPFileUtils.fileForms[\"" + formId + "\"].submitForm();");
        $("#" + options.fileId).attr("onchange", "FPFileUtils.fileForms[\"" + formId + "\"].setFile(this);");

        function setFile(obj) {
            file = $(obj);
        }

        function addNewBlock(index) {

            uploadForms[index] = $("<form id=\"" + uploadFormId + index + "\" action=\"" + options.action + "\""
                             + "method=\"post\" enctype=\"multipart/form-data\" target=\"" + uploadFrameId + index + "\"></form>").css({display : "none"});

            $(":input:not(:button):not(:reset):not(:image):not(:file):not(:submit):not(:disabled)").each(function(i, el) {
                if (el.form.id == options.formId) {
                    uploadForms[index].append($("<input type=\"hidden\" name=\"" + el.name + "\" />").val(el.value));
                }
            });
            uploadForms[index].append(file);

            fileBlock.append(file.clone());
            file.attr("id", fileId + index);

            responses[index] = $("<div id=\"" + responseId + index + "\"></div>").css({color : "#ff0000"});
            uploadFrames[index] = $("<iframe id=\"" + uploadFrameId + index + "\" name=\"" + uploadFrameId + index + '"></iframe>').css({"display": "none"})

            mainBlock.append(uploadForms[index]).append(responses[index]).append(uploadFrames[index]);

        }

        this.setFile = function(obj) {
            return setFile(obj);
        }

        this.submitForm = function() {

            stack.push(total);
            addNewBlock(total);
            eraseForm();

            if (!uploaded || (uploaded && wait)) {
                var fileValue = $("#" + fileId + total).val();
                var index = fileValue.lastIndexOf("\\") + ($.browser.msie ? 1 : 0);
                $("#" + responseId + total).text(
                        "<s:text name="common.file" /> \"" + fileValue.substring(index) + "\": <s:text name="common.file_upload.progress_bar.waiting" />"
                        );
            }
            total++;

            f();

        }

        function eraseForm() {
            $("#" + fileId).val("");
        }

        function upload() {
            if (uploaded && !wait) {
                uploaded = false;
                uploadingId = stack[0];
                var fileValue = $("#" + fileId + uploadingId).val();
                var index = fileValue.lastIndexOf("\\") + ($.browser.msie ? 1 : 0);
                uploadingFilename = "<s:text name="common.file" /> \"" + fileValue.substring(index) + "\": ";
                Array.remove(stack, 0);
                $("#" + uploadFormId + uploadingId).submit();
                setTimeout(getProgress, 1000);
            }
        }

        function getProgress() {
            $.post("<s:url action="fileUploadProgress" namespace="/common" includeParams="none" />", {},
                    function (data, textStatus) {
                        if (textStatus == successResponse && data != null && data != "") {
                            var ajaxResponse = $("#" + responseId + uploadingId).
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

        function waitFunction() {
            if (!wait) {
                return;
            }
            curRetry++;
            var frame = window.frames[uploadFrameId + uploadingId];
            if (frame != null) {
                var bodyFrame = frame.document.body;
                var ajaxResponse = $("#" + responseId + uploadingId);
                if (curRetry <= options.retries) {
                    if (bodyFrame != null && bodyFrame.innerHTML != null && bodyFrame.innerHTML != "") {
                        if (bodyFrame.innerHTML == successResponse) {
                            ajaxResponse.css({"color": "#008000"}).text(uploadingFilename + "<s:text name="common.file_upload.progress_bar.loaded" />");
                            success++;
                            wait = false;
                        } else if (bodyFrame.innerHTML == errorResponse) {
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

    }

    var FPFileUtils = {

        fileForms : [],

        createFileUploadForm : function(formId, submitId, options) {
            var fileForm = new FPFileUploadForm(formId, submitId, options);
            this.fileForms[formId] = fileForm;
        }

    };

    $(function() {
        FPFileUtils.createFileUploadForm("inputForm", "uploadBtn", {
            action : "<s:url action="doSzFileUploadAjax" namespace="/sz" includeParams="none" />"
        })
    });

</script>
