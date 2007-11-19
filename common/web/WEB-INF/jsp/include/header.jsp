<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="taglibs.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>


<head>
	<title><tiles:getAsString name="title"/></title>
	<link rel="stylesheet" type="text/css" href="/style/fp.css"/>
</head>


<body>

<table cellpadding="0" cellspacing="0" border="0" width="100%">
	<tr class="verytop">
		<td><a href="flexpay"><img src="img/logo.gif" width="123" height="37"
								   alt="FlexPay" border="0" hspace="25" vspace="6"/></a>
		</td>
		<form name="login-logout">
			<td align="right">
				<table cellpadding="0" cellspacing="0" border="0" width="100%">
					<tr>
						<td width="100%" align="right">
							<span class="text-small">
							<a href="?userinfo">Пользователь</a>: Мария Ивановна Сергаманова
						</span>
						</td>
						<td><img src="img/p.gif" width="10" height="25" alt=""/></td>
						<td><input type="button" value="Выйти" class="btn-exit"/></td>
						<td><img src="img/p.gif" width="25" height="25" alt=""/></td>
					</tr>
				</table>
			</td>
		</form>
	</tr>
</table>

<table cellpadding="0" cellspacing="0" border="0" width="100%">
	<tr>
		<td class="topmenu_s" nowrap="1">
			<span class="menu">Адресное бюро</span>
		</td>
		<td class="topmenu" nowrap="1">
			<a href="1" class="menu">БТИ</a>
		</td>
		<td class="topmenu" nowrap="1">
			<a href="2" class="menu">РЦ</a>
		</td>
		<td class="topmenu" nowrap="1">
			<a href="3" class="menu">ЕРЦ</a>
		</td>
		<td class="topmenu_form" nowrap="1">
			<table cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td><span class="text-small">Язык:&nbsp;</span></td>
					<form name="language">
						<td><select class="form-select">
							<option>Русский</option>
						</select>
						</td>
					</form>
				</tr>
			</table>

		</td>
		<form name="search">
			<td class="topmenu_form_search" nowrap="1">
				<input type="text" class="form-search">&nbsp;<input type="button"
																	value="Найти"
																	class="btn-search"/>
			</td>
		</form>


	</tr>
</table>

<table cellpadding="0" cellspacing="0" border="0" width="100%" height="30">
	<tr class="secondtop">
		<td class="second-padding"><a href="#" class="menu2">Справочники</a></td>
		<td><img src="/img/separator1.gif" hspace="12" alt=""/></td>
		<td><a href="#" class="menu2">Базы&nbsp;данных</a></td>
		<td><img src="/img/separator1.gif" hspace="12" alt=""/></td>
		<td><a href="#" class="menu2">Импорт</a></td>
		<td><img src="/img/separator1.gif" hspace="12" alt=""/></td>
		<td><a href="#" class="menu2">Экспорт</a></td>
		<td width="100%">&nbsp;</td>
	</tr>
</table>

<div class="gradusnik">
	<div class="gradusnik-padding">

		<a href="123" class="grad">FlexPay</a>

		&nbsp;&raquo;&nbsp;

		<a href="123" class="grad">Адресное бюро</a>

		&nbsp;&raquo;&nbsp;

		<a href="123" class="grad">Справочники</a>

		&nbsp;&raquo;&nbsp;

		<b>Какой-нибудь справочник</b>
	</div>
</div>

<div style="padding-left: 25px; padding-right: 25px;">
	<h1>Какой-нибудь справочник</h1>
</div>

<div class="columns">

	<tiles:insertTemplate template="menu.jsp" />

	<div class="main">
			<div class="main-content">
