# nrebl.middleware

*VERY ALPHA*

The start of an nREPL middleware that will spy on an nREPL connection
and capture the results of evaluation for browsing in
[REBL](https://github.com/cognitect-labs/REBL-distro).

**NOTE: REBL requires a commercial license if it's to be used for commercial work**

## Installation

Multiple setups should be possible.  Here we describe one way using
tools.deps.

1. Install [clojure](https://clojure.org/)
2. Install REBL to a known path
3. Setup your `deps.edn`

```clojure
{:aliases {:nrepl {:extra-deps {nrepl/nrepl {:mvn/version "0.4.5"}}}
           :rebl {:extra-deps {
	                  org.clojure/clojure {:mvn/version "1.10.0-RC3"}
                      rickmoynihan/nrebl.middleware {:git/url "https://github.com/rickmoynihan/dev.middleware", :sha "6f37f09fef0df14b855b443838f7dcc0ff6fd1d1"}
                      org.clojure/core.async {:mvn/version "0.4.490"}
     	              com.cognitect/rebl {:local/root "<PATH-TO-REBL-JAR>/REBL-0.9.108/REBL-0.9.108.jar"}}}
           :cider {,,,} ;; configure cider/nrepl deps here
           }}}}
```

## Leiningen Installation

Add something like the following to your lein `project.clj`

```clojure
 :nrepl {:repl-options {:nrepl-middleware [nrebl.middleware/wrap-nrebl]
                        ;; If you would like the REBL ui to start on repl init
                        :init (do (println "Starting cognitect.rebl/ui...")
                                  (cognitect.rebl/ui))})}
         :dependencies [[rickmoynihan/nrebl.middleware "0.1.0-SNAPSHOT"]
                        [org.clojure/core.async "0.4.490"]]

         ;; Download REBL and add to :resource-paths
         :resource-paths ["/Users/rick/Software/REBL-0.9.108/REBL-0.9.108.jar"]}

 :repl [:nrebl]
```

## Usage

```
clj -A:nrepl:cider:rebl -m nrepl.cmdline --middleware '[nrebl.middleware/wrap-nrebl cider.nrepl/cider-middleware]'
```

Then connect to your REPL. The REBL UI should display automatically but you can always run it manually:

```
(cognitect.rebl/ui)
```

For leiningen users:

```
lein repl
```

You should now be able to evaluate forms and have REBL capture them.

## Help Wanted

There's lots that can be done to improve this.  Help & suggestions welcome.

## License

Copyright © 2018 Rick Moynihan

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
