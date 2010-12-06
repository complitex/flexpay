package groovy

//----------------------------------------------------------------------------------
// using HtmlUnit (htmlunit.sf.net)


import com.gargoylesoftware.htmlunit.WebClient
import com.gargoylesoftware.htmlunit.html.HtmlPage

def webClient = new WebClient()
def page = webClient.getPage('http://dev.flexpay.complitex:8080/eirc/confirmationTradingDay?paymentPointId=1000&data=2009-06-24') as HtmlPage

def response = page.getWebResponse()
println "${response.statusCode} ${response.statusMessage} ${response.requestUrl}"

def form1 = page.getFormByName('frm1')
form1.getInputByName('IDToken1').setValueAttribute('developer')
def form2 = page.getFormByName('frm2')
form2.getInputByName('IDToken2').setValueAttribute('developer')

def jsResult = page.executeJavaScript('LoginSubmit("Log In")')

def newPage = jsResult.getNewPage()
response = newPage.getWebResponse()
println "${response.statusCode} ${response.statusMessage} ${response.requestUrl}"

webClient.closeAllWindows()
