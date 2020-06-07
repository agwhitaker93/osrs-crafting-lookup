(ns osrs-crafting-lookup.components.http-client-test
  (:require [clojure.test :refer :all]
            [clj-http.client :as client]
            [osrs-crafting-lookup.components.http-client :refer [get-with-retry]]))

(deftest get-with-retry-instant-response
  (with-redefs [client/get (fn [& args] {:body "hello"})]
    (is (= {:body "hello"} (get-with-retry "")))))
