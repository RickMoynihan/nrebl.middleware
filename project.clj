(defproject rickmoynihan/nrebl.middleware "0.2.1-SNAPSHOT"
  :description "An nREPL and cider middleware for capturing and
  browsing data in REBL."
  :url "https://github.com/Swirrl/matcha"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :deploy-repositories [["releases" :clojars]
                        ["snapshots" :clojars]]

  :profiles {:provided {:dependencies [[org.clojure/clojure "1.10.0"]
                                       [org.clojure/core.async "0.4.490"]
                                       [org.openjfx/javafx-base "11.0.1"]
                                       [org.openjfx/javafx-controls "11.0.1"]
                                       [org.openjfx/javafx-fxml "11.0.1"]
                                       [org.openjfx/javafx-swing "11.0.1"]
                                       [org.openjfx/javafx-web "11.0.1"]]
                        :resource-paths ["vendor/REBL-0.9.109.jar"]}

             :dev {:dependencies [[nrepl "0.5.3"]]} ;; Used by lein 2.8.3+
             :dev-old {:dependencies [[org.clojure/tools.nrepl "0.2.13"]]} ;; lein < 2.8.3
             }
  )
