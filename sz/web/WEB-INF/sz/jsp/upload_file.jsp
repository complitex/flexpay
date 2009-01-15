<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<div id="mainBlock">

    <s:form id="inputForm" onsubmit="return false;">
        <div>
            <span>
                <s:text name="year" />
                <s:select name="year" list="years" value="year" required="true" />
            </span>
            <span>
                <s:text name="month" />
                <s:select name="month" list="months" required="true" />
            </span>
            <span>
                <s:text name="sz.oszn" />
                <s:select name="osznId" list="osznList" listKey="id" listValue="description" required="true" />
            </span>
            <span>
                <s:text name="sz.file" />
                <s:file name="upload" label="File" />
            </span>
        </div>

        <input id="uploadBtn" type="button" value="<s:text name="common.upload" />" class="btn-exit" onclick="submitForm();" />

    </s:form>

    <div id="mainBlock">
        <div id="copyBlock">
            <div id="uploadDiv" style="display:none;">
                <form id="uploadForm" action="<s:url action="doSzFileUploadAjax" namespace="/sz" includeParams="none" />"
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

    function submitForm() {

        stack.push(newBlocks);
        addBlock();

        if (!uploaded) {
            $("ajaxResponse" + (newBlocks - 1)).innerHTML = "File " + (newBlocks - 1) + ": Waiting...";
        }
        if (!started) {
            started = true;
            upload();
            started = false;
        }

    }

    function upload() {
        if (uploaded) {
            uploadingId = stack[0];
            stack.remove(0);
            $("uploadForm" + uploadingId).submit();
            startUpload();
        }
    }

    function getProgress() {
        xmlHttp.open("POST", "<s:url action="fileUploadProgress" namespace="/common" includeParams="none" />", true);
        xmlHttp.onreadystatechange = updatePage;
        xmlHttp.send(null);
    }

    function updatePage() {
        if (xmlHttp.readyState == 4 && xmlHttp.responseText != null && xmlHttp.responseText != "") {
            $("ajaxResponse" + uploadingId).innerHTML = 'File ' + uploadingId + ': Loading ' + xmlHttp.responseText + '%';
            if (xmlHttp.responseText == "100") {
                $("ajaxResponse" + uploadingId).style.color = "#008000";
                $("ajaxResponse" + uploadingId).innerHTML = "File " + uploadingId + ": Loaded";
                uploaded = true;
                if (stack.length > 0) {
                    $("ajaxResponse" + stack[0]).innerHTML = "File " + stack[0] + ": Preparing...";
                    setTimeout(upload, 2000);
                }
                return true;
            }
            setTimeout(getProgress, 500);
        }
    }

    function startUpload() {
        uploaded = false;
        setTimeout(getProgress, 500);
    }
	
</script>
