<%@ taglib prefix="s" uri="/struts-tags" %>

<div id="mainBlock">

    <s:form id="inputForm" onsubmit="return false;">
        <table cellspacing="2" cellpadding="2" width="80%">
            <tr>
                <td nowrap="nowrap" id="fileRaw">
                    <s:text name="eirc.file" />&nbsp;<s:file id="" name="upload" label="File" required="true" size="75" onchange="setFile(this);" />
                </td>
            </tr>
        </table>
        <input id="uploadBtn" type="button" value="<s:text name="common.add_to_upload" />" class="btn-exit" onclick="submitForm();" />
    </s:form>

    <div id="mainBlock">
        <div id="copyBlock">
            <div id="uploadDiv" style="display:none;">
                <form id="uploadForm" action="<s:url action="doSpFileUploadAjax" namespace="/eirc" includeParams="none" />"
                      method="POST" enctype="multipart/form-data" target="uploadFrame" onsubmit="return false;">
                </form>
            </div>
            <div id="ajaxResponse" style="color:#ff0000;"></div>
        </div>
    </div>

</div>

<script type="text/javascript">

    var mainBlock = "mainBlock";
    var copyBlock = "copyBlock";
    var file = null;

    var unsuccess = 0;
    var success = 0;
    var total = 0;

    function setFile(obj) {
        file = obj;
    }

    function addBlock() {

        var block = $(copyBlock);

        var clone = block.cloneNode(true);
        clone.id = copyBlock + total;
        $(mainBlock).appendChild(clone);

        var uploadForm = $$("form[id=uploadForm]")[1];
        uploadForm.id = "uploadForm" + total;
        uploadForm.target = "uploadFrame" + total;

        var inputForm = $("inputForm");
        uploadForm.appendChild(file);
        $("fileRaw").appendChild(file.cloneNode(true));

        var ajaxResponse = $$("div[id=ajaxResponse]")[1];
        ajaxResponse.id = "ajaxResponse" + total;
        ajaxResponse.innerHTML = "";

        var iframe;
        if (Prototype.Browser.IE) {
            iframe = document.createElement('<iframe id="uploadFrame' + total + '" name="uploadFrame' + total + '"></iframe>');
        } else {
            iframe = document.createElement("iframe");
            iframe.id = "uploadFrame" + total;
            iframe.name = "uploadFrame" + total;
        }
        iframe.style.display = "none";
        $("mainBlock").appendChild(iframe);

        total++;
    }

    function eraseForm() {
        $("inputForm").elements["upload"].value = "";
    }

    function validateForm() {
        var fileEl = $("inputForm").elements["upload"];
        if (fileEl.value == null || fileEl.value == "") {
            alert("<s:text name="common.file.upload.error.empty_file_field" />");
            return false;
        }
        return true;
    }

    function newXMLHttpRequest() {
        var xmlreq = false;
        if (window.XMLHttpRequest) {
            xmlreq = new XMLHttpRequest();
        } else if (window.ActiveXObject) {
            try {
                xmlreq = new ActiveXObject("Msxml2.XMLHTTP");
            } catch (e1) {
                try {
                    xmlreq = new ActiveXObject("Microsoft.XMLHTTP");
                } catch (e2) {
                }
            }
        }

        return xmlreq;
    }

    var xmlHttp = newXMLHttpRequest();
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
            var fileValue = $("uploadForm" + (total - 1)).elements["upload"].value;
            var index = fileValue.lastIndexOf("\\") + (Prototype.Browser.IE ? 1 : 0);
            $("ajaxResponse" + (total - 1)).innerHTML = "<s:text name="common.file" /> \"" + fileValue.substring(index)
                    + "\": <s:text name="common.file_upload.progress_bar.waiting" />";
        }
        if (!started) {
            started = true;
            upload();
            started = false;
        }

    }

    var uploadingFilename;

    function upload() {
        if (uploaded && !wait) {
            uploaded = false;
            uploadingId = stack[0];
            var fileValue = $("uploadForm" + uploadingId).elements["upload"].value;
            var index = fileValue.lastIndexOf("\\") + (Prototype.Browser.IE ? 1 : 0);
            uploadingFilename = "<s:text name="common.file" /> \"" + fileValue.substring(index) + "\": ";
            stack.remove(0);
            $("uploadForm" + uploadingId).submit();
            setTimeout(getProgress, 1000);
        }
    }

    function getProgress() {
        xmlHttp.open("POST", "<s:url action="fileUploadProgress" namespace="/common" includeParams="none" />", true);
        xmlHttp.onreadystatechange = updatePage;
        xmlHttp.send(null);
    }

    var retries = 5;
    var curRetry = 0;

    function waiting() {
        if (!wait) {
            return;
        }
        curRetry++;
        var frame = window.frames["uploadFrame" + uploadingId];
        if (frame != null) {
            var bodyFrame = frame.document.body;
            var ajaxResponse = $("ajaxResponse" + uploadingId);
            if (curRetry <= retries) {
                if (bodyFrame != null && bodyFrame.innerHTML != null && bodyFrame.innerHTML != "") {
                    if (bodyFrame.innerHTML == "success") {
                        ajaxResponse.style.color = "#008000";
                        ajaxResponse.innerHTML = uploadingFilename + "<s:text name="common.file_upload.progress_bar.loaded" />";
                        success++;
                        wait = false;
                    } else if (bodyFrame.innerHTML == "error") {
                        ajaxResponse.innerHTML = uploadingFilename + "<s:text name="common.file_upload.progress_bar.error" />";
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
                setTimeout(waiting, 1500);
            } else {
                upload();
            }
        } else {
            if (wait) {
                setTimeout(waiting, 1500);
            }
        }
    }

    function updatePage() {
        if (xmlHttp.readyState == 4 && xmlHttp.responseText != null && xmlHttp.responseText != "") {
            var ajaxResponse = $("ajaxResponse" + uploadingId);
            ajaxResponse.innerHTML = uploadingFilename + "<s:text name="common.file_upload.progress_bar.loading" /> " + xmlHttp.responseText + "%";
            if (xmlHttp.responseText == "100") {
                uploaded = true;
                wait = true;
                ajaxResponse.innerHTML = uploadingFilename + "<s:text name="common.file_upload.progress_bar.processing" />";
                curRetry = 0;
                setTimeout(uploadWait, 100);;
                return true;
            }
            setTimeout(getProgress, 1000);
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
