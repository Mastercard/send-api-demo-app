$(document).ready(function () {
    $('[data-toggle="tooltip"]').tooltip();

    // set form jQuery validator
    $("#payment-search-form").validate({
        ignore: [],
        normalizer: function (value) {
            return $.trim(value);
        },
        rules: {
            paymentReference: {
                required: function (_element) {
                    return $("#paymentId").val() === '';
                }
            },
            paymentId: {
                required: function (_element) {
                    return $("#paymentReference").val() === '';
                }
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
        if ($('#payment-search-form').valid() &&
            ($('#paymentReference').val() != '' || $('#paymentId').val() != '')) {
            $("#spinner").addClass('loading');
            const request_object = createRequestObject();
            // API CALL SEND 2.0
            $.ajax({
                data: JSON.stringify(request_object),
                url: SERVER_URL + '/payments/searches',
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
        const serialized = $('#payment-search-form').serializeArray();
        const data = {};
        for (let s in serialized) {
            data[serialized[s]['name']] = serialized[s]['value'];
        }

        // remove blank keys
        Object.keys(data).forEach(k => (!data[k] && data[k] !== undefined) && delete data[k]);
        const req = JSON.stringify(data, undefined, 4);
        $('#request-object').text(req);
        return data;
    }
});