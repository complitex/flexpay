var FPO = {

    messages : {
        listWithDetails : "listWithDetails",
        listWithoutDetails : "listWithoutDetails"
    },

    showButtons : function (state) {

        var reg = $(".btn-register");
        var ret = $(".btn-return");
        var del = $(".btn-delete");

        switch (state) {
            case 1:
                buttons({disable:false,button:reg}, {button:ret}, {disable:false,button:del});
                break;
            case 2:
                buttons({button:reg}, {disable:false,button:ret}, {button:del});
                break;
            case 4:
                buttons({button:reg}, {button:ret}, {disable:false,button:del});
                break;
            case 5:
                buttons({button:reg}, {button:ret}, {disable:false,button:del});
                break;
            default:
                buttons({button:reg}, {button:ret}, {button:del});
                break;
        }
    },

    showDetails : function () {

        $("tr.document_row").toggle();
        //$("tr.operation_footer_row").toggle();

        var par = $("td.th.service_column:hidden").length == 0;

        if (par) {
            $("tr.brief_operation_header_row").removeClass("brief_operation_header_row").addClass("full_operation_header_row");
            $("td.service_column").hide();
            $("td.service_provider_column").hide();
            $("input[name=showDetails]").each(function() {
                this.value = FPO.messages.listWithDetails;
            });
        } else {
            $("tr.full_operation_header_row").removeClass("full_operation_header_row").addClass("brief_operation_header_row");
            $("td.service_column").show();
            $("td.service_provider_column").show();
            $("input[name=showDetails]").each(function() {
                this.value = FPO.messages.listWithoutDetails;
            });
        }

        this.documentSearch(!par);
    },

    documentSearch : function (par) {
        if (par) {
            $("#serviceTypeFilter").removeAttr("disabled");
        } else {
            $("#serviceTypeFilter").attr("disabled", true);
        }
        $("#documentSearch").val(par);
    }
};

function buttons() {
    for (var i = 0; i < arguments.length; i++) {

        var but = but || {};
        but = $.extend({
            disable: true
        }, arguments[i]);

        if (but.disable) {
            but.button.attr("disabled", true).removeClass("btn-exit").addClass("btn-search");
        } else {
            but.button.removeAttr("disabled").removeClass("btn-search").addClass("btn-exit");
        }
    }
};
