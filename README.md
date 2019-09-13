# nrebl.middleware

[![Clojars Project](https://img.shields.io/clojars/v/rickmoynihan/nrebl.middleware.svg)](https://clojars.org/rickmoynihan/nrebl.middleware)

*VERY ALPHA*

The start of an nREPL middleware that will spy on an nREPL connection
and capture the results of evaluation for browsing in
[REBL](https://github.com/cognitect-labs/REBL-distro).

**NOTE: REBL requires a commercial license if it's to be used for commercial work**

## Installation (tools.deps)

1. Install [clojure](https://clojure.org/)
2. Install REBL to a known path
3. Include some aliases either in your `~/.clojure/deps.edn` or your projects `deps.edn` respectively.  Note the `:rebl-8` and `:rebl-11` aliases are for running either on Oracle's JDK 8 (which bundles JavaFX) or OpenJDK 11 which doesn't.  

```clojure
{:aliases {
           :nrebl {:extra-deps {nrepl {:mvn/version "RELEASE"}
                                cider/cider-nrepl {:mvn/version "0.21.0"}
                                refactor-nrepl {:mvn/version "2.4.0"}
                                rickmoynihan/nrebl.middleware {:mvn/version "0.2.0"}}
          :main-opts ["-e" "((requiring-resolve,'cognitect.rebl/ui))" "-m" "nrepl.cmdline" "-i" "--middleware" "[nrebl.middleware/wrap-nrebl,cider.nrepl/cider-middleware]"]}
	  
           :nrepl {:extra-deps {nrepl/nrepl {:mvn/version "0.6.0"}}}
           ;; - see https://github.com/cognitect-labs/REBL-distro
           ;; - you need to download the REBL JAR (and update the local/root below)
           ;; - you need to be using Clojure 1.10 and either
           ;; - - Oracle JDK 8 -- use :rebl-8
           ;; - - OpenJDK 11 -- use :rebl-11
           :rebl-8
           {:extra-deps {org.clojure/core.async {:mvn/version "0.4.490"}
                         ;; deps for file datafication (0.9.149 or later)
                         org.clojure/data.csv {:mvn/version "0.1.4"}
                         org.clojure/data.json {:mvn/version "0.2.3"}
                         org.yaml/snakeyaml {:mvn/version "1.23"}
                         com.cognitect/rebl
                         ;; adjust to match your install location
                         {:local/root "/Developer/REBL/latest/REBL.jar"}}
            :main-opts ["-m" "cognitect.rebl"]}
            :rebl-11
            {:extra-deps {org.clojure/core.async {:mvn/version "0.4.490"}
                          ;; deps for file datafication (0.9.149 or later)
                          org.clojure/data.csv {:mvn/version "0.1.4"}
                          org.clojure/data.json {:mvn/version "0.2.3"}
                          org.yaml/snakeyaml {:mvn/version "1.23"}
                          com.cognitect/rebl
                          ;; adjust to match your install location
                          {:local/root "/Developer/REBL/latest/REBL.jar"}
                          org.openjfx/javafx-fxml     {:mvn/version "11.0.1"}
                          org.openjfx/javafx-controls {:mvn/version "11.0.1"}
                          org.openjfx/javafx-graphics {:mvn/version "11.0.1"}
                          org.openjfx/javafx-media    {:mvn/version "11.0.1"}
                          org.openjfx/javafx-swing    {:mvn/version "11.0.1"}
                          org.openjfx/javafx-base     {:mvn/version "11.0.1"}
                          org.openjfx/javafx-web      {:mvn/version "11.0.1"}}
             :main-opts ["-m" "cognitect.rebl"]}

             :cider {:extra-deps {cider/cider-nrepl {:mvn/version "0.22.1"
                                  refactor-nrepl {:mvn/version "2.4.0"}}}}}}}}
```

## Usage

Assuming OpenJDK 11:

```
clj -R:nrepl:cider:rebl-11 -A:nrebl
```

Or Oracle JDK 8:

```
clj -R:nrepl:cider:rebl-8 -A:nrebl
```


You should now be able to connect to your nREPL through your editor or via an nREPL client and have nREBL capture the evaluation of forms.

## Installation (leiningen)

Assuming you're running a recent leiningen (2.9.1) follow the steps:

1. Install REBL to a known path.
2. Add the following to your `~/.lein/profiles.clj` file in order to configure nrebl as part of your `:user` profile:

```clojure
 :nrebl  {:repl-options {:nrepl-middleware [nrebl.middleware/wrap-nrebl]}
         :dependencies [[rickmoynihan/nrebl.middleware "0.2.0"] ;; set this to the latest nrebl version
                        [org.clojure/core.async "0.4.490"]]
         :resource-paths ["/Users/rick/Software/rebl/REBL-0.9.157.jar"] ;; set this to where your REBL jar is installed
         :injections [(require '[cognitect.rebl :as rebl])]
         }

 :user [:nrebl
        ;;:other-tool-profiles...]
```

NOTE: the above configuration stores all `:nrebl` config in a single profile which is then merged into the `:user` profile, which will be available in dev/repl environments.  It is usually cleaner to do it this way as it makes it explicit what configuration belongs to each tool.

## Usage (lein)

1. Run `lein repl` and/or connect to  nREPL with your Editor.
2. Evaluate `(rebl/ui)` (the :injections should make this available in every namespace)
3. The REBL UI should appear
4. Evaluate more forms in the REPL, they should each then appear in REBL.

## Help Wanted

There's lots that can be done to improve this.  Help & suggestions welcome.

## Thanks to

- *@seancorfield* for the [dot-clojure](https://github.com/seancorfield/dot-clojure/blob/master/deps.edn) aliases.
- ...

## License

Copyright © 2018 Rick Moynihan

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
