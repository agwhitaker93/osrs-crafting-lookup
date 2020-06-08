(ns osrs-crafting-lookup.components.recipes-test
  (:require [clojure.test :refer :all]
            [clojure.java.jdbc :as jdbc]
            [osrs-crafting-lookup.migrations :refer [migrate rollback]]
            [osrs-crafting-lookup.config :refer [db]]
            [osrs-crafting-lookup.database :as database]
            [osrs-crafting-lookup.components.recipes :refer [get-recipe]]))

(def test-db {:dbtype "h2:mem"
              :dbname "testing"})
(def test-id 2361)
(def test-name "Ogre relic")
(def test-time (.toString (database/timestamp-now)))

(defn insert-item [db-conf id name last_updated]
  (jdbc/insert! db-conf "osrs.items" {:id id
                                      :name name
                                      :last_updated last_updated})
  (jdbc/insert! db-conf "osrs.materials" {:id id
                                          :last_updated last_updated})
  (jdbc/insert! db-conf "osrs.recipes" {:id id
                                        :last_updated last_updated})
  (jdbc/insert! db-conf "osrs.skills" {:id id
                                       :last_updated last_updated}))

(defn insert-data [db-conf]
  (insert-item db-conf test-id "adamant pick" test-time)
  (insert-item db-conf 2000 test-name test-time))

; TODO find out how to rollback all
(defn db-fixtures [f]
  (migrate test-db)
  (insert-data test-db)
  (f)
  (rollback test-db))

(use-fixtures :each db-fixtures)

(deftest given-we-request-one-recipe
  (with-redefs [db db]
    (testing "when we provide an id,"
      (let [id test-id]
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
                             :products '("there")}} (get-recipe {:name name})))))))))

(deftest given-we-want-many-recipes)
