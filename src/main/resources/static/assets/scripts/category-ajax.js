$('.has_parent').change(function(){



    if(this.checked){ //show

        $("#category").prop( "disabled", false );

        $('.parent').removeClass('d-none');

    }

    else{ // hidden

        $("#category").prop( "disabled", true );

        $('.parent').addClass('d-none');

        $("#sub_category").prop( "disabled", true );

        $('.sub_category').addClass('d-none');

        $('#has_child').prop('checked',false);

    }

});

$('#has_child').change(function(){

    if(this.checked){

        id=$('select[name=master_category] option').filter(':selected').val()

        $.ajax({

            method: 'get',

            url: 'getsubparent/'+id,

            success: function(resp) {

                console.log(resp);

                $('#sub_category').empty();

                $.each( resp, function( key, value ) {

                    $('#sub_category').append('<option value="'+value.id+'">'+value.name+'</option>')

                });





            },

            error: function() {



            }

        });

        $("#category").change(function(){

            id=$('select[name=master_category] option').filter(':selected').val()

            $.ajax({

                method: 'get',

                url: 'getsubparent/'+id,

                success: function(resp) {

                    $('#sub_category').empty();

                    $.each( resp, function( key, value ) {

                        $('#sub_category').append('<option value="'+value.id+'">'+value.name+'</option>')

                    });





                },

                error: function() {



                }

            });

        })

        $("#sub_category").prop( "disabled", false );

        $('.sub_category').removeClass('d-none');

    }

    else{

        $("#sub_category").prop( "disabled", true );

        $('.sub_category').addClass('d-none');

    }

});

function select2Category(master_category,sub_category,sub_parent,urls,urls_sub){

    master_category.change(function(){

        id = $("#"+master_category.attr('id')).find(':selected').val();

        $.ajax({

            url: urls+'/'+id,

            success : (function (data) {

                if(data.length>0){

                    // $(master_category).closest('div').removeClass('col-md-12').addClass('col-md-4');

                    // $(sub_category).closest('div').removeClass('d-none');

                    sub_category.empty();

                    $.each( data, function( key, value ) {

                        sub_category.append('<option value="'+value.id+'">'+value.name+'</option>')

                    });

                    sub_category.select2();

                    id_subcategory = $("#"+sub_category.attr('id')).find(':selected').val();

                    console.log(id_subcategory);

                    if (id_subcategory > 0) {

                        $.ajax({

                            url: urls_sub +'/'+ id_subcategory,

                            success : (function (data) {

                                if(data.length>0){

                                    sub_parent.empty();

                                    $.each( data, function( key, value ) {

                                        sub_parent.append('<option value="'+value.id+'">'+value.name+'</option>')

                                    });

                                    sub_parent.select2();

                                }

                            }),

                            error: function (jqXhr) {

                                $.notify(

                                    {

                                        title: "Error: " + jqXhr.status,

                                        message: jqXhr.statusText,

                                    },

                                    { type: "danger" }

                                );

                            },

                        });

                    }

                }







            }),

            error: function (jqXhr) {

                $.notify(

                    {

                        title: "Error: " + jqXhr.status,

                        message: jqXhr.statusText,

                    },

                    { type: "danger" }

                );

            },

        });

    });

    sub_category.change(function(){

        id = $("#"+sub_category.attr('id')).find(':selected').val();

        console.log(id);

        $.ajax({

            url: urls_sub+'/'+id,

            success : (function (data) {

                $(sub_parent).closest('div').removeClass('d-none');

                sub_parent.empty();

                $.each( data, function( key, value ) {

                    sub_parent.append('<option value="'+value.id+'">'+value.name+'</option>')

                });

                sub_parent.select2();

            }),

            error: function (jqXhr) {

                $.notify(

                    {

                        title: "Error: " + jqXhr.status,

                        message: jqXhr.statusText,

                    },

                    { type: "danger" }

                );

            },

        });

    });

}

var id_product;

var id_varian;

var ids;

function select2Product(a,b){

    ids=b;

    $(a).select2({

        ajax: {

            url: "produk",

            dataType: 'json',

            delay: 250,

            type: "post",

            data: function (params) {

                return {

                    q: params.term, // search term

                    page: params.page

                };

            },

            processResults: function (data, params) {

                params.page = params.page || 1;

                // console.log(data);

                return {

                    results: data.items,

                    pagination: {

                        more: (params.page * 30) < data.total_count

                    }

                };

            },

            cache: true

        },

        placeholder: 'Search for a repository',

        minimumInputLength: 3,

        templateResult: formatRepo,

        templateSelection: formatRepoSelections

    });

}

function formatRepo (repo) {



    if (repo.loading) {

        return repo.text;

    }

    //console.log(repo);

    var $container = $(

        "<div class='select2-result-repository clearfix'>" +

        "<div class='select2-result-repository__meta'>" +

        "<div class='select2-result-repository__title'></div>" +

        "<div class='select2-result-repository__statistics'>" +

        "<div class='select2-result-repository__watchers'><i class='fa fa-eye'></i> </div>" +

        "</div>" +

        "</div>" +

        "</div>"

    );

    $container.find(".select2-result-repository__title").text(repo.title);

    $container.find(".select2-result-repository__watchers").append("Active");

    return $container;

}



function setId(id){

    id_product=id;

}

function getid(){

    return id_product;

}

function formatRepoSelections (repo) {

    setId(repo.id);

    $.ajax ({

        url: "varian",

        dataType: 'json',

        delay: 250,

        type: "post",

        data: {

            id:getid()

        },

        success : function(data){

            $(ids).empty();

            $.each( data, function( key, value ) {

                $(ids).append('<option value="'+value.id+'">'

                    +'Color : '+value.color+' | Harga : '+value.price

                    +'</option>')

            });

        }

    })

    return repo.title || repo.title;

}

