describe('lov-typeahead sample usage page', function() {
	var countryId = element(by.binding('{{country.id}}'));
	var countryName = element(by.binding('{{country.name}}'));

	beforeEach(function() {
		browser.get('http://localhost:8000/typeahead-demo.html');
	});
	
	it('should have title "Typeahead LOV sample usage"', function() {

		browser.getTitle().then(function(title) {
			expect(title).toBe("Typeahead LOV sample usage");
		});
	});
	
	it('has no country selected at first', function() {		
		expect(countryId.getText()).toEqual('');
		expect(countryName.getText()).toEqual('No country selected');
	});
	
	it('contains one typeahead lov control', function() {
		element.all(by.css('input[x-lov-typeahead="test"]')).count().then(function(num) {
			expect(num).toBe(1);
		});
	});
	
	it('selects a Anguilla after typing Ang<tab>', function() {
		browser.findElement(by.css('input[x-lov-typeahead="test"]')).sendKeys('Ang\t');
		expect(countryId.getText()).toEqual("5");
		expect(countryName.getText()).toEqual("Anguilla");
	});
	
});
