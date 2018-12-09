(ns nrebl.middleware
  (:require [cognitect.rebl.ui :as ui]
            [cognitect.rebl :as rebl]
            [clojure.datafy :refer [datafy]]))

(defn try-loading-new-nrepl! []
  (require '[nrepl.middleware :refer [set-descriptor!]])
  (require '[nrepl.transport :as transport])
  (import '[nrepl.transport Transport])
  true)

(defn try-loading-old-nrepl! []
  (require '[clojure.tools.nrepl.middleware :refer [set-descriptor!]])
  (require '[clojure.tools.nrepl.transport :as transport])
  (import '[clojure.tools.nrepl.transport Transport])
  true)

(or (try
      (try-loading-new-nrepl!)
      (catch java.io.FileNotFoundException _
        false))
    (try
      (try-loading-old-nrepl!)
      (catch java.io.FileNotFoundException _
        false)))

(defn send-to-rebl! [{:keys [code] :as req} {:keys [value] :as resp}]
  (when-let [value (datafy value)]
    (rebl/submit (read-string code) value))
  resp)

(defn- wrap-rebl-sender
  "Wraps a `Transport` with code which prints the value of messages sent to
  it using the provided function."
  [{:keys [id op ^Transport transport] :as request}]
  (reify transport/Transport
    (recv [this]
      (.recv transport))
    (recv [this timeout]
      (.recv transport timeout))
    (send [this resp]
      (.send transport
             (send-to-rebl! request resp))
      this)))

(defn wrap-nrebl [handler]
  (fn [{:keys [id op transport] :as request}]
    (if (= op "start-rebl-ui")
      (rebl/ui)
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
         nrepl/response-values
         ))

  (with-open [conn (nrepl/connect :port 52756)]
     (-> (nrepl/client conn 1000)    ; message receive timeout required
         (nrepl/message {:op "start-rebl-ui"})
         nrepl/response-values
         ))

  (require '[nrepl.server :as ser])

  (def nrep (ser/start-server :port 55804
                              :handler (ser/default-handler #'wrap-nrebl)))
  )
