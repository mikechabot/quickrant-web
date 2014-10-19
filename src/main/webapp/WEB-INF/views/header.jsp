<%@ page language="java" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="Say anything to everyone. Express yourself without yourself. No login required.">
    <meta name="author" content="Mike Chabot">
    
    <title>quickrant | express yourself without yourself</title>
	
	<!-- scripts -->
	<script src="/js/jquery.js"></script>
	<script src="/js/bootstrap.min.js"></script>
	<script src="/js/jquery.timeago.js"></script>
	
	<!-- fonts -->
	<link href='http://fonts.googleapis.com/css?family=Open+Sans:300,400,600,700' rel='stylesheet' type='text/css'>
	
    <!-- styles -->
    <link href="/css/bootstrap.min.css" rel="stylesheet">
    <link href="/css/3d-corner-ribbons.css" rel="stylesheet">
    <link href="/css/custom.css" rel="stylesheet">
    	
    <!-- fav and touch icons -->
    <link rel="apple-touch-icon-precomposed" sizes="144x144" href="/ico/apple-touch-icon-144-precomposed.png">
    <link rel="apple-touch-icon-precomposed" sizes="114x114" href="/ico/apple-touch-icon-114-precomposed.png">
    <link rel="apple-touch-icon-precomposed" sizes="72x72" href="/ico/apple-touch-icon-72-precomposed.png">
    <link rel="apple-touch-icon-precomposed" href="/ico/apple-touch-icon-57-precomposed.png">
    <link rel="shortcut icon" href="/ico/favicon.png">
    
  </head>
  <body>

  <!-- navigation -->
  <nav class="navbar navbar-default" role="navigation">
    <div class="container-fluid">    
      <div class="navbar-header">
        <a class="navbar-brand" href="/">quickrant</a>
      </div>
      <div class="collapse navbar-collapse">
        <ul class="nav navbar-nav">
          <li><a href="/about">about</a></li>
        </ul>
        <p class="navbar-text navbar-right">
         <a class="navbar-link" href="/uid/${cookieValue}">${cookieValue}</a>
        </p>
      </div>   
    </div>
  </nav>