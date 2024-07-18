var TableAjax = (function () {
    var dataTable;
    var the;
    var ajaxParams = {};
    var customModal;

    var buttonConfirmAction = function (title) {
        var modal = $("#modalViewDetail");

        $(document).on("change", ".form-check-input-event", function (ev) {
            console.log('aaa');
            if ($('.form-check-input-event:checked').length === $('.form-check-input-event').length) {
                $('#selectAll').prop('checked', true);
            } else {
                  $('#selectAll').prop('checked', false);
            }
            if($('.form-check-input-event:checked').length  > 0 ){
              $("#bulk-select-actions").removeClass('d-none')
            }else{
                $("#bulk-select-actions").addClass('d-none')
            }
        });

        
        $(document).on("click", ".is_default_", function (ev) {
            ev.preventDefault();
            var dataName = $(this).attr('data-name');
            var href = $(this).data("href") || $(this).attr("href");
            Swal.fire({
                title: 'Are you sure to set ['+dataName+'] as the default warehouse?',
                text: "After modification, ["+dataName+"] will be set as the shipping warehouse for all new orders",
                icon: 'warning',
                showCancelButton: true,
     
                confirmButtonColor: '#3085d6',
                customClass: {
                    icon: 'custom-icon-size' // Apply custom class to the icon
                },
                cancelButtonColor: '#d33',
                confirmButtonText: 'Confirm',
                preConfirm: function () {
                    return new Promise(function (resolve) {
                        $.post(href)
                            .done(function () {
                                dataTable.ajax.reload();
                                swal.fire(
                                    "Successsfully!",
                                    dataName + " has been default warehouse!.",
                                    "success"
                                );
                            })
                            .fail(function (xhr) {
                                var message = xhr.statusText;
                                if (
                                    xhr.responseJSON &&
                                    xhr.responseJSON.hasOwnProperty("message")
                                ) {
                                    message = xhr.responseJSON.message;
                                }

                                swal.fire("Oops...", message, "error");
                            });
                    });
                },
              });
        });
        $(document).on("click", ".delete-item", function (ev) {
            ev.preventDefault();
            var href = $(this).data("href") || $(this).attr("href");
            var message = $(this).data("message") || $(this).attr("message");
            if(!message){
                message = "You won't be able to revert this!";
            }
            swal.fire({
                title: "Are you sure?",
                html: message,
                icon: "warning",
                showCancelButton: true,
                confirmButtonColor: "#4b7cf3",
                cancelButtonColor: "#dc3545",
                confirmButtonText: "Yes, delete it!",
                showLoaderOnConfirm: true,
                allowOutsideClick: false,
                preConfirm: function () {
                    return new Promise(function (resolve) {
                        
                        
                        $.post(href, { _method: "DELETE" })
                            .done(function () {
                                dataTable.ajax.reload();
                                swal.fire(
                                    "Deleted!",
                                    title + " has been deleted.",
                                    "success"
                                );
                            })
                            .fail(function (xhr) {
                                var message = xhr.statusText;
                                if (
                                    xhr.responseJSON &&
                                    xhr.responseJSON.hasOwnProperty("message")
                                ) {
                                    message = xhr.responseJSON.message;
                                }

                                swal.fire("Oops...", message, "error");
                            });
                    });
                },
            });
        });

        if (modal.length) {
            modal.on("show.bs.modal", function (e) {
                var button = $(e.relatedTarget);
                var json = button.data("json");
                var title = button.data("title");
                var self = $(this);
                self.find("#modalViewDetailTitle").text(title);
                self.find("#json").jsonViewer(json, {
                    collapsed: false,
                    withQuotes: false,
                    withLinks: true,
                    rootCollapsable: true,
                });
            });
        }
    };

    var mergeOptions = function (url, options) {
        options = $.extend(
            true,
            {
                processing: true,
                serverSide: true,
                responsive: true,
                autoWidth: false,
                ajax: {
                    url: url || "",
                    data: function (data) {
                        $.each(ajaxParams, function (key, value) {
                            data[key] = value;
                        });
                    },
                    error: function (jqXhr) {
                        $.notify(
                            {
                                title: "Error: " + jqXhr.status,
                                message: jqXhr.statusText,
                            },
                            { type: "danger" }
                        );
                    },
                },
            },
            options
        );

        return options;
    };

    var refreshBtn = function () {
        var btnRefresh = document.getElementById("btn_refresh");
        if (btnRefresh) {
            btnRefresh.addEventListener("click", function (ev) {
                ev.preventDefault();
                the.resetFilter();
            });
        }
    };

    return {
        initWithAction: function (
            selector,
            url,
            options,
            titleDelete,
            modalCallback
        ) {
            the = this;
            customModal = modalCallback;
            options = mergeOptions(url, options);
            dataTable = $(selector).DataTable(options);
            buttonConfirmAction(titleDelete);
            refreshBtn();
        },
        initWithoutAction: function (selector, url, options) {
            options = mergeOptions(url, options);
            dataTable = $(selector).DataTable(options);
            refreshBtn();
        },
        setAjaxParam: function (name, value) {
            ajaxParams[name] = value;
        },
        getDataTable: function () {
            return dataTable;
        },
        clearAjaxParams: function (name, value) {
            ajaxParams = {};
        },
        resetFilter: function () {
            the.clearAjaxParams();
            dataTable.ajax.reload();
        },
        submitFilter: function (field, value, type, user, types, status) {
            the.clearAjaxParams();
            if (field) the.setAjaxParam("filter_field", field);
            if (value) the.setAjaxParam("filter_value", value);
            if (type) the.setAjaxParam("filter_type", type);
            if (user) the.setAjaxParam("filter_user", user);
            if (types) the.setAjaxParam("filter_types", types);
            if (status) the.setAjaxParam("filter_status", status);
            dataTable.ajax.reload();
        },
    };
})();



