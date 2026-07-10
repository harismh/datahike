(ns datahike.test.query-interop-test
  (:require
   #?(:cljs [cljs.test    :as t :refer-macros [is are deftest]]
      :clj  [clojure.test :as t :refer        [is are deftest]])
   [clojure.string]
   [datahike.api :as d]
   [datahike.db :as db]))

#?(:cljs (def Exception js/Error))

(def test-db
  (d/db-with
   (db/empty-db)
   [[:db/add 1 :name "Vlad"]
    [:db/add 2 :name "Ivan"]
    [:db/add 3 :name "Sergey"]]))

(deftest test-filter
  (are [q args expected] (= (apply d/q q test-db args) expected)
    '[:find ?v
      :in $ ?pred
      :where [_ :name ?v]
      [(?pred ?v "Ser")]]
    [clojure.string/starts-with?]
    #{["Sergey"]}

    '[:find ?v
      :in $ ?pred
      :where [_ :name ?v]
      [(?pred ?v "a")]]
    [clojure.string/includes?]
    #{["Vlad"] ["Ivan"]}

    '[:find ?v
      :where [_ :name ?v]
      [(re-matches #".+rg.+" ?v)]]
    []
    #{["Sergey"]}))

(deftest test-bind
  (are [q args expected] (= (apply d/q q test-db args) expected)
    '[:find ?V
      :in $ ?lower-case
      :where
      [?e :name ?v]
      [(?lower-case ?v) ?V]]
    [clojure.string/lower-case]
    #{["vlad"] ["ivan"] ["sergey"]}))

(deftest test-method-not-found
  (is (thrown? Exception (d/q '[:find ?v
                                :where
                                [?e :name ?v]
                                [(.thisMethodDoesNotExist ?v 1)]]
                              test-db))))
