(ns osrs-crafting-lookup.components.items-test
  (:require [clojure.test :refer :all]
            [ring.util.response :refer [response]]
            [osrs-crafting-lookup.components.http-client :refer [get-with-retry]]
            [osrs-crafting-lookup.components.items :refer [items-url handle]]))

(deftest given-items-handler
  (with-redefs [items-url (fn [char page] (str char page))
                get-with-retry #(if (not (= %1 "a3"))
                                  (identity {:body (str "{\"items\":[{\"name\":\"ada-" %1 "\"}]}")}))]
    (testing "when no query is given,"
      (testing "then a NullPointerException is thrown"
        (is (thrown? NullPointerException (handle {})))))

    (testing "when a query is given,"
      (let [query "ada"]
        (testing "and a page is given,"
          (let [page 1]
            (testing "then we get one set of results"
              (is (= {:status  200
                      :headers {"Content-Type" "text/html; charset=utf-8"}
                      :body    '({:name "ada-a1"})}
                     (handle {:query query :page page}))))))

        (testing "and no page is given,"
          (testing "then we get multiple results"
            (is (= {:status  200
                    :headers {"Content-Type" "text/html; charset=utf-8"}
                    :body    '({:name "ada-a2"}
                               {:name "ada-a1"})}
                   (handle {:query query})))))))))
