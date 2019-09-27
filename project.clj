(defproject rickmoynihan/nrebl.middleware "0.3.2-SNAPSHOT"
  :description "An nREPL and cider middleware for capturing and
  browsing data in REBL."
  :url "https://github.com/Swirrl/matcha"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :deploy-repositories [["releases" :clojars]
                        ["snapshots" :clojars]]

  :profiles {:provided {

                        :dependencies [[org.clojure/clojure "1.10.1"]
                                       [org.clojure/core.async "0.4.490"]


                                       ;; JDK 11
                                       ;; [org.openjfx/javafx-base "11.0.1"]
                                       ;; [org.openjfx/javafx-controls "11.0.1"]
                                       ;; [org.openjfx/javafx-fxml "11.0.1"]
                                       ;; [org.openjfx/javafx-swing "11.0.1"]
                                       ;; [org.openjfx/javafx-web "11.0.1"]

                                       ]

                        :resource-paths ["vendor/REBL-0.9.220.jar"]}


             :repl {:repl-options {:nrepl-middleware [nrebl.middleware/wrap-nrebl]}}}

  :min-lein-version "2.9.0" ;; nrepl 0.6.0
  )
