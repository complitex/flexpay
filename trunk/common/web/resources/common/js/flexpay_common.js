
var FP =
{
	// Set checkboxes group (names starts with prefix) state to checked
	setCheckboxes : function (checked, prefix) {
		var boxes = $$('input[type="checkbox"]');
		boxes.each(
				function(inp) {
					if (inp.name.startsWith(prefix)) {
						inp.checked = checked;
					}
				});
	},

	sorters : [],

	activateSorter : function (sorter) {

		// disable all sorters
		this.sorters.each(
				function (field) {
					$(field).value = 0;
				});
		// set active passed sorter
		$(sorter).value = 1;
	}
};

Array.prototype.remove = function(from, to) {
    var rest = this.slice((to || from) + 1 || this.length);
    this.length = from < 0 ? this.length + from : from;
    return this.push.apply(this, rest);
};

// add trim() to strings (http://blog.stevenlevithan.com/archives/faster-trim-javascript)
String.prototype.trim = function() {
	var	str = this.replace(/^\s\s*/, ''),
		ws = /\s/,
		i = this.length;
	while (ws.test(str.charAt(--i)));
	return str.slice(0, i + 1);
};