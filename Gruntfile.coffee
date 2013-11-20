module.exports = (grunt) ->
	grunt.initConfig
		pkg: grunt.file.readJSON 'package.json'
		connect:
			options:
				base: ['dist', 'bower_components', 'test']
			test:
				options:
					keepalive: false
			demo:
				options:
					keepalive: true
		protractor:
			typeahead:
				options:
					configFile: 'protractor-conf.js'
					keepAlive: false
		open:
			demo:
				path: 'http://localhost:8000/thpeahead-demo.html'
				app: 'Firefox'
	grunt.loadNpmTasks 'grunt-contrib-connect'
	grunt.loadNpmTasks 'grunt-protractor-runner'
	grunt.loadNpmTasks 'grunt-open'
	
	grunt.registerTask 'default', ['connect:test', 'protractor']
	grunt.registerTask 'demo', ['connect:demo']
	