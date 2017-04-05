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
                <div class="pure-control-group">
                    <label for="name">Username</label>
                    <input id="name" type="text" placeholder="Username">
                    <span class="pure-form-message-inline">This is a required field.</span>
                </div>
        
                <div class="pure-control-group">
                    <label for="password">Password</label>
                    <input id="password" type="password" placeholder="Password">
                </div>
        
                <div class="pure-control-group">
                    <label for="email">Email Address</label>
                    <input id="email" type="email" placeholder="Email Address">
                </div>
        
                <div class="pure-control-group">
                    <label for="foo">Supercalifragilistic Label</label>
                    <input id="foo" type="text" placeholder="Enter something here...">
                </div>
        
                <div class="pure-controls">
                    <label for="cb" class="pure-checkbox">
                        <input id="cb" type="checkbox"> I've read the terms and conditions
                    </label>
                    
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
    
      $model.formfieldsWrapper.formfields: { ff | $raw_formElement(model,converter,ff)$ };separator="\n"$ 
     

    $elseif(model.relationTargetList)$
 
      <div class="col-md-16" id="showRelationTargetFormDiv">
        <br>
        $showRelationTargetForm(model.formfields, messages, model)$
      </div>
 
    $elseif(model.list)$
 
      <div class="col-md-16" id="renderTableDiv">
        <br>
		$if(converter.debug)$
          Filter     [Param _f]: $model.filterParamValue$<br>
          Sorting    [Param _s]: $model.sortingParamValue$<br>
          Pagination [Param _p]: <br>
          Query      [Param _q]: <br>
          <br>
        $endif$
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
 
    $else$
      <div class="col-md-16" id="renderEntityDiv">
        <br>
        $raw_renderEntity(user, model)$
      </div>
    $endif$
  </div>
  
</div>
>>