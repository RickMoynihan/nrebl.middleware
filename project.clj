(defproject rickmoynihan/nrebl.middleware "0.1.0-SNAPSHOT"
  :description "A clojure nREPL middleware for interacting with Cognitect’s REBL."
  :url "https://github.com/rickmoynihan/nrebl.middleware"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies []
  :exclusions [org.clojure/clojure
               nrepl
               cognitect.rebl]
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}
             :test {:main ^:skip-aot nrebl.middleware
                    :dependencies [[org.clojure/clojure "1.10.0"]
                                   [org.clojure/core.async "0.4.490"]
                                   [nrepl "0.5.3"]]}})
