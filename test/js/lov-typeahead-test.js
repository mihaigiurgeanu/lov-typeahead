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

			it('displays uses lov-limit attribute to set the number of items displayed', function() {
				var input = browser.findElement(by.css('input[x-lov-typeahead="test"]'));
				input.clear();
				input.sendKeys('A');
				browser.sleep(2000);
				
				element.all(by.className('tt-suggestion')).count().then(function(num) {
					expect(num).not.toBe(5);
					expect(num).toBe(8);
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
	describe('for remote only use-case', test_sets_for_url('http://localhost:8000/typeahead-demo-remote.html'));
	describe('for local only use-case', test_sets_for_url('http://localhost:8000/typeahead-demo-local.html'));
	

});

describe('model composed of multi-level vars', function () {
	var countryId = element(by.binding('{{address.country.id}}'));
	var countryName = element(by.binding('{{address.country.name}}'));

	
	beforeEach(function() {
		browser.get('http://localhost:8000/typeahead-demo-multilevel.html');
	});
	
	it('selects a Anguilla after typing Ang<tab>', function() {
		var input = browser.findElement(by.css('input[x-lov-typeahead="test"]'));
		input.clear();
		input.sendKeys('Ang');
		input.sendKeys('\t');
		expect(countryId.getText()).toEqual("5");
		expect(countryName.getText()).toEqual("Anguilla");
	});
});

describe('The lov-model', function() {
	var countryId = element(by.binding('{{address.country.id}}'));
	var countryName = element(by.binding('{{address.country.name}}'));
	var clearButton = element(by.id('clear-model'));
	var setButton = element(by.id('set-new-mode'));

	
	beforeEach(function() {
		browser.get('http://localhost:8000/typeahead-demo-model-update.html');
	});
	
	it('can have a default value', function() {
		expect(countryId.getText()).toEqual('222');
		expect(countryName.getText()).toEqual('Initial Value');
	});
	
	it('can be reset programatically', function() {
		clearButton.click();
		expect(countryId.getText()).toEqual('');
		expect(countryName.getText()).toEqual('');
	});
	
	it('can be set programatically', function() {
		setButton.click();
		expect(countryId.getText()).toEqual('128');
		expect(countryName.getText()).toEqual('Test Country');
	});
	
	describe('the typeahead control', function() {
		var searchCountry = function () {
			var input = browser.findElement(by.css('input[x-lov-typeahead="test"]'));
			input.clear();
			input.sendKeys('Ang');
			input.sendKeys('\t');
			expect(countryId.getText()).toEqual("5");
			expect(countryName.getText()).toEqual("Anguilla");
		};
		
		it('can be searched when after default value loaded', function() {
			searchCountry();
		});
		
		it('can be searched after value cleared', function () {
			clearButton.click();
			searchCountry();
		});
		
		it('can be searche after value was set', function () {
			setButton.click();
			searchCountry();
		});
	});
});

