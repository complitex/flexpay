<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<div id="mainBlock">

    <s:form id="inputForm" onsubmit="return false;">
        <table cellpadding="3" cellspacing="1" border="0" width="100%">
            <tr valign="top" class="cols_1">
                <td class="col">
                    <s:text name="tc.rules_file.name"/>:
                </td>
                <td class="col">
                    <s:iterator value="names">
                        <s:set name="l" value="%{getLang(key)}" />
                        <s:textfield name="names[%{key}]" value="%{value}" />(<s:if test="%{#l.default}">*</s:if><s:property value="%{getLangName(#l)}" />)<br />
                    </s:iterator>
                </td>
            </tr>
            <tr valign="top" class="cols_1">
                <td class="col">
                    <s:text name="tc.rules_file.description"/>:
                </td>
                <td class="col">
                    <s:iterator value="descriptions">
                        <s:set name="l" value="%{getLang(key)}" />
                        <s:textfield name="descriptions[%{key}]" value="%{value}" />(<s:property value="%{getLangName(#l)}" />)<br />
                    </s:iterator>
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <s:text name="tc.rules_file.file" />
                    <s:file name="upload" label="File" required="true" size="75" />
                </td>
            </tr>
        </table>
        <input id="uploadBtn" type="button" value="<s:text name="common.upload" />" class="btn-exit" onclick="submitForm();" />

    </s:form>

    <div id="mainBlock">
        <div id="copyBlock">
            <div id="uploadDiv" style="display:none;">
                <form id="uploadForm" action="<s:url action="doRulesFileUploadAjax" namespace="/tc" includeParams="none" />"
                      method="POST" enctype="multipart/form-data" target="uploadFrame" onsubmit="return false;">
                    <input type="hidden" name="year" value="" />
                    <input type="hidden" name="month" value="" />
                    <input type="hidden" name="osznId" value="" />
                </form>
            </div>
            <div id="ajaxResponse" style="color:#ff0000;"></div>
        </div>
    </div>

</div>

<script type="text/javascript">

    var newBlocks = 0;
    var mainBlock = "mainBlock";
    var copyBlock = "copyBlock";

    function addBlock() {

        var block = $(copyBlock);

        var clone = block.cloneNode(1);
        clone.id = copyBlock + newBlocks;
        $(mainBlock).appendChild(clone);

        var uploadForm = $$("form[id=uploadForm]")[1];
        uploadForm.id = "uploadForm" + newBlocks;
        uploadForm.target = "uploadFrame" + newBlocks;

        var inputForm = $("inputForm");
        uploadForm.elements["year"].value = inputForm.elements["year"].value;
        uploadForm.elements["month"].value = inputForm.elements["month"].value;
        uploadForm.elements["osznId"].value = inputForm.elements["osznId"].value;
        uploadForm.appendChild(inputForm.elements["upload"].cloneNode(1));

        var iframe = document.createElement("iframe");
        iframe.id = "uploadFrame" + newBlocks;
        iframe.name = "uploadFrame" + newBlocks;
        $("uploadDiv").appendChild(iframe);

        var ajaxResponse = $$("div[id=ajaxResponse]")[1];
        ajaxResponse.id = "ajaxResponse" + newBlocks;
        ajaxResponse.innerHTML = "";

        newBlocks++;
    }

    function eraseForm() {
        $("inputForm").elements["upload"].value = "";
    }

    function validateForm() {
        var fileEl = $("inputForm").elements["upload"];
        if (fileEl.value == null || fileEl.value == "") {
            alert("<s:text name="error.sz_file.upload.empty_file_field" />");
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
                    console.debug("Can't create http request for ajax");
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

        stack.push(newBlocks);
        addBlock();
        eraseForm();

        if (!uploaded || (uploaded && wait)) {
            var fileValue = $("uploadForm" + (newBlocks - 1)).elements["upload"].value;
            $("ajaxResponse" + (newBlocks - 1)).innerHTML = "<s:text name="sz.file" /> \"" + fileValue.substring(fileValue.lastIndexOf("\\")) + "\": <s:text name="sz.file_upload.progress_bar.waiting" />";
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
            uploadingFilename = "<s:text name="sz.file" /> \"" + fileValue.substring(fileValue.lastIndexOf("\\")) + "\": ";
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

    var retries = 3;
    var curRetry = 0;

    function waiting() {
        if (!wait) {
            return;
        }
        curRetry++;
        var uploadFrame = window.frames["uploadFrame" + uploadingId].document.body;
        var ajaxResponse = $("ajaxResponse" + uploadingId);
        if (curRetry <= retries) {
            if (uploadFrame.innerHTML == "") {
                console.debug("wait");
            } else {
                if (uploadFrame.innerHTML == "<pre>success</pre>") {
                    console.debug("succ");
                    ajaxResponse.style.color = "#008000";
                    ajaxResponse.innerHTML = uploadingFilename + "<s:text name="sz.file_upload.progress_bar.loaded" />";
                    wait = false;
                } else if (uploadFrame.innerHTML == "<pre>error</pre>") {
                    console.debug("error");
                    ajaxResponse.innerHTML = uploadingFilename + "<s:text name="sz.file_upload.progress_bar.error" />";
                    wait = false;
                }
            }
        }
        uploadWait();
    }

    function uploadWait() {
        if (stack.length > 0) {
            if (wait) {
                setTimeout(waiting, 4000);
            } else {
                upload();
            }
        } else {
            if (wait) {
                setTimeout(waiting, 4000);
            }
        }
    }

    function updatePage() {
        if (xmlHttp.readyState == 4 && xmlHttp.responseText != null && xmlHttp.responseText != "") {
            var ajaxResponse = $("ajaxResponse" + uploadingId);
            ajaxResponse.innerHTML = uploadingFilename + "<s:text name="sz.file_upload.progress_bar.loading" /> " + xmlHttp.responseText + "%";
            if (xmlHttp.responseText == "100") {
                uploaded = true;
                wait = true;
                ajaxResponse.innerHTML = uploadingFilename + "<s:text name="sz.file_upload.progress_bar.processing" />";
                curRetry = 0;
                setTimeout(uploadWait, 100);;
                return true;
            }
            setTimeout(getProgress, 1000);
        }
    }

</script>
