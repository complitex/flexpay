
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

    this.responseId = options.responseId;
    this.uploadFormId = options.uploadFormId;
    this.uploadFrameId = options.uploadFrameId;
    this.successResponse = options.successResponse;
    this.errorResponse = options.errorResponse;

    this.mainBlock = $("#" + options.mainBlockId);

    this.stack = new Array();
    this.unsuccess = 0;
    this.success = 0;
    this.total = 0;
    this.uploaded = true;
    this.uploadingId = -1;
    this.started = false;
    this.wait = false;
    this.uploadingFilename = "";
    this.curRetry = 0;

    this.uploadForms = [];
    this.responses = [];
    this.uploadFrames = [];

    var test = "";
    var test1 = null;

    $("input[type=file]").each(function() {
        if (this.form.id == options.formId && test == "") {
            if ($.browser.msie) {
                this.onchange = function() {
                    FPFile.fileForms[options.formId].setFile(this);
                };
            } else {
                this.setAttribute("onchange", "FPFile.fileForms[\"" + options.formId + "\"].setFile(this);");
            }
            test = this.name;
            test1 = $(this);
        }
    });

    this.file = test1;
    this.fileId = test;

    function setFile(form, obj) {
        form.file = $(obj);
    }

    function addNewBlock(form) {

        var index = form.total;

        form.uploadForms[index] = $('<form id="' + form.uploadFormId + index + '" action="' + options.action + '"'
                         + 'method="post" enctype="multipart/form-data" target="' + form.uploadFrameId + index + '"></form>').css({display : "none"});

        $(":input:not(:button):not(:reset):not(:image):not(:file):not(:submit):not(:disabled)").each(function() {
            if (this.form.id == options.formId) {
                form.uploadForms[index].append($('<input type="hidden" name="' + this.name + '" />').val(this.value));
            }
        });
        var fileBlock = form.file.parent();
        form.uploadForms[index].append(form.file);

        var fileNew = form.file.clone();
        fileBlock.append(fileNew);
        form.file.attr("id", form.fileId + index);
        form.file = fileNew;

        form.responses[index] = $('<div id="' + form.responseId + index + '"></div>').css({color : "#ff0000"});
        form.uploadFrames[index] = $('<iframe id="' + form.uploadFrameId + index + '" name="' + form.uploadFrameId + index + '"></iframe>').css({display: "none"});

        form.mainBlock.append(form.uploadForms[index]).append(form.responses[index]).append(form.uploadFrames[index]);

    }

    this.setFile = function(obj) {
        return setFile(this, obj);
    };

    this.submitForm = function() {

        if (!options.validate()) {
            return false;
        }

        this.stack.push(this.total);
        addNewBlock(this);
        eraseForm(this);

        if (!this.uploaded || (this.uploaded && this.wait)) {
            var fileValue = $("#" + this.fileId + this.total).val();
            var index = fileValue.lastIndexOf("\\") + ($.browser.msie ? 1 : 0);
            $("#" + this.responseId + this.total).text(FP.formatI18nMessage(FPFile.constants.statusWaiting, [fileValue.substring(index)]));
        }
        this.total++;

        f(this);

    };

    function eraseForm(form) {
        form.file.val("");
    }

    function upload() {
        var form = FPFile.fileForms[options.formId];
        if (form.uploaded && !form.wait) {
            form.uploaded = false;
            form.uploadingId = form.stack[0];
            var fileValue = $("#" + form.fileId + form.uploadingId).val();
            var index = fileValue.lastIndexOf("\\") + ($.browser.msie ? 1 : 0);
            form.uploadingFilename = fileValue.substring(index);
            Array.remove(form.stack, 0);
            $("#" + form.uploadFormId + form.uploadingId).submit();
            setTimeout(getProgress, 1000);
        }
    }

    function getProgress() {
        var form = FPFile.fileForms[options.formId];
        $.post(FPFile.constants.progressBarUrl, {},
                function (data, textStatus) {
                    if (textStatus == form.successResponse && data != null && data != "") {
                        var ajaxResponse = $("#" + form.responseId + form.uploadingId).
                                text(FP.formatI18nMessage(FPFile.constants.statusUploading, [form.uploadingFilename, data]));
                        if (data == "100") {
                            form.uploaded = true;
                            form.wait = true;
                            ajaxResponse.text(FP.formatI18nMessage(FPFile.constants.statusProcessing, [form.uploadingFilename]));
                            form.curRetry = 0;
                            setTimeout(uploadWait, 100);
                            return true;
                        }
                        setTimeout(getProgress, 1000);
                    }
                    return false;
                });
    }

    function waitFunction() {
        var form = FPFile.fileForms[options.formId];
        if (!form.wait) {
            return;
        }
        form.curRetry++;
        var frame = window.frames[form.uploadFrameId + form.uploadingId];
        if (frame != null) {
            var bodyFrame = frame.document.body;
            var ajaxResponse = $("#" + form.responseId + form.uploadingId);
            if (form.curRetry <= options.retries) {
                if (bodyFrame != null && bodyFrame.innerHTML != null && bodyFrame.innerHTML != "") {
                    if (bodyFrame.innerHTML == form.successResponse) {
                        ajaxResponse.css({color: "#008000"}).text(FP.formatI18nMessage(FPFile.constants.statusUploaded, [form.uploadingFilename]));
                        form.success++;
                        form.wait = false;
                    } else {
                        ajaxResponse.text(FP.formatI18nMessage(FPFile.constants.statusError, [form.uploadingFilename]));
                        form.wait = false;
                        form.unsuccess++;
                    }
                }
            }
        }
        if (form.stack.length == 0 && !form.wait) {
            form.started = false;
            window.onbeforeunload = "";
        }
        uploadWait();
    }

    function uploadWait() {
        var form = FPFile.fileForms[options.formId];
        if (form.stack.length > 0) {
            if (form.wait) {
                setTimeout(waitFunction, 1500);
            } else {
                upload();
            }
        } else {
            if (form.wait) {
                setTimeout(waitFunction, 1500);
            }
        }
    }

    function f(form) {
        if (!form.started) {
            form.started = true;
            window.onbeforeunload = closeIt(form);
            upload();
        }
    }

    function closeIt() {
        var form = FPFile.fileForms[options.formId];
        return FP.formatI18nMessage(FPFile.constants.confirmExit, [form.success, form.unsuccess, form.total - form.success - form.unsuccess - 1]);
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
        if ($.browser.msie) {
            $("#" + submitId).get(0).onclick = function() {
                FPFile.fileForms[formId].submitForm();
            };
        } else {
            $("#" + submitId).attr("onclick", "FPFile.fileForms[\"" + formId + "\"].submitForm();");
        }
    }

};
