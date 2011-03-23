<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<script type="text/javascript" src="<s:url value="/resources/common/js/flexpay_fileupload.js" />"></script>
<script type="text/javascript">

    FPFile.constants = {
        progressBarUrl: "<s:url action="fileUploadProgress" namespace="/common" />",
        statusWaiting: "<s:text name="common.file_upload.progress_bar.waiting" />",
        statusUploading: "<s:text name="common.file_upload.progress_bar.uploading" />",
        statusProcessing: "<s:text name="common.file_upload.progress_bar.processing" />",
        statusUploaded: "<s:text name="common.file_upload.progress_bar.uploaded" />",
        statusError: "<s:text name="common.file_upload.progress_bar.error" />",
        confirmExit: "<s:text name="common.file_upload.confirm_exit" />"
    };
    
</script>
