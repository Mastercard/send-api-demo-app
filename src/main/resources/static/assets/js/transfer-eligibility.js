$(document).ready(function () {
    $('[data-toggle="tooltip"]').tooltip();

    // code to fetch "PAYMENT TYPES" using JSON file
    let paymentTypeDropdown = $('#paymentType');
    paymentTypeDropdown.empty();
    const paymentType = '/assets/config/payment-type.json';
    // Populate dropdown with list of payment type
    $.getJSON(paymentType, function (data) {
        $.each(data, function (_key, entry) {
            paymentTypeDropdown.append($('<option></option>').attr('value', entry.value).text(entry.name));
        })
    });

    const recipientDiv = ['recipient_pan'];

    // function to generate recipient div as per recipient account selection
    function generateRecipientDiv(recipientAccount) {
        for (let singleRecDiv of recipientDiv) {
            $("#" + singleRecDiv + "").val('');
            if (singleRecDiv == recipientAccount) {
                $("#" + singleRecDiv + "_div").addClass('d-block');
            } else {
                $("#" + singleRecDiv + "_div").removeClass('d-block');
            }
        }
    }

    // code to show and hide recipient div
    $("#recipient_pan_div").addClass('d-block');
    $("#recipientAccountUriType").change(function () {
        if ($("#recipientAccountUriType").val() == "recipient_pan") {
            generateRecipientDiv('recipient_pan');
        }
    });


    const senderDiv = ['sender_pan'];

    // function to generate sender div as per sender account selection
    function generateSenderDiv(senderAccount) {
        for (let singleSenderDiv of senderDiv) {
            $("#" + singleSenderDiv + "").val('');
            if (singleSenderDiv == senderAccount) {
                $("#" + singleSenderDiv + "_div").addClass('d-block');
            } else {
                $("#" + singleSenderDiv + "_div").removeClass('d-block');
            }
        }
    }

    // code to show and hide sender div
    $("#sender_pan_div").addClass('d-block');
    $("#senderAccountUriType").change(function () {
        if ($("#senderAccountUriType").val() == "sender_pan") {
            generateSenderDiv('sender_pan');
        }
    });

    // code to show message type
    $("#acquiringIdentificationCode_div").addClass('d-block');
    $("#processorId_div").addClass('d-block');
    $("#messageType").change(function () {
        if ($("#messageType").val() === "dualMessage") {
            $("#acquirerReferenceId_div").addClass('d-block');
            $("#acquiringIdentificationCode_div").removeClass('d-block');
            $("#processorId_div").removeClass('d-block');
        } else if ($("#messageType").val() === "singleMessage") {
            $("#acquirerReferenceId_div").removeClass('d-block');
            $("#acquiringIdentificationCode_div").addClass('d-block');
            $("#processorId_div").addClass('d-block');
        }
    });

    // set form jQuery validator
    $("#transfer-eligibility-form").validate({
        ignore: [],
        normalizer: function (value) {
            return $.trim(value);
        },
        rules: {
            acquirerCountry: {
                required: true,
                minlength: 3,
                maxlength: 3
            },
            amount: {
                minlength: 1,
                maxlength: 12,
            },
            currency: {
                minlength: 3,
                maxlength: 3,
                required: function (_element) {
                    return $("#amount").val() != '';
                }
            },
            paymentType: {
                required: true,
            },
            recipient_pan: {
                required: function (_element) {
                    return $("#sender_pan").val() === '';
                }
            },
            sender_pan: {
                required: function (_element) {
                    return $("#recipient_pan").val() === '';
                }
            },
            transferAcceptorCountry: {
                required: true,
                minlength: 3,
                maxlength: 3,
            },
            messageType: {
                required: true
            },
            acquirerReferenceId: {
                required: function (_element) {
                    return $("#messageType").val() == 'dualMessage';
                },
                minlength: 6,
                maxlength: 6,
            },
            acquiringIdentificationCode: {
                required: function (_element) {
                    return $("#messageType").val() == 'singleMessage';
                },
                minlength: 1,
                maxlength: 9,
            },
            processorId: {
                required: function (_element) {
                    return $("#messageType").val() == 'singleMessage';
                },
                maxlength: 10,
            },
            walletProviderSignature: {
                minlength: 1,
                maxlength: 2058,
            }
        },
        messages: {

        },
        submitHandler: function (_form, event) {
            event.preventDefault();
        }
    });

    // on create button click submit form
    $(document).on('click', '#submitBtn', function () {
        if ($('#transfer-eligibility-form').valid()) {
            $("#spinner").addClass('loading');
            const request_object = createRequestObject();
            // API CALL SEND 2.0
            $.ajax({
                data: JSON.stringify(request_object),
                url: SERVER_URL + '/transfer-eligibilities',
                type: 'POST',
                dataType: 'json',
                headers: {
                    "Content-Type": "application/json"
                },
                success: function (response, _statusCode, _xhr) {
                    $("#spinner").removeClass('loading');
                    // Below code is use to format response json object
                    $('#response-object').text(JSON.stringify(response, undefined, 4));
                },
                error: function (jqXHR, _textStatus, _errorThrown) {
                    $("#spinner").removeClass('loading');
                    // Below code is use to format errorThrown json object
                    $('#response-object').text(JSON.stringify(JSON.parse(jqXHR.responseText), undefined, 4));
                }
            });

        }
    });

    function createRequestObject() {
        // Below code is use to format request json object
        const serialized = $('#transfer-eligibility-form').serializeArray();
        const data = {};
        for (let s in serialized) {
            data[serialized[s]['name']] = serialized[s]['value'];
        }

        if (data.recipient_pan) {
            data['recipientAccountUri'] = data.recipient_pan;
            delete data.recipient_pan;
        }


        if (data.sender_pan) {
            data['senderAccountUri'] = data.sender_pan;
            delete data.sender_pan;
        }

        if (data.walletProviderSignature) {
            const additionalProgramData = {
                crossNetwork: {
                    walletProviderSignature: data.walletProviderSignature
                }
            };
            data['additionalProgramData'] = additionalProgramData;
            delete data.walletProviderSignature;
        }

        // code block to add acquiringCredentials object
        const acquiringCredentials = {
            dualMessage: {},
            singleMessage: {}
        };
        if (data.acquiringCountry) {
            acquiringCredentials['acquiringCountry'] = data.acquiringCountry;
            delete data.acquiringCountry;
        }
        if (data.acquiringIca) {
            acquiringCredentials['acquiringIca'] = data.acquiringIca;
            delete data.acquiringIca;
        }
        switch (data.messageType) {
            case 'dualMessage':
                acquiringCredentials.dualMessage['acquirerReferenceId'] = data.acquirerReferenceId;
                delete data.acquirerReferenceId;
                break;
            case 'singleMessage':
                acquiringCredentials.singleMessage['acquiringIdentificationCode'] = data.acquiringIdentificationCode;
                delete data.acquiringIdentificationCode;
                acquiringCredentials.singleMessage['processorId'] = data.processorId;
                delete data.processorId;
                break;
        }

        if (data.messageType == 'dualMessage') {
            delete acquiringCredentials.singleMessage;
        }
        if (data.messageType == 'singleMessage') {
            delete acquiringCredentials.dualMessage;
        }
        delete data.messageType;
        data['acquiringCredentials'] = acquiringCredentials;

        // remove blank keys
        Object.keys(data).forEach(k => (!data[k] && data[k] !== undefined) && delete data[k]);
        const req = JSON.stringify(data, undefined, 4);
        $('#request-object').text(req);
        return data;
    }

});