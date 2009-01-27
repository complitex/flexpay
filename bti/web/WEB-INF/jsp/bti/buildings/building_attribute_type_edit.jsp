<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:actionerror />

<script type="text/javascript">
	var FPINT = {
		setFormType : function(type) {
			if (type == 'simple')
				this.showSimpleType();
			if (type == 'enum')
				this.showEnumType();
		},
		showSimpleType : function() {
			$('simple_fields').show();
			$('enum_fields').hide();
		},
		showEnumType : function() {
			$('simple_fields').hide();
			$('enum_fields').show();
		},

		maxIndex : 0,

		swapFields : function(f1, f2) {
			var tmp = $F(f1);
			$(f1).value = $F(f2);
			$(f2).value = tmp;
		},
		swapContents : function(s1, s2) {
			var tmp = $(s1).innerHTML;
			$(s1).innerHTML = $(s2).innerHTML;
			$(s2).innerHTML = tmp;
		},

		moveEnumValueUp : function(i) {
			if (i <= 0)
				return;
			this.swapFields("enumValue_"+i, "enumValue_"+(i-1));
			this.swapContents("enumValueDisp_"+i, "enumValueDisp_"+(i-1));
		},
		moveEnumValueDown : function(i) {
			if (i >= this.maxIndex)
				return;
			this.swapFields("enumValue_"+i, "enumValue_"+(i+1));
			this.swapContents("enumValueDisp_"+i, "enumValueDisp_"+(i+1));
		},
		addNewEnumFieldValue : function() {
			this.addNewEnumValue($F('newEnumValue'));
		},
		addNewEnumValue : function(value) {
			if (value == '')
				return;
			// ok, do some magick
			// hide previous element delete button and show move down button
			if ($("enumDeleteBtn_"+this.maxIndex))
				$("enumDeleteBtn_"+this.maxIndex).hide();
			if ($("enumMvDwnBtn_"+this.maxIndex))
				$("enumMvDwnBtn_"+this.maxIndex).show();
			++this.maxIndex;
			var html = '<td width="1%"><input type="hidden" name="enumValues['+this.maxIndex+']" id="enumValue_'+
					   this.maxIndex+'" value="'+value+'" />'+this.maxIndex+'</td>'+
						'<td width="1%">'+
						 (this.maxIndex > 1 ?
							'<a href="javascript:FPINT.moveEnumValueUp('+this.maxIndex+');" id="enumMvUpBtn_'+this.maxIndex+'"><img '+
								'src="<s:url value="/resources/common/img/i_arrow_up.gif" includeParams="none"/>" alt="" /></a>' : '&nbsp;')+
					   '</td><td width="1%">'+
							'<a href="javascript:FPINT.moveEnumValueDown('+this.maxIndex+');" style="display:none;" id="enumMvDwnBtn_'+this.maxIndex+'"><img ' +
								'src="<s:url value="/resources/common/img/i_arrow_down.gif" includeParams="none"/>" alt="" /></a>'+
					   '</td><td width="1%">'+
							'<a href="javascript:FPINT.deleteEnumValue();" id="enumDeleteBtn_'+this.maxIndex+'"><img ' +
								'src="<s:url value="/resources/common/img/i_delete.gif" includeParams="none"/>" alt="" /></a></td>'+
					   '<td width="98%"><span  id="enumValueDisp_'+this.maxIndex+ '">'+value+'</span></td>';
;
			var newTR = new Element('tr', {id: 'enum_value_'+this.maxIndex});
			$('newEnumValueRow').insert({'before' : newTR});
			newTR.insert(html);
			$('newEnumValue').value = '';
		},
		deleteEnumValue : function() {
			$('enum_value_'+this.maxIndex).remove();
			--this.maxIndex;
			// hide previous element move down button and show delete button
			if ($("enumDeleteBtn_"+this.maxIndex))
				$("enumDeleteBtn_"+this.maxIndex).show();
			if ($("enumMvDwnBtn_"+this.maxIndex))
				$("enumMvDwnBtn_"+this.maxIndex).hide();
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
						<option value="simple" <s:if test="typeName == 'simple'">selected</s:if>><s:text name="bti.building.attribute.type.simple" /></option>
						<option value="enum" <s:if test="typeName == 'enum'">selected</s:if>><s:text name="bti.building.attribute.type.enum" /></option>
					</select>
				</td>
			</tr>
		</s:if>
		<tr valign="top" class="cols_1">
			<td class="col" width="40%"><s:text name="bti.building.attribute.type.name" />:</td>
			<td class="col" width="60%">
				<s:iterator value="names"><s:set name="l" value="%{getLang(key)}" />
					<s:textfield name="names[%{key}]" value="%{value}" />(<s:if test="%{#l.default}">*</s:if><s:property
							value="%{getLangName(#l)}" />)<br />
				</s:iterator>
			</td>
		</tr>
		<tr valign="middle" class="cols_1" id="simple_fields"
			<s:if test="typeName != 'simple'">style="display:none;"</s:if>>
			<td class="col" colspan="2">&nbsp;</td>
		</tr>
		<tr valign="middle" class="cols_1" id="enum_fields"
			<s:if test="typeName != 'enum'">style="display:none;"</s:if>>
			<td class="col" valign="top"><s:text name="bti.building.attribute.type.enum.values" />:</td>
			<td class="col">
				<!-- All table build is done in JavaScript -->
				<table border="0" width="100%">
					<tr id="newEnumValueRow">
						<td colspan="5"><input type="text" id="newEnumValue" value="test"/>&nbsp;
							<input type="button" onclick="FPINT.addNewEnumFieldValue();" value="<s:text name="common.add"/>" />
						</td>
					</tr>
				</table>
				<script type="text/javascript"><s:iterator value="enumValues" status="st">
						FPINT.addNewEnumValue("<s:property value="%{value}" />");</s:iterator>
				</script>
			</td>
		</tr>
		<tr valign="middle">
			<td colspan="2"><input type="submit" class="btn-exit" name="submitted"
								   value="<s:text name="common.save"/>" /></td>
		</tr>
	</table>
</s:form>
