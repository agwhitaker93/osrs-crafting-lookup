(ns scratch
  (:require [clojure.java.jdbc :as cjdbc]
            [osrs-crafting-lookup [config :refer [db]]]))

(def ge-api-alphas (seq"abcdefghijklmnopqrstuvwxyz0123456789"))

(defn timestamp-now []
  (-> (java.util.Calendar/getInstance)
      (.getTimeInMillis)
      (java.sql.Timestamp.)))

(defn test-insert []
  (cjdbc/insert! db :osrs.items {:id 10 :icon "icon" :icon_large "icon large" :name "Test entry" :description "Test description" :skill "crafting" :xp 69 :price "69420" :last_updated (timestamp-now)}))

(defn pre-start-setup [insertion-list]
  (cjdbc/with-db-transaction [t-con db]
                             (map cjdbc/insert! "osrs" "items")))
