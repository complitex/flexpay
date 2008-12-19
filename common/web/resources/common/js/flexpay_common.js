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
