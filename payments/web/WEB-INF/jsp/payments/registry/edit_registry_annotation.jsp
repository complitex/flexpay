<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:actionerror/>

<s:form action="changeAnnotation">
  <s:hidden name="registryId"/>
  <s:textarea name="registryAnnotation"/><br/>
  <input type="submit" name="submitChange" class="" value="<s:text name="payments.registry.annotation.submit"/>"/>
  <input type="submit" name="cancel" class="" value="<s:text name="payments.registry.annotation.cancel"/>"/>
</s:form>