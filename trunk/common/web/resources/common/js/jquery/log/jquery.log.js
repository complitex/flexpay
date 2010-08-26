/*
(function($) {
    $.log =
    {
        log: function(message) {
            //if('console' in window && 'log' in window.console)
            if (typeof window.console != 'undefined' && typeof window.console.log != 'undefined') {
                console.log(message);
            }
            else {
                // do nothing
                //alert('console is not supported: ' + message);
            }
        },
        info : function(message) {
            // todo
        }
    }
})(jQuery);
*/

(function(jQuery) {

	/**
	 * log
	 *
	 * write debug errors to the console or
	 * alert them if the browser does not support
	 * the console object
	 *
	 * @public
	 * @param {Object}
	 * @return {Void}
	 */
	var log = function( object ) {
		if ( typeof console == 'object' )
			console.log( object );
		else if ( typeof opera == 'object' )
			opera.postError( object );
		else
			alert(object);
	};

	jQuery.fn.log = log;
	jQuery.log = log;

})(jQuery);