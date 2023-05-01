$(function () {
    $('[data-toggle="tooltip"]').tooltip();
    $('#partnerId').val(PARTNER_ID);

    // code to fetch dropdown values using JSON file
    let dynamicDropdowns = [
        "transactionType",
        "paymentType",
        "cardProductType",
        "cardType",
        "messageSystem",
    ];
    for (let dynamicDropdown of dynamicDropdowns) {
        let dropdown = $("#" + dynamicDropdown + "");
        dropdown.empty();
        let url = "";
        if (dynamicDropdown === "transactionType") {
            url = "/assets/config/transaction-type.json";
        } else if (dynamicDropdown === "paymentType") {
            url = "/assets/config/payment-type.json";
        } else if (dynamicDropdown === "cardProductType") {
            url = "/assets/config/card-product-type.json";
        } else if (dynamicDropdown === "cardType") {
            url = "/assets/config/card-type.json";
        } else if (dynamicDropdown === "messageSystem") {
            url = "/assets/config/message-type.json";
        } 

        // Populate dropdown with list
        $.getJSON(url, function (data) {
            $.each(data, function (_key, entry) {
                dropdown.append(
                    $("<option></option>")
                        .attr("value", entry.value)
                        .text(entry.name)
                );
            });
        });
    }

    // code to add & remove Aler Emails Dynamically
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
    $("#add-account-limit-form").validate({
        ignore: [],
        normalizer: function (value) {
            return $.trim(value);
        },
        rules: {
            partnerId: {
                required: true,
            },
            transferAcceptorId: {},
            accept: {},
            contentType: {},
            network: {},
            transactionType: {},
            paymentType: {},
            cardProductType: {},
            cardType: {},
            messageSystem: {},
            minTransactionAmount: {},
            dailyMaxAmount: {},
            weeklyMaxAmount: {},
            monthlyMaxAmount: {},
            dailyMaxVolume: {},
            weeklyMaxVolume: {},
            monthlyMaxVolume: {},
        },
        messages: {},
        submitHandler: function (_form, event) {
            event.preventDefault();
        },
    });

    // on create button click submit form
    $(document).on("click", "#submitBtn", function () {
        if ($("#add-account-limit-form").valid()) {
            $("#spinner").addClass("loading");
            const request_object = createRequestObject();
            const URL =
                SERVER_URL +
                "/" +
                $("#partnerId").val() +
                "/limits/daily-limits/account-limits";
            const acceptHeaders = $("#accept").val();
            const contentTypeHeaders = $("#contentType").val();
            let headers = {
                Accept: acceptHeaders,
                "Content-Type": contentTypeHeaders,
            };
            $.ajax({
                data: JSON.stringify(request_object),
                url: URL,
                type: "POST",
                dataType: "json",
                headers: headers,
                success: function (response, _statusCode, _xhr) {
                    $("#spinner").removeClass("loading");

                    $("#submitBtn").attr(
                        "data-add-account-limit-request",
                        JSON.stringify(
                            $("#add-account-limit-form").serializeArray()
                        )
                    );
                    $("#submitBtn").attr(
                        "data-add-account-limit-response",
                        JSON.stringify(response)
                    );
                    // Below code is use to format response json object
                    $("#response-object").text(
                        JSON.stringify(response, undefined, 4)
                    );
                },
                error: function (jqXHR, _textStatus, _errorThrown) {
                    $("#spinner").removeClass("loading");
                    // Below code is use to format errorThrown json object
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
        // Below code is use to format request json object
        let serialized = $("#add-account-limit-form").serializeArray();
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


        // remove blank keys
        Object.keys(data).forEach(
            (k) => !data[k] && data[k] !== undefined && delete data[k]
        );
        let req = JSON.stringify(data, undefined, 4);

        $("#request-object").text(req);
        return data;
    }
});
