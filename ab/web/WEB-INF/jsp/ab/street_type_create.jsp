<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>



<table cellpadding="3" cellspacing="1" border="0" width="100%">
  <s:form>
    <tr>
	  <td class="th">&nbsp;</td>
	  <td class="th"><s:text name="ab.language"/></td>
	  <td class="th"><s:text name="ab.street_type"/></td>
	  <td class="th"><s:text name="ab.short_name"/></td>
	</tr>
	<s:iterator value="translationList" status="rowstatus">
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
	      <s:textfield name="translationList[%{#rowstatus.index}].name" />
	      <s:fielderror>
            <s:param value="translationList[%{#rowstatus.index}].name" />
          </s:fielderror>
	    </td>
	    <td class="col">
	      <s:textfield name="translationList[%{#rowstatus.index}].shortName" />
	      <s:fielderror>
            <s:param value="translationList[%{#rowstatus.index}].shortName" />
          </s:fielderror>
	    </td>
	  </tr>
	</s:iterator>
	<tr>
      <td colspan="4" height="4" bgcolor="#4a4f4f"/>
    </tr>
	<tr>
	  <td colspan="4">
	    <s:submit name="submit" value="%{getText('ab.create')}" cssClass="btn-exit" />
	  </td>
	</tr>
  </s:form>		
</table>
