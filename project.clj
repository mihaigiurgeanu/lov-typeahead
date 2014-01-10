(defproject lov-typeahead "0.2.4-RC.1"
  :description "A list of values AngularJS component based on Twitter's typeahead"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :plugins [[lein-cljsbuild "1.0.0-SNAPSHOT"]]
  :dependencies [[org.clojure/clojurescript "0.0-1978"]]
  :cljsbuild {
    :builds [{
      :source-paths ["src/cljs"]
      :compiler {
        :output-to "dist/lov-typeahead.js"
        :externs ["src/js/externs.js"]
        :optimizations :advanced
        :pretty-print true}}]})
