// module generation script

import org.apache.commons.io.FileUtils

assert args.size() == 2, "Specify module_name parent_module_name as arguments"

def moduleName = args[0]
def parentModuleName = args[1]

def baseDir = new File("..").absoluteFile

assert moduleName =~ /[a-z]+/, "Module name should only consist of [a-z]"
assert new File(baseDir, parentModuleName).exists(), "No parent module found ${parentModuleName}"

def templateDir = new File(baseDir, "module_template")
def moduleDir = new File(baseDir, moduleName)

// TODO remove next line
// FileUtils.deleteQuietly moduleDir

assert !moduleDir.exists(), "${moduleDir} already exists, remove it first!"

println "copying template to directory ${moduleDir}"
FileUtils.copyDirectory templateDir, moduleDir

println "renaming template files and directories"
def hasDirRenames = true;
while (hasDirRenames) {
	hasDirRenames = false;

	try {
		moduleDir.eachFileRecurse { File f ->
			// if found '${module_name}' in file name replace it with moduleName
			if (f.name =~ /\$\{module_name\}/ ) {
				def fileName = f.name.replaceAll(/\$\{module_name\}/, moduleName)
				def targetFile = new File(f.parentFile, fileName)
				assert f.renameTo(targetFile), "rename of ${f} to ${targetFile} failed"

				println "Renamed ${f.name} to ${targetFile}"
				if (targetFile.isDirectory()) {
					hasDirRenames = true;
					// break from closure
					throw new RuntimeException("break ${targetFile}");
				}
			}
		}
	} catch (Exception e) {
		if (!e.message.startsWith("break")) {
			throw e;
		}
	}
}

println "Updating file contents"
moduleDir.eachFileRecurse {	File f ->
	if (f.isFile()) {
		def text = f.getText('UTF-8')
		f.write(
			text.replaceAll(/\$\{module_name\}/, moduleName)
				.replaceAll(/\$\{parent_module_name\}/, parentModuleName),
			'UTF-8')
	}
}

// destroy target generation script
new File(moduleDir, "generate_module.groovy").delete();
