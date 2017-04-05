raw_renderEntity(user, model) ::= <<
  <table class="table">
    $model.formfields: {field | $raw_renderRow(model, field, first(model.data))$}$
  </table>
>>