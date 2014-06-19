(defproject tri "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://example.com/FIXME"

  :dependencies [[org.clojure/clojurescript "0.0-2202"]
                 [org.clojure/clojure "1.5.1"]
                 [om "0.6.2"]
                 [prismatic/schema "0.2.4"]
                 [prismatic/plumbing "0.3.1"]
                 [prismatic/dommy "0.1.2"]
                 [prismatic/om-tools "0.1.1"]]

  :plugins [[lein-ring "0.8.10"]
            [com.cemerick/austin "0.1.4"]
            [lein-cljsbuild "1.0.3"]]

  :hooks [leiningen.cljsbuild]

  :cljsbuild
  {:builds [{:source-paths ["src"]
             :compiler
             {:output-to "tri.js"
              :optimizations :whitespace
              :pretty-print true
              :externs ["delaunay.js"]}}]})
