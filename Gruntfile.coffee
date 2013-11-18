module.exports = (grunt) ->
	grunt.initConfig 
		pkg: grunt.file.readJSON 'package.json'
		uglify:
			options:
				banner: '/*! <%= pkg.name %> <%= grunt.template.today("yyyy-mm-dd") %> */\n'
			build:
				src: '<%= pkg.directories.build %>/*.js'
				dest: '<%= pkg.main %>'
		bgShell:
			auto:
				cmd: 'lein cljsbuild auto'
			once:
				cmd: 'lein cljsbuild once'
				
	grunt.loadNpmTasks 'grunt-contrib-uglify'
	grunt.loadNpmTasks 'grunt-bg-shell'
		
	grunt.registerTask 'default', ['uglify']
	grunt.registerTask 'auto', ['bgShell:auto']
	grunt.registerTask 'build', ['bgShell:once', 'uglify']