function select2District(a){

    $(a).select2({

        ajax: {

            url: "districts",

            dataType: 'json',

            delay: 250,

            type: "GET",

            data: function (params) {

                return {

                    q: params.term,

                    page: params.page

                };

            },

            processResults: function (data, params) {

                params.page = params.page || 1;

                return {

                    results: data.items,

                    pagination: { more: (params.page * 30) < data.total_count }

                };

            },

            cache: true

        },

        placeholder: 'Search for a District/',

        minimumInputLength: 1,

        templateResult: formatRepos,

        templateSelection: formatRepoSelection

    });

    function formatRepos (repo) {

        if (repo.loading) { return repo.text; }

        var $container = $(

            "<div class='select2-result-repository clearfix'>" +

            "<div class='select2-result-repository__meta'>" +

            "<div class='select2-result-repository__title'></div>" +

            "<div class='select2-result-repository__statistics'>" +

            "<div class='select2-result-repository__usernames' style='display: inline-block;     padding: 10px;'><i class='fa fa-location'></i> </div>" +

            "</div>" +

            "</div>" +

            "</div>"

        );

        $container.find(".select2-result-repository__usernames").append(repo.name);

        return $container;

    }

    function formatRepoSelection (repo) {

        $('#name').val(repo.name);

        return repo.name || repo.name;

    }

}

function select2User(a){

    $(a).select2({

        ajax: {

            url: "member",

            dataType: 'json',

            delay: 250,

            type: "post",

            data: function (params) {

                return {

                    q: params.term, // search term

                    page: params.page

                };

            },

            processResults: function (data, params) {

                params.page = params.page || 1;

                // console.log(data);

                return {

                    results: data.items,

                    pagination: {

                        more: (params.page * 30) < data.total_count

                    }

                };

            },

            cache: true

        },

        placeholder: 'Search for a Member/',

        minimumInputLength: 1,

        templateResult: formatRepos,

        templateSelection: formatRepoSelection

    });

    function formatRepos (repo) {



        if (repo.loading) {

            return repo.text;

        }

        //console.log(repo);

        var $container = $(

            "<div class='select2-result-repository clearfix'>" +

            "<div class='select2-result-repository__meta'>" +

            "<div class='select2-result-repository__title'></div>" +

            "<div class='select2-result-repository__statistics'>" +

            "<div class='select2-result-repository__watchers' style='display: inline-block;    padding: 10px;'><i class='fa fa-eye'></i> </div>" +

            "<div class='select2-result-repository__mail' style='display: inline-block;     padding: 10px;'><i class='fa fa-envelope'></i> </div>" +

            "<div class='select2-result-repository__usernames' style='display: inline-block;     padding: 10px;'><i class='fa fa-user'></i> </div>" +

            "</div>" +

            "</div>" +

            "</div>"

        );

        $container.find(".select2-result-repository__title").append(repo.name);

        $container.find(".select2-result-repository__watchers").append("Active");

        $container.find(".select2-result-repository__mail").append(repo.email);

        $container.find(".select2-result-repository__usernames").append(repo.username);

        return $container;

    }

    function formatRepoSelection (repo)

    {

        $('#email').val(repo.email);

        return repo.name || repo.name;

    }

}

function select2Store(a){

    $(a).select2({

        ajax: {

            url: "stores",

            dataType: 'json',

            delay: 250,

            type: "post",

            data: function (params) {

                return {

                    q: params.term, // search term

                    page: params.page

                };

            },

            processResults: function (data, params) {

                params.page = params.page || 1;

                // console.log(data);

                return {

                    results: data.items,

                    pagination: {

                        more: (params.page * 30) < data.total_count

                    }

                };

            },

            cache: true

        },

        placeholder: 'Search for a Store/',

        minimumInputLength: 1,

        templateResult: formatRepoforStore,

        templateSelection: formatRepoSelectionStore

    });

    function formatRepoforStore (repo) {



        if (repo.loading) {

            return repo.text;

        }

        //console.log(repo);

        var $container = $(

            "<div class='select2-result-repository clearfix'>" +

            "<div class='select2-result-repository__meta'>" +

            "<div class='select2-result-repository__title'></div>" +

            "<div class='select2-result-repository__statistics'>" +

            "<div class='select2-result-repository__watchers' style='display: inline-block;    padding: 10px;'><i class='fa fa-eye'></i> </div>" +

            "<div class='select2-result-repository__mail' style='display: inline-block;     padding: 10px;'><i class='fa fa-globe'></i> </div>" +

            "<div class='select2-result-repository__usernames' style='display: inline-block;     padding: 10px;'><i class='fa fa-user'></i> </div>" +

            "</div>" +

            "</div>" +

            "</div>"

        );



        $container.find(".select2-result-repository__title").append(repo.store_name);

        $container.find(".select2-result-repository__watchers").append("Active");

        $container.find(".select2-result-repository__mail").append(repo.domains);

        $container.find(".select2-result-repository__usernames").append(repo.store_username);



        return $container;

    }

    function formatRepoSelectionStore (repo)

    {

        // $('#email').val(repo.email);

        $('#store_id').val(repo.id);

        return repo.id || repo.store_name;

    }

}



function checkUrl(a,b){

    $.ajax({

        method: 'post',

        url: 'url',

        data:{

            url:a

        },

        success: function(resp) {



            if(resp>0){

                b.text("store name already used");

                // if(b.hasClass('text-success')){

                b.removeClass('text-success').addClass('text-danger');

                // }

                b.show();

            }else{

                b.text("store name can be used");



                b.removeClass('text-danger').addClass('text-success');

                b.show();

            }





        },

        error: function() {



        }

    });

}