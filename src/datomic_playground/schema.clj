(ns datomic-playground.schema
  (:gen-class)
  (:require [datomic.api :as d]))

(defn padToRequiredLength
  [coll]
  (concat coll (repeat (- 5 (count coll)) nil)))

(defn toDatomSchema

  [meta]
  (def defaults [nil
                 :db.type/string
                 :db.cardinality/one
                 :db.part/db
                 (d/tempid :db.part/db)])
  (let [schemaValues
        (map #(or %1 %2)
             (padToRequiredLength meta)
             defaults)]
      (hash-map :db/ident (nth schemaValues 0)
                :db/valueType (nth schemaValues 1)
                :db/cardinality (nth schemaValues 2)
                :db.install/_attribute (nth schemaValues 3)
                :db/id (nth schemaValues 4))))

(defn gen-schema
  [meta-schema]
  (map toDatomSchema meta-schema))

(def schema-meta [[:user/email
                   :db.type/string
                   :db.cardinality/one]
                  [:user/role
                   :db.type/ref
                   :db.cardinality/many]
                  
                  [:membership/password
                   :db.type/string
                   :db.cardinality/one]

                  [:role/name
                   :db.type/string
                   :db.cardinality/one]])

(defn main []
  (gen-schema schema-meta))



