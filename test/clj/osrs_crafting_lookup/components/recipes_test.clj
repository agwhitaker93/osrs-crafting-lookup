(ns osrs-crafting-lookup.components.recipes-test
  (:require [clojure.test :refer :all]
            [clojure.java.jdbc :as jdbc]
            [osrs-crafting-lookup.migrations :refer [migrate rollback]]
            [osrs-crafting-lookup.config :refer [db]]
            [osrs-crafting-lookup.database :as database]
            [osrs-crafting-lookup.components.recipes :refer [get-recipe]]))

(def ^:dynamic test-db {:classname   "org.h2.Driver"
                        :subprotocol "h2:mem"
                        :subname     "./dev-target/test.db"
                        :dbname      "testing"
                        :make-pool?  true})

(def test-time (.toString (database/timestamp-now)))

(def test-item-addy-bar {:id 2361 :name "Adamantite bar"})
(def test-item-addy-2h {:id 1317 :name "Adamant 2h sword"})

(def empty-result {:target {:recipes  '()
                            :products '()}})
(def nil-results {:target {:tradable     nil
                           :wiki         nil
                           :products     '()
                           :value        nil
                           :icon         nil
                           :recipes      (conj '() {:tools        nil
                                                    :recipe_id    nil
                                                    :skills       '()
                                                    :members_only nil
                                                    :materials    '()
                                                    :ticks        nil
                                                    :facilities   nil})
                           :ge-value     nil
                           :examine      nil
                           :members-only nil
                           :exchange     nil
                           :icon_large   nil}})

(def id-result (as-> nil-results $
                 (assoc-in $
                   [:target :name] "Adamantite bar")
                 (assoc-in $
                   [:target :id] 2361)
                 (assoc-in $
                   [:target :last_updated] test-time)
                 (assoc-in $
                   [:target :recipes] (map #(-> %1
                                              (assoc :id 2361)
                                              (assoc :last_updated test-time))
                                        (get-in $ [:target :recipes])))))

(def name-result {:target {}})

(defn insert-item [db-conf {id :id name :name last_updated :last_updated}]
  (jdbc/insert! db-conf "osrs.items" {:id           id
                                      :name         name
                                      :last_updated last_updated})
  (jdbc/insert! db-conf "osrs.materials" {:id           id
                                          :last_updated last_updated})
  (jdbc/insert! db-conf "osrs.recipes" {:id           id
                                        :last_updated last_updated})
  (jdbc/insert! db-conf "osrs.skills" {:id           id
                                       :last_updated last_updated}))

(defn insert-data [db-conf]
  (insert-item db-conf (assoc test-item-addy-bar :last_updated test-time))
  (insert-item db-conf (assoc test-item-addy-2h :last_updated test-time)))

; TODO find out how to rollback all
(defn db-fixtures [f]
  (jdbc/with-db-transaction [test-con test-db]
    (migrate test-con)
    (insert-data test-con)
    (binding [test-db test-con]
      (f))
    (rollback test-con)))

(use-fixtures :each db-fixtures)

(deftest given-we-request-one-recipe
  (with-redefs [db test-db]
    (testing "when we provide an id,"
      (let [id (:id test-item-addy-bar)]
        (testing "and we do not provide a name,"
          (testing "then we get results matching the id"
            (is (= id-result (get-recipe {:id id})))))

        ;(testing "and we provide a name,"
        ;  (let [name (:name test-item-addy-2h)]
        ;    (testing "then we get results matching the id"
        ;      (is (= id-result (get-recipe {:id   id
        ;                                    :name name}))))))
        ))

    (testing "when we provide no id,"
      (testing "and we do not provide a name,"
        (testing "then an empty result is provided"
          (is (= empty-result (get-recipe {})))))

      ;(testing "and we provide a name,"
      ;  (let [name (:name test-item-addy-2h)]
      ;    (testing "then we get results matching the name"
      ;      (is (= name-result (get-recipe {:name name}))))))
      )))

(deftest given-we-want-many-recipes)
