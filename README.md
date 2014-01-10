# lov-typeahead

Implemetation of a list of values directive for Angula JS based on Twitter's typeahead.js component. It uses
typeahead.js component to select one javascript object from an array of objects.

## Usage

FIXME

	<!doctype html>
	<html x-ng-app="typeaheadDirectiveSample">
	  <head>
	  	<title>Typeahead LOV sample usage</title>
	  </head>
	  <body x-ng-controller="Ctrl">
	  
		<ul>
		<li>Country id: <span>{{country.id}}</span>
		<li>Country name: <span>{{country.name}}</span>
		</ul>
	
	    <div>
	    	<input type="text" 
	    	x-lov-typeahead="test" 
	    	x-lov-prefetch="test-data.json" 
	    	x-lov-limit="10"
	    	x-lov-value-key="name"
	    	x-lov-model="country"></input>
	    </div>
	
		
		<script src="jquery/jquery.min.js"></script>
		<script src="typeahead.js/dist/typeahead.min.js"></script>
	    <script src="http://code.angularjs.org/1.2.1/angular.min.js"></script>
	    <script src="lov-typeahead.js"></script>
	    <script src="typeahead-demo.js"></script>
	  </body>
	</html>

## Testing

For running tests automatically you need to start selenium server then type grunt:

	#> java -jar selenium/selenium-server-standalone-2.37.0.jar
	#> grunt
	
For see the demo usage and play with typeahead lov just runt grunt demo. This will
start a web server on port 8000 and you can load any html file from the test folder.
Most common will be http:://localhost:8000/typeahead-demo-prefetch.html.

	#> grunt demo

## License

Copyright © 2013 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
