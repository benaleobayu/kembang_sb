function select2Dis(a){
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
              "<div class='select2-result-repository__watchers' style='display: inline-block;    padding: 10px;'><i class='fa fa-eye'></i> </div>" +
              // "<div class='select2-result-repository__mail' style='display: inline-block;     padding: 10px;'><i class='fa fa-envelope'></i> </div>" +
              // "<div class='select2-result-repository__usernames' style='display: inline-block;     padding: 10px;'><i class='fa fa-user'></i> </div>" +
            "</div>" +
          "</div>" +
        "</div>"
      );
      $container.find(".select2-result-repository__title").append(repo.name);
      $container.find(".select2-result-repository__watchers").append("Active");
      // $container.find(".select2-result-repository__mail").append(repo.email);
      // $container.find(".select2-result-repository__usernames").append(repo.username);
      return $container;
  }
  function formatRepoSelection (repo) {
      $('#name').val(repo.name);
      return repo.name || repo.name;
  }
}