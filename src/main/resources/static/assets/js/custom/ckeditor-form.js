$(document).ready(function () {
    var formMode = '[[${formMode}]]';

    if (formMode !== 'view') {
        var ckClassicEditor = document.querySelectorAll("#form-description");
        if (ckClassicEditor) {
            Array.from(ckClassicEditor).forEach(function () {
                ClassicEditor
                    .create(document.querySelector('#form-description'))
                    .then(function (editor) {
                        editor.ui.view.editable.element.style.height = '200px';
                    })
                    .catch(function (error) {
                        console.error(error);
                    });
            });
        }
    } else {
        $('#form-description').attr('readonly', true); // Make textarea readonly if formMode is 'view'
    }
});