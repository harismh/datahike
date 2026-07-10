(ns datahike.test
  (:require
   #?(:cljs [cljs.test    :as t]
      :clj  [clojure.test :as t])
   [datahike.test.core-test :refer [wrap-res]]
   datahike.test.cache-test
   datahike.test.components-test
   #?(:clj datahike.test.config-test)
   datahike.test.db-test
   #?(:clj datahike.test.entity-test)
   datahike.test.explode-test
   datahike.test.filter-test
   datahike.test.ident-test
   datahike.test.index-test
   #?(:clj datahike.test.listen-test)
   datahike.test.lookup-refs-test
   datahike.test.lru-test
   datahike.test.lru-weighted-test
   datahike.test.lru-weighted-property-test
   #?(:clj datahike.test.migrate-test)
   datahike.test.pull-api-test
   #?(:clj datahike.test.purge-test)
   #?(:clj datahike.test.query-test)
   datahike.test.query-aggregates-test
   datahike.test.query-find-specs-test
   #?(:clj datahike.test.query-fns-test)
   #?(:clj datahike.test.query-interop-test)
   datahike.test.query-not-test
   datahike.test.query-or-test
   datahike.test.query-pull-test
   datahike.test.query-rules-test
   datahike.test.specification-test
   #?(:clj datahike.test.schema-test)
   #?(:clj datahike.test.store-test)
   datahike.test.time-variance-test
   #?(:clj datahike.test.transact-test)
   #?(:clj datahike.test.tuples-test)
   #?(:clj datahike.test.upsert-test)
   #?(:clj datahike.test.upsert-impl-test)
   datahike.test.validation-test
   datahike.test.cljs-pattern-scan-test
   datahike.test.cljs-tiered-storage-test
   datahike.test.optimistic-test
   datahike.test.valid-time-test
   datahike.test.experimental.graph-util-test
   datahike.test.experimental.graph-test
   datahike.test.experimental.anomaly-test
   #?(:clj datahike.test.attribute-refs.differences-test)
   #?(:clj datahike.test.attribute-refs.entity-test)
   #?(:clj datahike.test.attribute-refs.pull-api-test)
   #?(:clj datahike.test.attribute-refs.query-test)
   #?(:clj datahike.test.attribute-refs.transact-test)
   #?(:clj datahike.test.attribute-refs.utils)
   #?(:clj datahike.test.middleware.query-test)
   #?(:clj datahike.test.middleware.utils-test)))

(defn ^:export test-clj []
  (wrap-res #(t/run-all-tests #"datahike\..*")))

(defn ^:export test-cljs
  "Runs the CLJS-compatible test subset.

  Previously, tests discovered every `*.cljc` namespace,
  including namespaces that still depend on JVM-only behaviors.
  This alternative test runner uses a curated list of namespaces
  that have been proven portable. Quoted namespaces are added here
  as they are ported over. Eventually, this call should be dropped
  in favor of (cljs.test/run-all-tests #\"datahike.*\")"
  []
  (wrap-res
   #(t/run-tests
     'datahike.test.components-test
     'datahike.test.cache-test
     'datahike.test.core-test
     'datahike.test.db-test
     'datahike.test.explode-test
     'datahike.test.filter-test
     'datahike.test.ident-test
     ;; Sibling test namespaces
     'datahike.test.index-test
     'datahike.test.lookup-refs-test
     'datahike.test.lru-test
     'datahike.test.pull-api-test
     'datahike.test.cljs-tiered-storage-test
     'datahike.test.cljs-pattern-scan-test
     'datahike.test.optimistic-test
     'datahike.test.valid-time-test
     ;; Portable query suites — exercise the query-engine paths that were
     ;; JVM-only (NOT-JOIN, OR, aggregates, recursive rules) on cljs too.
     'datahike.test.query-aggregates-test
     'datahike.test.query-find-specs-test
     'datahike.test.query-not-test
     'datahike.test.query-or-test
     'datahike.test.query-pull-test
     'datahike.test.query-rules-test
     'datahike.test.specification-test
     'datahike.test.time-variance-test
     'datahike.test.validation-test
     ;; Portable graph algorithms.
     'datahike.test.experimental.graph-util-test
     'datahike.test.experimental.graph-test
     'datahike.test.experimental.anomaly-test
     ;; Weighted LRU query-cache — the cljs WeightedLRU deftype has its
     ;; own implementation, so cover it (unit + test.check property) here.
     'datahike.test.lru-weighted-test
     'datahike.test.lru-weighted-property-test)))

(comment
  (test-clj)
  (test-cljs))
