/*
 * Hides process variables section content
 */
function hideProcessVariables() {
    $('hideProcessVariablesDiv').hide();
    $('showProcessVariablesDiv').show();
    $('processVariables').hide();
}

/**
 * Shows process variables section
 */
function showProcessVariables() {
    $('hideProcessVariablesDiv').show();
    $('showProcessVariablesDiv').hide();
    $('processVariables').show();
}