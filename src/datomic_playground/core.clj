(ns datomic-playground.core
  (:gen-class)
  (use ring.middleware.reload)
  (use ring.adapter.jetty)
  (:require [datomic.api :refer [q db pull] :as d]
            [clojure.pprint :as pp]
            [datomic-playground.schema :refer [main]]
            [compojure.core :refer :all]
            [ring.util.response :refer [response
                                        content-type]]))



(defn makeConn []
  (def uri "datomic:dev://localhost:4334/hello")
  (d/create-database uri)
  (d/connect uri))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (def uri "datomic:dev://localhost:4334/hello")
  (d/create-database uri)
  
  (def conn (d/connect uri))

  (def datom-user-schema (main))
  
  (d/transact conn datom-user-schema)

  (def user [{:db/id #db/id[:db.part/user]
              :user/email "ghita3@google.com"
              :user/role [{:db/id 17592186045462}]}])
  (d/transact conn user)
  
  
  (def role [[:db/add 17592186045470
              :user/role 17592186045464]])

  (d/transact conn role)
  
  (def my-db (db conn))

  (->> (q '[:find ?name ?role
         :in $
         :where
            [?role :role/name ?name]
            [?user :user/role ?role]]
       my-db)
       seq
       println))

(defn printRoles
  [conn]
  (let [my-db (db conn)]
     (->> (q '[:find ?role ?name
                        :in $
                        :where [?role :role/name ?name]]
                      my-db)
                   seq)))


(def handler
  (routes
   (GET "/foo/:id{[0-9]+}" [id] (str "Hello, I'm " id))
   (GET "/bar" [] "Hello Bar")
   (GET "/hei/:helooooooooo" request (str request))))
