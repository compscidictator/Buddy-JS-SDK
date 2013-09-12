//DECLARE GLOBAL VARIABLE,CONSTANT,ENUM
var globalMessageCode = {
    StartUp_Mandatory: 1,
    EmailAddress_Validation:2
}

//Get Common Message
function GetMessage(msgCode) {
    var msg = '';
    switch (msgCode) {
        case globalMessageCode.StartUp_Mandatory:
            msg = 'Please fill all mandatory fields.';
            break;
        case globalMessageCode.EmailAddress_Validation:
        msg = 'Please enter valid Email-Address.';
        break;
            
    }
    return msg;
}

//Check
 function ValidateEmailAddress(strEmailAddress) {
     var isValidEmail = true;
     var regMail = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9])+$/;
     if (!regMail.test(strEmailAddress)) {
         isValidEmail = false;
     }
     return isValidEmail;
 }

function SubmitStartUpFunction() {
    $("#btnSubmitStartUp").click(function () {
        var modelIsValid = true;
        //Validation for all mandatory Fields Starts
        $("input[data-required='True']").each(function () {
            if ($(this).val() == '') {
                $(this).addClass('error');
                modelIsValid = false;
            }
            else { $(this).removeClass('error'); }
        });
        //Validation for all mandatory Fields Ends

        if (modelIsValid) {
            if ($.trim($("#StartUpContactEmail").val()) != "") {
                if (!ValidateEmailAddress($.trim($("#StartUpContactEmail").val()))) {
                    $("#StartUpContactEmail").addClass('error');
                    $("#divErrorMessage").html(GetMessage(globalMessageCode.EmailAddress_Validation));
                    modelIsValid = false;
                }
            }
            return modelIsValid;
        }
        else { $("#divErrorMessage").html(GetMessage(globalMessageCode.StartUp_Mandatory)); }
        return modelIsValid;
    });


    $("input[data-required='True']").keypress(function () {
        if ($(this).val() != '') {
            $(this).removeClass('error');
        }
    });
    $("#StartUpContactEmail").keypress(function () {
        if ($(this).val() != '') {
            $(this).removeClass('error');
        }
    });
}
