describe('lov-typeahead sample usage page', function() {
	var countryId = element(by.binding('{{country.id}}'));
	var countryName = element(by.binding('{{country.name}}'));

	var test_sets_for_url = function(url) {
		return function() {
			beforeEach(function() {
				browser.get(url);
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

			it('has the initial text in the input control "No country selected"', function () {
				var input = browser.findElement(by.css('input[x-lov-typeahead="test"]'));
				expect(input.getAttribute("value")).toEqual('No country selected');
			});
			
			it('contains one typeahead lov control', function() {
				element.all(by.css('input[x-lov-typeahead="test"]')).count().then(
						function(num) {
							expect(num).toBe(1);
						});
			});

			it('selects a Anguilla after typing Ang<tab>', function() {
				var input = browser.findElement(by.css('input[x-lov-typeahead="test"]'));
				input.clear();
				input.sendKeys('Ang');
				browser.sleep(2000);
				input.sendKeys('\t');
				expect(countryId.getText()).toEqual("5");
				expect(countryName.getText()).toEqual("Anguilla");
			});

		};
	};
	
	describe('for prefetch only use-case', test_sets_for_url('http://localhost:8000/typeahead-demo-prefetch.html'));
	//describe('for remote only use-case', test_sets_for_url('http://localhost:8000/typeahead-demo-remote.html'));

});
