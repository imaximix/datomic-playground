(ns datomic-playground.core
  (:gen-class)
  (use ring.middleware.reload)
  (use ring.adapter.jetty)
  (:require [datomic.api :refer [q db pull tempid] :as d]
            [clojure.pprint :as pp]
            [datomic-playground.schema :refer [main]]
            [compojure.core :refer :all]
            [ring.util.response :refer [response
                                        content-type]]))



(defn makeConn []
  (def uri "datomic:dev://localhost:4334/hello")
  (d/create-database uri)
  (d/connect uri))


(defn getUserRoles [userId]
  (let [conn (makeConn)
        my-db (db conn)]
    (q '[:find (pull ?user [{:user/role [:db/id :role/name]}]) .
         :in $ ?user
         :where [?user]]
       my-db
       userId)))

(defn createOrder [order userId]
  (let [conn (makeConn)
        roles (getUserRoles userId)]
    (d/transact conn [(merge order {:db/id (tempid :db.part/user)})])))

(createOrder {:order/price 100M :order/name "Lui Ghita"} 17592186045470)

(defn getOrders []
  (let [conn (makeConn)
        my-db (db conn)]
    (q '[:find (pull ?order [:order/name :order/price])
         :in $
         :where [?order :order/name _]]
       my-db)))

(getOrders)

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (def conn (makeConn))

  (def datom-user-schema (main))
  
  (d/transact conn datom-user-schema)

  (def user [[:db/add #db/id[:db.part/tx] :data/src "www"]
             {:db/id #db/id[:db.part/user]
                 :user/email "ghita5@google.com"}])
  (d/transact conn user)

  (def updatedUser [[:db/add #db/id[:db.part/tx] :data/src "www"]
                    [:db/add 17592186045470
                     :user/email "hello@hello.com"]])
  (d/transact conn updatedUser)
    
  (def role [[:db/add 17592186045470
              :user/role 17592186045464]])

  (d/transact conn role)
  
  (def my-db (db conn))

  (->> (q '[:find ?user
         :in $
         :where
            [?user :user/email _ ?tx]]
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
   (GET "/api" request (str request))))
