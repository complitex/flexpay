var FPP = {

    messages : {
        invalidPayValue : "invalidPayValue",
        inputSummIsTooSmall : "inputSummIsTooSmall",
        createOpServerError : "createOpServerError",
        clipboardAccessDenided : "clipboardAccessDenided",
        clipboardCopyError : "clipboardCopyError"
    },

    urls : {
        printButUrl : "printButUrl",
        payFormUrl : "payFormUrl",
        payButUrl : "payButUrl"
    },

    paymentEnabled : false,
    errorFields : [],
    fieldChain : [],
    currentFieldIndex : 0,

    createFieldChain : function () {
        var paymentInputs = $("input[id^=payments_]").each(function() {
            FPP.fieldChain.push(this.id);
        });
        // adding total input sum field to field chain
        FPP.fieldChain.push("inputSumm");

        // setting focus to the first payments field
        $("#" + FPP.fieldChain[0]).focus().select();
        FPP.currentFieldIndex = 0;
    },

    setCurrentFieldIndex : function (event) {

        var selectedFieldId = $(event.target).attr("id");
        var fc = this.fieldChain;
        $.each(fc, function(i, v) {
            if (v == selectedFieldId) {
                this.currentFieldIndex = i;
                $("#" + v).select();
                return false;
            }
        });
    },

    updateCurrentFieldIndex : function (event) {
        var oldNumberOfErrors = FPP.errorsNumber();
        var nextFieldId = $(event.target).attr("id");
        var cfi = this.currentFieldIndex;
        var fc = this.fieldChain;
        var currentFieldId = fc[cfi];

        if (event.keyCode == FP.ENTER_KEY_CODE || (event.keyCode == FP.TAB_KEY_CODE && !event.shiftKey)) {
            if (cfi < fc.length - 1) {
                nextFieldId = fc[cfi + 1];
                $("#" + nextFieldId).focus().select();

                if (FPP.errorsNumber() > oldNumberOfErrors) {
                    $("#" + currentFieldId).focus().select();
                }
            } else {
                if ($.inArray("inputSumm", FPP.errorFields) >= 0) {
                    $("#changeSumm").focus(); // to invalidate error by calling validator on change
                }
                $("#printQuittanceButton").focus();

                if (FPP.errorsNumber() > oldNumberOfErrors) {
                    $("#" + currentFieldId).focus().select();
                }
            }
            event.preventDefault();

        } else if (event.keyCode == FP.TAB_KEY_CODE && event.shiftKey) {
            if (cfi > 0) {
                nextFieldId = fc[cfi - 1];
                $("#" + nextFieldId).focus().select();

                if (FPP.errorsNumber() > oldNumberOfErrors) {
                    $("#" + currentFieldId).focus().select();
                }

            }
            event.preventDefault();
        }
    },

    bindEvents : function (elements) {

        $.each(elements, function(i, el) {
            $("#payments_" + el.index).focus(function(event) {
                FPP.setCurrentFieldIndex(event);
            }).keypress(function(event) {
                FPP.updateCurrentFieldIndex(event);

                switch (event.keyCode) {
                    case FP.TAB_KEY_CODE:
                    case FP.ENTER_KEY_CODE:
                    case FP.BACKSPACE_KEY_CODE:
                    case FP.DELETE_KEY_CODE:
                    case FP.LEFT_KEY_CODE:
                    case FP.RIGHT_KEY_CODE:
                    case FP.HOME_KEY_CODE:
                    case FP.END_KEY_CODE:
                        break;
                    case 0:
                        if (!(event.charCode == 46 || (event.charCode >= 48 && event.charCode <= 57))) {
                            event.preventDefault();
                        }
                        break;          
                    default:
                        event.preventDefault();
                        break;
                }
            });

            $("#payments_" + el.index + "_copy").click(function() {
                try {
                    FP.copyToClipboard(el.content);
                } catch(e) {
                    if ($.browser.mozilla) {
                        alert(FPP.messages.clipboardAccessDenided);
                    } else if ($.browser.msie) {
                        alert(FPP.messages.clipboardCopyError);
                    }
                }
            });
        });

        var inputSum = $("#inputSumm");
        var printBut = $("#printQuittanceButton");
        var payBut = $("#payQuittanceButton");

        inputSum.focus(function(event) {
            FPP.setCurrentFieldIndex(event);
        }).keypress(function(event) {
            FPP.updateCurrentFieldIndex(event);
        });

        printBut.keypress(function(event) {
            if (event.keyCode == FP.TAB_KEY_CODE) {
                if (event.shiftKey) {
                    inputSum.focus();
                } else {
                    if (FPP.paymentEnabled) {
                        printBut.focus();
                    }
                }
                event.preventDefault();
            }
        }).click(function() {
            $("#dialog").modal({
                overlayClose:true,
                escClose:true
            });
        });

        payBut.click(function() {
            FPP.doPayQuittance();
        });

    },

    dotted2Int : function (i) {
        i = i.replace(",", ".");
        var dotpos = i.indexOf(".");
        //noinspection PointlessArithmeticExpressionJS
        return dotpos != -1 ? i.substring(0, dotpos) * 100 + i.substring(dotpos + 1) * 1 : i * 100;
    },

    int2Dotted : function (i) {
        var divider = 100;
        var mod = i % divider;
        return ((i - mod) / divider) + "." + (mod < 10 ? "0" + mod : mod);
    },

    endisButton : function (button, endis) {
        var arr = typeof button === "array" ? button : [button];
        $.each(arr, function(i, v) {
            FP.endis(v, endis).toggleClass("btn-exit", endis).toggleClass("btn-search", !endis);
        });
    },

    endisPayment : function (endis) {
        FPP.endisButton("#payQuittanceButton", endis);
        this.paymentEnabled = endis;
    },

    endisButtons : function(endis) {
        FPP.endisButton(["#payQuittanceButton","#printQuittanceButton"], endis);
        if (endis) {
            FPP.endisPayment(this.paymentEnabled);
        }
    },

    postProcessPaymentValue : function (fieldId) {

        var payment = $("#" + fieldId);
        var paymentValue = payment.val();
        var dotpos = paymentValue.indexOf(".");
        var i = "";

        if (dotpos < 0) {
            i = ".00";
        } else if (dotpos == paymentValue.length - 1) {
            i = "00";
        } else if (dotpos == paymentValue.length - 2) {
            i = "0";
        }
        payment.val(paymentValue + i);
    },

    addErrorField : function (fieldId) {
        if (!FPP.containsError(fieldId)) {
            this.errorFields.push(fieldId);
            FPP.endisButtons(false);
        }
    },

    removeErrorField : function (fieldId) {
        var ind = $.inArray(fieldId, this.errorFields);
        if (ind >= 0) {
            if (Array.remove(this.errorFields, ind) == 0) {
                FPP.endisButtons(true);
            }
        }
    },

    errorsNumber: function () {
        return this.errorFields.length;
    },

    containsError : function (fieldId) {
        return $.inArray(fieldId, this.errorFields) >= 0;
    },

    validate : function (value) {
        return value.match(/^(\d)+([\.,]?\d{0,2})$/);
    },

    validateValue : function (fieldId, errText) {
        var field = $("#" + fieldId);
        var previousRow = field.parent("td").parent("tr").prev("tr");
        var errorCell = $(previousRow.children()[0]);

        if (errText == null || errText == undefined) {
            errorCell.empty();
            previousRow.hide();
            FPP.removeErrorField(fieldId);
            return true;
        } else {
            errorCell.text(errText);
            previousRow.show();
            FPP.addErrorField(fieldId);
            return false;
        }
    },

    validatePaymentValue : function (fieldId) {
        return FPP.validateValue(fieldId, FPP.validate($("#" + fieldId).val()) ? null : FPP.messages.invalidPayValue);
    },

    validateInputValue : function () {

        var inputValue = $("#inputSumm").val();
        var totalValue = $("#totalToPay").val();

        if (!FPP.validate(inputValue) || !FPP.validate(totalValue)) {
            return true;
        }

        var totalPaySum = FPP.dotted2Int(totalValue);
        var inputSum = FPP.dotted2Int(inputValue);
        return FPP.validateValue("inputSumm", totalPaySum > inputSum ? FPP.messages.inputSummIsTooSmall : null);
    },

    updateTotal : function () {
        var totalSum = 0;
        var total = $("#totalToPay");
        var input = $("#inputSumm");
        var change = $("#changeSumm");

        $("input[id^=payments_]").each(function() {
            var val = this.value;
            if (!FPP.validate(val)) {
                total.val("");
                input.val("");
                change.val("");
                return;
            }

            totalSum += FPP.dotted2Int(val);
        });

        total.val(FPP.int2Dotted(totalSum));
        input.val(FPP.int2Dotted(totalSum));
        FPP.validateInputValue();
        change.val("0.00");
    },

    updateChange : function () {
        var inputValue = $("#inputSumm").val();
        var totalValue = $("#totalToPay").val();
        var change = $("#changeSumm");

        if (!FPP.validate(inputValue) || !FPP.validate(totalValue)) {
            change.val("");
            return;
        }

        var changeSum = FPP.dotted2Int(inputValue) - FPP.dotted2Int(totalValue);

        if (changeSum < 0) {
            change.val("");
            return;
        }

        change.val(FPP.int2Dotted(changeSum));
    },

    doPayQuittance : function () {
        $("#quittancePayForm").attr("action", FPP.urls.payButUrl).submit().removeAttr("action");
    },

    doPrintQuittance : function (format) {

        var indicator = $("#indicator");
        var errorSpan = indicator.prev("span").hide();
        indicator.show();

        $.getJSON(FPP.urls.printButUrl, {},
                function(data) {
                    if (data.status == 0) {
                        $("#operationId").val(data.operationId);
                        indicator.hide();
                        $("#format").val(format);
                        $("#quittancePayForm").attr("action", FPP.urls.payFormUrl).attr("target", "_blank").submit().removeAttr("action").removeAttr("target");
                        $.modal.close();
                        FPP.endisPayment(true);
                        $("#payQuittanceButton").focus().select();
                    } else {
                        indicator.hide();
                        errorSpan.text(FPP.messages.createOpServerError).show();
                    }
                });
    }

};
