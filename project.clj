(defproject datomic-playground "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [ring/ring-core "1.4.0"]
                 [ring/ring-jetty-adapter "1.4.0"]
                 [ring/ring-devel "1.4.0"]
                 [compojure "1.5.0"]
                 [com.datomic/datomic-pro "0.9.5350"]]
  :main ^:skip-aot datomic-playground.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
