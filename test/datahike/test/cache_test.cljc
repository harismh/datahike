(ns datahike.test.cache-test
  (:require
   #?(:cljs [cljs.test :as t :refer-macros [is are deftest testing]]
      :clj  [clojure.test :as t :refer [is deftest]])
   [clojure.core.async :refer [<!]]
   [datahike.api :as d]
   [datahike.test.async #?(:clj :refer :cljs :refer-macros) [deftest-async]]
   [datahike.test.utils :as utils]))

(defn setup-db [cfg]
  (d/delete-database cfg)
  (d/create-database cfg)
  (d/connect cfg))

(deftest-async test-history-cache-miss
  (let [cfg {:store              {:backend :memory
                                  :id      #uuid "001a0000-0000-0000-0000-00000000001a"}
             :name               "cache-test"
             :keep-history?      true
             :schema-flexibility :write
             :search-cache-size  2
             :attribute-refs?    true}
        conn (<! (utils/setup-db-async cfg))
        schema [{:db/ident       :name
                 :db/cardinality :db.cardinality/one
                 :db/index       true
                 :db/unique      :db.unique/identity
                 :db/valueType   :db.type/string}]]
    (<! (d/transact! conn schema))

    (<! (d/transact! conn [{:name "Alice"}
                           {:name "Bob"}]))

    (<! (d/transact! conn [[:db/retractEntity [:name "Alice"]]]))
    (is (= #{["Bob" true] ["Alice" false] ["Alice" true]}
           (d/q {:query '[:find ?n ?op
                          :where
                          [?e :name ?n _ ?op]]
                 :args  [(d/history @conn)]})))
    (d/release conn)))
