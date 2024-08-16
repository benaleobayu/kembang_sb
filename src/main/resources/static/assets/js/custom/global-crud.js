$(document).ready(function () {
    var formMode = '[[${formMode}]]';

    if (formMode !== 'view') {
        var ckClassicEditor = document.querySelectorAll("#form-description, #form-description_id, #form-description_en");
        if (ckClassicEditor) {
            Array.from(ckClassicEditor).forEach(function (element) {
                ClassicEditor
                    .create(document.querySelector(`#${element.id}`))
                    .then(function (editor) {
                        editor.ui.view.editable.element.style.height = '400px';
                    })
                    .catch(function (error) {
                        console.error(error);
                    });
            });
        }
    } else {
        $('#form-description, #form-description_id, #form-description_en').prop('readonly', true); // Make textarea readonly if formMode is 'view'
    }
});

$(document).ready(function() {
    $('#form-status').select2({
        placeholder: "Select a status",
        allowClear: true,
    });
});