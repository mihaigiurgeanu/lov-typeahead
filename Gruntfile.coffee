module.exports = (grunt) ->
	grunt.initConfig 
		pkg: grunt.file.readJSON 'package.json'
		uglify:
			options:
				banner: '/*! <%= pkg.name %> <%= grunt.template.today("yyyy-mm-dd") %> */\n'
			dist:
				src: '<%= pkg.directories.build %>/*.js'
				dest: '<%= pkg.main %>'
		bgShell:
			auto:
				cmd: 'lein cljsbuild auto'
			once:
				cmd: 'lein cljsbuild once'
		watch: 
			target:
				files: ['<%= pkg.directories.build %>/*.js']
				tasks: ['uglify']
		concurrent:
			auto:
				tasks: ['watch:target', 'bgShell:auto']
			options:
				logConcurrentOutput: true
				
	grunt.loadNpmTasks 'grunt-contrib-uglify'
	grunt.loadNpmTasks 'grunt-bg-shell'
	grunt.loadNpmTasks 'grunt-contrib-watch'
	grunt.loadNpmTasks 'grunt-concurrent'
		
	grunt.registerTask 'default', ['uglify']
	grunt.registerTask 'auto', ['concurrent:auto']
	grunt.registerTask 'build', ['bgShell:once', 'uglify']
