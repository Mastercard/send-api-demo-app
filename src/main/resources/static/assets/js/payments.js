$(document).ready(function () {
    $('[data-toggle="tooltip"]').tooltip();

    setDynamicDropdownValues();

    $("#spinner").removeClass('loading');

    // code to add & remove recipient govverment URI Dynamically
    $("#recipientBtnAdd").on("click", function () {
        const div = $("<div class='col-xl-6 col-lg-6 col-md-6 col-sm-6 col-6 p-0' />");
        div.html(GetRecipientDynamicTextBox(""));
        $("#recipient-governmentid-div").append(div);
    });
    $("body").on("click", ".recipient-remove", function () {
        $(this).closest("div").remove();
    });

    function GetRecipientDynamicTextBox(value) {
        return '<input class="form-control" name="recipientGovernmentIdUris[]" id="recipientGovernmentIdUris" type="text" value = "' + value + '" /><br>' +
            '<input type="button" value="Remove" class="recipient-remove btn btn-danger" /><br><br>'
    }

    // code to add & remove sender govverment URI Dynamically
    $("#senderBtnAdd").on("click", function () {
        const div = $("<div class='col-xl-6 col-lg-6 col-md-6 col-sm-6 col-6 p-0' />");
        div.html(GetSenderDynamicTextBox(""));
        $("#sender-governmentid-div").append(div);
    });
    $("body").on("click", ".sender-remove", function () {
        $(this).closest("div").remove();
    });

    function GetSenderDynamicTextBox(value) {
        return '<input class="form-control" name="senderGovernmentIdUris[]" id="senderGovernmentIdUris" type="text" value = "' + value + '" /><br>' +
            '<input type="button" value="Remove" class="sender-remove btn btn-danger" /><br><br>'
    }

    // code to show message type
    $("#acquiringIdentificationCode_div").addClass('d-block');
    $("#processorId_div").addClass('d-block');
    $("#messageType").change(function () {
        if ($("#messageType").val() == "dualMessage") {
            $("#acquiringBin_div").addClass('d-block');
            $("#acquirerReferenceId_div").addClass('d-block');
            $("#acquiringIdentificationCode_div").removeClass('d-block');
            $("#processorId_div").removeClass('d-block');
        } else if ($("#messageType").val() == "singleMessage") {
            $("#acquiringBin_div").removeClass('d-block');
            $("#acquirerReferenceId_div").removeClass('d-block');
            $("#acquiringIdentificationCode_div").addClass('d-block');
            $("#processorId_div").addClass('d-block');
        }
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

    // set form jQuery validator
    $("#payment-form").validate({
        ignore: [],
        normalizer: function (value) {
            return $.trim(value);
        },
        rules: {
            paymentReference: {
                required: true,
                minlength: 6,
                maxlength: 40,
            },
            amount: {
                required: true,
                minlength: 1,
                maxlength: 12,
            },
            interchangeRateDesignator: {
                minlength: 2,
                maxlength: 2,
            },
            currency: {
                required: true,
                minlength: 3,
                maxlength: 3,
            },
            additionalMessage: {
                maxlength: 65,
            },
            deviceType: {

            },
            fundingSource: {
                required: true,
            },
            merchantCategoryCode: {
                required: true,
                minlength: 4,
                maxlength: 4,
            },
            participationId: {
                maxlength: 30,
            },
            paymentType: {
                required: true,
            },
            qrData: {
                maxlength: 237,
            },
            transactionLocalDateTime: {
                required: true,
            },
            pointOfServiceInteraction: {
                
            },
            transactionPurpose: {
                
            },
            uniqueTransactionReference: {
                maxlength: 19,
            },
            acquiringCountry: {
                minlength: 3,
                maxlength: 3,
            },
            acquiringIca: {
                required: true,
                minlength: 1,
                maxlength: 11,
            },
            messageType: {
                required: true
            },
            acquiringBin: {
                required: function (_element) {
                    return $("#messageType").val() === 'dualMessage' && $("#acquirerReferenceId").val() === '';
                },
                minlength: 6,
                maxlength: 6,
            },
            acquirerReferenceId: {
                required: function (_element) {
                    return $("#messageType").val() === 'dualMessage' && $("#acquiringBin").val() === '';
                },
                minlength: 6,
                maxlength: 6,
            },
            acquiringIdentificationCode: {
                required: function (_element) {
                    return $("#messageType").val() === 'singleMessage';
                },
                minlength: 1,
                maxlength: 9,
            },
            processorId: {
                required: function (_element) {
                    return $("#messageType").val() === 'singleMessage';
                },
                maxlength: 10,
            },
            messageTypeIndicator: {
                minlength: 4,
                maxlength: 4,
            },
            network: {

            },
            processingCode: {

            },
            convenienceAmount: {
                minlength: 1,
                maxlength: 12,
            },
            convenienceIndicator: {
                required: function (_element) {
                    return $("#convenienceAmount").val() !== '';
                },
            },
            transferAcceptorId: {
                required: true,
                minlength: 1,
                maxlength: 15,
            },
            mastercardAssignedMerchantId: {
                maxlength: 6,
            },
            transferAcceptorName: {
                required: true,
                minlength: 1,
                maxlength: 22,
            },
            paymentFacilitatorId: {
                maxlength: 11,
            },
            subMerchantId: {
                maxlength: 15,
            },
            terminalId: {
                minlength: 1,
                maxlength: 8,
            },
            transferAcceptorCity: {
                required: true,
                minlength: 1,
                maxlength: 25,
            },
            transferAcceptorCountry: {
                required: true,
                minlength: 3,
                maxlength: 3,
            },
            transferAcceptorPostalCode: {
                required: true,
                minlength: 1,
                maxlength: 10,
            },
            transferAcceptorState: {
                required: function (_element) {
                    return $("#transferAcceptorCountry").val() === 'USA' || $("#transferAcceptorCountry").val() === 'CAN'
                },
                minlength: 2,
                maxlength: 3,
            },
            transferAcceptorStreet: {
                minlength: 1,
                maxlength: 50,
            },
            recipientAccountUriType: {
                required: true,
            },
            recipient_pan: {
                required: function (_element) {
                    return $("#recipientAccountUriType").val() == 'recipient_pan';
                }
            },
            recipientCountryOfBirth: {
                minlength: 3,
                maxlength: 3,
            },
            recipientDateOfBirth: {

            },
            recipientDsTransactionId: {

            },
            recipientEmail: {
                minlength: 5,
                maxlength: 254,
            },
            recipientAccountNumberType: {
                
            },
            recipientName: {
                minlength: 1,
                maxlength: 120,
            },
            recipientNationality: {
                minlength: 3,
                maxlength: 3,
            },
            recipientPhone: {
                minlength: 1,
                maxlength: 20,
            },
            recipientGovernmentIdUris: {

            },
            recipientAuthenticationValue: {
                minlength: 1,
                maxlength: 32,
            },
            recipientCity: {
                minlength: 1,
                maxlength: 25,
            },
            recipientCountry: {
                minlength: 3,
                maxlength: 3,
            },
            recipientPostalCode: {
                minlength: 1,
                maxlength: 10,
            },
            recipientState: {
                required: function (_element) {
                    return $("#recipientCountry").val() === 'USA' || $("#recipientCountry").val() === 'CAN'
                },
                minlength: 2,
                maxlength: 3,
            },
            recipientStreet: {
                minlength: 1,
                maxlength: 50,
            },
            senderAccountUriType: {
                required: true,
            },
            sender_pan: {
                required: function (_element) {
                    return $("#senderAccountUriType").val() == 'sender_pan';
                }
            },
            senderCountryOfBirth: {
                minlength: 3,
                maxlength: 3,
            },
            senderDateOfBirth: {

            },
            digitalAccountReferenceNumber: {

            },
            senderEmail: {
                minlength: 5,
                maxlength: 254,
            },
            senderAccountNumberType: {
                
            },
            senderName: {
                required: true,
                minlength: 1,
                maxlength: 120,
            },
            senderNationality: {
                minlength: 3,
                maxlength: 3,
            },
            senderPhone: {
                minlength: 1,
                maxlength: 20,
            },
            senderGovernmentIdUris: {

            },
            senderCity: {
                minlength: 1,
                maxlength: 25,
            },
            senderCountry: {
                required: true,
                minlength: 3,
                maxlength: 3,
            },
            senderPostalCode: {
                minlength: 1,
                maxlength: 10,
            },
            senderState: {
                required: function (_element) {
                    return $("#senderCountry").val() === 'USA' || $("#senderCountry").val() === 'CAN'
                },
                minlength: 2,
                maxlength: 3,
            },
            senderStreet: {
                required: true,
                minlength: 1,
                maxlength: 50,
            },
            walletProviderSignature: {
                minlength: 1,
                maxlength: 2058,
            },
            recipientPreferredLanguage: {
                minlength: 2,
                maxlength: 2,
            },
            securityQuestion: {
                minlength: 1,
                maxlength: 40,
            },
            securityAnswer: {
                minlength: 1,
                maxlength: 64,
            },
        },
        messages: {

        },
        submitHandler: function (_form, event) {
            event.preventDefault();
        }
    });

    // on create button click submit form
    $(document).on('click', '#submitBtn', function () {
        if ($('#payment-form').valid()) {
            $("#spinner").addClass('loading');
            const request_object = createRequestObject();
            // API CALL SEND 2.0
            $.ajax({
                data: JSON.stringify(request_object),
                url: SERVER_URL + '/payments',
                type: 'POST',
                dataType: 'json',
                headers: {
                    "Content-Type": "application/json"
                },
                success: function (response, _statusCode, _xhr) {
                    $("#spinner").removeClass('loading');
                    $('#searchPaymentBtn').prop('disabled', false);
                    $('#submitBtn').attr('data-payments-response', JSON.stringify(response));
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

    // on search payment button click submit form
    $(document).on('click', '#searchPaymentBtn', function () {
        localStorage.setItem('paymentResponse', $('#submitBtn').attr("data-payments-response"));
        window.location = '/views/payments-search.html?paymentId=' + true;
    });
});

function createRequestObject() {
    // Below code is use to format request json object
    const serialized = $('#payment-form').serializeArray();
    const data = {};
    for (let s in serialized) {
        data[serialized[s]['name']] = serialized[s]['value'];
    }

    generateRecipientRequest(data);

    generateSenderRequest(data);

    // code block to add walletProviderSignature object
    const additionalProgramData = {
        crossNetwork: {}
    };
    if (data.walletProviderSignature) {
        additionalProgramData.crossNetwork['walletProviderSignature'] = data.walletProviderSignature;
        delete data.walletProviderSignature;
    }
    if (data.recipientPreferredLanguage) {
        additionalProgramData.crossNetwork['recipientPreferredLanguage'] = data.recipientPreferredLanguage;
        delete data.recipientPreferredLanguage;
    }
    if (data.securityQuestion) {
        additionalProgramData.crossNetwork['securityQuestion'] = data.securityQuestion;
        delete data.securityQuestion;
    }
    if (data.securityAnswer) {
        additionalProgramData.crossNetwork['securityAnswer'] = data.securityAnswer;
        delete data.securityAnswer;
    }
    data['additionalProgramData'] = additionalProgramData;

    if (data.walletProviderSignature == '' && data.recipientPreferredLanguage == '' && data.securityQuestion == '' && data.securityAnswer == '') {
        delete data['additionalProgramData'];
    }

    generateTransferAcceptorRequest(data);

    generateAcquiringCredentialsRequest(data);

    // change date format transactionLocalDateTime
    data['transactionLocalDateTime'] = moment(data.transactionLocalDateTime).format('YYYY-MM-DDTHH:MM:SS');

    // remove blank keys
    Object.keys(data).forEach(k => (!data[k] && data[k] !== undefined) && delete data[k]);
    const req = JSON.stringify(data, undefined, 4);
    $('#request-object').text(req);

    return data;
}

function setDynamicDropdownValues() {
    // code to fetch "PAYMENT TYPES", "TRANSACTION PURPOSE", "PROCESSING CODE", "DEVICE TYPE", "FUNDING SOURCE", "CONVENIENCE INDICATOR" using JSON file
    const dynamicDropdowns = ['paymentType', 'transactionPurpose', 'processingCode', 'deviceType', 'fundingSource', 'convenienceIndicator', 'recipientAccountNumberType', 'senderAccountNumberType', 'pointOfServiceInteraction'];
    for (let dynamicDropdown of dynamicDropdowns) {
        let dropdown = $('#' + dynamicDropdown + '');
        dropdown.empty();
        let url = '';
        if (dynamicDropdown === 'paymentType') {
            url = '/assets/config/payment-type.json';
        } else if (dynamicDropdown === 'transactionPurpose') {
            url = '/assets/config/transaction-purpose.json';
        } else if (dynamicDropdown === 'processingCode') {
            url = '/assets/config/processing-code.json';
        } else if (dynamicDropdown === 'deviceType') {
            url = '/assets/config/device-type.json';
        } else if (dynamicDropdown === 'fundingSource') {
            url = '/assets/config/funding-source.json';
        } else if (dynamicDropdown === 'pointOfServiceInteraction') {
            url = '/assets/config/point-of-service-interation.json';
        } else if (dynamicDropdown === 'convenienceIndicator') {
            url = '/assets/config/convenience-indicator.json';
        } else if (dynamicDropdown === 'recipientAccountNumberType' || dynamicDropdown === 'senderAccountNumberType') {
            url = '/assets/config/account-number-type.json';
        }
        // Populate dropdown with list
        $.getJSON(url, function (data) {
            $.each(data, function (_key, entry) {
                dropdown.append($('<option></option>').attr('value', entry.value).text(entry.name));
            });
        });
    }
}

function generateTransferAcceptorRequest(data) {
    // code block to add transferAcceptor object
    const transferAcceptor = {
        address: {}
    };
    if (data.transferAcceptorId) {
        transferAcceptor['id'] = data.transferAcceptorId;
        delete data.transferAcceptorId;
    }
    if (data.mastercardAssignedMerchantId) {
        transferAcceptor['mastercardAssignedMerchantId'] = data.mastercardAssignedMerchantId;
        delete data.mastercardAssignedMerchantId;
    }
    if (data.transferAcceptorName) {
        transferAcceptor['name'] = data.transferAcceptorName;
        delete data.transferAcceptorName;
    }
    if (data.paymentFacilitatorId) {
        transferAcceptor['paymentFacilitatorId'] = data.paymentFacilitatorId;
        delete data.paymentFacilitatorId;
    }
    if (data.subMerchantId) {
        transferAcceptor['subMerchantId'] = data.subMerchantId;
        delete data.subMerchantId;
    }
    if (data.terminalId) {
        transferAcceptor['terminalId'] = data.terminalId;
        delete data.terminalId;
    }
    if (data.convenienceAmount) {
        transferAcceptor['convenienceAmount'] = data.convenienceAmount;
        delete data.convenienceAmount;
    }
    if (data.convenienceIndicator) {
        transferAcceptor['convenienceIndicator'] = data.convenienceIndicator;
        delete data.convenienceIndicator;
    }

    // add address transferAcceptor fields
    if (data.transferAcceptorCity) {
        transferAcceptor.address['city'] = data.transferAcceptorCity;
        delete data.transferAcceptorCity;
    }
    if (data.transferAcceptorCountry) {
        transferAcceptor.address['country'] = data.transferAcceptorCountry;
        delete data.transferAcceptorCountry;
    }
    if (data.transferAcceptorPostalCode) {
        transferAcceptor.address['postalCode'] = data.transferAcceptorPostalCode;
        delete data.transferAcceptorPostalCode;
    }
    if (data.transferAcceptorState) {
        transferAcceptor.address['state'] = data.transferAcceptorState;
        delete data.transferAcceptorState;
    }
    if (data.transferAcceptorStreet) {
        transferAcceptor.address['street'] = data.transferAcceptorStreet;
        delete data.transferAcceptorStreet;
    }
    data['transferAcceptor'] = transferAcceptor;
}

function generateAcquiringCredentialsRequest(data) {
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
            acquiringCredentials.dualMessage['acquiringBin'] = data.acquiringBin;
            delete data.acquiringBin;
            acquiringCredentials.dualMessage['acquirerReferenceId'] = data.acquirerReferenceId;
            delete data.acquirerReferenceId;

            delete data.acquiringIdentificationCode;
            delete data.processorId;
            break;
        case 'singleMessage':
            acquiringCredentials.singleMessage['acquiringIdentificationCode'] = data.acquiringIdentificationCode;
            delete data.acquiringIdentificationCode;
            acquiringCredentials.singleMessage['processorId'] = data.processorId;
            delete data.processorId;

            delete data.acquiringBin;
            delete data.acquirerReferenceId;
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
}

function generateRecipientRequest(data) {
    if (data.recipientAccountUriType) {
        const recipient = {
            address: {},
            authentication: {}
        };
        // Assign recipient AccountUri Value
        if (data.recipientAccountUriType === 'recipient_pan') {
            recipient['accountUri'] = data.recipient_pan;
            delete data.recipient_pan;
        }

        // add normal recipient fields
        if (data.recipientCountryOfBirth) {
            recipient['countryOfBirth'] = data.recipientCountryOfBirth;
            delete data.recipientCountryOfBirth;
        }
        if (data.recipientDateOfBirth) {
            recipient['dateOfBirth'] = moment(data.recipientDateOfBirth).format('YYYY-MM-DD');
            delete data.recipientDateOfBirth;
        }
        if (data.recipientDsTransactionId) {
            recipient['dsTransactionId'] = data.recipientDsTransactionId;
            delete data.recipientDsTransactionId;
        }
        if (data.recipientEmail) {
            recipient['email'] = data.recipientEmail;
            delete data.recipientEmail;
        }
        if (data.recipientAccountNumberType) {
            recipient['accountNumberType'] = data.recipientAccountNumberType;
            delete data.recipientAccountNumberType;
        }
        if (data.recipientName) {
            recipient['name'] = data.recipientName;
            delete data.recipientName;
        }
        if (data.recipientNationality) {
            recipient['nationality'] = data.recipientNationality;
            delete data.recipientNationality;
        }
        if (data.recipientPhone) {
            recipient['phone'] = data.recipientPhone;
            delete data.recipientPhone;
        }

        // add address recipient fields
        if (data.recipientCity) {
            recipient.address['city'] = data.recipientCity;
            delete data.recipientCity;
        }
        if (data.recipientCountry) {
            recipient.address['country'] = data.recipientCountry;
            delete data.recipientCountry;
        }
        if (data.recipientPostalCode) {
            recipient.address['postalCode'] = data.recipientPostalCode;
            delete data.recipientPostalCode;
        }
        if (data.recipientState) {
            recipient.address['state'] = data.recipientState;
            delete data.recipientState;
        }
        if (data.recipientStreet) {
            recipient.address['street'] = data.recipientStreet;
            delete data.recipientStreet;
        }

        if (data.recipientAuthenticationValue) {
            recipient.authentication['authenticationValue'] = data.recipientAuthenticationValue;
            delete data.recipientAuthenticationValue;
        }

        // code block to add government ID URI's (multiple)
        const values = [];
        $('input:text[name="recipientGovernmentIdUris[]"]').each(function () {
            values.push($(this).val());
        });
        delete data["recipientGovernmentIdUris[]"];
        recipient.governmentIdUris = values;

        // Final recipient object
        data['recipient'] = recipient;
    }
    delete data.recipientAccountUriType;
}

function generateSenderRequest(data) {
    if (data.senderAccountUriType) {
        const sender = {
            address: {}
        };
        // Assign sender AccountUri Value
        if (data.senderAccountUriType === 'sender_pan') {
            sender['accountUri'] = data.sender_pan;
            delete data.sender_pan;
        }

        // add normal sender fields
        if (data.senderCountryOfBirth) {
            sender['countryOfBirth'] = data.senderCountryOfBirth;
            delete data.senderCountryOfBirth;
        }
        if (data.senderDateOfBirth) {
            sender['dateOfBirth'] = moment(data.senderDateOfBirth).format('YYYY-MM-DD');
            delete data.senderDateOfBirth;
        }
        if (data.digitalAccountReferenceNumber) {
            sender['digitalAccountReferenceNumber'] = data.digitalAccountReferenceNumber;
            delete data.digitalAccountReferenceNumber;
        }
        if (data.senderEmail) {
            sender['email'] = data.senderEmail;
            delete data.senderEmail;
        }
        if (data.senderAccountNumberType) {
            sender['accountNumberType'] = data.senderAccountNumberType;
            delete data.senderAccountNumberType;
        }
        if (data.senderName) {
            sender['name'] = data.senderName;
            delete data.senderName;
        }
        if (data.senderNationality) {
            sender['nationality'] = data.senderNationality;
            delete data.senderNationality;
        }
        if (data.senderPhone) {
            sender['phone'] = data.senderPhone;
            delete data.senderPhone;
        }

        // add address sender fields
        if (data.senderCity) {
            sender.address['city'] = data.senderCity;
            delete data.senderCity;
        }
        if (data.senderCountry) {
            sender.address['country'] = data.senderCountry;
            delete data.senderCountry;
        }
        if (data.senderPostalCode) {
            sender.address['postalCode'] = data.senderPostalCode;
            delete data.senderPostalCode;
        }
        if (data.senderState) {
            sender.address['state'] = data.senderState;
            delete data.senderState;
        }
        if (data.senderStreet) {
            sender.address['street'] = data.senderStreet;
            delete data.senderStreet;
        }

        // code block to add government ID URI's (multiple)
        const senderGovURIs = [];
        $('input:text[name="senderGovernmentIdUris[]"]').each(function () {
            senderGovURIs.push($(this).val());
        });
        delete data["senderGovernmentIdUris[]"];
        sender.governmentIdUris = senderGovURIs;

        // Final sender object
        data['sender'] = sender;
    }
    delete data.senderAccountUriType;
}