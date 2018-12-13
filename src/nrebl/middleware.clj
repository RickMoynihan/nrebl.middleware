(ns nrebl.middleware
  (:require [nrepl
             [middleware :refer [set-descriptor!]]
             [transport :as transport]]
            [nrepl.middleware.interruptible-eval :as ev]
            [cognitect.rebl.ui :as ui]
            [cognitect.rebl :as rebl]
            [clojure.datafy :refer [datafy]]
            [clojure.string :refer [starts-with?]])
  (:import
   nrepl.transport.Transport))

(defn send-to-rebl! [{:keys [code] :as req} {:keys [value] :as resp}]
  (when-let [value (datafy value)]
    (rebl/submit (read-string code) value))
  resp)


(defn- cursive?
  "Takes an nREPL request and returns true if a noisy cursive eval request."
  [request]
  (and (= (get request :op) "eval")
       (starts-with? (get request :code) "(cursive.repl")))

(defn- wrap-rebl-sender
  "Wraps a `Transport` with code which prints the value of messages sent to
  it using the provided function."
  [{:keys [id op ^Transport transport] :as request}]
  (reify Transport
    (recv [this]
      (.recv transport))
    (recv [this timeout]
      (.recv transport timeout))
    (send [this response]
      (.send transport
             ;; Filter out noisy cursive requests
             (if (cursive? request)
               response
               (send-to-rebl! request response)))
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
