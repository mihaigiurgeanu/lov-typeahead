module.exports = (grunt) ->
	grunt.initConfig 
		pkg: grunt.file.readJSON 'package.json'
		uglify:
			options:
				banner: '/*! <%= pkg.name %> <%= grunt.template.today("yyyy-mm-dd") %> */\n'
			build:
				src: '<%= pkg.directories.build %>/*.js'
				dest: '<%= pkg.main %>'

	grunt.loadNpmTasks 'grunt-contrib-uglify'
	grunt.registerTask 'default', ['uglify']
