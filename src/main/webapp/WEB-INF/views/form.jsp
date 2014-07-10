    <!-- Show those faces
    ================================================== -->
    <!-- Wrap the rest of the page in another container to center all the content. -->

    <div class="container emotion">
      <div class="row">
        <div class="span4">
          <a class="emoticon" id="angry" href="#"><img class="img-circle" data-src="holder.js/140x140" src="/img/angry.gif"></a>
        </div>
        <div class="span4">
           <a class="emoticon" id="happy" href="#"><img class="img-circle" data-src="holder.js/140x140" src="/img/happy.gif"></a>
        </div>
        <div class="span4">
          <a class="emoticon" id="sad" href="#"><img class="img-circle" data-src="holder.js/140x140" src="/img/sad.gif"></a>
        </div>
      </div>
    </div>
    
    <!-- TODO: fetch emotions from the db -->

    <div class="container questions" id="angry">
      <button class="btn btn-danger question" type="button">You know what I hate?</button>
      <button class="btn btn-danger question" type="button">You know what pisses me off?</button>
      <button class="btn btn-danger question" type="button">You know what's bullshit?</button>
      <button class="btn btn-danger question" type="button">You know what I don't like?</button>
      <button class="btn btn-danger question" type="button">You know what I can't stand?</button>
      <button class="btn btn-danger question" type="button">You know what makes me angry?</button>
    </div>
    <div class="container questions" id="happy">
      <button class="btn btn-success question" type="button">You know what I love?</button>
      <button class="btn btn-success question" type="button">You know what I like?</button>
      <button class="btn btn-success question" type="button">You know what's cool?</button>
      <button class="btn btn-success question" type="button">You know what make me happy?</button>
      <button class="btn btn-success question" type="button">You know what I can't live without?</button>
      <button class="btn btn-success question" type="button">You know what's pretty good?</button>
    </div>
    <div class="container questions" id="sad">
      <button class="btn btn-inverse question" type="button">You know what makes me cry?</button>
      <button class="btn btn-inverse question" type="button">You know what's depressing?</button>
      <button class="btn btn-inverse question" type="button">You know what makes me sad?</button>
      <button class="btn btn-inverse question" type="button">You know what I wish had happened?</button>
      <button class="btn btn-inverse question" type="button">You know what I miss?</button>
      <button class="btn btn-inverse question" type="button">You know what sucks?</button>
      <button class="btn btn-inverse question" type="button">You know what I regret?</button>
    </div>
    
    <!-- Holds the thank you message -->
    <div id="modal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="modal-label" aria-hidden="true">
	  <div class="modal-header">
	    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
	    <h3 id="modal-label"></h3>
	  </div>
	  <div class="modal-body">
	    <p></p>
	    <span></span>
	  </div>
	</div>	
    
    <div class="container rant-form-container retract" id="rant-form-container">
      <form method="post" action="/rant/post" onSubmit="return validate()">
      	<div style="display: none; visibility: hidden;"><input type="hidden" id="pageLoadTime" name="pageLoadTime" value=""></div>
      	<input type="hidden" id="emotion" name="emotion" value="">
      	<input type="hidden" id="question" name="question" value="">
        <fieldset>
        <legend id="legend" class="center-legend"></legend>
          <input type="text" id="ranter" name="ranter" placeholder="Name (optional)" class="optional">&nbsp;&nbsp;
          <input type="text" id="location" name="location" placeholder="Location (optional)" class="optional">
          <textarea name="rant" id="countable" rows="5" placeholder="Say something..."></textarea>
           <span class="help-block"><span id="counter"></span> characters left.</span>
          <button type="submit" id="submit" class="btn">Submit</button>
        </fieldset>
      </form>
	  <p class="text-error hidden-error-text retract" id="error-text"></p>
    </div>