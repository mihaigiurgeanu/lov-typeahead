describe('lov-typeahead sample usage page', function() {
	
  it('should have title "Typeahead LOV sample usage"', function() {
	  
    browser.get('http://localhost:8000/typeahead.html');
    browser.getTitle().then(function (title) {
    	expect(title).toBe("Typeahead LOV sample usage");
    });
  });
});
