(ns osrs-crafting-lookup.components.items-test
  (:require [clojure.test :refer :all]
            [cheshire.core :refer [parse-string]]
            [osrs-crafting-lookup.config :refer [ge-api-base-url]]
            [osrs-crafting-lookup.components.http-client :refer [get-with-retry]]
            [osrs-crafting-lookup.components.items :refer [items-url
                                                           get-page
                                                           get-all-pages
                                                           narrow-selection
                                                           rs-lookup
                                                           handle]]))

(deftest items-url-passes
  (with-redefs [ge-api-base-url "example.com"]
    (is (= (str ge-api-base-url "/items.json?category=1&alpha=a&page=1") (items-url "a" 1)))))

(deftest get-page-passes
  (with-redefs [items-url (constantly {:body {:items "hello"}})
                get-with-retry identity
                parse-string (fn [body & args] body)]
    (is (= "hello" (get-page 1 2)))))

(deftest get-all-pages-passes
  (with-redefs [get-page #(if (= %2 1) "result")]
    (is (= '("result") (get-all-pages "a")))))
