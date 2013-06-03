<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>quickrant | say anything to everyone</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">

    <!-- Le styles -->
    <link href="/css/bootstrap.css" rel="stylesheet">
    <link href="/css/bootstrap-responsive.css" rel="stylesheet">
    <style>

    /* GLOBAL STYLES
    -------------------------------------------------- */
    /* Padding below the footer and lighter body text */

    body {
      padding-bottom: 40px;
      color: #5a5a5a;
    }
    
    .buffer {
      padding-top: 10px;
      padding-bottom: 10px;
    }
    
    /* CUSTOMIZE THE NAVBAR
    -------------------------------------------------- */

    /* Special class on .container surrounding .navbar, used for positioning it into place. */
    .navbar-wrapper {
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      z-index: 10;
      margin-top: 20px;
    }
    .navbar-wrapper .navbar {

    }

    /* Remove border and change up box shadow for more contrast */
    .navbar .navbar-inner {
      border: 0;
      -webkit-box-shadow: 0 2px 10px rgba(0,0,0,.25);
         -moz-box-shadow: 0 2px 10px rgba(0,0,0,.25);
              box-shadow: 0 2px 10px rgba(0,0,0,.25);
    }

    /* Downsize the brand/project name a bit */
    .navbar .brand {
      padding: 14px 20px 16px; /* Increase vertical padding to match navbar links */
      font-size: 16px;
      font-weight: bold;
      text-shadow: 0 -1px 0 rgba(0,0,0,.5);
    }

    /* Navbar links: increase padding for taller navbar */
    .navbar .nav > li > a {
      padding: 15px 20px;
    }

    /* Offset the responsive button for proper vertical alignment */
    .navbar .btn-navbar {
      margin-top: 10px;
    }

    /* QUESTIONS CONTENT
    -------------------------------------------------- */

    .questions {
      text-align: center;
      display: none;
      width: 60%;
    }
    
    button.question {
      margin: 3px;
    }
    
    /* STREAM CONTENT
    -------------------------------------------------- */

    .stream {    
      width: 40%;
      margin: 0 auto;
    }

    /* TEXTING CONTENT
    -------------------------------------------------- */

    .texting {
      text-align: center;
      display: none; 
      width: 35%;
    }

    .texting textarea{   
    	width: 75%;
    }
    
    .texting label {
      font-style:italic;
    }

    /* EMOTION CONTENT
    -------------------------------------------------- */

    /* Center align the text within the three columns below the carousel */

    .emotion {
      margin-top: 90px;
    }

    .emotion .span4 {
      text-align: center;
    }
    .emotion h2 {
      font-weight: normal;
    }
    .emotion .span4 p {
      margin-left: 10px;
      margin-right: 10px;
    }


    /* RESPONSIVE CSS
    -------------------------------------------------- */

    @media (max-width: 979px) {

      .container.navbar-wrapper {
        margin-bottom: 0;
        width: auto;        
      }
      .navbar-inner {
        border-radius: 0;
        margin: -20px 0;
      }

    }

    @media (max-width: 767px) {

      .navbar-inner {
        margin: -20px;
      }

      .emotion .span4 + .span4 {
        margin-top: 40px;
      }

    }
    </style>

    <!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
      <script src="/js/html5shiv.js"></script>
    <![endif]-->

    <!-- Fav and touch icons -->
    <link rel="apple-touch-icon-precomposed" sizes="144x144" href="/ico/apple-touch-icon-144-precomposed.png">
    <link rel="apple-touch-icon-precomposed" sizes="114x114" href="/ico/apple-touch-icon-114-precomposed.png">
      <link rel="apple-touch-icon-precomposed" sizes="72x72" href="/ico/apple-touch-icon-72-precomposed.png">
                    <link rel="apple-touch-icon-precomposed" href="/ico/apple-touch-icon-57-precomposed.png">
                                   <link rel="shortcut icon" href="/ico/favicon.png">
  </head>
  <body>



    <!-- NAVBAR
    ================================================== -->
    <div class="navbar-wrapper">
      <!-- Wrap the .navbar in .container to center it within the absolutely positioned parent. -->
      <div class="container">

        <div class="navbar navbar-inverse">
          <div class="navbar-inner">
            <!-- Responsive Navbar Part 1: Button for triggering responsive navbar (not covered in tutorial). Include responsive CSS to utilize. -->
            <button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
              <span class="icon-bar"></span>
              <span class="icon-bar"></span>
              <span class="icon-bar"></span>
            </button>
            <a class="brand" href="#">quickrant</a>
            <!-- Responsive Navbar Part 2: Place all navbar contents you want collapsed withing .navbar-collapse.collapse. -->
            <div class="nav-collapse collapse">
              <ul class="nav">
                <li class="active"><a href="#">Home</a></li>
                <li><a href="#about">About</a></li>
                <li><a href="#contact">Contact</a></li>
              </ul>
            </div><!--/.nav-collapse -->
          </div><!-- /.navbar-inner -->
        </div><!-- /.navbar -->

      </div> <!-- /.container -->
    </div><!-- /.navbar-wrapper -->

    <!-- emotion messaging and featurettes
    ================================================== -->
    <!-- Wrap the rest of the page in another container to center all the content. -->

    <div class="container emotion">

      <!-- Three columns of text below the carousel -->
      <div class="row">
        <div class="span4">
           <a class="emotion" id="happy" href="#"><img class="img-circle" data-src="holder.js/140x140" src="/img/happy.gif"></a>
          <h2><a class="emotion" id="happy" href="#">I'm happy</a></h2>
        </div><!-- /.span4 -->
        <div class="span4">
          <a class="emotion" id="angry" href="#"><img class="img-circle" data-src="holder.js/140x140" src="/img/angry.gif"></a>
          <h2><a class="emotion" id="angry" href="#">I'm angry</a></h2>
        </div><!-- /.span4 -->
        <div class="span4">
          <a class="emotion" id="sad" href="#"><img class="img-circle" data-src="holder.js/140x140" src="/img/sad.gif"></a>
          <h2><a class="emotion" id="sad" href="#">I'm sad</a></h2>
        </div><!-- /.span4 -->
      </div><!-- /.row -->

    </div><!-- /.container -->

    <div class="container questions" id="happy">
      <button class="btn btn-success question" type="button">You know what I love?</button>
      <button class="btn btn-success question" type="button">You know what I like?</button>
      <button class="btn btn-success question" type="button">You know what's cool?</button>
      <button class="btn btn-success question" type="button">You know what make me happy?</button>  
      <button class="btn btn-success question" type="button">You know what I can't live without?</button>
      <button class="btn btn-success question" type="button">You know what's pretty good?</button>      
    </div>
    <div class="container questions" id="angry">
      <button class="btn btn-danger question" type="button">You know what I hate?</button>
      <button class="btn btn-danger question" type="button">You know what pisses me off?</button>
      <button class="btn btn-danger question" type="button">You know what's bullshit?</button>
      <button class="btn btn-danger question" type="button">You know what I don't like?</button>
      <button class="btn btn-danger question" type="button">You know what I can't stand?</button>      
      <button class="btn btn-danger question" type="button">You know what makes me angry?</button> 
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
    <div class="buffer">
    <div class="container texting" id="texting">
      <form>
        <fieldset>
          <label></label>
          <textarea rows="4" maxlength="280"></textarea>
          <br>
          <button type="submit" class="btn">Submit</button>
        </fieldset>
      </form>
    </div>	
    </div>
    
	<div class="buffer">
    <div class="container stream">
      <blockquote class="pull-right">
        <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer posuere erat a ante.</p>
          <small>Someone famous <cite title="Source Title">Source Title</cite></small>
        </blockquote><br>
        <blockquote class="pull-right">
        <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer posuere erat a ante.</p>
          <small>Someone famous <cite title="Source Title">Source Title</cite></small>
        </blockquote><br>
              <blockquote class="pull-right">
        <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer posuere erat a ante.</p>
          <small>Someone famous <cite title="Source Title">Source Title</cite></small>
        </blockquote><br>
              <blockquote class="pull-right">
        <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer posuere erat a ante.</p>
          <small>Someone famous <cite title="Source Title">Source Title</cite></small>
        </blockquote>
    </div>
	</div>
    <!-- Le javascript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="/js/jquery.js"></script>
    <script src="/js/stuff.js"></script>
    <script src="/js/bootstrap-transition.js"></script>
    <script src="/js/bootstrap-alert.js"></script>
    <script src="/js/bootstrap-modal.js"></script>
    <script src="/js/bootstrap-dropdown.js"></script>
    <script src="/js/bootstrap-scrollspy.js"></script>
    <script src="/js/bootstrap-tab.js"></script>
    <script src="/js/bootstrap-tooltip.js"></script>
    <script src="/js/bootstrap-popover.js"></script>
    <script src="/js/bootstrap-button.js"></script>
    <script src="/js/bootstrap-collapse.js"></script>
    <script src="/js/bootstrap-carousel.js"></script>
    <script src="/js/bootstrap-typeahead.js"></script>
    <script src="/js/holder/holder.js"></script>
  </body>
</html>
