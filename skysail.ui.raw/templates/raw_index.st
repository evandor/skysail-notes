raw_index(user, messages, converter, model, request) ::= <<
<!doctype html>
<html lang="en">
<head>
  $head/raw_head(converter)$
</head>
<body>

  <div id="layout">
    <!-- Menu toggle -->
    <a href="#menu" id="menuLink" class="menu-link">
        <!-- Hamburger icon -->
        <span></span>
    </a>

    <div id="menu">
        <div class="pure-menu">
            <a class="pure-menu-heading" href="/">skysail</a>
            <br>
            <ul class="pure-menu-list">
                $model.applicationContextLinks: { link | <li class="pure-menu-item"><a href='$link.uri$'>$link.title$</a></li>}$
            </ul>
        </div>
    </div>

    <div id="main">
        <div class="header">
            <h1>$model.resourceSimpleName$</h1>
            <h2>$request.request$</h2>
        </div>
        
        <div class="pure-g">
          <div class="pure-u-lg-1-24">&nbsp;</div>
          <div class="pure-u-lg-22-24">$content/raw_content(user,converter,messages,model,request)$</div>
          <div class="pure-u-lg-1-24">&nbsp;</div>
        </div>

    </div>
  </div>

  <script src="/_ui/raw/0.6.2/js/ui.js"></script>

</body>
</html>







>>