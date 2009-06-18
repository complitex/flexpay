<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:actionerror/>

<s:form action="changeAnnotation">
  <s:textarea name="registryAnnotation"/>
  <input type="submit" name="submitChange" class="" value="<s:text name="payments.registry.annotation.submit"/>"/>
</s:form>