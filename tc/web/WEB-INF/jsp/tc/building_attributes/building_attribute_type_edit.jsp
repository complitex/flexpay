<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<script type="text/javascript">
	var FPINT = {
		setFormType : function(type) {
			if (type == "simple") {
				this.showSimpleType();
            } else if (type == "enum") {
				this.showEnumType();
            }
		},
		showSimpleType : function() {
			$("#simple_fields").show();
			$("#enum_fields").hide();
		},
		showEnumType : function() {
			$("#simple_fields").hide();
			$("#enum_fields").show();
		},

		maxIndex : 0,

		swapFields : function(f1, f2) {
			var tmp = $(f1).val();
			$(f1).val($(f2).val());
			$(f2).val(tmp);
		},
		swapContents : function(s1, s2) {
			var tmp = $(s1).html();
			$(s1).html($(s2).html());
			$(s2).html(tmp);
		},

		editEnumValue : function(i) {
			$("#enumValueDisp_" + i).hide();
			$("#enumValue_" + i).show().focus();
		},
		stopEditEnumValue : function(i) {
			$("#enumValueDisp_" + i).val($("enumValue_" + i).val()).show();
			$("#enumValue_" + i).hide();
		},
		moveEnumValueUp : function(i) {
			if (i <= 0) {
				return;
            }
			this.swapFields("#enumValue_" + i, "#enumValue_" + (i - 1));
			this.swapContents("#enumValueDisp_" + i, "#enumValueDisp_" + (i - 1));
		},
		moveEnumValueDown : function(i) {
			if (i >= this.maxIndex) {
				return;
            }
			this.swapFields("#enumValue_" + i, "#enumValue_" + (i + 1));
			this.swapContents("#enumValueDisp_" + i, "#enumValueDisp_" + (i + 1));
		},
		addNewEnumFieldValues : function() {
			var text = $("#newEnumValue").val();
			var fields = text.split("\n");
			for (i = 0; i < fields.length; i++) {
				this.addNewEnumValue(fields[i]);
            }
			$("#newEnumValue").val("");
		},
		hasDuplicate : function(val) {
			var values = this.getAllValues();
			for (var i = 0; i < values.length; ++i) {
				if (val == values[i]) {
					return true;
				}
			}
			return false;
		},
		addNewEnumValue : function(value) {
			value = $.trim(value);
			if (value == null || value == "") {
				return;
            }
			if (this.hasDuplicate(value)) {
				alert("<s:text name="common.duplicate"/>: " + value);
				return;
			}
			// ok, do some magic
			// show move down button
			if ($("#enumMvDwnBtn_" + this.maxIndex)) {
				$("#enumMvDwnBtn_" + this.maxIndex).show();
            }
			++this.maxIndex;
			var html = '<td width="1%">' + this.maxIndex + '</td>' +
					   '<td width="98%">' + '<input type="hidden" name="enumValues[' + this.maxIndex + ']" id="enumValue_' +
					   this.maxIndex + '" value="' + value + '" onblur="FPINT.stopEditEnumValue(' + this.maxIndex + ')"/>' +
					   '<span id="enumValueDisp_' + this.maxIndex + '" onclick="FPINT.editEnumValue(' + this.maxIndex + ')">' + value + '</span></td>' +
					   '<td width="1%">' +
					   (this.maxIndex > 1 ?
						'<a href="javascript:FPINT.moveEnumValueUp(' + this.maxIndex + ');" id="enumMvUpBtn_' + this.maxIndex + '"><img ' +
						'src="<s:url value="/resources/common/img/i_arrow_up.gif" includeParams="none"/>" alt="" /></a>' : '&nbsp;') +
					   '</td><td width="1%">' +
					   '<a href="javascript:FPINT.moveEnumValueDown(' + this.maxIndex + ');" style="display:none;" id="enumMvDwnBtn_' + this.maxIndex + '"><img ' +
					   'src="<s:url value="/resources/common/img/i_arrow_down.gif" includeParams="none"/>" alt="" /></a>' +
					   '</td><td width="1%">' +
					   '<a href="javascript:FPINT.deleteEnumValue(' + this.maxIndex + ');" id="enumDeleteBtn_' + this.maxIndex + '"><img ' +
					   'src="<s:url value="/resources/common/img/i_delete.gif" includeParams="none"/>" alt="" /></a></td>';

			var newTR = $("<tr id=\"enum_value_" + this.maxIndex + "\"></tr>");
			newTR.insertBefore($("#newEnumValueRow"));
			newTR.html(html);
		},
		deleteEnumValue : function(i) {
			while (i <= this.maxIndex) {
				this.moveEnumValueDown(i);
				++i;
			}
			$("#enum_value_" + this.maxIndex).remove();
			--this.maxIndex;
			// hide previous element move down button and show delete button
            var el1 = $("#enumDeleteBtn_" + this.maxIndex);
            var el2 = $("#enumMvDwnBtn_" + this.maxIndex);
			if (el1) {
				el1.show();
            }
			if (el2) {
				el2.hide();
            }
		},
		setEnumValue : function (value, i) {
			$("input[type=hidden][name=enumValues[" + i + "]]").val(value);
			$("#enumValueDisp_" + i).text(value);
		},
		getAllValues : function() {
			var values = [];
			for (var i = 1; i <= this.maxIndex; ++i) {
				values.push($("#enumValue_" + i).val());
			}
			return values;
		},
		sortEnumValues : function() {
			var values = this.getAllValues();
			values.sort();
			for (var j = 1; j <= this.maxIndex; ++j) {
				this.setEnumValue(values[j - 1], j);
			}
		}
	};
