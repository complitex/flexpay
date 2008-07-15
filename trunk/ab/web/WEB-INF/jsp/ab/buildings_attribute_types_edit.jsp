<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>


<table cellpadding="3" cellspacing="1" border="0" width="100%">
  <s:form>
    <tr>
	  <td class="th">&nbsp;</td>
	  <td class="th"><s:text name="ab.language"/></td>
	  <td class="th"><s:text name="ab.name"/></td>
	  <td class="th"><s:text name="ab.short_name"/></td>
	</tr>
	<s:iterator value="buildingAttributeType.translations" status="rowstatus">
	  <tr valign="middle" class="cols_1">
	    <td class="col_1s">
	      <s:property value="#rowstatus.index + 1" />
	    </td>
	    <td class="col">
	      <s:property value="getLangName(lang)" />
	      <s:if test="lang.default == true">
	        <font color="red">*</font>
	      </s:if>
	    </td>
	    <td class="col">
	      <s:textfield name="translationMap[%{lang.id}].name" value="%{name}" />
	    </td>
	    <td class="col">
	      <s:textfield name="translationMap[%{lang.id}].shortName" value="%{shortName}" />
	    </td>
	  </tr>
	</s:iterator>
	<tr>
      <td colspan="4" height="4" bgcolor="#4a4f4f"/>
    </tr>
    <tr>
      <td colspan="2">
        type
      </td>
      <td colspan="2">
	    <s:textfield name="type" value="%{type}" disabled="1" />
	    <s:hidden name="type" value="%{type}" />
      </td>
    </tr>
	<tr>
	  <td colspan="4">
	    <input type="submit" class="btn-exit" name="submitted"
					 value="<s:text name="common.save"/>"/>
	  </td>
	</tr>
	<tr>
	  <td colspan="4">
	    <s:text name="%{blancTypeFieldError}"/>
	    <s:text name="%{allTranslationBlancError}"/>
	  </td>
	</tr>
  </s:form>		
</table>