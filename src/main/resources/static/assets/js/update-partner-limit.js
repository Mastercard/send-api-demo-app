$(function () {
    $('[data-toggle="tooltip"]').tooltip();
    $('#partnerId').val(PARTNER_ID);

    // code to add & remove Alert Emails Dynamically
    $("#alertEmailsBtnAdd").on("click", function () {
        const div = $("<div class='row pl-0 alert-emails-div-sub-div'/>");
        div.html(GeAlertEmailsDynamicTextBox(""));
        $("#alert-emails-div").append(div);
    });
    $("body").on("click", ".alert-emails-div-remove", function () {
        $(this).closest("div.alert-emails-div-sub-div").remove();
    });

    function GeAlertEmailsDynamicTextBox(value) {
        return (
            '<div class="col-xl-6 col-lg-6 col-md-12 col-sm-12 col-12 mb-3"><label for="alertEmails">Alert Emails</label><input class="form-control" name="alertEmails[]" id="alertEmails" type="text" value = "' +
            value +
            '" /></div><br><br>' +
            '<div class="col-xl-12 col-lg-12 col-md-12 col-sm-12 col-12"><input type="button" value="REMOVE" class="alert-emails-div-remove btn btn-danger" /></div><br><br>'
        );
    }

    // set form jQuery validator
    $("#update-partner-limit-form").validate({
        ignore: [],
        normalizer: function (value) {
            return $.trim(value);
        },
        rules: {
            partnerId: {
                required: true,
            },
            partnerLimitReference: {
                required: true,
            },
            accept: {},
            contentType: {},
            dailyTransactionAmount: {
                required: true,
            },
            dailyTransactionCurrency: {
                required: true,
            },
            maxTransactionVolume: {},
            alerEmails: {
                required: true,
            },
            alertsEnabled: {
                required: true,
            },
            amountAlertThreshold: {
                required: true,
            },
            volumeAlertThreshold: {},
        },
        messages: {},
        submitHandler: function (_form, event) {
            event.preventDefault();
        },
    });

    // on create button click submit form
    $(document).on("click", "#submitBtn", function () {
        if ($("#update-partner-limit-form").valid()) {
            $("#spinner").addClass("loading");
            const request_object = createRequestObject();
            const URL =
                SERVER_URL +
                "/" +
                $("#partnerId").val() +
                "/limits/daily-limits/" +
                $("#partnerLimitReference").val();
            const acceptHeaders = $("#accept").val();
            const contentTypeHeaders = $("#contentType").val();
            let headers = {
                Accept: acceptHeaders,
                "Content-Type": contentTypeHeaders,
            };
            $.ajax({
                data: JSON.stringify(request_object),
                url: URL,
                type: "PUT",
                dataType: "json",
                headers: headers,
                success: function (response, _statusCode, _xhr) {
                    $("#spinner").removeClass("loading");

                    $("#submitBtn").attr(
                        "data-update-partner-limit-request",
                        JSON.stringify(
                            $("#update-partner-limit-form").serializeArray()
                        )
                    );
                    $("#submitBtn").attr(
                        "data-update-partner-limit-response",
                        JSON.stringify(response)
                    );
                    // Below code is used to format response json object
                    $("#response-object").text(
                        JSON.stringify(response, undefined, 4)
                    );
                },
                error: function (jqXHR, _textStatus, _errorThrown) {
                    $("#spinner").removeClass("loading");
                    // Below code is used to format errorThrown json object
                    $("#response-object").text(
                        JSON.stringify(
                            JSON.parse(jqXHR.responseText),
                            undefined,
                            4
                        )
                    );
                },
            });
        }
    });

    function createRequestObject() {
        // Below code is used to format request json object
        let serialized = $("#update-partner-limit-form").serializeArray();
        let data = {};
        for (let s in serialized) {
            data[serialized[s]["name"]] = serialized[s]["value"];
        }

        // Remove unwanted form values from request object
        if (data.accept) {
            delete data.accept;
        }
        if (data.contentType) {
            delete data.contentType;
        }
        if (data.partnerId) {
            delete data.partnerId;
        }
        if (data.partnerLimitReference) {
            delete data.partnerLimitReference;
        }

        let transactionAmountLimitValues = {};
        if (data.dailyTransactionAmount || data.dailyTransactionCurrency) {
            transactionAmountLimitValues["transactionAmountLimit"] = {};

            transactionAmountLimitValues.transactionAmountLimit["amount"] =
                data.dailyTransactionAmount;
            delete data.dailyTransactionAmount;
            transactionAmountLimitValues.transactionAmountLimit["currency"] =
                data.dailyTransactionCurrency;
            delete data.dailyTransactionCurrency;
        }

        if (data.maxTransactionVolume) {
            transactionAmountLimitValues["transactionVolumeLimit"] = {};
            transactionAmountLimitValues.transactionVolumeLimit[
                "maxTransactionVolume"
            ] = data.maxTransactionVolume;
            delete data.maxTransactionVolume;
        }

        data["dailyLimit"] = transactionAmountLimitValues;

        let alertSettingsValues = {};
        if (data.alertsEnabled) {
            alertSettingsValues["alertsEnabled"] = Boolean(data.alertsEnabled);
            delete data.alertsEnabled;
        }
        if (data.amountAlertThreshold) {
            alertSettingsValues["amountAlertThreshold"] = Number(
                data.amountAlertThreshold
            );
            delete data.amountAlertThreshold;
        }
        if (data.volumeAlertThreshold) {
            alertSettingsValues["volumeAlertThreshold"] = Number(
                data.volumeAlertThreshold
            );
            delete data.volumeAlertThreshold;
        }

        // code block to add alertEmails object (multiple)
        let alertEmailsvalues = [];
        $('input:text[name="alertEmails[]"]').each(function () {
            if ($(this).val()) {
                alertEmailsvalues.push($(this).val());
            }
        });
        delete data["alertEmails[]"];
        if (alertEmailsvalues.length > 0) {
            alertSettingsValues.alertEmails = alertEmailsvalues;
        }

        data["alertSettings"] = alertSettingsValues;

        // remove blank keys
        Object.keys(data).forEach(
            (k) => !data[k] && data[k] !== undefined && delete data[k]
        );
        let req = JSON.stringify(data, undefined, 4);

        $("#request-object").text(req);
        return data;
    }
});
