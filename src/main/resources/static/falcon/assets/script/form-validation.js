var FormValidation = function () {
    var formValidation;
    var request;
    var callbackBeforeSend;
    var callbackSuccess;
    var callbackError;
    var callbackNewOption;
    var passwordElements = ['#validation-password'];

    /**
     * Handle Submit form
     * @param node
     * @param formData
     */
    var onSubmit = function (node, formData) {
        if (request) request.abort();

        var inputs = $(node).find('input, select, textarea, button');
        var url = node[0].action;
        var btnSubmit = node[0].querySelector('button[type=submit]');
        var l = Ladda.create(btnSubmit);

        inputs.prop('disabled', true);
      //  l.start();

        $.each(formData, function (k, v) {
            if (v === '' || v === null) delete formData[k];
        });

        if (callbackBeforeSend) {
            formData = $.extend({}, formData, callbackBeforeSend(node, formData))
        }

        var optionAjax = { url: url, method: 'post', data: formData };

        if (callbackNewOption) {
            optionAjax = $.extend({}, optionAjax, callbackNewOption(node, formData));
        }

        request = $.ajax(optionAjax);
        request.done(function (data) {
            if (callbackSuccess) callbackSuccess(data);
            else location.reload();
        });

        request.fail(function (xhr) {
            if ([422, 429].includes(xhr.status)) {
                if (callbackError) callbackError(xhr.responseJSON.errors, node);
                else handleErrors(node, xhr.responseJSON.errors);
            } else {
                var message = xhr.hasOwnProperty('responseJSON') && xhr.responseJSON.hasOwnProperty('message')
                    ? xhr.responseJSON.message
                    : xhr.statusText;
              //  $.notify({ title: 'Error', message: message }, { type: 'danger' })
            }
        });

        request.always(function () {
            inputs.prop('disabled', false);
            l.stop();
        });
    };

    /**
     * Handle Errors 422 from server side
     * @param node
     * @param errors
     */
    var handleErrors = function (node, errors) {
        var input, onlyOnce = true;
        for (var inputName in errors) {
            if (!errors.hasOwnProperty(inputName)) continue;

            input = $(node).find('[name="' + inputName + '"]');

            if (!input[0]) continue;

            if (onlyOnce) {
                window.Validation.hasScrolled = false;
                onlyOnce = false;
            }

            window.Validation.form[undefined].resetOneError(inputName, input);

            for (var i = 0; i < errors[inputName].length; i++) {
                if (typeof errors[inputName][i] !== "string") continue;
                window.Validation.form[undefined].registerError(inputName, errors[inputName][i]);
            }

            window.Validation.form[undefined].displayOneError(inputName);
        }
    };

    /**
     * Handle Password Hide/Show
     */
    var passwordCustom = function () {
        passwordElements.forEach(function (el) {
            $(el).password({
                eyeClass: '',
                eyeOpenClass: 'far fa-eye',
                eyeCloseClass: 'far fe-eye-off'
            });
        });
    };

    return {
        /**
         * Init Form Validation
         * @param passwordEls
         * @param cbSuccess
         * @param cbError
         * @param cbBeforeSend
         * @param cbNewOption
         */
        init: function (passwordEls, cbSuccess, cbError, cbBeforeSend, cbNewOption) {
            if (passwordEls && passwordEls instanceof Array) {
                passwordEls.forEach(function (el) {
                    passwordElements.push(el);
                });

                passwordCustom();
            }

            formValidation = $('#form-validation');
            callbackSuccess = cbSuccess;
            callbackError = cbError;
            callbackBeforeSend = cbBeforeSend;
            callbackNewOption = cbNewOption;

            formValidation.validate({
                submit: {
                    settings: {
                        inputContainer: '.form-group',
                        errorListClass: 'invalid-feedback',
                        errorClass: 'is-invalid',
                        scrollToError: true
                    },
                    callback: {
                        onSubmit: onSubmit
                    }
                }
            });
        },
        getFormElement: function () {
            return formValidation;
        },
        customRemoveError: function (el) {
            el.removeClass('has-danger');
            el.closest('.form-group').removeClass('has-danger');
            el.siblings('.form-control-error').remove();
        },
        resetErrors: function () {
            window.Validation.form[undefined].resetErrors();
        },
        handleClientError: handleErrors
    }
}();