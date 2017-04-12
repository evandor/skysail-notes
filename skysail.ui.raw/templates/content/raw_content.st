raw_content(user,converter,messages,model,request)::= <<
  <div class="container">
    <div class="row">
      <div class="col-md-16">
        <sky-content identifier="$model.resourceClassName$.message" content='' edit-base-url='$converter.editBaseUrl$' >
        $messages.("content.header").value$
        </sky-content>
      </div>
    </div>
    <div class="row">
    
    $if(model.form)$
    
      <div class="pure-g">
        <div class="pure-u-4-5">
          <form class="pure-form pure-form-aligned" action="$model.formTarget$" method="POST" id="form" name="form">
            <fieldset>
              $model.formfieldsWrapper.formfields: { ff | $raw_formElement(model,converter,ff)$ };separator="\n"$ 
              <div class="pure-controls">
                <button type="submit" name="submit" value="submit" class="pure-button pure-button-primary" >submit</button>
              </div>
            </fieldset>
          </form>
        </div>
        <div class="pure-u-1-5">
        <br>
        $raw_renderEntityInfo(model.formfields, messages, model)$
        </div>
      </div>

    $elseif(model.relationTargetList)$
 
      <div class="col-md-16" id="showRelationTargetFormDiv">
        <br>
        $showRelationTargetForm(model.formfields, messages, model)$
      </div>
 
    $elseif(model.list)$

      <div class="col-md-16" id="renderTableDiv">
        <br>
        <table class="table table-striped table-condensed table-hover ROWCLICK">
          <tr>
            $model.formfieldsWrapper.formfields: { ff | $raw_thElement(model,ff)$ };separator="\n"$ 
	  	    <th>Actions</th>
          </tr>
          $model.data: { row | $raw_trElement(model.formfieldsWrapper.formfields,row)$ };separator="\n"$ 
        </table>
        <div class="row">
    	    <div class="col-md-16">
        	  $raw_menuNav(model.createFormLinks)$
    	    </div>
        </div>
      </div>

      <div>
        <h3>Resource Model</h3>
        <h5>Form Fields</h5>
        <small>$model.formfieldsWrapper.formfields: { row | $row$};separator="<br>\n"$</small>
        <h5>Raw Data</h5>
        <small>$model.rawData: { row | $row$};separator="<br>\n"$</small>
        <h5>Data</h5>
        <small>$model.data: { row | $row$};separator="<br>\n"$</small>
        <h5>Links</h5>
        <small>$model.links: { link | $link$};separator="<br>\n"$</small>
      </div> 

      <div>
        <h3>Request</h3>
        $request$
      </div> 

      <div>
        <h3>User</h3>
        $user$
      </div> 

      <div>
        <h3>Converter</h3>
        $converter$
      </div> 

      <div>
        <h3>Messages</h3>
        $messages$
      </div> 
      
    $else$
      <div class="col-md-16" id="renderEntityDiv">
        <br>
        $raw_renderEntity(user, model)$
      </div>
    $endif$
  </div>
  
</div>
>>