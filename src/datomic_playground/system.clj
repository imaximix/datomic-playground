(ns datomic-playground.system
  (:gen-class)
  (use ring.adapter.jetty)
  (:require [datomic-playground.core :refer [handler]]
            [clojure.tools.namespace.repl :refer [refresh]]))

(def system
  nil)


(defn init
  []
  (alter-var-root
   #'system
   (fn [system]
     {:server (run-jetty handler {:port 3000
                                  :join? false})})))

(defn start []
  (init))


(defn stop
  []
  (.stop (:server system)))

(defn reset
  []
  (stop)
  (refresh :after 'datomic-playground.system/start))
