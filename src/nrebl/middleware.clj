(ns nrebl.middleware
  (:require [clojure.datafy :refer [datafy]]
            [cognitect.rebl :as rebl]))

  ;; Looks for legacy leiningen namespace and imports relevant resources
(if (find-ns 'clojure.tools.nrepl)
  (do (require
       '[clojure.tools.nrepl.middleware :refer [set-descriptor!]])
      (import
       'clojure.tools.nrepl.transport.Transport))
  (do (require
       '[nrepl.middleware :refer [set-descriptor!]])
      (import
       'nrepl.transport.Transport)))

(defn send-to-rebl!
  "Displays evaluated code in REBL results.
  Takes a nrepl middleware request map and a response map.
  Returns a nrepl middleware response"
  [{:keys [code] :as req} {:keys [value] :as resp}]
  (when-let [value (datafy value)]
    (rebl/submit (read-string code) value))
  resp)

(defn- wrap-rebl-sender
  "Wraps a `Transport` with code which prints the value of messages sent to
  it using the provided function."
  [{:keys [id op ^Transport transport] :as request}]
  (reify Transport
    (recv [this]
      (.recv transport))
    (recv [this timeout]
      (.recv transport timeout))
    (send [this resp]
      (.send transport
             (send-to-rebl! request resp))
      this)))

(defn wrap-nrebl [handler]
  "nREPL middleware wrapper.
  Takes the next middleware handler function.
  Returns a middlware handler function.
  See https://nrepl.xyz/nrepl/design/middleware.html for more."
  (fn [{:keys [id op transport] :as request}]
    ;; Using case so that it's easier to add\remove ops later
    (case op
      "start-rebl-ui" (rebl/ui)
      (handler (assoc request :transport (wrap-rebl-sender request))))))

(set-descriptor! #'wrap-nrebl
                 {:requires #{}
                  :expects #{"eval"}
                  :handles {"start-rebl-ui" "Launch the REBL inspector and have it capture interactions over nREPL"}})

(comment

  (rebl/ui)

  (require '[nrepl.core :as nrepl])

  (with-open [conn (nrepl/connect :port 55803)]
     (-> (nrepl/client conn 1000)    ; message receive timeout required
         ;(nrepl/message {:op "inspect-nrebl" :code "[1 2 3 4 5 6 7 8 9 10 {:a :b :c :d :e #{5 6 7 8 9 10}}]"})
         (nrepl/message {:op "eval" :code "(do {:a :b :c [1 2 3 4] :d #{5 6 7 8} :e (range 20)})"})
         nrepl/response-values))


  (with-open [conn (nrepl/connect :port 52756)]
     (-> (nrepl/client conn 1000)    ; message receive timeout required
         (nrepl/message {:op "start-rebl-ui"})
         nrepl/response-values))


  (require '[nrepl.server :as ser])

  (def nrep (ser/start-server :port 55804
                              :handler (ser/default-handler #'wrap-nrebl))))
