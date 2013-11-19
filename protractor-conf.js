exports.config = {
  seleniumAddress: 'http://localhost:4444/wd/hub',
  specs: ['test/js/*.js'],
  capabilities: {
	  'browserName': 'firefox'
	},
  jasmineNodeOpts: {
  }
};

