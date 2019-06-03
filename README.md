# nrebl.middleware

[![Clojars Project](https://img.shields.io/clojars/v/rickmoynihan/nrebl.middleware.svg)](https://clojars.org/rickmoynihan/nrebl.middleware)

*VERY ALPHA*

The start of an nREPL middleware that will spy on an nREPL connection
and capture the results of evaluation for browsing in
[REBL](https://github.com/cognitect-labs/REBL-distro).

**NOTE: REBL requires a commercial license if it's to be used for commercial work**

## Installation (leiningen)

Assuming you're running a recent leiningen (2.8.3) follow the steps:

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

## Installation (tools.deps)

1. Install [clojure](https://clojure.org/)
2. Install REBL to a known path
3. Setup your `deps.edn`

```clojure
{:aliases {:nrepl {:extra-deps {nrepl/nrepl {:mvn/version "0.4.5"}}}
           :rebl {:extra-deps {
	           org.clojure/clojure {:mvn/version "1.10.0"}
                   rickmoynihan/nrebl.middleware {:mvn/version "0.2.0"}
                   org.clojure/core.async {:mvn/version "0.4.490"}
     	           com.cognitect/rebl {:local/root "<PATH-TO-REBL-JAR>/REBL-0.9.108/REBL-0.9.157.jar"}}}
           :cider {,,,} ;; configure cider/nrepl deps here
           }}}}
```


## Usage

```
clj -A:nrepl:cider:rebl -m nrepl.cmdline --middleware '[nrebl.middleware/wrap-nrebl cider.nrepl/cider-middleware]' -i
```

Then connect to your REPL, and run

```
(cognitect.rebl/ui)
```

You should now be able to evaluate forms (in your editor) and have REBL capture them. Adding the `-i` option to
the `clj` command line also allows nrepl to capture evaluations from an interactive repl. 

## Help Wanted

There's lots that can be done to improve this.  Help & suggestions welcome.

## License

Copyright © 2018 Rick Moynihan

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
