(ns osrs-crafting-lookup.components.details-test
  (:require [clojure.test :refer :all]
            [osrs-crafting-lookup.config :refer [ge-api-base-url]]
            [osrs-crafting-lookup.components.details :refer [detail-url]]))

(deftest detail-url-passes
  (with-redefs [ge-api-base-url "example.com"]
    (is (= (str ge-api-base-url "/detail.json?item=" 1) (detail-url 1)))))
