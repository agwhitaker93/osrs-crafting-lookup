(ns osrs-crafting-lookup.components.recipes-test
  (:require [clojure.test :refer :all]
            [osrs-crafting-lookup.database :as db]
            [osrs-crafting-lookup.components.recipes :refer [get-recipe]]))

; TODO add db mocks

(def test-id 147)
(def test-name "adamant")

(deftest given-we-request-one-recipe
  (testing "when we provide an id,"
    (let [id 147]
      (testing "and we do not provide a name,"
        (testing "then we get results matching the id"
          (is (= {:target {:recipes  '("hello")
                           :products '("world")}} (get-recipe {:id id})))))

      (testing "and we provide a name,"
        (let [name test-name]
          (is (= {:target {:recipes  '("hello")
                           :products '("world")}} (get-recipe {:id   id
                                                               :name name})))
          (testing "then we get results matching the id")))))

  (testing "when we provide no id,"
    (testing "and we do not provide a name,"
      (testing "then an empty result is provided"
        (is (= {:target {:recipes  '()
                         :products '()}} (get-recipe {})))))

    (testing "and we provide a name,"
      (let [name test-name]
        (testing "then we get results matching the name"
          (is (= {:target {:recipes  '("hi")
                           :products '("there")}} (get-recipe {:name name}))))))))

(deftest given-we-want-many-recipes)