</script>

<s:form>
	<s:hidden name="attributeType.id" />
	<s:set name="maxIndex" value="0" scope="page" />
	<table cellpadding="3" cellspacing="1" border="0" width="100%">
		<s:if test="attributeType.isNew()">
			<tr valign="middle" class="cols_1">
				<td class="col" width="40%"><s:text name="bti.building.attribute.type.type" />:</td>
				<td class="col" width="60%">
					<select onchange="FPINT.setFormType(this.value);" name="typeName">
						<option value="simple" <s:if test="typeName == 'simple'">selected</s:if>><s:text
								name="bti.building.attribute.type.simple" /></option>
						<option value="enum" <s:if test="typeName == 'enum'">selected</s:if>><s:text
								name="bti.building.attribute.type.enum" /></option>
					</select>
				</td>
			</tr>
		</s:if>
		<tr valign="top" class="cols_1">
			<td class="col"><s:text name="bti.building.attribute.type.name" />:</td>
			<td class="col">
				<s:iterator value="names"><s:set name="l" value="%{getLang(key)}" />
					<s:textfield name="names[%{key}]" value="%{value}" />(<s:if test="%{#l.default}">*</s:if><s:property
							value="%{getLangName(#l)}" />)<br />
				</s:iterator>
			</td>
		</tr>
		<tr valign="top" class="cols_1">
			<td class="col"><s:text name="bti.building.attribute.type.time_dependent" />:</td>
			<td class="col">
				<input type="radio" name="temporal" value="0" <s:if test="temporal == 0" >checked="checked"</s:if>/><s:text name="common.no" />
				<input type="radio" name="temporal" value="1" <s:if test="temporal == 1" >checked="checked"</s:if>/><s:text name="common.yes" /><br />
			</td>
		</tr>
		<tr valign="top" class="cols_1">
			<td class="col"><s:text name="bti.building.attribute.type.group" />:</td>
			<td class="col">
				<%@ include file="../filters/building_attribute_group_filter.jsp" %>
			</td>
		</tr>
		<tr valign="middle" class="cols_1" id="simple_fields"
			<s:if test="typeName != 'simple'">style="display:none;"</s:if>>
			<td class="col" colspan="2">&nbsp;</td>
		</tr>
		<tr valign="middle" class="cols_1" id="enum_fields"
			<s:if test="typeName != 'enum'">style="display:none;"</s:if>>
			<td class="col" colspan="2">
				<table border="0" width="100%">
					<tr>
						<td width="60%"><s:text name="bti.building.attribute.type.enum.values" /></td>
						<td width="64%" align="right"><input type="button" align="right"
															 onclick="FPINT.sortEnumValues();"
															 value="<s:text name="common.sort"/>" class="btn-exit" />
					</tr>
				</table>

				<!-- All table build is done in JavaScript -->
				<table border="0" width="100%">
					<tr id="newEnumValueRow">
						<td colspan="5">
							<%--suppress CheckTagEmptyBody --%>
							<textarea type="text" id="newEnumValue" class="form-textarea" cols="35"
									  rows="15"></textarea>

							<div align="right"><input type="button" onclick="FPINT.addNewEnumFieldValues();"
													  class="btn-exit" value="<s:text name="common.add"/>" /></div>
						</td>
					</tr>
				</table>
				<script type="text/javascript">
                    <s:iterator value="enumValues">
					    FPINT.addNewEnumValue("<s:property value="%{value}" />");
                    </s:iterator>
				</script>
			</td>
		</tr>
		<tr valign="middle">
			<td colspan="2"><input type="submit" class="btn-exit" name="submitted"
								   value="<s:text name="common.save"/>" /></td>
		</tr>
	</table>
</s:form>
