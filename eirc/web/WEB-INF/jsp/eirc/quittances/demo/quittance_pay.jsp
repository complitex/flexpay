<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:actionerror/>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td><s:textfield value="%{getText('eirc.quittances.demo.quittance_pay.data.quittance_number')}"
                         readonly="true"/></td>
        <td><s:textfield value="%{getText('eirc.quittances.demo.quittance_pay.data.total_summ')}" readonly="true"/></td>
    </tr>
    <tr>
        <td><s:textfield value="%{getText('eirc.quittances.demo.quittance_pay.data.fio')}" readonly="true"/></td>
        <td><s:textfield value="%{getText('eirc.quittances.demo.quittance_pay.data.address')}" readonly="true"/></td>
    </tr>
</table>

<br/>

<s:form action="demoQuittancePay">

    <table cellpadding="3" cellspacing="1" border="0" width="100%">
        <tr>
            <td class="th"><s:text name="eirc.quittances.demo.quittance_pay.service"/></td>
            <td class="th"><s:text name="eirc.quittances.demo.quittance_pay.service_supplier"/></td>
            <td class="th"><s:text name="eirc.quittances.demo.quittance_pay.in_balance"/></td>
            <td class="th"><s:text name="eirc.quittances.demo.quittance_pay.out_balance"/></td>
            <td class="th"><s:text name="eirc.quittances.demo.quittance_pay.payed"/></td>
            <td class="th"><s:text name="eirc.quittances.demo.quittance_pay.pay"/></td>
        </tr>

        <tr class="cols_1">
            <td class="col"><s:text name="eirc.quittances.demo.quittance_pay.data.service1.name"/></td>
            <td class="col"><s:text name="eirc.quittances.demo.quittance_pay.data.service1.supplier"/></td>
            <td class="col"><s:text name="eirc.quittances.demo.quittance_pay.data.service1.in"/></td>
            <td class="col"><s:text name="eirc.quittances.demo.quittance_pay.data.service1.out"/></td>
            <td class="col"><s:text name="eirc.quittances.demo.quittance_pay.data.service1.payed"/></td>
            <td class="col"><s:textfield/></td>
        </tr>
        <tr class="cols_1">
            <td class="col"><s:text name="eirc.quittances.demo.quittance_pay.data.service2.name"/></td>
            <td class="col"><s:text name="eirc.quittances.demo.quittance_pay.data.service2.supplier"/></td>
            <td class="col"><s:text name="eirc.quittances.demo.quittance_pay.data.service2.in"/></td>
            <td class="col"><s:text name="eirc.quittances.demo.quittance_pay.data.service2.out"/></td>
            <td class="col"><s:text name="eirc.quittances.demo.quittance_pay.data.service2.payed"/></td>
            <td class="col"><s:textfield/></td>
        </tr>
        <tr class="cols_1">
            <td class="col"><s:text name="eirc.quittances.demo.quittance_pay.data.service3.name"/></td>
            <td class="col"><s:text name="eirc.quittances.demo.quittance_pay.data.service3.supplier"/></td>
            <td class="col"><s:text name="eirc.quittances.demo.quittance_pay.data.service3.in"/></td>
            <td class="col"><s:text name="eirc.quittances.demo.quittance_pay.data.service3.out"/></td>
            <td class="col"><s:text name="eirc.quittances.demo.quittance_pay.data.service3.payed"/></td>
            <td class="col"><s:textfield/></td>
        </tr>
    </table>

    <br/>

    <table cellpadding="3" cellspacing="1" border="0" width="100%">
        <tr>
            <td style="text-align: right;">
                <s:textfield name="summ"/>
                <s:submit name="submitted" value="%{getText('eirc.quittances.demo.quittance_pay.pay')}"
                          cssClass="btn-exit"/>
            </td>
        </tr>
    </table>

</s:form>