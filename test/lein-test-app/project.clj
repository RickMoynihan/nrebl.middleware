(defproject lein-test-app "0.1.0-SNAPSHOT"
  :dependencies [[rickmoynihan/nrebl.middleware "0.1.0-SNAPSHOT"]
                 [org.clojure/clojure "1.10.0"]
                 [org.clojure/core.async "0.4.490"]]
  :repl-options {:nrepl-middleware [nrebl.middleware/wrap-nrebl]
                 :init (do (println "Starting cognitect.rebl/ui...")
                           (cognitect.rebl/ui))})
