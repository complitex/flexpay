
function FPFileUploadForm(formId, options) {

    options = options || {};

    options.formId = formId;

    options = $.extend({
        action : "",
        mainBlockId : "mainBlock",
        responseId : "ajaxResponse",
        uploadFormId : "uploadForm",
        uploadFrameId : "uploadFrame",
        successResponse : "success",
        errorResponse : "error",
        retries : 5,
        validate : function() {
            return true;
        }
    }, options);

    this.options = options;

    responseId = options.responseId;
    uploadFormId = options.uploadFormId;
    uploadFrameId = options.uploadFrameId;
    successResponse = options.successResponse;
    errorResponse = options.errorResponse;

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

    uploadForms = [];
    responses = [];
    uploadFrames = [];

    fileId = "";

    $('input[type="file"]').each(function(i, el) {
        if (el.form.id == options.formId && fileId == "") {
            el.setAttribute("onchange", "FPFile.fileForms[\"" + formId + "\"].setFile(this);")
            fileId = el.name;
            file = $(this);
        }
    });

    function setFile(obj) {
        file = $(obj);
    }

    function addNewBlock(index) {

        uploadForms[index] = $('<form id="' + uploadFormId + index + '" action="' + options.action + '"'
                         + 'method="post" enctype="multipart/form-data" target="' + uploadFrameId + index + '"></form>').css({display : "none"});

        $(":input:not(:button):not(:reset):not(:image):not(:file):not(:submit):not(:disabled)").each(function(i, el) {
            if (el.form.id == options.formId) {
                uploadForms[index].append($('<input type="hidden" name="' + el.name + '" />').val(el.value));
            }
        });
        var fileBlock = file.parent();
        uploadForms[index].append(file);

        var fileNew = file.clone();
        fileBlock.append(fileNew);
        file.attr("id", fileId + index);
        file = fileNew;

        responses[index] = $('<div id="' + responseId + index + '"></div>').css({color : "#ff0000"});
        uploadFrames[index] = $('<iframe id="' + uploadFrameId + index + '" name="' + uploadFrameId + index + '"></iframe>').css({display: "none"})

        mainBlock.append(uploadForms[index]).append(responses[index]).append(uploadFrames[index]);

    }

    this.setFile = function(obj) {
        return setFile(obj);
    }

    this.submitForm = function() {

        if (!options.validate()) {
            return false;
        }

        stack.push(total);
        addNewBlock(total);
        eraseForm();

        if (!uploaded || (uploaded && wait)) {
            var fileValue = $("#" + fileId + total).val();
            var index = fileValue.lastIndexOf("\\") + ($.browser.msie ? 1 : 0);
            $("#" + responseId + total).text(FP.formatI18nMessage(FPFile.constants.statusWaiting, [fileValue.substring(index)]));
        }
        total++;

        f();

    }

    function eraseForm() {
        file.val("");
    }

    function upload() {
        if (uploaded && !wait) {
            uploaded = false;
            uploadingId = stack[0];
            var fileValue = $("#" + fileId + uploadingId).val();
            var index = fileValue.lastIndexOf("\\") + ($.browser.msie ? 1 : 0);
            uploadingFilename = fileValue.substring(index);
            Array.remove(stack, 0);
            $("#" + uploadFormId + uploadingId).submit();
            setTimeout(getProgress, 1000);
        }
    }

    function getProgress() {
        $.post(FPFile.constants.progressBarUrl, {},
                function (data, textStatus) {
                    if (textStatus == successResponse && data != null && data != "") {
                        var ajaxResponse = $("#" + responseId + uploadingId).
                                text(FP.formatI18nMessage(FPFile.constants.statusUploading, [uploadingFilename, data]));
                        if (data == "100") {
                            uploaded = true;
                            wait = true;
                            ajaxResponse.text(FP.formatI18nMessage(FPFile.constants.statusProcessing, [uploadingFilename]));
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
                        ajaxResponse.css({color: "#008000"}).text(FP.formatI18nMessage(FPFile.constants.statusUploaded, [uploadingFilename]));
                        success++;
                        wait = false;
                    } else {
                        ajaxResponse.text(FP.formatI18nMessage(FPFile.constants.statusError, [uploadingFilename]));
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
        return FP.formatI18nMessage(FPFile.constants.confirmExit, [success, unsuccess, total - success - unsuccess - 1]);
    }

}

var FPFile = {

    constants: {
        progressBarUrl: "",
        statusWaiting: "",
        statusUploading: "",
        statusProcessing: "",
        statusUploaded: "",
        statusError: "",
        confirmExit: ""
    },

    fileForms: [],

    createFileUploadForm : function(formId, submitId, options) {
        this.fileForms[formId] = new FPFileUploadForm(formId, options);
        $("#" + submitId).attr("onclick", "FPFile.fileForms[\"" + formId + "\"].submitForm();");
    }

};
