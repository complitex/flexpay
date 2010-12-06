package groovy

assert args.size() == 1, 'Please specify version source web.xml as parameter'

def file = new File(args[0])
assert file.exists(), "File ${file} not found"
assert file.name.equalsIgnoreCase('web.xml'), "Expected web.xml file"

def ns = new groovy.xml.Namespace('http://java.sun.com/xml/ns/javaee', 'ns')
def root = new XmlParser().parse(file);
root[ns.'security-role'].each {
	def role = it[ns.'role-name'].text().substring('ROLE_'.size())
	println """# ${role}, groups, opensso.java.net
dn: cn=${role},ou=groups,dc=opensso,dc=java,dc=net
objectClass: groupofuniquenames
objectClass: top
objectClass: groupofurls
cn: ${role}
"""
}
