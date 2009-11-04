// Script cuts off any security tags from
// _build/web/WEB-INF/web.xml and _build/web/WEB-INF/sun-web.xml
// as all security management is done with SpringSecurity+OpenSSO

private boolean isDir(File parent, String dir) {
	File file = new File(parent, dir)
	return file.isDirectory()
}

public boolean validRoot(File file) {
	return isDir(file, "common") && isDir(file, "ab") && isDir(file, "bti")   \
		  && isDir(file, "orgs") && isDir(file, "payments") && isDir(file, "eirc")   \
		  && isDir(file, "rent")
}

File root = new File(".").getAbsoluteFile()
while (root.getParent() != null && !validRoot(root)) {
	root = root.parentFile
}

assert validRoot(root), "Invalid startup directory ${new File('').absolutePath}, please, run from project directory"

File webXml = new File(root, '_build/web/WEB-INF/web.xml')
assert webXml.exists(), "No web.xml found in ${webXml.absolutePath}"
File sunWebXml = new File(root, '_build/web/WEB-INF/sun-web.xml')
assert sunWebXml.exists(), "No web.xml found in ${sunWebXml.absolutePath}"


def webApp = new XmlParser().parse(webXml)
List<Node> toRemove = []
toRemove.addAll webApp['security-constraint']
toRemove.addAll webApp['security-role']
toRemove.addAll webApp['login-config']

toRemove.each {
	webApp.remove(it)
}
new File(root, '_build/web/WEB-INF/web.xml-tmp').withPrintWriter('UTF-8') { PrintWriter w ->
//webXml.withPrintWriter('UTF-8') { PrintWriter w ->
	w.write '<?xml version="1.0" encoding="UTF-8"?>\n'
	new XmlNodePrinter(w, ).print(webApp)
}

def sunWebApp = new XmlParser().parse(sunWebXml)
// remove magic 'error-url' attribute
sunWebApp.attributes().remove('error-url')
toRemove = []
toRemove.addAll sunWebApp['security-role-mapping']
toRemove.each {
	sunWebApp.remove(it)
}
//new File(root, '_build/web/WEB-INF/sun-web.xml-tmp').withPrintWriter('UTF-8') { PrintWriter w ->
sunWebXml.withPrintWriter('UTF-8') { PrintWriter w ->
	w.write '''<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE sun-web-app PUBLIC
        "-//Sun Microsystems, Inc.//DTD Application Server 9.0 Servlet 2.5//EN"
        "http://www.sun.com/software/dtd/appserver/sun-web-app_2_5-0.dtd">
'''
	new XmlNodePrinter(w).print(sunWebApp)
}
