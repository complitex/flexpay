
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
        uuid : "",
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
    this.waitRetry = 0;

    this.uploadForms = [];
    this.responses = [];
    this.uploadFrames = [];

    this.uuid = "";
    this.nginx = false;

    var test = "";
    var test1 = null;

    $("input[type='file']").each(function() {
        if (this.form.id == options.formId && test == "") {
            test = this.name;
            test1 = $(this).change(function() {
                FPFile.fileForms[options.formId].setFile(this);
            });
        }
    });

    this.file = test1;
    this.fileId = test;

    function setFile(form, obj) {
        form.file = $(obj);
    }

    function addNewBlock(form) {

        var index = form.total;

        var uuid = "";
        for (i = 0; i < 32; i++) { uuid += Math.floor(Math.random() * 16).toString(16); }
        form.uuid = uuid;

        form.uploadForms[index] = $('<form id="' + form.uploadFormId + index + '" action="' + options.action + '?X-Progress-ID=' + uuid + '"'
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
                function(data, textStatus) {
                    if (data == "" && textStatus == "success") {
                        window.location.href = FP.base;
                    }
                    console.log('Success responce');
                    if (textStatus == form.successResponse && data != null && data != "") {
                        var ajaxResponse;
                        if (data == "Wait") {
                            /*
                            $.ajax({
                                type: "GET",
                                url: "/progress?X-Progress-ID=" + form.uuid,
                                dataType: "json",
                                success: function(data) {
                                    console.debug("upload=%s", data);
                                    if (data.state == 'uploading') {
                                        console.debug("uploading");
                                        data.percents = Math.floor((upload.received / upload.size)*1000)/10;
                                        ajaxResponse = $("#" + form.responseId + form.uploadingId).
                                            text(FP.formatI18nMessage(FPFile.constants.statusUploading, [form.uploadingFilename, data.percents]));
                                    }
                                }
                            });
                            */
                            $.get("/progress", {"X-Progress-ID" : form.uuid},
                                function(data, status) {
                                    console.debug("upload=%s", data);
                                    var upload = $.parseJSON(data.substring(1, data.length - 4));
                                    console.debug("upload2=%s", upload.state);
                                    if (status == "success" && upload.state == 'uploading') {
                                        form.nginx = true;
                                        upload.percents = Math.floor((upload.received / upload.size)*1000)/10;
                                        //console.debug("uploading s%/%s=%s", upload.received, upload.size, );
                                        ajaxResponse = $("#" + form.responseId + form.uploadingId).
                                            text(FP.formatI18nMessage(FPFile.constants.statusUploading, [form.uploadingFilename, upload.percents]));
                                    }
                                }
                            );
                          if (!form.nginx) {
                              ajaxResponse = $("#" + form.responseId + form.uploadingId).
                                        text(FP.formatI18nMessage(FPFile.constants.statusWaitingTime, [form.uploadingFilename, ++form.waitRetry]));
                          }
                        } else if (!form.nginx || data == "100") {
                            ajaxResponse = $("#" + form.responseId + form.uploadingId).
                                    text(FP.formatI18nMessage(FPFile.constants.statusUploading, [form.uploadingFilename, data]));
                        }
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
        statusWaitingTime: "",
        statusUploading: "",
        statusProcessing: "",
        statusUploaded: "",
        statusError: "",
        confirmExit: ""
    },

    fileForms: [],

    createFileUploadForm : function(formId, submitId, options) {
        this.fileForms[formId] = new FPFileUploadForm(formId, options);
        $("#" + submitId).click(function() {
            FPFile.fileForms[formId].submitForm();
        });
    }

};
