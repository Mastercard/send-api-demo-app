$(function () {
    $('[data-toggle="tooltip"]').tooltip();
    $('#partnerId').val(PARTNER_ID);

    // set form jQuery validator
    $("#retrieve-partner-limit-form").validate({
        ignore: [],
        normalizer: function (value) {
            return $.trim(value);
        },
        rules: {
            partnerId: {
                required: true,
            },
            accept: {},
        },
        messages: {},
        submitHandler: function (_form, event) {
            event.preventDefault();
        },
    });

    // on create button click submit form
    $(document).on("click", "#submitBtn", function () {
        const acceptHeaders = $("#accept").val();
        if ($("#retrieve-partner-limit-form").valid()) {
            $("#spinner").addClass("loading");
            let headers = {
                Accept: acceptHeaders,
            };
            const URL = SERVER_URL + "/" + $("#partnerId").val() + "/limits";
            $("#request-url").html(URL);

            $.ajax({
                url: URL,
                type: "GET",
                dataType: "json",
                headers: headers,
                success: function (response, _statusCode, _xhr) {
                    $("#spinner").removeClass("loading");

                    $("#submitBtn").attr(
                        "data-retrieve-partner-limit-request",
                        JSON.stringify($("#enroll-card-form").serializeArray())
                    );
                    $("#submitBtn").attr(
                        "data-retrieve-partner-limit-response",
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
});
